package com.dofuspulse.api;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * This class sets up a PostgreSQL container and configures Spring Boot's datasource to connect to
 * it dynamically for integration tests.
 */

public abstract class PostgresIntegrationTestContainer {

  /**
   * Defines and starts the PostgreSQL Testcontainers container. The container is static and shared
   * across all tests that extend this class.
   */

  static final DockerImageName TIMESCALE_IMAGE = DockerImageName
      .parse("timescale/timescaledb:latest-pg17")
      .asCompatibleSubstituteFor("postgres");

  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(TIMESCALE_IMAGE);

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