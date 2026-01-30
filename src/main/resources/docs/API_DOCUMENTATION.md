# 🔌 API Documentation

## Overview

Το PharmaGPO REST API παρέχει πλήρη πρόσβαση στις λειτουργίες του συστήματος μέσω RESTful endpoints με JWT authentication.

**Base URL:** `http://localhost:8080`  
**API Version:** 1.0  
**Authentication:** Bearer Token (JWT)

---

## 🔐 Authentication

### Login

Λήψη JWT token για πρόσβαση στα protected endpoints.

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "username": "user",
  "password": "123456789"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 3600
}
```

**Usage:**
```bash
# Example με curl
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"123456789"}'
```

### Authorization Header

Για όλα τα protected endpoints, συμπεριλάβετε το token:

```
Authorization: Bearer <your-jwt-token>
```

---

## 👤 User Management

### Get My Profile

Λήψη πληροφοριών του συνδεδεμένου χρήστη.

**Endpoint:** `GET /api/users/me`  
**Auth Required:** ✅ Yes

**Response:** `200 OK`
```json
{
  "id": 1,
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "username": "user",
  "firstname": "Γιώργος",
  "lastname": "Παπαδόπουλος",
  "email": "user@example.com",
  "phoneNumber": "+30 210 1234567",
  "vat": "123456789",
  "region": "Αττική",
  "role": "PHARMACIST",
  "createdAt": "2026-01-15T10:30:00",
  "updatedAt": "2026-01-30T14:20:00"
}
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <token>"
```

---

## 📦 Products

### Get All Products

Λήψη καταλόγου όλων των διαθέσιμων προϊόντων.

**Endpoint:** `GET /api/products`  
**Auth Required:** ✅ Yes

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Panadol Extra 500mg",
    "description": "Αναλγητικό - Αντιπυρετικό",
    "price": 4.50,
    "categoryName": "Αναλγητικά"
  },
  {
    "id": 2,
    "name": "Vitamin C 1000mg",
    "description": "Συμπλήρωμα διατροφής",
    "price": 8.90,
    "categoryName": "Βιταμίνες"
  }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>"
```

---

### Get Product by ID

Λήψη λεπτομερειών συγκεκριμένου προϊόντος.

**Endpoint:** `GET /api/products/{id}`  
**Auth Required:** ✅ Yes

**Parameters:**

