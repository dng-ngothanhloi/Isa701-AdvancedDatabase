# 🚀 GitHub Free Deployment Guide

## 🎯 **OVERVIEW**

Hướng dẫn này giúp bạn deploy ứng dụng warehouse management system lên các dịch vụ miễn phí của GitHub và các platform khác.

---

## 🌍 **ENVIRONMENT SELECTION**

### **🏠 Development Environment (Local)**
```bash
# Build và chạy cho local development
./mvnw spring-boot:run -Pwebapp

# Hoặc với development profile
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Frontend sẽ call: http://localhost:8080
# Backend chạy tại: http://localhost:8080
```

### **☁️ Cloud Environment (Codespaces)**
```bash
# Build và chạy cho cloud environment
./mvnw spring-boot:run -Pcloud

# Hoặc với cloud profile
./mvnw spring-boot:run -Dspring.profiles.active=cloud

# Frontend sẽ call: https://{codespace-id}-8080.app.github.dev
# Backend chạy tại: https://{codespace-id}-8080.app.github.dev
```

### **🚀 Production Environment**
```bash
# Build cho production
./mvnw clean package -Pprod

# Chạy production JAR
java -jar target/*.jar --spring.profiles.active=prod

# Frontend sẽ call: Production URL
# Backend chạy tại: Production URL
```

---

## 🌐 **CÁC DỊCH VỤ MIỄN PHÍ**

### **1. GitHub Codespaces**
- ✅ **Miễn phí**: 60 giờ/tháng cho tài khoản free
- ✅ **Môi trường**: Full development environment
- ✅ **Tính năng**: VS Code, terminal, ports forwarding
- ✅ **URL**: `https://{codespace-name}-8080.app.github.dev`

### **2. GitHub Pages**
- ✅ **Miễn phí**: Không giới hạn
- ✅ **Môi trường**: Static hosting
- ✅ **Tính năng**: Custom domain, HTTPS
- ✅ **URL**: `https://{username}.github.io/{repo-name}/`

### **3. GitHub Actions**
- ✅ **Miễn phí**: 2000 phút/tháng cho tài khoản free
- ✅ **Môi trường**: CI/CD automation
- ✅ **Tính năng**: Auto build, test, deploy

### **4. Vercel (Free Tier)**
- ✅ **Miễn phí**: Không giới hạn
- ✅ **Môi trường**: Serverless hosting
- ✅ **Tính năng**: Auto deploy, custom domain
- ✅ **URL**: `https://{project-name}.vercel.app`

### **5. Netlify (Free Tier)**
- ✅ **Miễn phí**: Không giới hạn
- ✅ **Môi trường**: Static hosting
- ✅ **Tính năng**: Auto deploy, forms, functions
- ✅ **URL**: `https://{project-name}.netlify.app`

---

## 🚀 **GITHUB CODESPACES**

### **Cách sử dụng:**

1. **Mở Codespaces:**
   ```
   https://github.com/{username}/{repo-name}/codespaces
   ```

2. **Hoặc từ VS Code:**
   - Install GitHub Codespaces extension
   - Press `Ctrl+Shift+P` → "Codespaces: Create New Codespace"

3. **Auto setup:**
   - Codespaces sẽ tự động chạy `.devcontainer/setup.sh`
   - Environment được configure sẵn
   - Application tự động build và start

### **Quick Commands trong Codespaces:**

```bash
# Check status
./status.sh

# Quick start (Cloud environment)
./quick-start.sh

# Deploy to Codespaces
./deploy-codespaces.sh

# Check environment
./smart-env.sh --check

# Start development (Cloud)
./mvnw spring-boot:run -Pcloud

# Set Codespace ID (nếu cần)
./set-codespace-id.sh super-broccoli-pj96jxxr4p7q3945r

# Test environment
./test-codespace-env.sh
```

### **URL Access:**
- **Frontend**: `https://{codespace-name}-8080.app.github.dev`
- **Backend API**: `https://{codespace-name}-8080.app.github.dev/api`
- **MongoDB**: `mongodb://localhost:27017`

