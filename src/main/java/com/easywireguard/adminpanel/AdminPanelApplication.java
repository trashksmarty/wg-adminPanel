package com.easywireguard.adminpanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Точка входа в приложение админ-панели.
 *
 * <p>Аннотация {@link EnableScheduling} включает планировщик Spring,
 * который нужен для регулярной проверки просроченных туннелей и их выключения.</p>
 */
@EnableScheduling
@SpringBootApplication
public class AdminPanelApplication {

    /**
     * Стандартный main-метод запуска Spring Boot приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(AdminPanelApplication.class, args);
    }
}
