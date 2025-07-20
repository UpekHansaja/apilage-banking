# Apilage Banking - Implementation Guide

## Table of Contents
1. [System Architecture](#system-architecture)
2. [Module Implementation Details](#module-implementation-details)
3. [File Structure](#file-structure)
3. [Security Implementation](#security-implementation)
4. [API Reference](#api-reference)
5. [Database Design](#database-design)
6. [Development Setup](#development-setup)

## System Architecture

### High-Level Architecture Diagram
```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Client     │     │   REST API   │     │  Business    │
│   (Web/App)  │────▶│   Layer      │────▶│   Layer      │
└──────────────┘     └──────────────┘     └──────────────┘
                            │                     │
                            │               ┌─────▼─────┐
                            │               │   Data    │
                            └──────────────▶│   Layer   │
                                           └───────────┘
```

### Module Structure
```
apilage-banking/
├── jpa/          # Data Access Layer
├── ejb/          # Business Logic Layer
├── security/     # Security Implementation
├── rest/         # REST API Layer
├── web/          # Web Frontend
└── ear/          # Enterprise Application Package
```

# File Structure
```
tree -L 10 -I 'node_modules|dist|artifacts|cache|coverage|typechain-types|target'
.
├── README.md
├── docs
│   ├── API-Guide.md
│   ├── ApilageBanking-Black-BG-Logo.png
│   ├── ApilageBanking-Transparent-Logo.png
│   ├── BCD II - Paper.pdf
│   ├── BCD II.pdf
│   ├── Development-Phases.md
│   ├── Implementation-Guide.md
│   ├── REST-API.md
│   ├── TODO.md
│   └── apilagebanking-schema.sql
├── ear
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── application
│       │   │   └── META-INF
│       │   │       ├── application.xml
│       │   │       └── glassfish-application.xml
│       │   ├── java
│       │   └── resources
│       │       └── META-INF
│       │           └── beans.xml
│       └── test
│           └── java
├── ejb
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── lk
│       │   │       └── jiat
│       │   │           └── bank
│       │   │               └── ejb
│       │   │                   ├── exception
│       │   │                   ├── interceptor
│       │   │                   │   ├── Audit.java
│       │   │                   │   ├── AuditInterceptor.java
│       │   │                   │   ├── Logging.java
│       │   │                   │   └── LoggingInterceptor.java
│       │   │                   ├── mdb
│       │   │                   │   └── TransactionNotificationMDB.java
│       │   │                   ├── security
│       │   │                   ├── service
│       │   │                   │   ├── AccountServiceBean.java
│       │   │                   │   ├── AdminServiceBean.java
│       │   │                   │   ├── LoanServiceBean.java
│       │   │                   │   ├── ReportServiceBean.java
│       │   │                   │   ├── TransactionServiceBean.java
│       │   │                   │   └── UserServiceBean.java
│       │   │                   └── timer
│       │   │                       ├── InterestCalculatorTimer.java
│       │   │                       └── ScheduledTasksBean.java
│       │   └── resources
│       │       └── META-INF
│       │           └── beans.xml
│       └── test
│           └── java
├── jpa
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── lk
│       │   │       └── jiat
│       │   │           └── bank
│       │   │               └── jpa
│       │   │                   ├── audit
│       │   │                   ├── converter
│       │   │                   └── entity
│       │   │                       ├── Account.java
│       │   │                       ├── AccountStatus.java
│       │   │                       ├── AccountType.java
│       │   │                       ├── JmsMessage.java
│       │   │                       ├── Role.java
│       │   │                       ├── Transaction.java
│       │   │                       ├── TransactionType.java
│       │   │                       ├── User.java
│       │   │                       └── UserStatus.java
│       │   └── resources
│       │       └── META-INF
│       │           └── persistence.xml
│       └── test
│           └── java
├── pom.xml
├── rest
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── lk
│       │   │       └── jiat
│       │   │           └── bank
│       │   │               └── rest
│       │   │                   ├── RestApplication.java
│       │   │                   ├── filter
│       │   │                   │   ├── CORSFilter.java
│       │   │                   │   ├── GlobalExceptionMapper.java
│       │   │                   │   └── JwtFilter.java
│       │   │                   └── resource
│       │   │                       ├── AccountResource.java
│       │   │                       ├── AdminResource.java
│       │   │                       ├── AuthResource.java
│       │   │                       ├── ReportResource.java
│       │   │                       ├── TestResource.java
│       │   │                       ├── TransferResource.java
│       │   │                       └── UserResource.java
│       │   ├── resources
│       │   │   └── META-INF
│       │   │       └── beans.xml
│       │   └── webapp
│       │       └── WEB-INF
│       │           ├── glassfish-web.xml
│       │           └── web.xml
│       └── test
│           └── java
├── rest.iml
├── security
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── lk
│       │   │       └── jiat
│       │   │           └── bank
│       │   │               └── security
│       │   │                   ├── auth
│       │   │                   │   └── DbLoginModule.java
│       │   │                   ├── jwt
│       │   │                   │   └── JwtUtil.java
│       │   │                   └── rbac
│       │   │                       └── RoleManager.java
│       │   └── resources
│       └── test
│           └── java
├── src
│   ├── main
│   │   ├── java
│   │   └── resources
│   └── test
│       └── java
└── web
    ├── pom.xml
    └── src
        ├── main
        │   ├── java
        │   │   └── lk
        │   │       └── jiat
        │   │           └── bank
        │   │               └── web
        │   ├── resources
        │   │   └── META-INF
        │   └── webapp
        │       ├── admin-dashboard.html
        │       ├── css
        │       │   ├── dashboard.css
        │       │   └── style.css
        │       ├── index.html
        │       ├── js
        │       │   ├── dashboard.js
        │       │   └── login.js
        │       ├── login.html
        │       ├── res
        │       │   ├── ApilageBanking-Black-BG-Logo.png
        │       │   └── ApilageBanking-Transparent-Logo.png
        │       └── user-dashboard.html
        └── test
            └── java

97 directories, 73 files
```

## Module Implementation Details

### JPA Module Implementation

```java
// Example Entity: User.java
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String username;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    // Getters and setters
}
```

### EJB Service Layer

```java
// Example Service: UserService.java
@Stateless
public class UserServiceBean implements UserService {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public User createUser(User user) {
        em.persist(user);
        return user;
    }
    
    @Override
    @RolesAllowed({"ADMIN"})
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class)
                 .getResultList();
    }
}
```

### REST API Implementation

```java
// Example REST Resource: UserResource.java
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    private UserService userService;
    
    @POST
    @RolesAllowed({"ADMIN"})
    public Response createUser(User user) {
        User created = userService.createUser(user);
        return Response.status(Response.Status.CREATED)
                      .entity(created)
                      .build();
    }
    
    @GET
    @RolesAllowed({"ADMIN"})
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
```

## Security Implementation

### Authentication Flow
```java
@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        // Extract JWT token from Authorization header
        String token = extractToken(httpRequest);
        
        if (token != null && validateToken(token)) {
            // Set security context
            SecurityContext context = createSecurityContext(token);
            ((HttpServletRequest) request).setAttribute(
                "SecurityContext", context);
        }
        
        chain.doFilter(request, response);
    }
}
```

### Role-Based Access Control (RBAC)
```java
@Stateless
public class SecurityService {
    @Inject
    private UserService userService;
    
    public boolean hasPermission(String username, String permission) {
        User user = userService.findByUsername(username);
        return user.getRoles().stream()
                  .flatMap(role -> role.getPermissions().stream())
                  .anyMatch(p -> p.getName().equals(permission));
    }
}
```

## API Reference

### Authentication API
- POST /api/auth/login
- POST /api/auth/refresh
- POST /api/auth/logout

### User Management API
- GET /api/users
- POST /api/users
- PUT /api/users/{id}
- DELETE /api/users/{id}

### Account Management API
- GET /api/accounts
- POST /api/accounts
- GET /api/accounts/{id}/balance
- POST /api/accounts/{id}/transfer

## Database Design

### Entity Relationship Diagram
```
┌──────────┐     ┌──────────┐     ┌──────────────┐
│  Users   │     │  Roles   │     │ Permissions  │
├──────────┤     ├──────────┤     ├──────────────┤
│ id       │◄────┤ id       │◄────┤ id           │
│ username │     │ name     │     │ name         │
│ password │     │          │     │              │
└──────────┘     └──────────┘     └──────────────┘
      ▲                ▲                  ▲
      │                │                  │
      │          ┌──────────┐      ┌──────────┐
      │          │  Account │      │Transaction│
      └──────────┤         │◄─────┤           │
                 │ balance │      │ amount    │
                 └──────────┘      └──────────┘
```

## Development Setup

### Prerequisites
- JDK 11 or higher
- Maven 3.6 or higher
- GlassFish 7.0 or higher
- PostgreSQL 12 or higher

### Build and Deploy
```bash
# Clone the repository
git clone https://github.com/your-org/apilage-banking.git

# Build the project
mvn clean install

# Deploy to GlassFish
asadmin deploy ear/target/ear-1.0.ear
```

### Configuration
1. Database Configuration (persistence.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence
             http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="bankPU" transaction-type="JTA">
        <jta-data-source>jdbc/BankDB</jta-data-source>
        <properties>
            <property name="jakarta.persistence.schema-generation.database.action"
                      value="create"/>
        </properties>
    </persistence-unit>
</persistence>
```

2. Security Configuration (web.xml)
```xml
<security-constraint>
    <web-resource-collection>
        <web-resource-name>Protected Area</web-resource-name>
        <url-pattern>/api/*</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <role-name>USER</role-name>
        <role-name>ADMIN</role-name>
    </auth-constraint>
</security-constraint>
```

### Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify
```

This implementation guide provides a comprehensive overview of the Apilage Banking system architecture, implementation details, and setup instructions. For detailed API documentation, please refer to the API-Guide.md file.
