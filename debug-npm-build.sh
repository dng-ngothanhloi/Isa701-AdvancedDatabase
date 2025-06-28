#!/bin/bash

# Debug NPM Build Script for GitHub Deployment
# This script helps identify and fix npm build issues

set -e

echo "🔍 Debugging NPM Build Process..."
echo "=================================="

# Check Node.js version
echo "📋 Node.js Version:"
node --version
npm --version

# Check if we're in the right directory
echo "📁 Current Directory:"
pwd
ls -la

# Check package.json
echo "📦 Package.json exists:"
if [ -f "package.json" ]; then
    echo "✅ package.json found"
    echo "📋 Package name: $(node -p "require('./package.json').name")"
    echo "📋 Package version: $(node -p "require('./package.json').version")"
else
    echo "❌ package.json not found"
    exit 1
fi

# Check webpack configuration
echo "🔧 Webpack Configuration:"
if [ -d "webpack" ]; then
    echo "✅ webpack directory found"
    ls -la webpack/
    
    if [ -f "webpack/environment.js" ]; then
        echo "✅ environment.js found"
        echo "📋 Testing environment.js syntax:"
        node -e "console.log('Environment.js syntax OK'); const env = require('./webpack/environment.js'); console.log('SERVER_API_URL:', env.SERVER_API_URL);"
    else
        echo "❌ environment.js not found"
    fi
else
    echo "❌ webpack directory not found"
fi

# Check environment variables
echo "🌍 Environment Variables:"
echo "NODE_ENV: $NODE_ENV"
echo "SERVER_API_URL: $SERVER_API_URL"
echo "APP_VERSION: $APP_VERSION"

# Load cloud environment if available
if [ -f "env.cloud" ]; then
    echo "☁️ Loading cloud environment..."
    export $(cat env.cloud | grep -v '^#' | xargs)
    echo "NODE_ENV after loading: $NODE_ENV"
    echo "SERVER_API_URL after loading: $SERVER_API_URL"
fi

# Clean previous builds
echo "🧹 Cleaning previous builds..."
rm -rf node_modules package-lock.json target/classes/static/

# Install dependencies
echo "📥 Installing dependencies..."
npm install --verbose

# Test webpack configuration
echo "🧪 Testing webpack configuration..."
if npm run webapp:build:dev --dry-run 2>/dev/null; then
    echo "✅ webapp:build:dev dry-run successful"
else
    echo "⚠️ webapp:build:dev dry-run failed, trying direct webpack..."
fi

# Try direct webpack build
echo "🔨 Testing direct webpack build..."
if [ -f "webpack/webpack.dev.js" ]; then
    echo "✅ webpack.dev.js found"
    npx webpack --config webpack/webpack.dev.js --env stats=minimal --dry-run 2>/dev/null || echo "⚠️ Direct webpack dry-run failed"
else
    echo "❌ webpack.dev.js not found"
fi

# Check for common issues
echo "🔍 Checking for common issues..."

# Check TypeScript configuration
if [ -f "tsconfig.json" ]; then
    echo "✅ tsconfig.json found"
    npx tsc --noEmit --skipLibCheck 2>/dev/null && echo "✅ TypeScript compilation OK" || echo "⚠️ TypeScript compilation issues"
else
    echo "❌ tsconfig.json not found"
fi

# Check ESLint
if [ -f "eslint.config.mjs" ]; then
    echo "✅ eslint.config.mjs found"
    npx eslint --version 2>/dev/null && echo "✅ ESLint available" || echo "⚠️ ESLint issues"
else
    echo "❌ eslint.config.mjs not found"
fi

# Check for missing dependencies
echo "📦 Checking for missing dependencies..."
npm ls --depth=0 2>/dev/null || echo "⚠️ Some dependencies may be missing"

# Try actual build with verbose output
echo "🚀 Attempting actual build..."
echo "Running: npm run webapp:build"
npm run webapp:build

echo "✅ Debug script completed!" 