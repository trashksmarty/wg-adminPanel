package com.easywireguard.adminpanel.controller;

import com.easywireguard.adminpanel.dto.TunnelCreateRequest;
import com.easywireguard.adminpanel.dto.TunnelResponse;
import com.easywireguard.adminpanel.dto.TunnelUpdateRequest;
import com.easywireguard.adminpanel.service.TunnelService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/tunnels")
public class TunnelController {

    private final TunnelService tunnelService;

    public TunnelController(TunnelService tunnelService) {
        this.tunnelService = tunnelService;
    }

    @GetMapping
    public List<TunnelResponse> getAll() {
        return tunnelService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TunnelResponse create(@Valid @RequestBody TunnelCreateRequest request) {
        return tunnelService.create(request);
    }

    @PatchMapping("/{id}/expiration")
    public TunnelResponse updateExpiration(@PathVariable Long id, @Valid @RequestBody TunnelUpdateRequest request) {
        return tunnelService.updateExpiration(id, request);
    }

    @PatchMapping("/{id}/state/{active}")
    public TunnelResponse setState(@PathVariable Long id, @PathVariable boolean active) {
        return tunnelService.setTunnelState(id, active);
    }
}
