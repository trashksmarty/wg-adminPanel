package com.easywireguard.adminpanel.repository;

import com.easywireguard.adminpanel.model.Tunnel;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий туннелей.
 */
public interface TunnelRepository extends JpaRepository<Tunnel, Long> {

    /**
     * Возвращает активные туннели, у которых время жизни уже истекло.
     */
    List<Tunnel> findAllByActiveTrueAndExpiresAtBefore(OffsetDateTime timestamp);
}
