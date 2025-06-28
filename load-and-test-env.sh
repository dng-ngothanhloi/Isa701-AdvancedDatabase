#!/bin/bash

# Load and Test Environment Script
# This script helps you load and test environment variables

if [ $# -eq 0 ]; then
    echo "🔧 LOAD AND TEST ENVIRONMENT"
    echo "============================"
    echo ""
    echo "Cách sử dụng:"
    echo "  ./load-and-test-env.sh dev    # Load development environment"
    echo "  ./load-and-test-env.sh cloud  # Load cloud environment"
    echo "  ./load-and-test-env.sh test   # Test current environment"
    echo ""
    exit 1
fi

ENV_TYPE=$1

case $ENV_TYPE in
    "dev"|"development")
        echo "🔄 Loading DEVELOPMENT environment..."
        if [ -f "env.development" ]; then
            export $(cat env.development | grep -v '^#' | xargs)
            echo "✅ Development environment loaded successfully"
        else
            echo "❌ env.development file not found"
            exit 1
        fi
        ;;
    "cloud")
        echo "🔄 Loading CLOUD environment..."
        if [ -f "env.cloud" ]; then
            export $(cat env.cloud | grep -v '^#' | xargs)
            echo "✅ Cloud environment loaded successfully"
        else
            echo "❌ env.cloud file not found"
            exit 1
        fi
        ;;
    "test")
        echo "🧪 Testing current environment..."
        ;;
    *)
        echo "❌ Invalid environment type: $ENV_TYPE"
        echo "Valid options: dev, cloud, test"
        exit 1
        ;;
esac

echo ""
echo "📋 Environment Variables:"
echo "NODE_ENV: $NODE_ENV"
echo "ENVIRONMENT: $ENVIRONMENT"
echo "SERVER_API_URL: $SERVER_API_URL"
echo "SERVER_API_URL_WS: $SERVER_API_URL_WS"
echo "CLOUD_DEPLOYMENT: $CLOUD_DEPLOYMENT"
echo "APP_VERSION: $APP_VERSION"
echo ""

echo "🧪 Testing webpack configuration..."
if node -e "
const env = require('./webpack/environment.js');
console.log('✅ webpack/environment.js loaded successfully');
console.log('SERVER_API_URL:', env.SERVER_API_URL);
console.log('SERVER_API_URL_WS:', env.SERVER_API_URL_WS);
console.log('VERSION:', env.VERSION);
" 2>/dev/null; then
    echo "✅ webpack/environment.js configuration is correct"
else
    echo "❌ Error in webpack/environment.js configuration"
fi

echo ""
echo "📊 Environment Summary:"
if [ "$NODE_ENV" = "cloud" ]; then
    echo "🎯 Environment: CLOUD"
    echo "🌐 API sẽ gọi đến: $SERVER_API_URL"
    echo "🔌 WebSocket sẽ kết nối đến: $SERVER_API_URL_WS"
    echo "📝 Phù hợp cho: GitHub Codespaces, AWS, Azure, GCP"
elif [ "$NODE_ENV" = "development" ]; then
    echo "🎯 Environment: DEVELOPMENT"
    echo "🌐 API sẽ gọi đến: $SERVER_API_URL"
    echo "🔌 WebSocket sẽ kết nối đến: $SERVER_API_URL_WS"
    echo "📝 Phù hợp cho: Local development"
else
    echo "🎯 Environment: UNKNOWN"
    echo "⚠️ Environment variables not properly loaded"
fi

echo ""
echo "🚀 Next steps:"
if [ "$NODE_ENV" = "cloud" ]; then
    echo "   npm run webapp:build"
    echo "   ./mvnw spring-boot:run -Dspring.profiles.active=cloud"
elif [ "$NODE_ENV" = "development" ]; then
    echo "   npm run webapp:build"
    echo "   ./mvnw spring-boot:run"
else
    echo "   Load environment first: ./load-and-test-env.sh dev|cloud"
fi 