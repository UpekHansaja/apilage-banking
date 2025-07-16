# ApilageBanking - Banking System with J2EE

![PNG Logo Image of Apilage Banking](./docs/ApilageBanking-Transparent-Logo.png)

### Project Overview

Project Name: ApilageBanking
<br />
Architecture: Multi-module EAR deployment
<br />
Base Package: lk.jiat.bank
<br />
Application Server: GlassFish 7.0.23
<br />
Database: MySQL with Hibernate (JPA)
<br />
Security: JAAS + RBAC with comprehensive security annotations

### Project Structure

```plaintext
.
├── common
├── docs
│   ├── API-Guide.md
│   ├── ApilageBanking-Black-BG-Logo.png
│   ├── apilagebanking-schema.sql
│   ├── ApilageBanking-Transparent-Logo.png
│   ├── BCD II - Paper.pdf
│   └── BCD II.pdf
├── ear                         # EAR wrapper aggregates modules
├── ejb                         # Business logic, JMS, timers
├── jpa                         # JPA + Hibernate persistence
├── pom.xml
├── README.md
├── rest                        # JAX‑RS REST API layer
├── security                    # JAAS + Centralized RBAC auth logic
├── src
└── web                         # Optional frontend

```

