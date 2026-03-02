package com.easywireguard.adminpanel.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * DTO для обновления срока действия туннеля.
 *
 * @param expiresAt новая дата/время (обязательно в будущем)
 */
public record TunnelUpdateRequest(@NotNull @Future OffsetDateTime expiresAt) {
}
