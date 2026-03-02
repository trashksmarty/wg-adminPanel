package com.easywireguard.adminpanel.repository;

import com.easywireguard.adminpanel.model.Prefix;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий доступа к таблице prefix.
 */
public interface PrefixRepository extends JpaRepository<Prefix, String> {
}
