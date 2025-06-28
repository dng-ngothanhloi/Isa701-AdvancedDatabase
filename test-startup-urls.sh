#!/bin/bash

# Test Startup URLs Script
# Tests the application startup with different SERVER_API_URL values

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}=== $1 ===${NC}"
}

# Function to test startup with specific SERVER_API_URL
test_startup_url() {
    local test_name="$1"
    local server_api_url="$2"
    local spring_profile="$3"
    
    print_header "Testing: $test_name"
    print_status "SERVER_API_URL: $server_api_url"
    print_status "Spring Profile: $spring_profile"
    
    # Set environment variables
    export SERVER_API_URL="$server_api_url"
    export SPRING_PROFILES_ACTIVE="$spring_profile"
    
    print_status "Starting application with test configuration..."
    print_status "Expected Local URL: $server_api_url"
    
    # Start application in background and capture startup logs
    timeout 30s ./mvnw spring-boot:run -Dspring.profiles.active="$spring_profile" 2>&1 | grep -A 10 -B 5 "Application.*is running" || {
        print_warning "Application startup log not found or timeout reached"
    }
    
    # Kill any running Java processes
    pkill -f "spring-boot:run" || true
    pkill -f "WarehouseMgmtApp" || true
    
    echo ""
}

# Main test function
main() {
    print_header "Testing Startup URL Display"
    
    # Test 1: Development environment (localhost)
    test_startup_url "Development Environment" "http://localhost:8080/" "dev"
    
    # Test 2: Cloud environment (Codespaces)
    test_startup_url "Cloud Environment" "https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/" "cloud"
    
    # Test 3: Production environment
    test_startup_url "Production Environment" "https://warehouse-app.vercel.app/" "prod"
    
    # Test 4: No SERVER_API_URL (fallback to localhost)
    print_header "Testing: No SERVER_API_URL (Fallback)"
    print_status "SERVER_API_URL: NOT SET"
    print_status "Spring Profile: dev"
    
    unset SERVER_API_URL
    export SPRING_PROFILES_ACTIVE="dev"
    
    print_status "Starting application without SERVER_API_URL..."
    print_status "Expected Local URL: http://localhost:8080/"
    
    timeout 30s ./mvnw spring-boot:run -Dspring.profiles.active="dev" 2>&1 | grep -A 10 -B 5 "Application.*is running" || {
        print_warning "Application startup log not found or timeout reached"
    }
    
    # Kill any running Java processes
    pkill -f "spring-boot:run" || true
    pkill -f "WarehouseMgmtApp" || true
    
    echo ""
    
    print_header "Test Summary"
    print_status "All tests completed. Check the output above for URL display."
    print_status "The Local URL should now show SERVER_API_URL instead of localhost when available."
}

# Show help
show_help() {
    echo "Test Startup URLs Script"
    echo ""
    echo "Usage: $0 [test|help]"
    echo ""
    echo "Options:"
    echo "  test    Run all startup URL tests"
    echo "  help    Show this help message"
    echo ""
    echo "This script tests the application startup with different SERVER_API_URL values"
    echo "to verify that the Local URL display uses the environment variable correctly."
}

# Parse command line arguments
case "${1:-}" in
    "test")
        main
        ;;
    "help"|"-h"|"--help"|"")
        show_help
        ;;
    *)
        print_error "Unknown option: $1"
        echo ""
        show_help
        exit 1
        ;;
esac 