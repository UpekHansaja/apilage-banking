**Quick reference guide** for REST API endpoints.

---

## **Base URL**
```
http://<your-server>:<port>/<context-root>/api
```
- On GlassFish, `<context-root>` is usually your WAR name (e.g., `apilageBanking`), or `/` if deployed as the root app.

---

## **1. User API**

### **Endpoints**
| Method | URL                | Description         | Request Body (JSON)         |
|--------|--------------------|---------------------|-----------------------------|
| GET    | /users             | List all users      | —                           |
| GET    | /users/{id}        | Get user by ID      | —                           |
| POST   | /users             | Create new user     | `{ "username": "...", "email": "...", "fullName": "...", "status": "ACTIVE" }` |
| PUT    | /users/{id}        | Update user         | `{ "username": "...", "email": "...", "fullName": "...", "status": "ACTIVE" }` |
| DELETE | /users/{id}        | Delete user         | —                           |
| POST   | /users/logout       | Logout current user   | —                           |
| GET    | /users/me           | Get current user info & roles | —                           |

### **Example Request Body**
```json
{
  "username": "john",
  "email": "john@example.com",
  "fullName": "John Doe",
  "status": "ACTIVE"
}
```

### **Get Current User Info & Roles**
**Endpoint:** `GET /users/me`

**Description:**
Returns the username and roles of the currently authenticated user (based on the session/cookie).

**Example Response:**
```json
{
  "username": "john",
  "roles": ["user"]
}
```

---

## **2. Account API**

### **Endpoints**
| Method | URL                  | Description         | Request Body (JSON)         |
|--------|----------------------|---------------------|-----------------------------|
| GET    | /accounts            | List all accounts   | —                           |
| GET    | /accounts/{id}       | Get account by ID   | —                           |
| POST   | /accounts            | Create new account  | `{ "accountNumber": "...", "balance": 1000.0 }` |
| PUT    | /accounts/{id}       | Update account      | `{ "accountNumber": "...", "balance": 1000.0 }` |
| DELETE | /accounts/{id}       | Delete account      | —                           |

### **Example Request Body**
```json
{
  "accountNumber": "1234567890",
  "balance": 1000.0
}
```

---

## **3. Role API**

### **Endpoints**
| Method | URL                | Description         | Request Body (JSON)         |
|--------|--------------------|---------------------|-----------------------------|
| GET    | /roles             | List all roles      | —                           |
| GET    | /roles/{id}        | Get role by ID      | —                           |
| POST   | /roles             | Create new role     | `{ "name": "...", "description": "..." }` |
| PUT    | /roles/{id}        | Update role         | `{ "name": "...", "description": "..." }` |
| DELETE | /roles/{id}        | Delete role         | —                           |

### **Example Request Body**
```json
{
  "name": "ADMIN",
  "description": "Administrator role"
}
```

---

## **4. Transaction API**

### **Endpoints**
| Method | URL                      | Description         | Request Body (JSON)         |
|--------|--------------------------|---------------------|-----------------------------|
| GET    | /transactions            | List all transactions | —                         |
| GET    | /transactions/{id}       | Get transaction by ID | —                         |
| POST   | /transactions            | Create new transaction | `{ "accountId": 1, "type": "DEPOSIT", "amount": 100.0, "description": "Initial deposit" }` |
| PUT    | /transactions/{id}       | Update transaction    | `{ "accountId": 1, "type": "DEPOSIT", "amount": 100.0, "description": "Initial deposit" }` |
| DELETE | /transactions/{id}       | Delete transaction   | —                          |

### **Example Request Body**
```json
{
  "accountId": 1,
  "type": "DEPOSIT",
  "amount": 100.0,
  "description": "Initial deposit"
}
```

---

## **5. JMS Message API**

### **Endpoints**
| Method | URL                | Description         | Request Body (JSON)         |
|--------|--------------------|---------------------|-----------------------------|
| GET    | /messages          | List all messages   | —                           |
| GET    | /messages/{id}     | Get message by ID   | —                           |
| POST   | /messages          | Create new message  | `{ "content": "...", "processed": false }` |
| PUT    | /messages/{id}     | Update message      | `{ "content": "...", "processed": true }` |
| DELETE | /messages/{id}     | Delete message      | —                           |

### **Example Request Body**
```json
{
  "content": "This is a JMS message",
  "processed": false
}
```

---

## **Usage Notes**
- All endpoints accept and return `application/json`.
- For `PUT`, the `{id}` in the URL should match the `id` in the request body (if present).
- For `POST`, the `id` field is not required (it will be auto-generated).
- For `DELETE`, no request body is needed.

---

## **Testing**
You can use tools like **Postman**, **curl**, or any HTTP client to test these endpoints.

**Example curl:**
```sh
curl -X POST http://localhost:8080/apilageBanking/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","fullName":"John Doe","status":"ACTIVE"}'
```

**Example curl for logout:**
```sh
curl -X POST http://localhost:8080/apilageBanking/api/users/logout \
  -H "Content-Type: application/json" \
  --cookie "JSESSIONID=<your-session-cookie>"
```

---

If you need:
- More detailed examples
- OpenAPI/Swagger documentation
- Help with authentication headers (when security is added)
- Or anything else

Just let me know!