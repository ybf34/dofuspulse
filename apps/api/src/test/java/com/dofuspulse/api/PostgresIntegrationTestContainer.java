package com.dofuspulse.api;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * This class sets up a PostgreSQL container and configures Spring Boot's datasource to connect to
 * it dynamically for integration tests.
 */

@ActiveProfiles("test")
public abstract class PostgresIntegrationTestContainer {

  /**
   * Defines and starts the PostgreSQL Testcontainers container. The container is static and shared
   * across all tests that extend this class.
   */

  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
      "postgres:17.3-alpine");

  static {
    postgres.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    // registry.add("spring.datasource.hibernate.ddl-auto", () -> "create-drop");
  }
}