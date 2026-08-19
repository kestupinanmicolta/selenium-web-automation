# Selenium Web Automation

Suite completa de automatización web con Selenium 4, TestNG, Page Object Model y Allure Reports.

## Características

- **Selenium 4**: Última versión con soporte completo
- **Page Object Model**: Arquitectura mantenible y escalable
- **TestNG**: Organización de tests con groups y suites
- **Allure Reports**: Reportes visuales detallados
- **WebDriverManager**: Gestión automática de drivers
- **Multi-browser**: Chrome y Firefox

## Requisitos

- JDK 21
- Maven
- Chrome o Firefox instalado

## Ejecución

```bash
# Ejecutar todos los tests
mvn clean test

# Ejecutar solo tests de regression
mvn clean test -Dgroups=regression

# Ejecutar solo tests de smoke
mvn clean test -Dgroups=smoke

# Ejecutar con Firefox
mvn clean test -Dbrowser=firefox

# Generar reporte Allure
mvn allure:serve
```

## Estructura

```
src/test/java/com/karen/
├── pages/
│   ├── LoginPage.java
│   ├── HomePage.java
│   ├── ProductPage.java
│   └── CartPage.java
├── tests/
│   ├── LoginTest.java
│   ├── SearchTest.java
│   ├── CartTest.java
│   └── CheckoutTest.java
└── utils/
    └── DriverFactory.java
```

## Tags

- `@smoke`: Tests críticos
- `@regression`: Suite completa de regresión

<!-- lastupdate: 2026-08-18 21:18 -->
