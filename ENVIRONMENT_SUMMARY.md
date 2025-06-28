# 🌍 Environment Summary

## 🎯 **Quick Reference**

| Environment | Command | Frontend API | Backend URL | Use Case |
|-------------|---------|--------------|-------------|----------|
| **🏠 Development** | `./mvnw spring-boot:run -Pwebapp` | `http://localhost:8080` | `http://localhost:8080` | Local development |
| **☁️ Cloud** | `./mvnw spring-boot:run -Pcloud` | `https://{codespace-id}-8080.app.github.dev` | `https://{codespace-id}-8080.app.github.dev` | GitHub Codespaces |
| **🚀 Production** | `./mvnw clean package -Pprod` | Production URL | Production URL | Production deployment |

## 🚀 **Quick Start Commands**

```bash
# Development (Local)
./quick-start-env.sh dev

# Cloud (Codespaces)
./quick-start-env.sh cloud

# Production
./quick-start-env.sh prod

# Check current environment
./quick-start-env.sh check
```

## 🔧 **Environment Variables**

| Variable | Development | Cloud | Production |
|----------|-------------|-------|------------|
| `NODE_ENV` | `development` | `cloud` | `production` |
| `SPRING_PROFILES_ACTIVE` | `dev` | `cloud` | `prod` |
| `SERVER_API_URL` | `http://localhost:8080/` | `https://{codespace-id}-8080.app.github.dev/` | Production URL |
| `CLOUD_DEPLOYMENT` | `false` | `true` | `false` |

## 📋 **Configuration Files**

| File | Purpose |
|------|---------|
| `env.development` | Development environment variables |
| `env.cloud` | Cloud environment variables |
| `application-dev.yml` | Development Spring configuration |
| `application-cloud.yml` | Cloud Spring configuration |
| `application-prod.yml` | Production Spring configuration |

## 🎯 **Maven Profiles**

| Profile | Description | Frontend Build | Backend Profile |
|---------|-------------|----------------|-----------------|
| `webapp` | Development with frontend build | Development URLs | `dev` |
| `cloud` | Cloud deployment | Cloud URLs | `cloud` |
| `prod` | Production build | Production URLs | `prod` |

## 🚨 **Troubleshooting**

### **Check Environment:**
```bash
./check-environment.sh
./smart-env.sh --check
./quick-start-env.sh check
```

### **Load Environment:**
```bash
./load-and-test-env.sh dev
./load-and-test-env.sh cloud
```

### **Test Configuration:**
```bash
./test-api-config.sh
./test-cors.sh
./test-spring-profiles.sh
```

## 📞 **Support**

- **Environment Issues**: `./check-environment.sh`
- **Build Issues**: `./test-cloud-build.sh`
- **CORS Issues**: `./debug-cors-issues.sh`
- **General Help**: `./quick-start-env.sh help` 