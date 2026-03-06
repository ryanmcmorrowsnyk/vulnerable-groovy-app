// Intentionally Vulnerable Groovy/Grails Application
// DO NOT USE IN PRODUCTION - FOR SECURITY TESTING ONLY

package com.vulnerable

import groovy.sql.Sql
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.security.MessageDigest

// VULNERABILITY: Hardcoded Secrets (CWE-798)
class App {
    static final String JWT_SECRET = 'super_secret_jwt_key_12345'
    static final String ADMIN_PASSWORD = 'admin123'
    static final String DB_PASSWORD = 'password123'
    static final String API_KEY = 'AKIA_FAKE_GROOVY_KEY_FOR_TESTING_ONLY'
    static final String AWS_SECRET_KEY = 'fake_aws_secret_key_12345678901234567890'

    // In-memory user store
    static final List<Map> users = [
        [id: 1, username: 'admin', password: 'hashed_password', email: 'admin@example.com', role: 'admin'],
        [id: 2, username: 'user', password: 'hashed_password', email: 'user@example.com', role: 'user']
    ]

    static void main(String[] args) {
        println '======================================'
        println 'Vulnerable Groovy Application'
        println 'WARNING: For security testing only!'
        println '======================================'
        println()

        // Demonstrate vulnerabilities
        testVulnerabilities()
    }

    static void testVulnerabilities() {
        println 'Testing vulnerabilities:'

        // Test SQL injection
        println '\n1. SQL Injection:'
        def loginResult = handleLogin("admin' OR '1'='1", 'anything')
        println loginResult

        // Test command injection
        println '\n2. Command Injection:'
        def execResult = handleExec('ls; whoami')
        println execResult

        // Test weak crypto
        println '\n3. Weak Cryptography (MD5):'
        def hash = hashPassword('password123')
        println "MD5 Hash: ${hash}"

        // Test debug endpoint
        println '\n4. Information Exposure:'
        def debugResult = handleDebug()
        println debugResult

        println '\n======================================'
        println 'Application demonstrates 18+ vulnerabilities'
        println 'See README.md for full vulnerability list'
        println '======================================'
    }

    // VULNERABILITY 1: SQL Injection (CWE-89)
    static String handleLogin(String username, String password) {
        // Vulnerable: String concatenation in SQL query
        def query = "SELECT * FROM users WHERE username = '${username}' AND password = '${password}'"

        return JsonOutput.toJson([
            query: query,
            vulnerable: true
        ])
    }

    // VULNERABILITY 2: Command Injection (CWE-78)
    static String handleExec(String cmd) {
        // Vulnerable: Direct execution of user input
        def command = "sh -c '${cmd}'"
        def process = command.execute()
        def output = process.text

        return JsonOutput.toJson([
            success: true,
            output: output
        ])
    }

    // VULNERABILITY 3: Path Traversal (CWE-22)
    static String handleReadFile(String filename) {
        // Vulnerable: No sanitization of file path
        def path = "./uploads/${filename}"

        try {
            def content = new File(path).text
            return JsonOutput.toJson([content: content])
        } catch (Exception e) {
            return JsonOutput.toJson([error: e.message])
        }
    }

    // VULNERABILITY 4: SSRF (CWE-918)
    static String handleProxy(String url) {
        // Vulnerable: No URL validation
        try {
            def content = new URL(url).text
            return JsonOutput.toJson([data: content.take(500)])
        } catch (Exception e) {
            return JsonOutput.toJson([error: e.message])
        }
    }

    // VULNERABILITY 5: Code Injection / Eval (CWE-94)
    static String handleEval(String code) {
        // Vulnerable: Groovy eval pattern
        try {
            def shell = new GroovyShell()
            def result = shell.evaluate(code)
            return JsonOutput.toJson([result: result?.toString()])
        } catch (Exception e) {
            return JsonOutput.toJson([error: e.message])
        }
    }

    // VULNERABILITY 6: Insecure Deserialization (CWE-502)
    static Object deserialize(String data) {
        // Vulnerable: Deserializing untrusted data
        def slurper = new JsonSlurper()
        return slurper.parseText(data)
    }

    // VULNERABILITY 7: Mass Assignment (CWE-915)
    static String handleRegister(Map data) {
        // Vulnerable: Allows setting 'role' field directly
        def newUser = [
            id: users.size() + 1,
            username: data.username,
            password: data.password,
            email: data.email,
            role: data.role ?: 'user'  // Attacker can set role=admin
        ]

        return JsonOutput.toJson([
            success: true,
            user: newUser
        ])
    }

    // VULNERABILITY 8: IDOR (CWE-639)
    static String getUser(int userId) {
        // Vulnerable: No authorization check
        def user = users.find { it.id == userId }
        return JsonOutput.toJson([user: user])
    }

    // VULNERABILITY 9: Missing Authentication (CWE-306)
    static String deleteUser(int userId) {
        // Vulnerable: No authentication required
        return JsonOutput.toJson([
            success: true,
            deleted: userId
        ])
    }

    // VULNERABILITY 10: Sensitive Data Exposure (CWE-200)
    static String handleDebug() {
        // Vulnerable: Exposes sensitive information
        return JsonOutput.toJson([
            jwt_secret: JWT_SECRET,
            admin_password: ADMIN_PASSWORD,
            db_password: DB_PASSWORD,
            api_key: API_KEY,
            aws_secret_key: AWS_SECRET_KEY,
            users: users
        ])
    }

    // VULNERABILITY 11: Open Redirect (CWE-601)
    static String handleRedirect(String url) {
        // Vulnerable: No validation of redirect URL
        return "Redirecting to: ${url}"
    }

