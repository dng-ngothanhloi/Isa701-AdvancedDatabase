#!/bin/bash

# Test Cloud Startup Script
# This script tests cloud startup and debugs logging issues

echo "🧪 TESTING CLOUD STARTUP"
echo "========================"

# Stop any running backend
echo "🛑 Stopping any running backend..."
pkill -f "spring-boot" || true

# Clean previous build
echo "🧹 Cleaning previous build..."
./mvnw clean

# Build with cloud profile
echo "🔨 Building with cloud profile..."
./mvnw compile -Pcloud

if [ $? -eq 0 ]; then
    echo "✅ Build completed successfully!"
    echo ""
    echo "🚀 Starting with cloud profile..."
    echo "   Backend URL: https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev"
    echo "   Logging: Standard format (no logstash)"
    echo ""
    
    # Start with cloud profile and capture logs
    ./mvnw spring-boot:run -Pcloud 2>&1 | tee cloud-startup.log
    
else
    echo "❌ Build failed!"
    echo ""
    echo "🔧 Troubleshooting:"
    echo "1. Check if all dependencies are installed: ./mvnw dependency:resolve"
    echo "2. Check if Node.js and npm are available: node --version && npm --version"
    echo "3. Try cleaning and rebuilding: ./mvnw clean compile -Pcloud"
    exit 1
fi 