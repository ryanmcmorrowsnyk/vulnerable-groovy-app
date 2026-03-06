# Vulnerable Groovy/Grails Application

**⚠️ WARNING: Intentionally vulnerable - NEVER deploy to production!**

## 🎯 Purpose

- **200+ dependency vulnerabilities**
- **18 code-level vulnerabilities** (OWASP Top 10)

## 📊 Vulnerabilities

### Dependencies (SCA)
- **40+ vulnerable Groovy/Java packages** from 2019
- Groovy 2.5.7, Grails 3.3.10, Spring Boot 2.1.6
- Expected **200+ total vulnerabilities**

### Code (SAST) - 18 Vulnerabilities

1. SQL Injection - App.groovy:66
2. Command Injection - App.groovy:77
3. Path Traversal - App.groovy:90
4. SSRF - App.groovy:103
5. Code Injection (Eval) - App.groovy:114
6. Insecure Deserialization - App.groovy:126
7. Mass Assignment - App.groovy:133
8. IDOR - App.groovy:150
9. Missing Authentication - App.groovy:157
10. Sensitive Data Exposure - App.groovy:166
11. Open Redirect - App.groovy:179
12. Weak Cryptography - MD5 - App.groovy:185
13. Insecure Randomness - App.groovy:193
14. Hardcoded Credentials - App.groovy:13-17, 201
15. Information Exposure - App.groovy:207
16. Missing Rate Limiting - App.groovy:217
17. XXE - App.groovy:227
18. LDAP Injection - App.groovy:238

## 🚀 Setup

```bash
git clone https://github.com/YOUR_USERNAME/vulnerable-groovy-app.git
cd vulnerable-groovy-app

./gradlew run
```

Access: `http://localhost:8080`

## 🔍 Testing

```bash
snyk test
# Expected: 200+ vulnerabilities
```

## 📚 Endpoints

- POST /api/login - SQL Injection
- GET /api/exec?cmd=ls - Command Injection
- GET /api/files?filename=test.txt - Path Traversal
- GET /api/proxy?url=http://example.com - SSRF
- POST /api/eval - Code Injection
- POST /api/register - Mass Assignment
- GET /api/users/:id - IDOR
- DELETE /api/admin/users/:id - Missing Auth
- GET /api/debug - Sensitive Data Exposure
- And 6 more...

## ⚠️ Security Notice

Educational use only. DO NOT deploy to production.

MIT License - Testing purposes only.
