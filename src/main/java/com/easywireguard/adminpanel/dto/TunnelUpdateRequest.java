package com.easywireguard.adminpanel.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record TunnelUpdateRequest(@NotNull @Future OffsetDateTime expiresAt) {
}
