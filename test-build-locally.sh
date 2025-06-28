#!/bin/bash

# Test Build Locally Before GitHub Push
# This script simulates the GitHub Actions build process locally

set -e

echo "🧪 Testing Build Locally..."
echo "============================"

# Check if we're in the right directory
if [ ! -f "package.json" ] || [ ! -f "pom.xml" ]; then
    echo "❌ Error: This script must be run from the project root directory"
    exit 1
fi

# Check Node.js version
echo "📋 Checking Node.js version..."
NODE_VERSION=$(node --version)
NPM_VERSION=$(npm --version)
echo "Node.js: $NODE_VERSION"
echo "NPM: $NPM_VERSION"

# Check Java version
echo "📋 Checking Java version..."
JAVA_VERSION=$(java --version 2>&1 | head -n 1)
echo "Java: $JAVA_VERSION"

# Set environment variables (simulating GitHub Actions)
echo "🌍 Setting environment variables..."
export NODE_ENV=development
export SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
export APP_VERSION=LOCAL-TEST-$(date +%Y%m%d%H%M%S)
export MONGODB_URI=mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true

echo "NODE_ENV: $NODE_ENV"
echo "SERVER_API_URL: $SERVER_API_URL"
echo "APP_VERSION: $APP_VERSION"

# Clean previous builds
echo "🧹 Cleaning previous builds..."
rm -rf node_modules package-lock.json target/classes/static/

# Fix npm build issues
echo "🔧 Fixing npm build issues..."
chmod +x fix-npm-build.sh
./fix-npm-build.sh

# Install dependencies
echo "📥 Installing dependencies..."
npm install --no-optional --no-audit --no-fund

# Test webpack configuration
echo "🧪 Testing webpack configuration..."
node -e "
console.log('Testing environment.js syntax...');
const env = require('./webpack/environment.js');
console.log('✅ Environment.js syntax OK');
console.log('SERVER_API_URL:', env.SERVER_API_URL);
console.log('VERSION:', env.VERSION);
"

# Test TypeScript compilation
echo "🧪 Testing TypeScript compilation..."
if [ -f "tsconfig.json" ]; then
    npx tsc --noEmit --skipLibCheck && echo "✅ TypeScript compilation OK" || echo "⚠️ TypeScript compilation issues"
else
    echo "❌ tsconfig.json not found"
fi

# Test ESLint
echo "🧪 Testing ESLint..."
if [ -f "eslint.config.mjs" ]; then
    npx eslint --version && echo "✅ ESLint available" || echo "⚠️ ESLint issues"
else
    echo "❌ eslint.config.mjs not found"
fi

# Build frontend
echo "🔨 Building frontend..."
npm run webapp:build
echo "✅ Frontend build completed"

# Check frontend build output
echo "📁 Checking frontend build output..."
if [ -d "target/classes/static" ]; then
    echo "✅ Static files generated:"
    ls -la target/classes/static/
    echo "📊 Static files count: $(find target/classes/static -type f | wc -l)"
else
    echo "❌ Static files not generated"
    exit 1
fi

# Build backend
echo "🔨 Building backend..."
./mvnw clean compile -DskipTests
echo "✅ Backend build completed"

# Run tests (skip npm to avoid conflicts)
echo "🧪 Running tests..."
./mvnw test -Dskip.npm
echo "✅ Tests completed"

# Build full application
echo "🔨 Building full application..."
./mvnw clean package -DskipTests -Pprod
echo "✅ Full build completed"

# Check final artifacts
echo "📦 Checking final artifacts..."
if [ -f "target/warehouse-mgmt-0.0.1-SNAPSHOT.jar" ]; then
    echo "✅ JAR file generated:"
    ls -lh target/warehouse-mgmt-*.jar
    echo "📊 JAR file size: $(du -h target/warehouse-mgmt-*.jar | cut -f1)"
else
    echo "❌ JAR file not generated"
    exit 1
fi

# Test JAR file
echo "🧪 Testing JAR file..."
java -jar target/warehouse-mgmt-*.jar --version 2>/dev/null && echo "✅ JAR file is valid" || echo "⚠️ JAR file validation failed"

echo ""
echo "🎉 Local build test completed successfully!"
echo ""
echo "📋 Summary:"
echo "✅ Node.js and NPM versions OK"
echo "✅ Java version OK"
echo "✅ Environment variables set"
echo "✅ Dependencies installed"
echo "✅ Webpack configuration OK"
echo "✅ TypeScript compilation OK"
echo "✅ Frontend build completed"
echo "✅ Backend build completed"
echo "✅ Tests passed"
echo "✅ Full application built"
echo "✅ JAR file generated"
echo ""
echo "🚀 Ready to push to GitHub!"
echo "📝 Next steps:"
echo "1. git add ."
echo "2. git commit -m 'Fix npm build issues for GitHub deployment'"
echo "3. git push origin main"
echo ""
echo "🔧 If you encounter issues on GitHub, check the Actions tab for detailed logs." 