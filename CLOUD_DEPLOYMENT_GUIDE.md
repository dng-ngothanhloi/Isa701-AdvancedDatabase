# Cloud Deployment & CORS Configuration Guide

## 🎯 **OVERVIEW**

Hướng dẫn này giúp bạn cấu hình ứng dụng warehouse management system để chạy trên các môi trường cloud như GitHub Codespaces, AWS, Azure, GCP và các dịch vụ cloud khác mà không gặp lỗi CORS.

---

## 🚀 **API ENDPOINT CONFIGURATION**

### **1. Environment.js Configuration**

Đã cập nhật `webpack/environment.js` để hỗ trợ parameter hóa API endpoint:

```javascript
// SERVER_API_URL configuration with environment variable support
// Priority order:
// 1. SERVER_API_URL environment variable (highest priority)
// 2. NODE_ENV based configuration
// 3. Default empty string (relative to current context)
SERVER_API_URL: (() => {
  // Check for explicit SERVER_API_URL environment variable
  if (process.env.SERVER_API_URL) {
    return process.env.SERVER_API_URL;
  }
  
  // Check for NODE_ENV based configuration
  const nodeEnv = process.env.NODE_ENV || 'development';
  
  switch (nodeEnv) {
    case 'production':
      return process.env.PROD_API_URL || '';
    case 'development':
      return process.env.DEV_API_URL || 'http://localhost:8080/';
    case 'test':
      return process.env.TEST_API_URL || 'http://localhost:8080/';
    default:
      return process.env.DEFAULT_API_URL || '';
  }
})(),
```

### **2. Environment Files**

**Development (env.development):**
```bash
SERVER_API_URL=http://localhost:8080/
DEV_API_URL=http://localhost:8080/
NODE_ENV=development
CLOUD_DEPLOYMENT=false
DEBUG=true
```

**Production (env.production):**
```bash
# SERVER_API_URL=https://your-production-domain.com/
# PROD_API_URL=https://your-production-domain.com/
NODE_ENV=production
CLOUD_DEPLOYMENT=true
DEBUG=false
```

**Cloud (env.cloud):**
```bash
# SERVER_API_URL=https://your-codespace-url.githubpreview.dev/
# DEV_API_URL=https://your-codespace-url.githubpreview.dev/
NODE_ENV=development
ENVIRONMENT=cloud
CLOUD_DEPLOYMENT=true
DEBUG=true
```

### **3. Environment Variable Loader**

Sử dụng script `load-env.sh` để load environment variables:

```bash
# Load development environment
./load-env.sh dev

# Load cloud environment
./load-env.sh cloud

# Load production environment
./load-env.sh prod

# Load custom environment
./load-env.sh custom my-env-file
```

### **4. Test API Configuration**

Sử dụng script `test-api-config.sh` để test API endpoint:

```bash
# Test current API configuration
./test-api-config.sh
```

---

## 🚀 **CẤU HÌNH CLOUD PROFILE**

### **1. Tạo Cloud Profile**

Đã tạo file `application-cloud.yml` với cấu hình tối ưu cho cloud:

```yaml
# Key configurations for cloud deployment
server:
  port: 8080
  address: 0.0.0.0  # Allow binding to all interfaces

jhipster:
  cors:
    allowed-origins: '*'  # Allow all origins
    allowed-origin-patterns: 'https://*.githubpreview.dev,https://*.codespaces.dev'
    allowed-methods: '*'
    allowed-headers: '*'
    allow-credentials: true
```

### **2. Kích hoạt Cloud Profile**

```bash
# Chạy với cloud profile
./mvnw spring-boot:run -Dspring.profiles.active=cloud

# Hoặc set environment variable
export SPRING_PROFILES_ACTIVE=cloud
./mvnw spring-boot:run
```

---

## 🌐 **CORS CONFIGURATION**

### **1. Cấu hình CORS hiện tại**

**Development (application-dev.yml):**
```yaml
cors:
  allowed-origins: 'http://localhost:8100,https://localhost:8100,http://localhost:9000,https://localhost:9000,http://localhost:9060,https://localhost:9060'
  allowed-origin-patterns: 'https://*.githubpreview.dev'
  allowed-methods: '*'
  allowed-headers: '*'
  allow-credentials: true
```

**Cloud (application-cloud.yml):**
```yaml
cors:
  allowed-origins: '*'  # Allow all origins
  allowed-origin-patterns: 'https://*.githubpreview.dev,https://*.codespaces.dev,https://*.cloudflare.dev,https://*.vercel.app,https://*.netlify.app'
  allowed-methods: '*'
  allowed-headers: '*'
  allow-credentials: true
```

### **2. CORS trong SecurityConfiguration**

```java
@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
            .cors(withDefaults())  // Enable CORS
            .csrf(csrf -> csrf.disable())
            // ... other configurations
    }
}
```

### **3. CORS trong WebConfigurer**

