# 🌍 Environment Selection Guide

## 🎯 **OVERVIEW**

Hướng dẫn này giúp developer chọn đúng environment và build command cho từng môi trường phát triển.

---

## 🏠 **DEVELOPMENT ENVIRONMENT (LOCAL)**

### **Mục đích:**
- Phát triển ứng dụng trên máy local
- Debug và test features
- Sử dụng localhost URLs

### **Commands:**
```bash
# Cách 1: Sử dụng Maven webapp profile (khuyến nghị)
./mvnw spring-boot:run -Pwebapp

# Cách 2: Sử dụng development Spring profile
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Cách 3: Load environment variables thủ công
export $(cat env.development | grep -v '^#' | xargs)
./mvnw spring-boot:run
```

### **Configuration:**
- **Frontend API**: `http://localhost:8080`
- **Backend URL**: `http://localhost:8080`
- **Environment**: `development`
- **Profile**: `dev`
- **CORS**: Localhost only
- **Database**: Local MongoDB

### **Environment Variables:**
```bash
NODE_ENV=development
SPRING_PROFILES_ACTIVE=dev
SERVER_API_URL=http://localhost:8080/
SERVER_API_URL_WS=ws://localhost:8080/
CLOUD_DEPLOYMENT=false
```

### **Use Cases:**
- ✅ Local development
- ✅ Feature development
- ✅ Debugging
- ✅ Unit testing
- ✅ Integration testing

---

## ☁️ **CLOUD ENVIRONMENT (CODESPACES)**

### **Mục đích:**
- Chạy ứng dụng trên GitHub Codespaces
- Demo và testing trên cloud
- Collaboration với team

### **Commands:**
```bash
# Cách 1: Sử dụng Maven cloud profile (khuyến nghị)
./mvnw spring-boot:run -Pcloud

# Cách 2: Sử dụng cloud Spring profile
./mvnw spring-boot:run -Dspring.profiles.active=cloud

# Cách 3: Load environment variables thủ công
export $(cat env.cloud | grep -v '^#' | xargs)
./mvnw spring-boot:run -Dspring.profiles.active=cloud

# Cách 4: Set Codespace ID và chạy
./set-codespace-id.sh super-broccoli-pj96jxxr4p7q3945r
./mvnw spring-boot:run -Pcloud
```

### **Configuration:**
- **Frontend API**: `https://{codespace-id}-8080.app.github.dev`
- **Backend URL**: `https://{codespace-id}-8080.app.github.dev`
- **Environment**: `cloud`
- **Profile**: `cloud`
- **CORS**: All cloud domains
- **Database**: Cloud MongoDB

### **Environment Variables:**
```bash
NODE_ENV=cloud
SPRING_PROFILES_ACTIVE=cloud
SERVER_API_URL=https://{codespace-id}-8080.app.github.dev/
SERVER_API_URL_WS=wss://{codespace-id}-8080.app.github.dev/
CLOUD_DEPLOYMENT=true
CODESPACE_ID=super-broccoli-pj96jxxr4p7q3945r
```

### **Use Cases:**
- ✅ GitHub Codespaces development
- ✅ Cloud demos
- ✅ Team collaboration
- ✅ Cloud testing
- ✅ Remote development

---

## 🚀 **PRODUCTION ENVIRONMENT**

### **Mục đích:**
- Deploy ứng dụng lên production servers
- Live application cho end users
- High performance và security

### **Commands:**
```bash
# Build cho production
./mvnw clean package -Pprod

# Chạy production JAR
java -jar target/*.jar --spring.profiles.active=prod

# Hoặc với Docker
docker run -p 8080:8080 warehouse-app:latest
```

### **Configuration:**
- **Frontend API**: Production URL (configurable)
- **Backend URL**: Production URL (configurable)
- **Environment**: `production`
- **Profile**: `prod`
- **CORS**: Restricted to production domains
- **Database**: Production MongoDB

### **Environment Variables:**
```bash
NODE_ENV=production
SPRING_PROFILES_ACTIVE=prod
SERVER_API_URL=https://your-production-domain.com/
SERVER_API_URL_WS=wss://your-production-domain.com/
CLOUD_DEPLOYMENT=false
```

### **Use Cases:**
- ✅ Production deployment
- ✅ Live application
- ✅ End user access
- ✅ High availability
- ✅ Performance optimization

---

## 🔧 **ENVIRONMENT DETECTION & MANAGEMENT**

### **Smart Environment Script:**
```bash
# Kiểm tra environment hiện tại
./smart-env.sh --check

# Build với auto-detect environment
./smart-env.sh --build

# Kiểm tra và build (mặc định)
./smart-env.sh
```

### **Environment Testing:**
```bash
# Test environment configuration
./test-codespace-env.sh

# Check environment variables
./check-environment.sh

# Test API configuration
./test-api-config.sh
```

### **Environment Loading:**
```bash
# Load development environment
./load-and-test-env.sh dev

# Load cloud environment
./load-and-test-env.sh cloud

# Load production environment
./load-and-test-env.sh prod
```

---

## 📋 **ENVIRONMENT COMPARISON**

