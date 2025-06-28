#!/bin/bash

# Build Frontend for Cloud Environment
# This script builds the frontend with cloud environment variables

echo "🌐 BUILDING FRONTEND FOR CLOUD ENVIRONMENT"
echo "=========================================="

# Load cloud environment variables
echo "📋 Loading cloud environment variables..."
source env.cloud

# Verify environment variables
echo "🔍 Verifying environment variables:"
echo "NODE_ENV: ${NODE_ENV}"
echo "SERVER_API_URL: ${SERVER_API_URL}"
echo ""

# Clean previous build
echo "🧹 Cleaning previous build..."
npm run clean-www

# Build frontend with cloud configuration
echo "🔨 Building frontend with cloud configuration..."
npm run webapp:build:cloud

if [ $? -eq 0 ]; then
    echo "✅ Frontend build completed successfully!"
    echo ""
    echo "📁 Build output: target/classes/static/"
    echo "🌐 Frontend will use API URL: ${SERVER_API_URL}"
    echo ""
    echo "🚀 You can now start the backend with:"
    echo "   ./mvnw spring-boot:run -Dspring.profiles.active=cloud"
else
    echo "❌ Frontend build failed!"
    exit 1
fi 