| Name | Type | In | Required | Description |
|------|------|-----|----------|-------------|
| `id` | `integer` | path | ✅ Yes | Product ID |

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Panadol Extra 500mg",
  "description": "Αναλγητικό - Αντιπυρετικό με παρακεταμόλη",
  "price": 4.50,
  "categoryName": "Αναλγητικά"
}
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <token>"
```

---

## 🛒 Orders

### Get My Orders

Λήψη ιστορικού παραγγελιών του συνδεδεμένου χρήστη.

**Endpoint:** `GET /api/orders`  
**Auth Required:** ✅ Yes  
**Role:** `PHARMACIST`

**Response:** `200 OK`
```json
[
  {
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "status": "PENDING",
    "totalAmount": 127.50,
    "createdAt": "2026-01-30T10:15:00",
    "items": [
      {
        "productName": "Panadol Extra 500mg",
        "quantity": 10,
        "unitPrice": 4.50
      },
      {
        "productName": "Vitamin C 1000mg",
        "quantity": 5,
        "unitPrice": 8.90
      }
    ]
  }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer <token>"
```

---

### Get Order by UUID

Λήψη λεπτομερειών συγκεκριμένης παραγγελίας.

**Endpoint:** `GET /api/orders/{uuid}`  
**Auth Required:** ✅ Yes  
**Role:** `PHARMACIST`

**Parameters:**

| Name | Type | In | Required | Description |
|------|------|-----|----------|-------------|
| `uuid` | `string` | path | ✅ Yes | Order UUID |

**Response:** `200 OK`
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "status": "COMPLETED",
  "totalAmount": 127.50,
  "createdAt": "2026-01-30T10:15:00",
  "items": [
    {
      "productName": "Panadol Extra 500mg",
      "quantity": 10,
      "unitPrice": 4.50
    },
    {
      "productName": "Vitamin C 1000mg",
      "quantity": 5,
      "unitPrice": 8.90
    }
  ]
}
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/orders/550e8400-e29b-41d4-a716-446655440000 \
  -H "Authorization: Bearer <token>"
```

---

### Add Bulk Products to Order

Προσθήκη πολλαπλών προϊόντων σε παραγγελία (bulk operation).

**Endpoint:** `POST /pharmacist/orders/add-bulk`  
**Auth Required:** ✅ Yes  
**Role:** `PHARMACIST`

**Request Body:**
```json
[
  {
    "productId": 1,
    "quantity": 10
  },
  {
    "productId": 2,
    "quantity": 5
  },
  {
    "productId": 3,
    "quantity": 15
  }
]
```

**Response:** `200 OK`
```json
"Order created successfully"
```

**Example:**
```bash
curl -X POST http://localhost:8080/pharmacist/orders/add-bulk \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '[
    {"productId": 1, "quantity": 10},
    {"productId": 2, "quantity": 5}
  ]'
```

---

## 📋 Data Models

### OrderItemRequestDTO

```json
{
  "productId": 1,        // integer (required)
  "quantity": 10         // integer (required)
}
```

### AuthenticationRequest

```json
{
  "username": "user",    // string (required)
  "password": "pass123"  // string (required)
}
```

### UserReadOnlyDTO

```json
{
  "id": 1,
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "username": "user",
  "firstname": "Γιώργος",
  "lastname": "Παπαδόπουλος",
  "email": "user@example.com",
  "phoneNumber": "+30 210 1234567",
  "vat": "123456789",
  "region": "Αττική",
  "role": "PHARMACIST",
  "createdAt": "2026-01-30T10:00:00",
  "updatedAt": "2026-01-30T14:00:00"
}
```

### ProductReadOnlyDTO

```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product description",
  "price": 9.99,
  "categoryName": "Category"
}
```

### OrderReadOnlyDTO

```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "status": "PENDING",
  "totalAmount": 99.99,
  "createdAt": "2026-01-30T10:00:00",
  "items": [
    {
      "productName": "Product 1",
      "quantity": 5,
      "unitPrice": 9.99
    }
  ]
}
```

### OrderItemReadOnlyDTO

```json
{
  "productName": "Product Name",
  "quantity": 5,
  "unitPrice": 9.99
}
```

---

## 🔒 Security Schema

### Bearer Authentication

**Type:** HTTP  
**Scheme:** bearer  
**Bearer Format:** JWT

Όλα τα protected endpoints απαιτούν JWT token στο Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 📊 API Endpoints Summary

### Authentication

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| `POST` | `/api/auth/login` | ❌ No | Public | Σύνδεση & λήψη JWT token |

### Users

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| `GET` | `/api/users/me` | ✅ Yes | Any | Προφίλ συνδεδεμένου χρήστη |

### Products

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| `GET` | `/api/products` | ✅ Yes | Any | Λίστα προϊόντων |
| `GET` | `/api/products/{id}` | ✅ Yes | Any | Λεπτομέρειες προϊόντος |

### Orders

| Method | Endpoint | Auth | Role | Description |
|--------|----------|------|------|-------------|
| `GET` | `/api/orders` | ✅ Yes | PHARMACIST | Ιστορικό παραγγελιών |
| `GET` | `/api/orders/{uuid}` | ✅ Yes | PHARMACIST | Λεπτομέρειες παραγγελίας |
| `POST` | `/pharmacist/orders/add-bulk` | ✅ Yes | PHARMACIST | Bulk δημιουργία παραγγελίας |

---

## 🧪 Testing with Postman

### 1. Import OpenAPI Spec

Κατέβασε το OpenAPI specification:
```bash
curl http://localhost:8080/v3/api-docs > openapi.json
```

Στο Postman: **Import → Upload Files → openapi.json**

### 2. Setup Environment Variables

Δημιούργησε environment με:
```
base_url: http://localhost:8080
token: <your-jwt-token-here>
```

### 3. Authentication Flow

1. **Login** → `POST /api/auth/login`
2. **Copy token** από το response
3. **Set token** στο Authorization tab (Type: Bearer Token)
4. **Test endpoints** με το token

---

## 🔍 Interactive API Documentation

Πρόσβαση στο Swagger UI για interactive testing:

**URL:** http://localhost:8080/swagger-ui/index.html

### Features:
- ✅ Live API testing
- ✅ Request/Response examples
- ✅ Schema visualization
- ✅ Authorization support

---

## ⚠️ Error Responses

### Common HTTP Status Codes

| Status | Description |
|--------|-------------|
| `200 OK` | Επιτυχής επεξεργασία |
| `201 Created` | Επιτυχής δημιουργία resource |
| `400 Bad Request` | Λάθος δεδομένα στο request |
| `401 Unauthorized` | Μη έγκυρο ή λείπει JWT token |
| `403 Forbidden` | Δεν έχετε δικαιώματα πρόσβασης |
| `404 Not Found` | Το resource δεν βρέθηκε |
| `500 Internal Server Error` | Σφάλμα στο server |

### Error Response Format

```json
{
  "timestamp": "2026-01-30T14:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "JWT token is invalid or expired",
  "path": "/api/users/me"
}
```

---

## 📱 Client Examples

### JavaScript (Fetch API)

```javascript
// Login
const login = async (username, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  const data = await response.json();
  return data.token;
};

// Get Products
const getProducts = async (token) => {
  const response = await fetch('http://localhost:8080/api/products', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  return await response.json();
};

// Create Order
const createOrder = async (token, items) => {
  const response = await fetch('http://localhost:8080/pharmacist/orders/add-bulk', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(items)
  });
  return await response.json();
};
```

### Python (Requests)

```python
import requests

# Login
def login(username, password):
    response = requests.post(
        'http://localhost:8080/api/auth/login',
        json={'username': username, 'password': password}
    )
    return response.json()['token']

# Get Products
def get_products(token):
    response = requests.get(
        'http://localhost:8080/api/products',
        headers={'Authorization': f'Bearer {token}'}
    )
    return response.json()

# Create Order
def create_order(token, items):
    response = requests.post(
        'http://localhost:8080/pharmacist/orders/add-bulk',
        headers={'Authorization': f'Bearer {token}'},
        json=items
    )
    return response.json()
```

---

## 📚 Additional Resources

- 📖 [Swagger UI](http://localhost:8080/swagger-ui/index.html) - Interactive documentation
- 📄 [OpenAPI Spec (JSON)](http://localhost:8080/v3/api-docs) - Machine-readable spec
- 📝 [OpenAPI Spec (YAML)](http://localhost:8080/v3/api-docs.yaml) - Human-readable spec

---

**💡 Tip:** Για production deployment, αλλάξτε το `base_url` και προσθέστε HTTPS support!
