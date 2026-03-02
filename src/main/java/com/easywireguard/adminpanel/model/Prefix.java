package com.easywireguard.adminpanel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "prefix")
public class Prefix {

    @Id
    @Column(name = "prefix", length = 40, nullable = false)
    private String prefix;

    public Prefix() {
    }

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
