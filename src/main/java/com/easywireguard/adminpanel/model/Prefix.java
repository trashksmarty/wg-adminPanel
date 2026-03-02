package com.easywireguard.adminpanel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Сущность префикса для имени туннеля.
 *
 * <p>По требованию проекта таблица называется {@code prefix}
 * и содержит только одну колонку типа varchar(40).</p>
 */
@Entity
@Table(name = "prefix")
public class Prefix {

    /**
     * Сам текст префикса (например, "[-]", "(-)", "{-}").
     * Поле является одновременно и первичным ключом.
     */
    @Id
    @Column(name = "prefix", length = 40, nullable = false)
    private String prefix;

    /** Пустой конструктор требуется JPA. */
    public Prefix() {
    }

    /**
     * Удобный конструктор для создания объекта префикса в коде.
     *
     * @param prefix строковое значение префикса
     */
    public Prefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
