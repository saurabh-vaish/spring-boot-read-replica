# Error Response Format

This application uses RFC 7807 Problem Details for HTTP APIs to provide standardized error responses.

## ProblemDetails Response Format

All error responses follow the RFC 7807 standard and include the following fields:

```json
{
  "type": "https://api.readreplica.com/problems/token-not-found",
  "title": "Token Not Found",
  "status": 404,
  "detail": "Token not found with id: 123",
  "timestamp": "2025-07-23T22:45:30.123456Z",
  "path": "/api/tokens/123"
}
```

## Error Types

### 1. Token Not Found (404)
**Type URI:** `https://api.readreplica.com/problems/token-not-found`

Returned when a requested token does not exist.

**Example Response:**
```json
{
  "type": "https://api.readreplica.com/problems/token-not-found",
  "title": "Token Not Found",
  "status": 404,
  "detail": "Token not found with id: 123",
  "timestamp": "2025-07-23T22:45:30.123456Z",
  "path": "/api/tokens/123"
}
```

### 2. Token Service Error (500)
**Type URI:** `https://api.readreplica.com/problems/token-service-error`

Returned when there's an error in the token service logic.

**Example Response:**
```json
{
  "type": "https://api.readreplica.com/problems/token-service-error",
  "title": "Token Service Error",
  "status": 500,
  "detail": "Failed to process token operation",
  "timestamp": "2025-07-23T22:45:30.123456Z",
  "path": "/api/tokens"
}
```

### 3. Database Error (500)
**Type URI:** `https://api.readreplica.com/problems/database-error`

Returned when there's a database access error.

**Example Response:**
```json
{
  "type": "https://api.readreplica.com/problems/database-error",
  "title": "Database Error",
  "status": 500,
  "detail": "An error occurred while accessing the database",
  "timestamp": "2025-07-23T22:45:30.123456Z",
  "path": "/api/tokens",
  "errorType": "DataAccessException"
}
```

### 4. Transaction Error (500)
**Type URI:** `https://api.readreplica.com/problems/transaction-error`

Returned when there's a transaction processing error.

**Example Response:**
```json
{
  "type": "https://api.readreplica.com/problems/transaction-error",
  "title": "Transaction Error",
  "status": 500,
  "detail": "An error occurred during transaction processing",
  "timestamp": "2025-07-23T22:45:30.123456Z",
  "path": "/api/tokens",
  "errorType": "TransactionException"
}
```

### 5. Invalid Argument (400)
**Type URI:** `https://api.readreplica.com/problems/invalid-argument`

Returned when invalid arguments are provided.

**Example Response:**
```json
{
  "type": "https://api.readreplica.com/problems/invalid-argument",
  "title": "Invalid Argument",
  "status": 400,
  "detail": "Invalid token value provided",
  "timestamp": "2025-07-23T22:45:30.123456Z",
  "path": "/api/tokens/123"
}
```

### 6. Internal Server Error (500)
**Type URI:** `https://api.readreplica.com/problems/internal-server-error`

Returned for unexpected errors.

**Example Response:**
```json
{
  "type": "https://api.readreplica.com/problems/internal-server-error",
  "title": "Internal Server Error",
  "status": 500,
  "detail": "An unexpected error occurred",
  "timestamp": "2025-07-23T22:45:30.123456Z",
  "path": "/api/tokens",
  "errorType": "RuntimeException"
}
```

## Benefits of ProblemDetails

1. **Standardized Format**: Follows RFC 7807 standard
2. **Machine Readable**: Consistent structure for automated error handling
3. **Human Readable**: Clear titles and descriptions
4. **Extensible**: Can include additional properties as needed
5. **Type URIs**: Unique identifiers for each error type
6. **Debugging Information**: Includes timestamps, paths, and error types
