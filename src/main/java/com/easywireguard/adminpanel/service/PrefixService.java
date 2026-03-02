package com.easywireguard.adminpanel.service;

import com.easywireguard.adminpanel.dto.PrefixResponse;
import com.easywireguard.adminpanel.model.Prefix;
import com.easywireguard.adminpanel.repository.PrefixRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrefixService implements CommandLineRunner {

    private static final List<String> DEFAULT_PREFIXES = List.of("[-]", "(-)", "{-}");

    private final PrefixRepository prefixRepository;

    public PrefixService(PrefixRepository prefixRepository) {
        this.prefixRepository = prefixRepository;
    }

    @Transactional(readOnly = true)
    public List<PrefixResponse> getAll() {
        return prefixRepository.findAll().stream()
                .map(prefix -> new PrefixResponse(prefix.getPrefix()))
                .toList();
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (prefixRepository.count() == 0) {
            List<Prefix> defaults = DEFAULT_PREFIXES.stream().map(Prefix::new).toList();
            prefixRepository.saveAll(defaults);
        }
    }
}
