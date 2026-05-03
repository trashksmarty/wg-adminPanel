package com.easywireguard.adminpanel.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

/**
 * Сущность туннеля, которую мы храним в локальной БД админки.
 */
@Entity
@Table(name = "tunnels")
public class Tunnel {

    /** Внутренний ID записи в нашей БД. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя туннеля, которое видит пользователь.
     *
     * <p>При создании формируется как "prefix + name".</p>
     */
    @Column(nullable = false)
    private String name;

    /**
     * ID туннеля во внешнем easy-wireguard API.
     *
     * <p>Этот ID генерируется удалённой системой.</p>
     */
    @Column(name = "external_tunnel_id", nullable = false, unique = true)
    private String externalTunnelId;

    /** Текущее состояние туннеля: включён / выключен. */
    @Column(nullable = false)
    private boolean active;

    /** Дата и время, до которого туннель должен быть активен. */
    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalTunnelId() {
        return externalTunnelId;
    }

    public void setExternalTunnelId(String externalTunnelId) {
        this.externalTunnelId = externalTunnelId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
