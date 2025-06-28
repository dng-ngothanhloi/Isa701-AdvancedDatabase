#!/bin/bash

# Run Application in Cloud Environment
# This script builds and runs the application with cloud profile

echo "🌐 RUNNING APPLICATION IN CLOUD ENVIRONMENT"
echo "==========================================="

# Stop any running backend
echo "🛑 Stopping any running backend..."
pkill -f "spring-boot" || true

# Clean and build with cloud profile
echo "🔨 Building application with cloud profile..."
./mvnw clean compile -Pcloud

if [ $? -eq 0 ]; then
    echo "✅ Build completed successfully!"
    echo ""
    echo "🚀 Starting backend with cloud profile..."
    echo "   Backend URL: https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev"
    echo "   Frontend will use cloud URLs"
    echo ""
    
    # Start backend with cloud profile
    ./mvnw spring-boot:run -Pcloud
    
else
    echo "❌ Build failed!"
    exit 1
fi 