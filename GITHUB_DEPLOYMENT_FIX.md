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

### **Bước 1: Chạy Script Fix Tự Động**

```bash
# Chạy script fix tự động
./fix-npm-build.sh
```

### **Bước 2: Test Build Locally**

```bash
# Test build trước khi push lên GitHub
./test-build-locally.sh
```

### **Bước 3: Debug Nếu Cần**

```bash
# Debug chi tiết nếu vẫn có lỗi
./debug-npm-build.sh
```

## 📋 **CÁC FILE ĐÃ ĐƯỢC SỬA**

### **1. webpack/environment.js**
- ✅ Sửa syntax error
- ✅ Thêm cấu hình cho cloud deployment
- ✅ Thêm WebSocket URL configuration
- ✅ Thêm MongoDB URI

### **2. package.json**
- ✅ Sửa package name từ "warehous-mmgmt" thành "warehouse-mgmt"
- ✅ Thêm Node.js engine requirement

### **3. GitHub Actions Workflow**
- ✅ Tạo `.github/workflows/build-and-deploy.yml`
- ✅ Cấu hình Node.js 22.14.0
- ✅ Cấu hình Java 17
- ✅ Cache dependencies
- ✅ Environment variables cho cloud

## 🚀 **DEPLOYMENT STEPS**

### **Local Testing**
```bash
# 1. Fix issues
./fix-npm-build.sh

# 2. Test locally
./test-build-locally.sh

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
NODE_ENV=development
SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
APP_VERSION=GITHUB-ACTIONS
MONGODB_URI=mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true
```

### **Local Development**
```bash
NODE_ENV=development
SERVER_API_URL=http://localhost:8080/
APP_VERSION=DEV
```

## 📊 **BUILD PROCESS**

### **Frontend Build**
```bash
npm run webapp:build
# → Generates static files in target/classes/static/
```

### **Backend Build**
```bash
./mvnw clean package -DskipTests -Pprod
# → Generates JAR file in target/
```

### **Full Build**
```bash
./mvnw clean package -DskipTests -Pprod
# → Combines frontend + backend
```

## ✅ **SUCCESS INDICATORS**

### **Local Success**
- ✅ `target/classes/static/` contains frontend files
- ✅ `target/warehouse-mgmt-*.jar` exists
- ✅ JAR file is valid and executable
- ✅ All tests pass

### **GitHub Actions Success**
- ✅ Build job completes without errors
- ✅ Artifacts are uploaded
- ✅ Deploy job completes
- ✅ Application is accessible

## 🆘 **EMERGENCY FIXES**

### **Quick Fix for Immediate Deployment**
```bash
# 1. Apply all fixes
./fix-npm-build.sh

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