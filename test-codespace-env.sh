#!/bin/bash

# Test Codespace Environment Script
# This script tests if the Codespace environment is correctly configured

echo "🧪 TESTING CODESPACE ENVIRONMENT"
echo "================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Test 1: Check environment variables
echo "🔍 Test 1: Environment Variables"
echo "================================"

CODESPACE_ID="${CODESPACE_ID:-unknown}"
CODESPACE_NAME="${CODESPACE_NAME:-unknown}"
SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE:-unknown}"
NODE_ENV="${NODE_ENV:-unknown}"
SERVER_API_URL="${SERVER_API_URL:-unknown}"

echo "CODESPACE_ID: $CODESPACE_ID"
echo "CODESPACE_NAME: $CODESPACE_NAME"
echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
echo "NODE_ENV: $NODE_ENV"
echo "SERVER_API_URL: $SERVER_API_URL"

# Check if environment is correctly set
if [ "$SPRING_PROFILES_ACTIVE" = "cloud" ]; then
    print_success "✅ SPRING_PROFILES_ACTIVE is set to 'cloud'"
else
    print_error "❌ SPRING_PROFILES_ACTIVE should be 'cloud', but is '$SPRING_PROFILES_ACTIVE'"
fi

if [ "$NODE_ENV" = "cloud" ]; then
    print_success "✅ NODE_ENV is set to 'cloud'"
else
    print_error "❌ NODE_ENV should be 'cloud', but is '$NODE_ENV'"
fi

if [ "$CODESPACE_ID" != "unknown" ]; then
    print_success "✅ CODESPACE_ID is set: $CODESPACE_ID"
else
    print_warning "⚠️ CODESPACE_ID is not set (will use default)"
fi

if [[ "$SERVER_API_URL" == *"app.github.dev"* ]]; then
    print_success "✅ SERVER_API_URL is set to Codespaces URL: $SERVER_API_URL"
else
    print_error "❌ SERVER_API_URL should be a Codespaces URL, but is: $SERVER_API_URL"
fi

echo ""

# Test 2: Check webpack configuration
echo "🔍 Test 2: Webpack Configuration"
echo "================================"

if node -e "const env = require('./webpack/environment.js'); console.log('SERVER_API_URL:', env.SERVER_API_URL); console.log('SERVER_API_URL_WS:', env.SERVER_API_URL_WS);" 2>/dev/null; then
    print_success "✅ webpack/environment.js loads successfully"
else
    print_error "❌ webpack/environment.js failed to load"
fi

echo ""

# Test 3: Check smart-env script
echo "🔍 Test 3: Smart Environment Script"
echo "==================================="

if [ -f "./smart-env.sh" ]; then
    print_success "✅ smart-env.sh exists"
    if [ -x "./smart-env.sh" ]; then
        print_success "✅ smart-env.sh is executable"
        echo "Running smart-env.sh --check..."
        ./smart-env.sh --check | head -10
    else
        print_error "❌ smart-env.sh is not executable"
    fi
else
    print_error "❌ smart-env.sh not found"
fi

echo ""

# Test 4: Check Maven cloud profile
echo "🔍 Test 4: Maven Cloud Profile"
echo "=============================="

if [ -f "./pom.xml" ]; then
    if grep -q "cloud" ./pom.xml; then
        print_success "✅ Cloud profile found in pom.xml"
    else
        print_error "❌ Cloud profile not found in pom.xml"
    fi
else
    print_error "❌ pom.xml not found"
fi

echo ""

# Test 5: Check if we're in Codespaces
echo "🔍 Test 5: Codespaces Detection"
echo "==============================="

if [ -n "$CODESPACE_NAME" ] || [ -n "$CODESPACE_ID" ]; then
    print_success "✅ Running in GitHub Codespaces"
    echo "Codespace: ${CODESPACE_NAME:-$CODESPACE_ID}"
else
    print_warning "⚠️ Not running in GitHub Codespaces (local environment)"
fi

echo ""

# Test 6: Check application build
echo "🔍 Test 6: Application Build Test"
echo "================================="

print_status "Testing if application can build with cloud profile..."

# Set environment for test
export SPRING_PROFILES_ACTIVE=cloud
export NODE_ENV=cloud

# Try to compile (without running tests)
if ./mvnw clean compile -Pcloud -q; then
    print_success "✅ Application builds successfully with cloud profile"
else
    print_error "❌ Application build failed with cloud profile"
fi

echo ""

# Summary
echo "📊 TEST SUMMARY"
echo "==============="

if [ "$SPRING_PROFILES_ACTIVE" = "cloud" ] && [ "$NODE_ENV" = "cloud" ] && [[ "$SERVER_API_URL" == *"app.github.dev"* ]]; then
    print_success "🎉 Codespace environment is correctly configured!"
    echo ""
    echo "🚀 Ready to start application:"
    echo "   ./mvnw spring-boot:run -Pcloud"
    echo "   ./quick-start.sh"
    echo ""
    echo "🌐 Application will be available at:"
    echo "   $SERVER_API_URL"
else
    print_error "❌ Codespace environment needs configuration!"
    echo ""
    echo "🔧 To fix:"
    echo "   ./set-codespace-id.sh super-broccoli-pj96jxxr4p7q3945r"
    echo "   ./update-codespace-env.sh super-broccoli-pj96jxxr4p7q3945r"
    echo ""
    echo "📋 Required settings:"
    echo "   SPRING_PROFILES_ACTIVE=cloud"
    echo "   NODE_ENV=cloud"
    echo "   SERVER_API_URL=https://<codespace-id>-8080.app.github.dev/"
fi 