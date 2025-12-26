# Endpoints - Information and Response Codes

**Note that every endpoint has a 200 OK status code!**

---
---

## EmailVerification

---

### /api/email-validation POST

- **USE: Validates the email when the user requests to change the email. If it is valid, generates the code and it's sent to the email**

- **BODY DEPENDENCIES: email**

| Status | Code | Reason |
|--------|------|------|
| 401 | EMAIL_NOT_VALID | The email is already in use |

---

### /api/email-verification POST

- **USE: Verifies that the user has to be verified or requested an email change. Compares the code with the generated code. If the code is valid, updates the authenticated user and generates new cookies**

- **BODY DEPENDENCIES: email, token**

| Status | Code | Reason |
|--------|------|------|
| 401 | EMAIL_NOT_VALID | The email given in the request haven't requested an email change or a registration |
| 410 | TOKEN_EXPIRED | The verification code expires in 15 mins. The request has been done +15 mins later |
| 401 | INVALID_TOKEN | The given code don't match with the generated code |

---
---

## Users

---

### /api/users GET

- **USE: Retrieves the user profile information: email and username**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/users/change-username PATCH

- **USE: Change the username of the authenticated user**

- **BODY DEPENDENCIES: username**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 400 | INVALID_USER | The new username is already in use |

---

### /api/users DELETE

- **USE: Deletes the logged user account**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/users/change-password PATCH

- **USE: Change the password of the authenticated user**

- **BODY DEPENDENCIES: oldPassword and newPassword**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 400 | INVALID_PASSWORD | The old password don't match with the current password |
| 400 | INVALID_PASSWORD | The new password is the same than the current password |

---
---

## Resolutions

---

### /api/resolutions GET

- **USE: Retrieves the authenticated user resolutions**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/resolutions POST

- **USE: The authenticated user creates a new resolution**

- **BODY DEPENDENCIES: resolution (title), details**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/resolutions/{resolutionId} PATCH

- **USE: The authenticated user deletes the resolution specified by resolutionId**

- **BODY DEPENDENCIES: resolution (title) OR details OR status OR some/all of them**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | RESOLUTION_NOT_FOUND | The resolution specified by resolutionId doesn't exist |
| 400 | INVALID_REQUEST | The user and resolution ids cannot be modified |

---

### /api/resolutions/{resolutionId} DELETE

- **USE: The logged user deletes the resolution specified by resolutionId**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | RESOLUTION_NOT_FOUND | The resolution specified by resolutionId doesn't exist |

---
---

## Groups

---

### /api/groups GET

- **USE: Get the groups that the authenticated user is participant**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/groups POST

- **USE: Create a new group**

- **BODY DEPENDENCIES: (group) name, description**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |

---

### /api/groups/{groupId} PATCH

- **USE: Update the group information (must be admin)**

- **BODY DEPENDENCIES: name OR description OR BOTH**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The groupId don't match with any group |
| 401 | INVALID_RELATION | The authenticated user is not a participant of the group |
| 401 | INVALID_ADMIN | The authenticated user is not the admin of the group |
| 400 | INVALID_REQUEST | The request sent is not valid, groupId and Capacity cannot be modified |

---

### /api/groups/{groupId} DELETE

- **USE: Deletes the group (must be admin)**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The groupId don't match with any group |
| 401 | INVALID_RELATION | The authenticated user is not a participant of the group |
| 401 | INVALID_ADMIN | The authenticated user is not the admin of the group |

---

### /api/groups/{groupId} GET

- **USE: Gets the group info: name, description and the list of usernames of the participants**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The groupId don't match with any group |
| 401 | INVALID_RELATION | The authenticated user is not a participant of the group |

---

### /api/groups/{groupId}/{username} GET

- **USE: Gets the resolutions of the specified group participant (user)**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The groupId don't match with any group |
| 401 | INVALID_RELATION | The authenticated user is not a participant of the group |

**NOTE: Aquí hay otro USER_NOT_FOUND si el user 'username' no existe y también otro INVALID_RELATION**

---

### /api/groups/{groupId}/join POST

- **USE: The user joins to the specified group**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The groupId don't match with any group |
| 403 | GROUP_IS_FULL | Cannot join the group because its capacity is full |

---

### /api/groups/{groupId}/leave POST

- **USE: The user leaves from the specified group**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The groupId don't match with any group |
| 401 | INVALID_RELATION | The authenticated user is not a participant of the group |
| 403 | FORBIDDEN_ACTION | The admin cannot leave the group if there's more than one user |

---

### /api/groups/{groupId}/change-admin POST

- **USE: Changes the group administrator**

- **BODY DEPENDENCIES: username**

| Status | Code | Reason |
|--------|------|------|
| 404 | USER_NOT_FOUND | The logged user cannot be found in the database |
| 404 | GROUP_NOT_FOUND | The groupId don't match with any group |
| 401 | INVALID_RELATION | The authenticated user is not a participant of the group |
| 401 | INVALID_ADMIN | The authenticated user is not the admin of the group |

**NOTE: Aquí hay otro USER_NOT_FOUND si el user 'username' no existe y también otro INVALID_RELATION**

---
---