---

## 📄 **GITHUB PAGES**

### **Setup:**

1. **Enable GitHub Pages:**
   - Go to repository Settings → Pages
   - Source: "GitHub Actions"

2. **Deploy tự động:**
   - Push code lên `main` branch
   - GitHub Actions sẽ tự động build và deploy
   - URL: `https://{username}.github.io/{repo-name}/`

### **Manual Deploy:**

```bash
# Build locally với cloud profile
./mvnw clean compile -Pcloud

# Deploy to GitHub Pages
npx gh-pages -d target/classes/static
```

---

## ⚡ **GITHUB ACTIONS**

### **Workflows có sẵn:**

1. **Auto Build & Deploy** (`.github/workflows/auto-deploy.yml`)
   - Trigger: Push to main, PR
   - Build với cloud profile
   - Deploy to multiple platforms

2. **GitHub Pages** (`.github/workflows/github-pages.yml`)
   - Trigger: Push to main
   - Deploy frontend to GitHub Pages

### **Manual Trigger:**

1. **Từ GitHub UI:**
   - Go to Actions tab
   - Select workflow
   - Click "Run workflow"

2. **Từ command line:**
   ```bash
   gh workflow run auto-deploy.yml
   ```

---

## 🚀 **VERCEL DEPLOYMENT**

### **Setup:**

1. **Connect to Vercel:**
   ```bash
   npm i -g vercel
   vercel login
   ```

2. **Deploy:**
   ```bash
   # Build first với cloud profile
   ./mvnw clean compile -Pcloud
   
   # Deploy
   vercel --prod
   ```

3. **Auto Deploy:**
   - Connect GitHub repository to Vercel
   - Push code → Auto deploy

### **Configuration:**
- File: `vercel.json`
- Environment variables tự động set
- Custom domain support

---

## 🌐 **NETLIFY DEPLOYMENT**

### **Setup:**

