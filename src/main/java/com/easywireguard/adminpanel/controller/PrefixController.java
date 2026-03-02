package com.easywireguard.adminpanel.controller;

import com.easywireguard.adminpanel.dto.PrefixResponse;
import com.easywireguard.adminpanel.service.PrefixService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/prefixes")
public class PrefixController {

    private final PrefixService prefixService;

    public PrefixController(PrefixService prefixService) {
        this.prefixService = prefixService;
    }

    @GetMapping
    public List<PrefixResponse> getAll() {
        return prefixService.getAll();
    }
}
