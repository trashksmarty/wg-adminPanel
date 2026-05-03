package com.easywireguard.adminpanel.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

/**
 * DTO для создания туннеля из UI.
 *
 * @param name пользовательская часть имени туннеля (без префикса)
 * @param prefix выбранный префикс из таблицы prefix
 * @param expiresAt дата/время деактивации (должно быть в будущем)
 */
public record TunnelCreateRequest(
        @NotBlank String name,
        @NotBlank String prefix,
        @NotNull @Future OffsetDateTime expiresAt
) {
}
