# 📘 API Guidance

***

### 🔐 Login
Endpoint:
`POST` `/apilagebanking/api/auth/login`

Request Body:

```json
{
"username": "admin",
"password": "admin123"
}
```

Response on Success:

```json
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
"username": "admin",
"roles": ["ADMIN"],
"redirect": "/admin/dashboard"
}
```

Response on Failure:

```json
{
"error": "❌ Invalid credentials"
}
```

***

### Admin Report Summary

Endpoint: `GET` `/api/admin/reports/summary`

Request Headers:

```http
Authorization: Bearer <JWT-TOKEN>
```

Response on Success:

```json
{
  "totalUsers": 124,
  "totalTransactions": 2156
}
```

***

### Admin Report Transactions

Endpoint: `GET` `/api/admin/reports/transactions?limit=5`

Request Headers:

```http
Authorization: Bearer <JWT-TOKEN>
``` 

Response on Success:

```json
[
  {
    "id": 105,
    "amount": 5000.00,
    "type": "DEPOSIT",
    "createdAt": "2025-07-18T12:40:20",
    ...
  },
  ...
]
```

***

### Admin Report Users

Endpoint: `GET` `/api/admin/reports/users`

Request Headers:

```http
Authorization: Bearer <JWT-TOKEN>
```

Response on Success:

```json
[
  {
    "id": 1,
    "username": "john_doe",
    "email": "user@abc.com",
    "status": "ACTIVE",
  },
  ...
]
```

***
