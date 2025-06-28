#!/bin/bash

# Check Environment Variables Script
# This script helps you check which environment is currently loaded

echo "🔍 KIỂM TRA BIẾN MÔI TRƯỜNG HIỆN TẠI"
echo "======================================"

# Check current environment variables
echo "📋 Biến môi trường hiện tại:"
echo "NODE_ENV: ${NODE_ENV:-'NOT SET'}"
echo "ENVIRONMENT: ${ENVIRONMENT:-'NOT SET'}"
echo "SERVER_API_URL: ${SERVER_API_URL:-'NOT SET'}"
echo "SERVER_API_URL_WS: ${SERVER_API_URL_WS:-'NOT SET'}"
echo "CLOUD_DEPLOYMENT: ${CLOUD_DEPLOYMENT:-'NOT SET'}"
echo "APP_VERSION: ${APP_VERSION:-'NOT SET'}"
echo ""

# Determine which environment is loaded
if [ "$NODE_ENV" = "cloud" ]; then
    echo "✅ Đang sử dụng CLOUD environment"
    echo "🌐 API URL: $SERVER_API_URL"
    echo "🔌 WebSocket URL: $SERVER_API_URL_WS"
elif [ "$NODE_ENV" = "development" ]; then
    echo "✅ Đang sử dụng DEVELOPMENT environment"
    echo "🌐 API URL: $SERVER_API_URL"
    echo "🔌 WebSocket URL: $SERVER_API_URL_WS"
else
    echo "⚠️ Không xác định được environment"
    echo "NODE_ENV: $NODE_ENV"
fi

echo ""

# Test webpack configuration
echo "🧪 Kiểm tra webpack configuration:"
if node -e "const env = require('./webpack/environment.js'); console.log('✅ webpack/environment.js loaded successfully'); console.log('SERVER_API_URL:', env.SERVER_API_URL); console.log('SERVER_API_URL_WS:', env.SERVER_API_URL_WS);" 2>/dev/null; then
    echo "✅ webpack/environment.js hoạt động bình thường"
else
    echo "❌ Lỗi khi load webpack/environment.js"
fi

echo ""

# Check if environment files exist
echo "📁 Kiểm tra file environment:"
if [ -f "env.development" ]; then
    echo "✅ env.development tồn tại"
else
    echo "❌ env.development không tồn tại"
fi

if [ -f "env.cloud" ]; then
    echo "✅ env.cloud tồn tại"
else
    echo "❌ env.cloud không tồn tại"
fi

echo ""

# Show how to load environments
echo "📖 CÁCH LOAD ENVIRONMENT:"
echo "Development: export \$(cat env.development | grep -v '^#' | xargs)"
echo "Cloud: export \$(cat env.cloud | grep -v '^#' | xargs)"
echo ""

# Show current environment summary
echo "📊 TÓM TẮT ENVIRONMENT HIỆN TẠI:"
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
    echo "⚠️ Cần load environment variables"
fi

echo ""
echo "🔧 Để thay đổi environment, chạy:"
echo "   ./check-environment.sh" 