    // VULNERABILITY 12: Weak Cryptography - MD5 (CWE-327)
    static String hashPassword(String password) {
        // Vulnerable: Using MD5 for password hashing
        def md = MessageDigest.getInstance('MD5')
        md.update(password.bytes)
        return md.digest().encodeHex().toString()
    }

    // VULNERABILITY 13: Insecure Random (CWE-330)
    static String generateToken() {
        // Vulnerable: Using predictable random
        def random = new Random()
        def token = random.nextInt(999999999)
        return token.toString()
    }

    // VULNERABILITY 14: Hardcoded Credentials (CWE-798)
    static boolean adminLogin(String password) {
        // Vulnerable: Hardcoded admin password
        return password == ADMIN_PASSWORD
    }

    // VULNERABILITY 15: Information Exposure Through Error Messages (CWE-209)
    static String handleDatabaseConnect() {
        // Vulnerable: Detailed error messages exposed
        def errorMsg = "Connection failed: Access denied for user 'postgres'@'localhost' using password '${DB_PASSWORD}'"
        return JsonOutput.toJson([
            error: errorMsg,
            stackTrace: 'Simulated stack trace'
        ])
    }

    // VULNERABILITY 16: Missing Rate Limiting (CWE-770)
    static String handleBruteForceTarget(String password) {
        // Vulnerable: No rate limiting
        if (password == 'correct_password') {
            return JsonOutput.toJson([success: true])
        } else {
            return JsonOutput.toJson([success: false])
        }
    }

    // VULNERABILITY 17: XML External Entity (XXE) (CWE-611)
    static String parseXml(String xmlData) {
        // Vulnerable: XML parsing without disabling external entities
        try {
            def xml = new XmlSlurper().parseText(xmlData)
            return JsonOutput.toJson([parsed: xml.toString()])
        } catch (Exception e) {
            return JsonOutput.toJson([error: e.message])
        }
    }

    // VULNERABILITY 18: LDAP Injection (CWE-90)
    static String ldapSearch(String username) {
        // Vulnerable: User input in LDAP filter without sanitization
        def filter = "(uid=${username})"
        return JsonOutput.toJson([
            filter: filter,
            vulnerable: true
        ])
    }

    // HTTP request handler
    static String handleRequest(String method, String path, Map params, String body) {
        if (path == '/') {
            return """
                <html>
                <head><title>Vulnerable Groovy App</title></head>
                <body>
                    <h1>Intentionally Vulnerable Groovy/Grails Application</h1>
                    <p>This application contains numerous security vulnerabilities for testing purposes.</p>
                    <h2>Available Endpoints:</h2>
                    <ul>
                        <li>POST /api/login - SQL Injection</li>
                        <li>GET /api/exec?cmd=ls - Command Injection</li>
                        <li>GET /api/files?filename=test.txt - Path Traversal</li>
                        <li>GET /api/proxy?url=http://example.com - SSRF</li>
                        <li>POST /api/eval - Code Injection</li>
                        <li>POST /api/register - Mass Assignment</li>
                        <li>GET /api/users/:id - IDOR</li>
                        <li>DELETE /api/admin/users/:id - Missing Authentication</li>
                        <li>GET /api/debug - Sensitive Data Exposure</li>
                        <li>GET /api/redirect?url=http://example.com - Open Redirect</li>
                        <li>POST /api/xml - XXE</li>
                        <li>GET /api/ldap?username=admin - LDAP Injection</li>
                    </ul>
                </body>
                </html>
            """
        } else if (path == '/api/login' && method == 'POST') {
            def data = deserialize(body)
            return handleLogin(data.username, data.password)
        } else if (path == '/api/exec' && method == 'GET') {
            return handleExec(params.cmd)
        } else if (path == '/api/files' && method == 'GET') {
            return handleReadFile(params.filename)
        } else if (path == '/api/proxy' && method == 'GET') {
            return handleProxy(params.url)
        } else if (path == '/api/eval' && method == 'POST') {
            def data = deserialize(body)
            return handleEval(data.code)
        } else if (path == '/api/register' && method == 'POST') {
            def data = deserialize(body)
            return handleRegister(data)
        } else if (path.startsWith('/api/users/') && method == 'GET') {
            def userId = path.split('/').last().toInteger()
            return getUser(userId)
        } else if (path.startsWith('/api/admin/users/') && method == 'DELETE') {
            def userId = path.split('/').last().toInteger()
            return deleteUser(userId)
        } else if (path == '/api/debug' && method == 'GET') {
            return handleDebug()
        } else if (path == '/api/redirect' && method == 'GET') {
            return handleRedirect(params.url)
        } else if (path == '/api/hash' && method == 'POST') {
            def data = deserialize(body)
            def hash = hashPassword(data.password)
            return JsonOutput.toJson([hash: hash, algorithm: 'MD5'])
        } else if (path == '/api/generate-token' && method == 'GET') {
            def token = generateToken()
            return JsonOutput.toJson([token: token, algorithm: 'Predictable'])
        } else if (path == '/api/admin-login' && method == 'POST') {
            def data = deserialize(body)
            def success = adminLogin(data.password)
            return JsonOutput.toJson([success: success, role: success ? 'admin' : null])
        } else if (path == '/api/database-connect' && method == 'GET') {
            return handleDatabaseConnect()
        } else if (path == '/api/brute-force-target' && method == 'POST') {
            def data = deserialize(body)
            return handleBruteForceTarget(data.password)
        } else if (path == '/api/xml' && method == 'POST') {
            return parseXml(body)
        } else if (path == '/api/ldap' && method == 'GET') {
            return ldapSearch(params.username)
        } else {
            return 'Not Found'
        }
    }
}
