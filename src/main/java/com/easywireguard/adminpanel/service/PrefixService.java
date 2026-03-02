package com.easywireguard.adminpanel.service;

import com.easywireguard.adminpanel.dto.PrefixResponse;
import com.easywireguard.adminpanel.model.Prefix;
import com.easywireguard.adminpanel.repository.PrefixRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис работы со справочником префиксов.
 */
@Service
public class PrefixService implements CommandLineRunner {

    /** Дефолтные значения, которые должны быть в таблице по условию. */
    private static final List<String> DEFAULT_PREFIXES = List.of("[-]", "(-)", "{-}");

    private final PrefixRepository prefixRepository;

    public PrefixService(PrefixRepository prefixRepository) {
        this.prefixRepository = prefixRepository;
    }

    /**
     * Возвращает все доступные префиксы для UI.
     */
    @Transactional(readOnly = true)
    public List<PrefixResponse> getAll() {
        return prefixRepository.findAll().stream()
                .map(prefix -> new PrefixResponse(prefix.getPrefix()))
                .toList();
    }

    /**
     * Инициализация справочника префиксов при запуске приложения.
     *
     * <p>Если таблица пустая, добавляем 3 обязательных значения.</p>
     */
    @Override
    @Transactional
    public void run(String... args) {
        if (prefixRepository.count() == 0) {
            List<Prefix> defaults = DEFAULT_PREFIXES.stream().map(Prefix::new).toList();
            prefixRepository.saveAll(defaults);
        }
    }
}
