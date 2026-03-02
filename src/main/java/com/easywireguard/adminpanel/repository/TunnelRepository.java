package com.easywireguard.adminpanel.repository;

import com.easywireguard.adminpanel.model.Tunnel;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TunnelRepository extends JpaRepository<Tunnel, Long> {

    List<Tunnel> findAllByActiveTrueAndExpiresAtBefore(OffsetDateTime timestamp);
}
