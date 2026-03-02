package com.easywireguard.adminpanel.service;

import com.easywireguard.adminpanel.client.EasyWireguardClient;
import com.easywireguard.adminpanel.dto.TunnelCreateRequest;
import com.easywireguard.adminpanel.dto.TunnelResponse;
import com.easywireguard.adminpanel.dto.TunnelUpdateRequest;
import com.easywireguard.adminpanel.model.Tunnel;
import com.easywireguard.adminpanel.repository.PrefixRepository;
import com.easywireguard.adminpanel.repository.TunnelRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Основная бизнес-логика управления туннелями.
 */
@Service
public class TunnelService {

    private final TunnelRepository tunnelRepository;
    private final PrefixRepository prefixRepository;
    private final EasyWireguardClient easyWireguardClient;

    public TunnelService(
            TunnelRepository tunnelRepository,
            PrefixRepository prefixRepository,
            EasyWireguardClient easyWireguardClient
    ) {
        this.tunnelRepository = tunnelRepository;
        this.prefixRepository = prefixRepository;
        this.easyWireguardClient = easyWireguardClient;
    }

    /**
     * Возвращает список туннелей для отображения в UI.
     */
    @Transactional(readOnly = true)
    public List<TunnelResponse> getAll() {
        return tunnelRepository.findAll().stream().map(this::map).toList();
    }

    /**
     * Создаёт новый туннель:
     * 1) валидирует префикс,
     * 2) собирает итоговое имя,
     * 3) создаёт туннель во внешнем API,
     * 4) сохраняет запись локально,
     * 5) включает туннель.
     */
    @Transactional
    public TunnelResponse create(TunnelCreateRequest request) {
        if (!prefixRepository.existsById(request.prefix())) {
            throw new EntityNotFoundException("Prefix not found: " + request.prefix());
        }

        String tunnelName = request.prefix() + request.name();
        String externalTunnelId = easyWireguardClient.createTunnel(tunnelName);

        Tunnel tunnel = new Tunnel();
        tunnel.setName(tunnelName);
        tunnel.setExternalTunnelId(externalTunnelId);
        tunnel.setExpiresAt(request.expiresAt());
        tunnel.setActive(true);

        easyWireguardClient.setTunnelEnabled(externalTunnelId, true);

        Tunnel saved = tunnelRepository.save(tunnel);
        return map(saved);
    }

    /**
     * Меняет дату окончания активности туннеля.
     */
    @Transactional
    public TunnelResponse updateExpiration(Long id, TunnelUpdateRequest request) {
        Tunnel tunnel = findById(id);
        tunnel.setExpiresAt(request.expiresAt());
        return map(tunnelRepository.save(tunnel));
    }

    /**
     * Ручное включение/выключение туннеля.
     */
    @Transactional
    public TunnelResponse setTunnelState(Long id, boolean active) {
        Tunnel tunnel = findById(id);
        easyWireguardClient.setTunnelEnabled(tunnel.getExternalTunnelId(), active);
        tunnel.setActive(active);
        return map(tunnelRepository.save(tunnel));
    }

    /**
     * Фоновая задача: периодически выключает просроченные активные туннели.
     */
    @Scheduled(fixedDelayString = "${tunnels.deactivation-check-ms:60000}")
    @Transactional
    public void deactivateExpiredTunnels() {
        List<Tunnel> expired = tunnelRepository.findAllByActiveTrueAndExpiresAtBefore(OffsetDateTime.now());
        for (Tunnel tunnel : expired) {
            easyWireguardClient.setTunnelEnabled(tunnel.getExternalTunnelId(), false);
            tunnel.setActive(false);
        }
        tunnelRepository.saveAll(expired);
    }

    /**
     * Находит туннель по id или бросает 404-совместимое исключение.
     */
    private Tunnel findById(Long id) {
        return tunnelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tunnel not found: " + id));
    }

    /**
     * Маппинг entity -> response DTO.
     */
    private TunnelResponse map(Tunnel tunnel) {
        return new TunnelResponse(
                tunnel.getId(),
                tunnel.getName(),
                tunnel.getExternalTunnelId(),
                tunnel.isActive(),
                tunnel.getExpiresAt()
        );
    }
}
