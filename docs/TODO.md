
| Missing Feature                                                                                  | Responsible Role | Module/Phase            |
| ------------------------------------------------------------------------------------------------ | ---------------- | ----------------------- |
| 🔲 Role-based admin panel endpoints (e.g., fetch all users, block user, manual interest run)     | Admin            | REST/EJB                |
| 🔲 REST endpoint for viewing system logs or reports (could be limited to audit info via MDB log) | Admin            | REST                    |
| 🔲 Proper UI for login, error pages (HTML available, but integration may be limited)             | All              | `web/`                  |
| 🔲 `@RolesAllowed` on each EJB method fully reviewed and tested                                  | All              | EJB                     |
| 🔲 Final test cases (JUnit or Postman test scripts)                                              | All              | `test/java` or external |
| 🔲 Scheduled loan processing (if required separately from transactions)                          | Admin            | EJB Timer (Optional)    |
| 🔲 Email/SMS-like notification mock via JMS (currently simulated with console logs)              | User             | MDB (Enhance)           |
