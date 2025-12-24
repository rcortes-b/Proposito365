## EmailVerification

### /api/email-validation

| Status | Code | Reason |
|--------|------|------|
| 404 | EMAIL_NOT_VALID | The email is already in use |

### /api/email-verification

| Status | Code | Reason |
|--------|------|------|
| 404 | EMAIL_NOT_VALID | The email given in the request haven't requested an email change or a registration |
| 404 | TOKEN_EXPIRED | The verification code expires in 15 mins. The request has been done +15 mins later |
| 404 | INVALID_TOKEN | The token given doesn't match with the token generated |
