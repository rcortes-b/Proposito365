# Endpoints - Information and Response Codes

**Note that every endpoint has a 200 OK status code!**

---

## EmailVerification

---

### /api/email-validation POST

**Validates the email when the user requests to change the email and if it is valid generates the code and sends to the email**

**Dependencies from the client: email**

| Status | Code | Reason |
|--------|------|------|
| 401 | EMAIL_NOT_VALID | The email is already in use |

---

### /api/email-verification POST

**Verifies that the user request has to verify / requested an email change. Compares the code with the generated code**
**If the code is valid, updates the authenticated user and generates new cookies**

**Dependencies from the client: email, token**

| Status | Code | Reason |
|--------|------|------|
| 401 | EMAIL_NOT_VALID | The email given in the request haven't requested an email change or a registration |
| 410 | TOKEN_EXPIRED | The verification code expires in 15 mins. The request has been done +15 mins later |
| 401 | INVALID_TOKEN | The given code don't match with the generated code |

---

## Users

---

### /api/users GET

**Retrieves the user information**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/users/change-username PATCH

**Change the logged user username**

**Dependencies from the client: username**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 401 | INVALID_USER | The specified user already exists |

---

### /api/users DELETE

**Deletes the logged user account**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/users/{groupId}/join POST

**The logged user joins to the specified groupId**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The group doesn't exist |

---

### /api/users/{groupId}/leave POST

**The logged user leaves from the specified groupId**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The group doesn't exist |
| 401 | INVALID_RELATION | The user don't belong to the specified group |

---

## Resolutions

---

### /api/resolutions GET

**Retrieve the resolutions from the logged user**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/resolutions POST

**The logged user creates a new resolution**

**Dependencies from the client: resolution (title), details**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/resolutions/{resolutionId} PATCH

**The logged user deletes the resolution specified by resolutionId**

**Dependencies from the client: resolution (title) OR details OR status OR some/all of them**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | RESOLUTION_NOT_FOUND | The resolution specified by resolutionId doesn't exist |
| 401 | BAD_REQUEST | The user and resolution ids cannot be modified |

---

### /api/resolutions/{resolutionId} DELETE

**The logged user deletes the resolution specified by resolutionId**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | RESOLUTION_NOT_FOUND | The resolution specified by resolutionId doesn't exist |

---