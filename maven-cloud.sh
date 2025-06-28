#!/bin/bash

# Maven Cloud Build and Run Script
# This script runs Maven with cloud profile to build frontend and backend together

echo "🌐 MAVEN CLOUD BUILD AND RUN"
echo "============================"

# Stop any running backend
echo "🛑 Stopping any running backend..."
pkill -f "spring-boot" || true

# Clean and build with cloud profile
echo "🔨 Building application with cloud profile..."
echo "   - Frontend will be built with cloud URLs"
echo "   - Backend will use cloud configuration"
echo ""

./mvnw clean compile -Pcloud

if [ $? -eq 0 ]; then
    echo "✅ Build completed successfully!"
    echo ""
    echo "🚀 Starting application with cloud profile..."
    echo "   Backend URL: https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev"
    echo "   Frontend will use cloud URLs"
    echo ""
    
    # Start with cloud profile
    ./mvnw spring-boot:run -Pcloud
    
else
    echo "❌ Build failed!"
    echo ""
    echo "🔧 Troubleshooting:"
    echo "1. Check if all dependencies are installed: ./mvnw dependency:resolve"
    echo "2. Check if Node.js and npm are available: node --version && npm --version"
    echo "3. Try cleaning and rebuilding: ./mvnw clean compile -Pcloud"
    exit 1
fi 