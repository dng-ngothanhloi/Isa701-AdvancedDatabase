# 🔧 GitHub Setup Guide

## **Bước 1: Enable GitHub Pages**

1. **Vào repository Settings**
   - Go to: `https://github.com/{username}/{repo-name}/settings`

2. **Pages section**
   - Scroll down to "Pages"
   - Source: "GitHub Actions"
   - Click "Save"

## **Bước 2: Setup Secrets (Tùy chọn)**

### **Cho Vercel:**
1. **Tạo Vercel account**: https://vercel.com
2. **Connect repository**: Import GitHub repo
3. **Get tokens**:
   - Vercel Token: Settings → Tokens
   - Org ID: Settings → General
   - Project ID: Project Settings → General

4. **Add secrets**:
   - Go to: `https://github.com/{username}/{repo-name}/settings/secrets/actions`
   - Add secrets:
     - `VERCEL_TOKEN`
     - `VERCEL_ORG_ID`
     - `VERCEL_PROJECT_ID`

### **Cho Netlify:**
1. **Tạo Netlify account**: https://netlify.com
2. **Connect repository**: Import GitHub repo
3. **Get tokens**:
   - Auth Token: User Settings → Applications → Personal access tokens
   - Site ID: Site Settings → General

4. **Add secrets**:
   - Go to: `https://github.com/{username}/{repo-name}/settings/secrets/actions`
   - Add secrets:
     - `NETLIFY_AUTH_TOKEN`
     - `NETLIFY_SITE_ID`

## **Bước 3: Test Deployment**

### **Test GitHub Pages:**
```bash
# Push code
git push origin main

# Check Actions
# Go to: https://github.com/{username}/{repo-name}/actions
```

### **Test Codespaces:**
```bash
# Open Codespaces
# Go to: https://github.com/{username}/{repo-name}/codespaces
```

## **Bước 4: Verify URLs**

Sau khi deploy, kiểm tra các URLs:

- **GitHub Pages**: `https://{username}.github.io/{repo-name}/`
- **Vercel**: `https://{repo-name}.vercel.app`
- **Netlify**: `https://{repo-name}.netlify.app`
- **Codespaces**: `https://{codespace-name}-8080.app.github.dev`

## **Troubleshooting**

### **GitHub Pages không hoạt động:**
- Check Actions tab
- Verify Pages source is "GitHub Actions"
- Check for build errors

### **Secrets không hoạt động:**
- Verify secret names match workflow
- Check secret values are correct
- Re-run failed workflows

### **Codespaces không start:**
- Check `.devcontainer/devcontainer.json`
- Verify Node.js and Java versions
- Check setup script permissions 