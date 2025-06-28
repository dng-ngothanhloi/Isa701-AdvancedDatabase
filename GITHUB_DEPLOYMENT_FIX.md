# 🔧 GitHub Deployment Fix Guide

## 🚨 **LỖI HIỆN TẠI**

```
[ERROR] Failed to execute goal com.github.eirslett:frontend-maven-plugin:1.15.1:npm (webapp build dev) on project warehouse-mgmt: Failed to run task: 'npm run webapp:build' failed. org.apache.commons.exec.ExecuteException: Process exited with an error: 2
```

## 🎯 **NGUYÊN NHÂN**

Lỗi này xảy ra do:
1. **Syntax error trong `webpack/environment.js`**
2. **Package name typo trong `package.json`**
3. **Thiếu environment variables cho cloud deployment**
4. **Node.js version mismatch**
5. **Dependencies conflicts**

## 🛠️ **GIẢI PHÁP**

### **Bước 1: Sửa Configuration Files**

**1. Sửa webpack/environment.js:**
```javascript
module.exports = {
  VERSION: process.env.APP_VERSION || 'DEV',
  SERVER_API_URL: (() => {
    if (process.env.SERVER_API_URL) {
      return process.env.SERVER_API_URL;
    }
    
    const nodeEnv = process.env.NODE_ENV || 'development';
    
    switch (nodeEnv) {
      case 'production':
        return process.env.PROD_API_URL || '';
      case 'development':
        return process.env.DEV_API_URL || 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
      case 'test':
        return process.env.TEST_API_URL || 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
      case 'cloud':
        return process.env.CLOUD_API_URL || 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
      default:
        return process.env.DEFAULT_API_URL || 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
    }
  })(),
  SERVER_API_URL_WS: (() => {
    if (process.env.SERVER_API_URL_WS) {
      return process.env.SERVER_API_URL_WS;
    }
    
    const nodeEnv = process.env.NODE_ENV || 'development';
    
    switch (nodeEnv) {
      case 'production':
        return process.env.PROD_API_URL_WS || '';
      case 'development':
        return process.env.DEV_API_URL_WS || 'wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
      case 'test':
        return process.env.TEST_API_URL_WS || 'wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
      case 'cloud':
        return process.env.CLOUD_API_URL_WS || 'wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
      default:
        return process.env.DEFAULT_API_URL_WS || 'wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/';
    }
  })(),
  MONGODB_URI: process.env.MONGODB_URI || 'mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true'
};
```

**2. Sửa package.json:**
```json
{
  "name": "warehouse-mgmt",
  "engines": {
    "node": ">=22.14.0"
  }
}
```

**3. Tạo env.cloud:**
```bash
# API Configuration
SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
DEV_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
CLOUD_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
DEFAULT_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/

# WebSocket Configuration
SERVER_API_URL_WS=wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
DEV_API_URL_WS=wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
CLOUD_API_URL_WS=wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
DEFAULT_API_URL_WS=wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/

# Environment
NODE_ENV=cloud
ENVIRONMENT=cloud
DEBUG=true
CLOUD_DEPLOYMENT=true
APP_VERSION=CLOUD
```

**4. Tạo env.development:**
```bash
# API Configuration
SERVER_API_URL=http://localhost:8080/
DEV_API_URL=http://localhost:8080/
TEST_API_URL=http://localhost:8080/
DEFAULT_API_URL=http://localhost:8080/

# WebSocket Configuration
SERVER_API_URL_WS=ws://localhost:8080/
DEV_API_URL_WS=ws://localhost:8080/
TEST_API_URL_WS=ws://localhost:8080/
DEFAULT_API_URL_WS=ws://localhost:8080/

# Environment
NODE_ENV=development
ENVIRONMENT=development
DEBUG=true
CLOUD_DEPLOYMENT=false
APP_VERSION=DEV
```

### **Bước 2: Test Build Locally**

```bash
# Test với development environment
export $(cat env.development | grep -v '^#' | xargs)
npm run webapp:build

# Test với cloud environment
export $(cat env.cloud | grep -v '^#' | xargs)
npm run webapp:build
```

### **Bước 3: Debug Nếu Cần**

```bash
# Test webpack configuration
node -e "const env = require('./webpack/environment.js'); console.log('SERVER_API_URL:', env.SERVER_API_URL);"

# Test environment variables
echo $NODE_ENV
echo $SERVER_API_URL
```

## 📋 **CÁC FILE ĐÃ ĐƯỢC SỬA**

### **1. webpack/environment.js**
- ✅ Sửa syntax error
- ✅ Thêm cấu hình cho cloud deployment
- ✅ Thêm WebSocket URL configuration
- ✅ Thêm MongoDB URI
- ✅ Fallback URLs cho Codespaces

### **2. package.json**
- ✅ Sửa package name từ "warehous-mmgmt" thành "warehouse-mgmt"
- ✅ Thêm Node.js engine requirement

### **3. Environment Files**
- ✅ **env.development**: Cấu hình cho local development
- ✅ **env.cloud**: Cấu hình cho cloud deployment
- ✅ Đầy đủ URL variables cho cả API và WebSocket

### **4. GitHub Actions Workflow**
- ✅ Tạo `.github/workflows/build-and-deploy.yml`
- ✅ Cấu hình Node.js 22.14.0
- ✅ Cấu hình Java 17
- ✅ Cache dependencies
- ✅ Environment variables cho cloud

## 🚀 **DEPLOYMENT STEPS**

