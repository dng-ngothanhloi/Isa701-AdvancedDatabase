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
6. **Frontend build sử dụng development environment thay vì cloud environment**

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
  },
  "scripts": {
    "webapp:build": "npm run clean-www && npm run webapp:build:dev --",
    "webapp:build:dev": "webpack --config webpack/webpack.dev.js --env stats=minimal",
    "webapp:build:cloud": "NODE_ENV=production webpack --config webpack/webpack.cloud.js --env stats=minimal",
    "webapp:build:smart": "npm run clean-www && npm run webapp:build:smart:detect --",
    "webapp:build:smart:detect": "node scripts/smart-env.js",
    "webapp:build:prod": "webpack --config webpack/webpack.prod.js --progress=profile"
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

### **Bước 2: Smart Environment Management**

**1. Tạo scripts/smart-env.js:**
```javascript
#!/usr/bin/env node

const { spawnSync } = require('child_process');
const fs = require('fs');
const path = require('path');

// Get command line arguments
const args = process.argv.slice(2);
const isCheckOnly = args.includes('--check') || args.includes('-c');
const isBuildOnly = args.includes('--build') || args.includes('-b');

console.log('🔍 SMART ENVIRONMENT MANAGER');
console.log('============================');

// Get Spring profiles from environment variable
const springProfiles = process.env.SPRING_PROFILES_ACTIVE || '';
const nodeEnv = process.env.NODE_ENV || 'development';

// Determine environment type
let environmentType = 'UNKNOWN';
let isCloudEnvironment = false;

if (springProfiles.includes('cloud') || nodeEnv === 'production') {
    environmentType = 'CLOUD';
    isCloudEnvironment = true;
} else if (springProfiles.includes('dev') || nodeEnv === 'development') {
    environmentType = 'DEVELOPMENT';
    isCloudEnvironment = false;
}

console.log(`🎯 Detected Environment: ${environmentType}`);

// If check-only mode, exit here
if (isCheckOnly) {
    console.log('📊 ENVIRONMENT SUMMARY:');
    if (isCloudEnvironment) {
        console.log('🎯 Environment: CLOUD');
        console.log('🌐 API will call: https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev');
    } else {
        console.log('🎯 Environment: DEVELOPMENT');
        console.log('🌐 API will call: http://localhost:8080');
    }
    process.exit(0);
}

// Build functionality
if (isBuildOnly || (!isCheckOnly && !isBuildOnly)) {
    console.log('🔨 Building frontend...');
    
    if (isCloudEnvironment) {
        console.log('🌐 Building for CLOUD environment');
        console.log('   - Using webpack.cloud.js');
        console.log('   - Setting NODE_ENV=production');
        console.log('   - Using cloud URLs');
        
        // Set environment variables for cloud build
        process.env.NODE_ENV = 'production';
        process.env.SERVER_API_URL = 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev';
        process.env.SERVER_API_URL_WS = 'wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev';
        
        // Run cloud build
        const result = spawnSync('npm', ['run', 'webapp:build:cloud'], {
            stdio: 'inherit',
            env: process.env,
            cwd: path.join(__dirname, '..')
        });
        
        process.exit(result.status);
    } else {
        console.log('🏠 Building for DEVELOPMENT environment');
        console.log('   - Using webpack.dev.js');
        console.log('   - Setting NODE_ENV=development');
        console.log('   - Using localhost URLs');
        
        // Set environment variables for dev build
        process.env.NODE_ENV = 'development';
        
        // Run dev build
        const result = spawnSync('npm', ['run', 'webapp:build:dev'], {
            stdio: 'inherit',
            env: process.env,
            cwd: path.join(__dirname, '..')
        });
        
        process.exit(result.status);
    }
}
```

**2. Tạo Maven cloud profile trong pom.xml:**
```xml
<profile>
    <id>cloud</id>
    <properties>
        <!-- Spring profiles for cloud environment -->
        <spring.profiles.active>cloud</spring.profiles.active>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>webapp build cloud</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <arguments>run webapp:build:cloud</arguments>
                            <environmentVariables>
                                <APP_VERSION>${project.version}</APP_VERSION>
                                <NODE_ENV>production</NODE_ENV>
                                <SERVER_API_URL>https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev</SERVER_API_URL>
                                <SERVER_API_URL_WS>wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev</SERVER_API_URL_WS>
                            </environmentVariables>
                            <npmInheritsProxyConfigFromMaven>false</npmInheritsProxyConfigFromMaven>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</profile>
```

**3. Cập nhật webapp profile để sử dụng smart build:**
```xml
<execution>
    <id>webapp build smart</id>
    <goals>
        <goal>npm</goal>
    </goals>
    <phase>generate-resources</phase>
    <configuration>
        <arguments>run webapp:build:smart</arguments>
        <environmentVariables>
            <APP_VERSION>${project.version}</APP_VERSION>
            <SPRING_PROFILES_ACTIVE>${spring.profiles.active}</SPRING_PROFILES_ACTIVE>
        </environmentVariables>
        <npmInheritsProxyConfigFromMaven>false</npmInheritsProxyConfigFromMaven>
    </configuration>
</execution>
```

### **Bước 3: Test Build Locally**

```bash
# Test với development environment
export $(cat env.development | grep -v '^#' | xargs)
npm run webapp:build

# Test với cloud environment
export $(cat env.cloud | grep -v '^#' | xargs)
npm run webapp:build

# Test smart environment
./smart-env.sh --check
./smart-env.sh --build

# Test Maven cloud profile
./mvnw spring-boot:run -Pcloud

# Test Spring profile override
./mvnw spring-boot:run -Dspring.profiles.active=cloud
```

### **Bước 4: Debug Nếu Cần**

```bash
# Test webpack configuration
node -e "const env = require('./webpack/environment.js'); console.log('SERVER_API_URL:', env.SERVER_API_URL);"

# Test environment variables
echo $NODE_ENV
echo $SERVER_API_URL

# Test Spring profiles
./test-spring-profiles.sh
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
- ✅ Thêm smart build scripts

### **3. Environment Files**
- ✅ **env.development**: Cấu hình cho local development
- ✅ **env.cloud**: Cấu hình cho cloud deployment
- ✅ Đầy đủ URL variables cho cả API và WebSocket

### **4. Smart Environment Management**
- ✅ **scripts/smart-env.js**: Auto-detect environment and build
- ✅ **smart-env.sh**: Bash wrapper for easy usage
- ✅ **webpack/webpack.cloud.js**: Cloud-specific webpack configuration

### **5. Maven Integration**
- ✅ **cloud profile**: Automatic frontend build with cloud environment
- ✅ **smart build**: Auto-detect environment based on Spring profiles
- ✅ **Environment variable passing**: From Maven to npm

### **6. GitHub Actions Workflow**
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

# 3. Test smart environment
./smart-env.sh --check
./smart-env.sh --build

# 4. Test Maven cloud profile
./mvnw spring-boot:run -Pcloud

# 5. Commit changes
git add .
git commit -m "Fix npm build issues and add smart environment management"
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

# Test smart environment
./smart-env.sh --check
```

### **Lỗi Maven Profile**
```bash
# Test Spring profiles
./test-spring-profiles.sh

# Test Maven cloud profile
./mvnw help:evaluate -Dexpression=spring.profiles.active -Pcloud -q -DforceStdout
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

### **5. smart-env.sh**
- Smart environment management
- Auto-detect and build
- Check environment status

### **6. test-spring-profiles.sh**
- Test Spring profiles flow
- Verify environment variable passing
- Debug Maven integration

### **7. maven-cloud.sh**
- Run Maven with cloud profile
- Automatic frontend build
- Cloud environment setup

### **8. test-cloud-build.sh**
- Test cloud build process
- Verify cloud URLs
- Check build artifacts

## 🌐 **ENVIRONMENT VARIABLES**

### **Cloud Deployment**
```bash
NODE_ENV=cloud
SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
SERVER_API_URL_WS=wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
APP_VERSION=CLOUD
MONGODB_URI=mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true
SPRING_PROFILES_ACTIVE=cloud
```

### **Local Development**
```bash
NODE_ENV=development
SERVER_API_URL=http://localhost:8080/
SERVER_API_URL_WS=ws://localhost:8080/
APP_VERSION=DEV
SPRING_PROFILES_ACTIVE=dev
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

# Smart (auto-detect)
./smart-env.sh --build
```

### **Backend Build**
```bash
# Development
./mvnw spring-boot:run

# Cloud
./mvnw spring-boot:run -Pcloud

# Override Spring profile
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

# Smart (auto-detect)
./smart-env.sh --build
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

### **Smart Environment Success**
- ✅ `./smart-env.sh --check` shows correct environment
- ✅ Maven cloud profile builds frontend correctly
- ✅ Both `-Pcloud` and `-Dspring.profiles.active=cloud` work
- ✅ Frontend uses correct URLs for each environment

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
7. **Test smart environment** with `./smart-env.sh --check`
8. **Test Spring profiles** with `./test-spring-profiles.sh`

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

### **Smart Environment Switching**
```bash
# Auto-detect and build
./smart-env.sh

# Check current environment
./smart-env.sh --check

# Build for specific environment
./smart-env.sh --build
```

### **Maven Profile Switching**
```bash
# Development (default)
./mvnw spring-boot:run -Pwebapp

# Cloud
./mvnw spring-boot:run -Pcloud

# Override Spring profile
./mvnw spring-boot:run -Dspring.profiles.active=cloud
```

## 🆕 **NEW FEATURES**

### **Smart Environment Management**
- ✅ `scripts/smart-env.js` - Auto-detect environment and build
- ✅ `smart-env.sh` - Bash wrapper for easy usage
- ✅ Maven cloud profile - Automatic frontend build with cloud URLs
- ✅ Environment detection from Spring profiles

### **Maven Integration**
- ✅ `-Pcloud` profile - Builds frontend with cloud environment
- ✅ `-Dspring.profiles.active=cloud` - Also builds frontend with cloud environment
- ✅ Automatic environment variable passing from Maven to npm
- ✅ Smart build detection based on Spring profiles

### **Testing Tools**
- ✅ `test-spring-profiles.sh` - Test Spring profiles flow
- ✅ `debug-cors-issues.sh` - Debug CORS issues
- ✅ `maven-cloud.sh` - Run Maven with cloud profile
- ✅ `test-cloud-build.sh` - Test cloud build process

### **Build Process**
- ✅ Smart build detection
- ✅ Automatic environment switching
- ✅ Cloud-specific webpack configuration
- ✅ Environment variable management 