#!/bin/bash

# Test Cloud Build Script
# This script tests the cloud build process and verifies configuration

echo "🧪 TESTING CLOUD BUILD PROCESS"
echo "=============================="

# Check environment
echo "📋 Environment Check:"
echo "NODE_ENV: ${NODE_ENV:-'NOT SET'}"
echo "SERVER_API_URL: ${SERVER_API_URL:-'NOT SET'}"
echo ""

# Test webpack cloud configuration
echo "🔨 Testing webpack cloud configuration..."
NODE_ENV=production SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev npm run webapp:build:cloud

if [ $? -eq 0 ]; then
    echo "✅ Webpack cloud build successful!"
    
    # Check if the built files contain cloud URLs
    echo "🔍 Checking built files for cloud URLs..."
    if grep -r "super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev" target/classes/static/ > /dev/null 2>&1; then
        echo "✅ Cloud URLs found in built files"
    else
        echo "⚠️ Cloud URLs not found in built files"
    fi
    
else
    echo "❌ Webpack cloud build failed!"
    exit 1
fi

echo ""
echo "🚀 Ready to run with Maven cloud profile:"
echo "   ./mvnw spring-boot:run -Pcloud" 