1. **Connect to Netlify:**
   - Go to [netlify.com](https://netlify.com)
   - Connect GitHub repository

2. **Build settings:**
   - Build command: `./mvnw clean compile -Pcloud`
   - Publish directory: `target/classes/static`

3. **Environment variables:**
   ```
   NODE_ENV=production
   SERVER_API_URL=https://your-app.netlify.app/
   ```

### **Manual Deploy:**
```bash
# Install Netlify CLI
npm i -g netlify-cli

# Build với cloud profile
./mvnw clean compile -Pcloud

# Deploy
netlify deploy --prod --dir=target/classes/static
```

---

## 🔧 **ENVIRONMENT CONFIGURATION**

### **Environment Variables by Environment:**

```bash
# Development (Local)
NODE_ENV=development
SPRING_PROFILES_ACTIVE=dev
SERVER_API_URL=http://localhost:8080/
SERVER_API_URL_WS=ws://localhost:8080/

# Cloud (Codespaces)
NODE_ENV=cloud
SPRING_PROFILES_ACTIVE=cloud
SERVER_API_URL=https://{codespace-id}-8080.app.github.dev/
SERVER_API_URL_WS=wss://{codespace-id}-8080.app.github.dev/

# Production
NODE_ENV=production
SPRING_PROFILES_ACTIVE=prod
SERVER_API_URL=https://your-production-domain.com/
SERVER_API_URL_WS=wss://your-production-domain.com/
```

### **Build Commands by Environment:**

```bash
# Development (Local)
./mvnw spring-boot:run -Pwebapp
# Frontend calls: http://localhost:8080

# Cloud (Codespaces)
./mvnw spring-boot:run -Pcloud
# Frontend calls: https://{codespace-id}-8080.app.github.dev

# Production
./mvnw clean package -Pprod
java -jar target/*.jar --spring.profiles.active=prod
# Frontend calls: Production URL
```

### **Environment Detection:**

```bash
# Check current environment
./smart-env.sh --check

# Test environment configuration
./test-codespace-env.sh

# Check environment variables
./check-environment.sh
```

---

## 📋 **DEPLOYMENT CHECKLIST**

### **Pre-Deployment:**
- [ ] Code tested locally
- [ ] Environment variables configured
- [ ] Cloud profile working
- [ ] Frontend builds successfully
- [ ] Backend starts without errors

### **Deployment:**
- [ ] Push code to GitHub
- [ ] Check GitHub Actions logs
- [ ] Verify deployment URLs
- [ ] Test application functionality
- [ ] Check CORS configuration

### **Post-Deployment:**
- [ ] Monitor application logs
- [ ] Test all features
- [ ] Verify database connection
- [ ] Check performance
- [ ] Update documentation

---

## 🚨 **TROUBLESHOOTING**

### **Common Issues:**

1. **Build fails:**
   ```bash
   # Check logs
   ./debug-classpath.sh
   ./test-cloud-startup.sh
   ```

2. **CORS errors:**
   ```bash
   # Check CORS configuration
   ./debug-cors-issues.sh
   ```

3. **Environment issues:**
   ```bash
   # Check environment
   ./smart-env.sh --check
   ./check-environment.sh
   ```

4. **Deployment fails:**
   - Check GitHub Actions logs
   - Verify environment variables
   - Test locally first

### **Debug Commands:**

```bash
# Test cloud build
./test-cloud-build.sh

# Test startup
./test-cloud-startup.sh

# Debug classpath
./debug-classpath.sh

# Check environment
./smart-env.sh --check
```

---

## 🎉 **SUCCESS INDICATORS**

### **Codespaces Success:**
- ✅ Codespace starts without errors
- ✅ Application accessible at Codespaces URL
- ✅ Frontend and backend working
- ✅ Database connection established

### **GitHub Pages Success:**
- ✅ GitHub Actions build passes
- ✅ Site accessible at GitHub Pages URL
- ✅ Frontend loads correctly
- ✅ API calls work

### **Vercel/Netlify Success:**
- ✅ Build completes successfully
- ✅ Site accessible at custom URL
- ✅ Environment variables loaded
- ✅ Application functional

---

## 📞 **SUPPORT**

### **GitHub Resources:**
- [GitHub Codespaces Docs](https://docs.github.com/en/codespaces)
- [GitHub Pages Docs](https://docs.github.com/en/pages)
- [GitHub Actions Docs](https://docs.github.com/en/actions)

### **Platform Resources:**
- [Vercel Docs](https://vercel.com/docs)
- [Netlify Docs](https://docs.netlify.com)

### **Local Testing:**
```bash
# Test cloud environment locally
./mvnw spring-boot:run -Pcloud

# Test frontend build
./smart-env.sh --build

# Test deployment
./test-cloud-startup.sh
```

---

## 🚀 **QUICK START**

```bash
# 1. Setup Codespaces
# Open: https://github.com/{username}/{repo-name}/codespaces

# 2. Auto deploy to GitHub Pages
git push origin main

# 3. Deploy to Vercel
vercel --prod

# 4. Deploy to Netlify
netlify deploy --prod
```

---

## 🎯 **ENVIRONMENT SELECTION SUMMARY**

| Environment | Command | Frontend API | Backend URL | Use Case |
|-------------|---------|--------------|-------------|----------|
| **🏠 Development** | `./mvnw spring-boot:run -Pwebapp` | `http://localhost:8080` | `http://localhost:8080` | Local development |
| **☁️ Cloud** | `./mvnw spring-boot:run -Pcloud` | `https://{codespace-id}-8080.app.github.dev` | `https://{codespace-id}-8080.app.github.dev` | GitHub Codespaces |
| **🚀 Production** | `./mvnw clean package -Pprod` | Production URL | Production URL | Production deployment |

Bây giờ bạn có thể deploy ứng dụng lên các dịch vụ miễn phí của GitHub một cách dễ dàng! 🎉 