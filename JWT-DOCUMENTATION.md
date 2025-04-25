# JWT Authentication Documentation

## Overview

This document explains how to use JWT (JSON Web Token) authentication in the MeManager application. JWT is a compact, URL-safe means of representing claims to be transferred between two parties. The claims in a JWT are encoded as a JSON object that is used as the payload of a JSON Web Signature (JWS) structure.

## How JWT Works in MeManager

1. **User Authentication**: When a user logs in with valid credentials, the server generates a JWT token.
2. **Token Structure**: The token contains encoded user information (like username and roles) and an expiration time.
3. **Client Storage**: The client (frontend application) stores this token (typically in local storage or a cookie).
4. **Subsequent Requests**: For subsequent requests to protected resources, the client includes the token in the Authorization header.
5. **Server Validation**: The server validates the token and grants access to the protected resources if the token is valid.

## Authentication Endpoints

### Register a New User

```
POST /api/auth/register
```

**Request Body:**
```json
{
  "username": "user123",
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
- Success (200 OK): `"User registered successfully!"`
- Error (400 Bad Request): `"Error: Username is already taken!"` or `"Error: Email is already in use!"`

### Login

```
POST /api/auth/login
```

**Request Body:**
```json
{
  "username": "user123",
  "password": "password123"
}
```

**Response:**
- Success (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "user123",
  "email": "user@example.com",
  "roles": ["ROLE_USER"]
}
```
- Error (401 Unauthorized): Authentication failed

## Using the JWT Token

After receiving the token from the login endpoint, include it in the Authorization header for all subsequent requests to protected resources:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Example using fetch API in JavaScript:

```javascript
fetch('https://api.example.com/protected-resource', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

## Token Expiration

Tokens are valid for 24 hours (86400000 milliseconds) from the time of issuance. After expiration, the client must obtain a new token by authenticating again.

## Security Considerations

1. **Store tokens securely**: On the client side, store tokens in a secure manner (HttpOnly cookies are recommended for web applications).
2. **HTTPS**: Always use HTTPS to prevent token interception.
3. **Token Validation**: The server validates tokens for each request to protected resources.
4. **Logout**: To logout, the client should remove the token from storage.

## Troubleshooting

- **401 Unauthorized**: This could mean the token is invalid, expired, or not provided.
- **403 Forbidden**: The token is valid, but the user doesn't have the required permissions.

## Implementation Details

The JWT implementation in MeManager uses:
- Spring Security for authentication and authorization
- JJWT library for token generation and validation
- HS256 algorithm for token signing