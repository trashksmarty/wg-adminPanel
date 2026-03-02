package com.easywireguard.adminpanel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.easywireguard.adminpanel.client.EasyWireguardClient;
import com.easywireguard.adminpanel.dto.TunnelCreateRequest;
import com.easywireguard.adminpanel.model.Tunnel;
import com.easywireguard.adminpanel.repository.PrefixRepository;
import com.easywireguard.adminpanel.repository.TunnelRepository;
import com.easywireguard.adminpanel.service.TunnelService;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit-тесты бизнес-логики TunnelService.
 */
@ExtendWith(MockitoExtension.class)
class TunnelServiceTest {

    @Mock
    private TunnelRepository tunnelRepository;

    @Mock
    private PrefixRepository prefixRepository;

    @Mock
    private EasyWireguardClient easyWireguardClient;

    @InjectMocks
    private TunnelService tunnelService;

    /**
     * Проверяем, что при создании:
     * - валидируется префикс,
     * - вызывается удалённое создание туннеля,
     * - туннель сохраняется локально,
     * - и удалённо включается.
     */
    @Test
    void createShouldCreateInRemoteAndSave() {
        TunnelCreateRequest request = new TunnelCreateRequest(
                "office",
                "[-]",
                OffsetDateTime.now().plusDays(1)
        );

        when(prefixRepository.existsById("[-]")).thenReturn(true);
        when(easyWireguardClient.createTunnel("[-]office")).thenReturn("remote-1");
        when(tunnelRepository.save(any(Tunnel.class))).thenAnswer(invocation -> {
            Tunnel tunnel = invocation.getArgument(0);
            tunnel.setId(10L);
            return tunnel;
        });

        var response = tunnelService.create(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("[-]office");
        assertThat(response.externalTunnelId()).isEqualTo("remote-1");
        assertThat(response.active()).isTrue();
        verify(easyWireguardClient, times(1)).createTunnel("[-]office");
        verify(easyWireguardClient, times(1)).setTunnelEnabled("remote-1", true);
    }

    /**
     * Проверяем автоматическое выключение просроченного активного туннеля.
     */
    @Test
    void deactivateExpiredShouldDisableAllExpired() {
        Tunnel tunnel = new Tunnel();
        tunnel.setId(1L);
        tunnel.setName("expired");
        tunnel.setExternalTunnelId("remote-expired");
        tunnel.setActive(true);
        tunnel.setExpiresAt(OffsetDateTime.now().minusMinutes(1));

        when(tunnelRepository.findAllByActiveTrueAndExpiresAtBefore(any())).thenReturn(List.of(tunnel));

        tunnelService.deactivateExpiredTunnels();

        assertThat(tunnel.isActive()).isFalse();
        verify(easyWireguardClient, times(1)).setTunnelEnabled("remote-expired", false);
        verify(tunnelRepository, times(1)).saveAll(List.of(tunnel));
    }
}