| Aspect | Development | Cloud | Production |
|--------|-------------|-------|------------|
| **Command** | `./mvnw spring-boot:run -Pwebapp` | `./mvnw spring-boot:run -Pcloud` | `./mvnw clean package -Pprod` |
| **Frontend API** | `http://localhost:8080` | `https://{codespace-id}-8080.app.github.dev` | Production URL |
| **Backend URL** | `http://localhost:8080` | `https://{codespace-id}-8080.app.github.dev` | Production URL |
| **Environment** | `development` | `cloud` | `production` |
| **Profile** | `dev` | `cloud` | `prod` |
| **CORS** | Localhost only | All cloud domains | Restricted |
| **Database** | Local MongoDB | Cloud MongoDB | Production MongoDB |
| **Debug** | Enabled | Enabled | Disabled |
| **Performance** | Development | Cloud optimized | Production optimized |

---

## 🚨 **TROUBLESHOOTING**

### **Common Issues:**

1. **Wrong API URLs:**
   ```bash
   # Check current environment
   ./check-environment.sh
   
   # Load correct environment
   ./load-and-test-env.sh dev    # hoặc cloud
   ```

2. **CORS Errors:**
   ```bash
   # Check CORS configuration
   ./debug-cors-issues.sh
   
   # Test with correct profile
   ./mvnw spring-boot:run -Pcloud
   ```

3. **Build Failures:**
   ```bash
   # Test cloud build
   ./test-cloud-build.sh
   
   # Debug classpath
   ./debug-classpath.sh
   ```

4. **Environment Mismatch:**
   ```bash
   # Check environment variables
   echo $NODE_ENV
   echo $SPRING_PROFILES_ACTIVE
   echo $SERVER_API_URL
   
   # Reset environment
   ./smart-env.sh --check
   ```

### **Debug Commands:**
```bash
# Test environment detection
./smart-env.sh --check

# Test API configuration
./test-api-config.sh

# Test CORS configuration
./test-cors.sh

# Test Spring profiles
./test-spring-profiles.sh
```

---

## 🎯 **QUICK REFERENCE**

### **Development (Local):**
```bash
./mvnw spring-boot:run -Pwebapp
# Frontend calls: http://localhost:8080
```

### **Cloud (Codespaces):**
```bash
./mvnw spring-boot:run -Pcloud
# Frontend calls: https://{codespace-id}-8080.app.github.dev
```

### **Production:**
```bash
./mvnw clean package -Pprod
java -jar target/*.jar --spring.profiles.active=prod
# Frontend calls: Production URL
```

---

## 📞 **SUPPORT**

### **Environment Issues:**
1. Check current environment: `./check-environment.sh`
2. Load correct environment: `./load-and-test-env.sh {env}`
3. Test configuration: `./test-api-config.sh`
4. Debug issues: `./smart-env.sh --check`

### **Build Issues:**
1. Test build: `./test-cloud-build.sh`
2. Debug classpath: `./debug-classpath.sh`
3. Check logs: `./test-cloud-startup.sh`

### **CORS Issues:**
1. Test CORS: `./test-cors.sh`
2. Debug CORS: `./debug-cors-issues.sh`
3. Check configuration: `./test-spring-profiles.sh`

---

## 🎉 **SUCCESS INDICATORS**

### **Development Success:**
- ✅ Application runs on `http://localhost:8080`
- ✅ Frontend calls localhost APIs
- ✅ No CORS errors
- ✅ Database connection established
- ✅ Startup log shows correct Local URL

### **Cloud Success:**
- ✅ Application runs on Codespaces URL
- ✅ Frontend calls cloud APIs
- ✅ CORS allows cloud domains
- ✅ Cloud database connected
- ✅ Startup log shows SERVER_API_URL as Local URL

### **Production Success:**
- ✅ Application runs on production URL
- ✅ Frontend calls production APIs
- ✅ CORS restricted to production domains
- ✅ Production database connected
- ✅ Startup log shows production URL

## 🚀 **STARTUP URL DISPLAY**

### **Feature Overview:**
Ứng dụng đã được cập nhật để hiển thị `SERVER_API_URL` trong startup logs thay vì localhost mặc định.

### **Before (Old):**
```
Application 'WarehousMmgmt' is running! Access URLs:
        Local:          http://localhost:8080/
        External:       http://127.0.0.1:8080/
        Profile(s):     [cloud]
```

### **After (New):**
```
Application 'WarehousMmgmt' is running! Access URLs:
        Local:          https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
        External:       http://127.0.0.1:8080/
        Profile(s):     [cloud]
```

### **How it works:**
1. **With SERVER_API_URL**: Hiển thị giá trị từ biến môi trường
2. **Without SERVER_API_URL**: Fallback về localhost như cũ
3. **Automatic detection**: Tự động detect từ environment variables

### **Test the feature:**
```bash
# Test with development environment
export SERVER_API_URL="http://localhost:8080/"
./mvnw spring-boot:run -Pwebapp

# Test with cloud environment
export SERVER_API_URL="https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/"
./mvnw spring-boot:run -Pcloud

# Test without SERVER_API_URL (fallback)
unset SERVER_API_URL
./mvnw spring-boot:run -Pwebapp
```

### **Test scripts:**
```bash
# Simple test
./test-url-display.sh

# Comprehensive test
./test-startup-urls.sh test
```

Bây giờ bạn có thể dễ dàng chọn và chuyển đổi giữa các môi trường! 🚀 