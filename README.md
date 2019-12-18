# 📦 Post room

This is a system for storing, managing and processing information about received parcels.
This includes My Warwick integration.

This is a [Spring Boot](https://spring.io/projects/spring-boot) application written in [Kotlin](https://kotlinlang.org). [Hibernate](https://hibernate.org/orm/) is used for persistence. Database migrations are handled by [Flyway](https://flywaydb.org). [FreeMarker](https://freemarker.apache.org) is the template language. Authentication is provided via [SSO Client](https://github.com/UniversityofWarwick/sso-client) and authorisation is managed by [Spring Security](https://spring.io/projects/spring-security).

## Getting started

1. Copy `application-example.yml` to `application.yml` and replace the example PostgreSQL database details with your own
1. Copy `external-conf/postroom-sso-config-example.xml` to `external-conf/postroom-sso-config.xml` and add your own details
1. Run `./gradlew bootRun` to install dependencies, build assets and compile and run the application
1. Run `./gradlew test` to run the tests