```java
@Bean
public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = jHipsterProperties.getCors();
    if (!CollectionUtils.isEmpty(config.getAllowedOrigins()) || !CollectionUtils.isEmpty(config.getAllowedOriginPatterns())) {
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/management/**", config);
        source.registerCorsConfiguration("/v3/api-docs", config);
        source.registerCorsConfiguration("/swagger-ui/**", config);
    }
    return new CorsFilter(source);
}
```

---

## 🔧 **DEPLOYMENT OPTIONS**

### **1. GitHub Codespaces**

**Codespaces Configuration (.devcontainer/devcontainer.json):**
```json
{
  "name": "Warehouse Management System",
  "image": "mcr.microsoft.com/devcontainers/java:17",
  "features": {
    "ghcr.io/devcontainers/features/mongodb:1": {}
  },
  "forwardPorts": [8080, 27017],
  "portsAttributes": {
    "8080": {
      "label": "Warehouse Management App",
      "onAutoForward": "notify"
    }
  },
  "postCreateCommand": "mvn clean compile",
  "customizations": {
    "vscode": {
      "extensions": [
        "vscjava.vscode-java-pack",
        "mongodb.mongodb-vscode"
      ]
    }
  }
}
```

**Chạy trong Codespaces:**
```bash
# Load cloud environment
./load-env.sh cloud

# Build và chạy với cloud profile
./mvnw clean compile
./mvnw spring-boot:run -Dspring.profiles.active=cloud
```

### **2. Docker Deployment**

**Dockerfile:**
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=cloud"]
```

**Docker Compose:**
```yaml
version: '3.8'
services:
  warehouse-app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=cloud
      - SERVER_URL=http://localhost:8080
      - SERVER_API_URL=http://localhost:8080/
    networks:
      - warehouse-network

networks:
  warehouse-network:
    driver: bridge
```

### **3. Cloud Platforms**

**AWS Elastic Beanstalk:**
```yaml
# .ebextensions/01_environment.config
option_settings:
  aws:elasticbeanstalk:application:environment:
    SPRING_PROFILES_ACTIVE: cloud
    SERVER_URL: http://your-eb-url.elasticbeanstalk.com
    SERVER_API_URL: http://your-eb-url.elasticbeanstalk.com/
  aws:elasticbeanstalk:container:java:
    Xms: 512m
    Xmx: 1024m
```

**Azure App Service:**
```bash
# Application settings
SPRING_PROFILES_ACTIVE=cloud
SERVER_URL=https://your-app.azurewebsites.net
SERVER_API_URL=https://your-app.azurewebsites.net/
```

---

## 🧪 **TESTING CORS & API CONFIGURATION**

### **1. Test CORS với curl**

```bash
# Test preflight request
curl -X OPTIONS \
  -H "Origin: https://your-domain.com" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type" \
  http://localhost:8080/api/selective-embedding-migration/migrate

# Test actual request
curl -X POST \
  -H "Origin: https://your-domain.com" \
  -H "Content-Type: application/json" \
  http://localhost:8080/api/selective-embedding-migration/migrate
```

### **2. Test API Configuration**

```bash
# Test API configuration
./test-api-config.sh

# Test CORS configuration
./test-cors.sh
```

### **3. Test với JavaScript**

```javascript
// Test CORS from browser
fetch('http://localhost:8080/api/selective-embedding-migration/stats', {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json',
  },
})
.then(response => response.json())
.then(data => console.log('Success:', data))
.catch(error => console.error('Error:', error));
```

### **4. Test với Postman**

1. **Preflight Request:**
   - Method: OPTIONS
   - URL: `http://localhost:8080/api/selective-embedding-migration/migrate`
   - Headers: `Origin: https://your-domain.com`

2. **Actual Request:**
   - Method: POST
   - URL: `http://localhost:8080/api/selective-embedding-migration/migrate`
   - Headers: `Origin: https://your-domain.com`

---

## ⚠️ **TROUBLESHOOTING CORS & API CONFIGURATION**

### **1. Lỗi CORS thường gặp**

**Lỗi: "Access to fetch at 'http://localhost:8080/api/...' from origin 'https://your-domain.com' has been blocked by CORS policy"**

**Giải pháp:**
```yaml
# Thêm domain vào allowed-origins
jhipster:
  cors:
    allowed-origins: 'https://your-domain.com,https://*.githubpreview.dev'
```

**Lỗi: "Request header field authorization is not allowed by Access-Control-Allow-Headers"**

**Giải pháp:**
```yaml
# Đảm bảo allowed-headers bao gồm Authorization
jhipster:
  cors:
    allowed-headers: 'Authorization,Content-Type,X-Requested-With'
```

### **2. Lỗi API Configuration**

**Lỗi: "SERVER_API_URL is not configured"**

**Giải pháp:**
```bash
# Set environment variable
export SERVER_API_URL=https://your-domain.com/

# Or use environment loader
./load-env.sh cloud
```

**Lỗi: "API endpoint not reachable"**

**Giải pháp:**
```bash
# Test API configuration
./test-api-config.sh

# Check if server is running
curl -I http://localhost:8080/management/health
```

