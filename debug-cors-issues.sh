#!/bin/bash

# Debug CORS Issues Script
# This script helps debug CORS issues in cloud environment

echo "🔍 DEBUG CORS ISSUES"
echo "===================="

# Get current environment
echo "📋 Current Environment:"
echo "NODE_ENV: ${NODE_ENV:-'NOT SET'}"
echo "SERVER_API_URL: ${SERVER_API_URL:-'NOT SET'}"
echo ""

# Check if backend is running
echo "🔍 Checking if backend is running..."
if pgrep -f "spring-boot" > /dev/null; then
    echo "✅ Backend is running"
    ps aux | grep spring-boot | grep -v grep
else
    echo "❌ Backend is not running"
    echo "Start backend with: ./mvnw spring-boot:run -Dspring.profiles.active=cloud"
fi

echo ""

# Test backend health
echo "🧪 Testing backend health..."
BACKEND_URL="${SERVER_API_URL:-https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/}"
HEALTH_URL="${BACKEND_URL%/}/management/health"

echo "Testing: $HEALTH_URL"
curl -s -o /dev/null -w "HTTP Status: %{http_code}\n" "$HEALTH_URL" || echo "❌ Backend health check failed"

echo ""

# Test CORS headers
echo "🧪 Testing CORS headers..."
echo "Testing preflight request to: ${BACKEND_URL%/}/api/account"

curl -X OPTIONS \
  -H "Origin: https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev" \
  -H "Access-Control-Request-Method: GET" \
  -H "Access-Control-Request-Headers: Authorization,Content-Type" \
  -v "${BACKEND_URL%/}/api/account" 2>&1 | grep -E "(HTTP|Access-Control|Origin)" || echo "❌ CORS preflight test failed"

echo ""

# Test actual API call
echo "🧪 Testing actual API call..."
curl -X GET \
  -H "Origin: https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev" \
  -H "Content-Type: application/json" \
  -v "${BACKEND_URL%/}/api/account" 2>&1 | head -20 || echo "❌ API call test failed"

echo ""

# Check application logs
echo "📋 Checking application logs..."
echo "Look for CORS-related errors in the application logs:"
echo "  - CORS configuration loaded"
echo "  - CORS filter registered"
echo "  - CORS preflight requests"
echo ""

# Check if cloud profile is active
echo "🔍 Checking if cloud profile is active..."
if curl -s "${BACKEND_URL%/}/management/info" | grep -q "cloud" 2>/dev/null; then
    echo "✅ Cloud profile is active"
else
    echo "⚠️ Cloud profile may not be active"
    echo "Start with: ./mvnw spring-boot:run -Dspring.profiles.active=cloud"
fi

echo ""

# Test with different origins
echo "🧪 Testing with different origins..."
ORIGINS=(
    "https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev"
    "https://*.app.github.dev"
    "https://*.githubpreview.dev"
    "https://*.codespaces.dev"
)

for origin in "${ORIGINS[@]}"; do
    echo "Testing origin: $origin"
    curl -X OPTIONS \
      -H "Origin: $origin" \
      -H "Access-Control-Request-Method: GET" \
      -s -o /dev/null -w "Status: %{http_code}\n" \
      "${BACKEND_URL%/}/api/account" || echo "Failed"
done

echo ""

# Provide solutions
echo "🔧 SOLUTIONS FOR CORS ISSUES:"
echo "============================="
echo ""
echo "1. **Ensure backend is running with cloud profile:**"
echo "   ./mvnw spring-boot:run -Dspring.profiles.active=cloud"
echo ""
echo "2. **Check application logs for CORS errors:**"
echo "   Look for 'CORS' in the application startup logs"
echo ""
echo "3. **Verify CORS configuration is loaded:**"
echo "   Check if application-cloud.yml is being used"
echo ""
echo "4. **Test with curl to isolate frontend vs backend issues:**"
echo "   curl -H 'Origin: https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev' \\"
echo "        -H 'Access-Control-Request-Method: GET' \\"
echo "        -X OPTIONS ${BACKEND_URL%/}/api/account"
echo ""
echo "5. **Check browser console for specific CORS errors:**"
echo "   - Open Developer Tools (F12)"
echo "   - Go to Console tab"
echo "   - Look for CORS violation messages"
echo ""
echo "6. **Verify frontend is using correct API URL:**"
echo "   Check browser Network tab to see actual API calls"
echo ""

# Check if this is a Codespaces environment
if [[ "$CODESPACE_NAME" != "" ]]; then
    echo "🌐 CODESPACES ENVIRONMENT DETECTED"
    echo "=================================="
    echo "Codespace URL: https://$CODESPACE_NAME-8080.app.github.dev"
    echo "Make sure your frontend is calling the correct backend URL"
    echo ""
fi

echo "📞 If issues persist:"
echo "1. Check application logs for detailed CORS errors"
echo "2. Verify the backend is accessible from the frontend domain"
echo "3. Ensure all CORS headers are being sent correctly"
echo "4. Test with a simple curl request to isolate the issue" 