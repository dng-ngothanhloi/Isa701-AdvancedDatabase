#!/bin/bash

# CORS Test Script for Warehouse Management System
# This script tests CORS configuration for cloud deployment

echo "🚀 Testing CORS Configuration for Cloud Deployment"
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test URLs
BASE_URL="http://localhost:8080"
API_ENDPOINT="/api/selective-embedding-migration/stats"

# Test origins
TEST_ORIGINS=(
    "https://githubpreview.dev"
    "https://codespaces.dev"
    "https://your-domain.com"
    "http://localhost:3000"
    "https://localhost:3000"
)

echo -e "\n${YELLOW}1. Testing Preflight Requests (OPTIONS)${NC}"
echo "------------------------------------------------"

for origin in "${TEST_ORIGINS[@]}"; do
    echo -n "Testing origin: $origin ... "
    
    response=$(curl -s -o /dev/null -w "%{http_code}" \
        -X OPTIONS \
        -H "Origin: $origin" \
        -H "Access-Control-Request-Method: GET" \
        -H "Access-Control-Request-Headers: Content-Type" \
        "$BASE_URL$API_ENDPOINT")
    
    if [ "$response" = "200" ]; then
        echo -e "${GREEN}✓ OK (200)${NC}"
    else
        echo -e "${RED}✗ Failed ($response)${NC}"
    fi
done

echo -e "\n${YELLOW}2. Testing Actual Requests (GET)${NC}"
echo "-------------------------------------------"

for origin in "${TEST_ORIGINS[@]}"; do
    echo -n "Testing origin: $origin ... "
    
    response=$(curl -s -o /dev/null -w "%{http_code}" \
        -X GET \
        -H "Origin: $origin" \
        -H "Content-Type: application/json" \
        "$BASE_URL$API_ENDPOINT")
    
    if [ "$response" = "200" ]; then
        echo -e "${GREEN}✓ OK (200)${NC}"
    else
        echo -e "${RED}✗ Failed ($response)${NC}"
    fi
done

echo -e "\n${YELLOW}3. Testing CORS Headers${NC}"
echo "--------------------------------"

# Test CORS headers
echo -n "Checking CORS headers ... "
cors_headers=$(curl -s -I -H "Origin: https://githubpreview.dev" \
    "$BASE_URL$API_ENDPOINT" | grep -i "access-control")

if [ -n "$cors_headers" ]; then
    echo -e "${GREEN}✓ CORS headers present${NC}"
    echo "$cors_headers"
else
    echo -e "${RED}✗ No CORS headers found${NC}"
fi

echo -e "\n${YELLOW}4. Testing Migration Endpoint${NC}"
echo "-------------------------------------"

# Test migration endpoint
echo -n "Testing migration endpoint ... "
migration_response=$(curl -s -o /dev/null -w "%{http_code}" \
    -X POST \
    -H "Origin: https://githubpreview.dev" \
    -H "Content-Type: application/json" \
    "$BASE_URL/api/selective-embedding-migration/validate")

if [ "$migration_response" = "200" ]; then
    echo -e "${GREEN}✓ Migration endpoint accessible (200)${NC}"
else
    echo -e "${RED}✗ Migration endpoint failed ($migration_response)${NC}"
fi

echo -e "\n${YELLOW}5. Testing with curl verbose output${NC}"
echo "----------------------------------------"

echo "Testing detailed CORS response:"
curl -v -X OPTIONS \
    -H "Origin: https://githubpreview.dev" \
    -H "Access-Control-Request-Method: GET" \
    -H "Access-Control-Request-Headers: Content-Type" \
    "$BASE_URL$API_ENDPOINT" 2>&1 | grep -E "(Access-Control|HTTP/)"

echo -e "\n${GREEN}✅ CORS Test Completed${NC}"
echo "=================================================="
echo ""
echo "If all tests pass, your application is ready for cloud deployment!"
echo ""
echo "To run with cloud profile:"
echo "  ./mvnw spring-boot:run -Dspring.profiles.active=cloud"
echo ""
echo "To test from a different domain:"
echo "  curl -X GET -H 'Origin: https://your-domain.com' $BASE_URL$API_ENDPOINT" 