### **Local Testing**
```bash
# 1. Test development environment
export $(cat env.development | grep -v '^#' | xargs)
npm run webapp:build
./mvnw spring-boot:run

# 2. Test cloud environment
export $(cat env.cloud | grep -v '^#' | xargs)
npm run webapp:build
./mvnw spring-boot:run -Dspring.profiles.active=cloud

# 3. Commit changes
git add .
git commit -m "Fix npm build issues for GitHub deployment"
git push origin main
```

### **GitHub Actions**
1. Push code lên GitHub
2. GitHub Actions sẽ tự động chạy
3. Kiểm tra Actions tab để xem logs
4. Download artifacts nếu cần

## 🔍 **TROUBLESHOOTING**

### **Lỗi Node.js Version**
```bash
# Kiểm tra version
node --version  # Phải là v22.14.0
npm --version   # Phải là v11.2.0

# Cài đặt đúng version
nvm install 22.14.0
nvm use 22.14.0
```

### **Lỗi Dependencies**
```bash
# Clean và reinstall
rm -rf node_modules package-lock.json
npm install --no-optional --no-audit --no-fund
```

### **Lỗi Webpack**
```bash
# Test webpack config
node -e "const env = require('./webpack/environment.js'); console.log(env.SERVER_API_URL);"
```

### **Lỗi TypeScript**
```bash
# Test TypeScript compilation
npx tsc --noEmit --skipLibCheck
```

### **Lỗi Environment Variables**
```bash
# Test environment loading
export $(cat env.cloud | grep -v '^#' | xargs)
echo $NODE_ENV
echo $SERVER_API_URL
```

## 📁 **CÁC SCRIPT HỖ TRỢ**

### **1. fix-npm-build.sh**
- Sửa tất cả issues tự động
- Clean và reinstall dependencies
- Test build process

### **2. test-build-locally.sh**
- Simulate GitHub Actions locally
- Test toàn bộ build process
- Verify artifacts

### **3. debug-npm-build.sh**
- Debug chi tiết build issues
- Check configurations
- Identify problems

### **4. build-for-github.sh**
- Optimized build cho GitHub Actions
- Set environment variables
- Clean build process

## 🌐 **ENVIRONMENT VARIABLES**

### **Cloud Deployment**
```bash
NODE_ENV=cloud
SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
SERVER_API_URL_WS=wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
APP_VERSION=CLOUD
MONGODB_URI=mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true
```

### **Local Development**
```bash
NODE_ENV=development
SERVER_API_URL=http://localhost:8080/
SERVER_API_URL_WS=ws://localhost:8080/
APP_VERSION=DEV
```

## 📊 **BUILD PROCESS**

### **Frontend Build**
```bash
# Development
export $(cat env.development | grep -v '^#' | xargs)
npm run webapp:build

# Cloud
export $(cat env.cloud | grep -v '^#' | xargs)
npm run webapp:build
```

### **Backend Build**
```bash
# Development
./mvnw spring-boot:run

# Cloud
./mvnw spring-boot:run -Dspring.profiles.active=cloud
```

### **Full Build**
```bash
# Development
export $(cat env.development | grep -v '^#' | xargs)
npm run webapp:build
./mvnw clean package -DskipTests -Pprod

# Cloud
export $(cat env.cloud | grep -v '^#' | xargs)
npm run webapp:build
./mvnw clean package -DskipTests -Pprod
```

## ✅ **SUCCESS INDICATORS**

### **Local Success**
- ✅ `target/classes/static/` contains frontend files
- ✅ `target/warehouse-mgmt-*.jar` exists
- ✅ JAR file is valid and executable
- ✅ All tests pass
- ✅ API calls to localhost:8080

### **Cloud Success**
- ✅ `target/classes/static/` contains frontend files
- ✅ `target/warehouse-mgmt-*.jar` exists
- ✅ JAR file is valid and executable
- ✅ All tests pass
- ✅ API calls to Codespaces URL

### **GitHub Actions Success**
- ✅ Build job completes without errors
- ✅ Artifacts are uploaded
- ✅ Deploy job completes
- ✅ Application is accessible

## 🆘 **EMERGENCY FIXES**

### **Quick Fix for Immediate Deployment**
```bash
# 1. Set environment variables
export NODE_ENV=cloud
export SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/

# 2. Build manually
npm install
npm run webapp:build
./mvnw clean package -DskipTests -Pprod

# 3. Push to GitHub
git add .
git commit -m "Emergency fix for npm build"
git push origin main
```

### **Rollback if Needed**
```bash
# Revert to previous working version
git log --oneline -10
git checkout <commit-hash>
git push origin main --force
```

## 📞 **SUPPORT**

Nếu vẫn gặp vấn đề:

1. **Check logs** trong GitHub Actions
2. **Run debug script** locally
3. **Verify environment** variables
4. **Check Node.js version** compatibility
5. **Review webpack configuration**
6. **Test environment loading** with `export $(cat env.cloud | grep -v '^#' | xargs)`

---

## 🎉 **SUCCESS MESSAGE**

Sau khi fix thành công, bạn sẽ thấy:

```
✅ Build completed successfully!
📦 Artifacts:
-rw-r--r-- 1 user user 45M target/warehouse-mgmt-0.0.1-SNAPSHOT.jar
📁 Static files:
drwxr-xr-x 2 user user 4.0K target/classes/static/
🚀 Ready for deployment!
```

## 🔄 **ENVIRONMENT SWITCHING**

### **Switch to Development**
```bash
export $(cat env.development | grep -v '^#' | xargs)
npm run webapp:build
./mvnw spring-boot:run
```

### **Switch to Cloud**
```bash
export $(cat env.cloud | grep -v '^#' | xargs)
npm run webapp:build
./mvnw spring-boot:run -Dspring.profiles.active=cloud
``` 