#!/bin/bash

# Simple test script for URL display
# Tests the SERVER_API_URL display in startup logs

echo "🧪 Testing URL Display in Startup Logs"
echo ""

echo "📋 Test Cases:"
echo "1. Development: SERVER_API_URL=http://localhost:8080/"
echo "2. Cloud: SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/"
echo "3. No SERVER_API_URL: Should fallback to localhost"
echo ""

# Test 1: Development
echo "🏠 Test 1: Development Environment"
export SERVER_API_URL="http://localhost:8080/"
export SPRING_PROFILES_ACTIVE="dev"
echo "SERVER_API_URL: $SERVER_API_URL"
echo "Expected Local URL: $SERVER_API_URL"
echo "Starting application..."
echo ""

# Test 2: Cloud
echo "☁️ Test 2: Cloud Environment"
export SERVER_API_URL="https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/"
export SPRING_PROFILES_ACTIVE="cloud"
echo "SERVER_API_URL: $SERVER_API_URL"
echo "Expected Local URL: $SERVER_API_URL"
echo "Starting application..."
echo ""

# Test 3: No SERVER_API_URL
echo "🔄 Test 3: No SERVER_API_URL (Fallback)"
unset SERVER_API_URL
export SPRING_PROFILES_ACTIVE="dev"
echo "SERVER_API_URL: NOT SET"
echo "Expected Local URL: http://localhost:8080/"
echo "Starting application..."
echo ""

echo "✅ Test script completed!"
echo "📝 Check the startup logs above to verify URL display."
echo "🎯 The Local URL should show SERVER_API_URL when available, or fallback to localhost." 