### **3. Debug CORS & API**

**Enable CORS logging:**
```yaml
logging:
  level:
    org.springframework.web.cors: DEBUG
    org.springframework.security: DEBUG
```

**Check CORS headers:**
```bash
curl -I -H "Origin: https://your-domain.com" \
  http://localhost:8080/api/selective-embedding-migration/stats
```

**Check API configuration:**
```bash
# Test environment variables
echo $SERVER_API_URL
echo $NODE_ENV
echo $CLOUD_DEPLOYMENT

# Test webpack configuration
node -e "console.log(require('./webpack/environment.js'))"
```

### **4. Security Considerations**

**Production CORS (Restrictive):**
```yaml
jhipster:
  cors:
    allowed-origins: 'https://your-production-domain.com'
    allowed-methods: 'GET,POST,PUT,DELETE'
    allowed-headers: 'Authorization,Content-Type'
    allow-credentials: true
```

**Development CORS (Permissive):**
```yaml
jhipster:
  cors:
    allowed-origins: '*'
    allowed-methods: '*'
    allowed-headers: '*'
    allow-credentials: true
```

---

## 🚀 **MIGRATION EXECUTION ON CLOUD**

### **1. REST API Migration (Recommended for Cloud)**

```bash
# Load environment variables
./load-env.sh cloud

# Start application with cloud profile
./mvnw spring-boot:run -Dspring.profiles.active=cloud

# Run migration from any origin
curl -X POST \
  -H "Origin: https://your-domain.com" \
  -H "Content-Type: application/json" \
  http://your-server:8080/api/selective-embedding-migration/migrate
```

### **2. Standalone Migration Runner**

```bash
# Load environment variables
./load-env.sh cloud

# Run with cloud profile
./mvnw exec:java \
  -Dexec.mainClass="dtu.k30.msc.whm.MigrationRunner" \
  -Dexec.args="--selective" \
  -Dspring.profiles.active=cloud
```

### **3. Environment Variables**

```bash
# Set environment variables for cloud deployment
export SPRING_PROFILES_ACTIVE=cloud
export SERVER_URL=https://your-domain.com
export SERVER_API_URL=https://your-domain.com/
export MONGODB_URI=mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure

# Run application
./mvnw spring-boot:run
```

---

## 📋 **CHECKLIST FOR CLOUD DEPLOYMENT**

### **Pre-Deployment**
- [ ] Cấu hình `application-cloud.yml`
- [ ] Cấu hình `webpack/environment.js`
- [ ] Tạo environment files (`env.development`, `env.production`, `env.cloud`)
- [ ] Test API configuration với `./test-api-config.sh`
- [ ] Test CORS với domain thực tế
- [ ] Verify MongoDB connection
- [ ] Set environment variables
- [ ] Test migration endpoints

### **Deployment**
- [ ] Load environment variables với `./load-env.sh cloud`
- [ ] Deploy với cloud profile
- [ ] Verify application starts
- [ ] Test CORS headers
- [ ] Test API endpoints
- [ ] Run migration
- [ ] Verify functionality

### **Post-Deployment**
- [ ] Monitor logs
- [ ] Test all endpoints
- [ ] Verify data consistency
- [ ] Monitor performance
- [ ] Set up monitoring

---

## 🎉 **SUCCESS INDICATORS**

### **CORS Success**
- ✅ No CORS errors in browser console
- ✅ Preflight requests return 200 OK
- ✅ Actual requests work from any origin
- ✅ Authorization headers accepted

### **API Configuration Success**
- ✅ SERVER_API_URL is properly configured
- ✅ Environment variables are loaded correctly
- ✅ Webpack configuration works
- ✅ Axios interceptor uses correct base URL

### **Migration Success**
- ✅ Migration runs without CORS issues
- ✅ All endpoints accessible from cloud
- ✅ Data migration completes successfully
- ✅ Performance improvements achieved

---

## 📞 **SUPPORT**

Nếu gặp vấn đề với CORS hoặc cloud deployment:

1. **Check logs** for CORS-related errors
2. **Verify profile** is set to `cloud`
3. **Test API configuration** with `./test-api-config.sh`
4. **Test CORS** with `./test-cors.sh`
5. **Check network** connectivity
6. **Review security** configuration
7. **Verify environment variables** with `./load-env.sh`

Ứng dụng đã được cấu hình để hỗ trợ đầy đủ các môi trường cloud và không gặp lỗi CORS khi deploy.

---

## 🚀 **QUICK START FOR CLOUD**

```bash
# 1. Load cloud environment
./load-env.sh cloud

# 2. Test API configuration
./test-api-config.sh

# 3. Build application
./mvnw clean compile

# 4. Start with cloud profile
./mvnw spring-boot:run -Dspring.profiles.active=cloud

# 5. Test CORS
./test-cors.sh

# 6. Run migration
curl -X POST http://localhost:8080/api/selective-embedding-migration/migrate

# 7. Verify results
curl -X GET http://localhost:8080/api/selective-embedding-migration/stats
``` 