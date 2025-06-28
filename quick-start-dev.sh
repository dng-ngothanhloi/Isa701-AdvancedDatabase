#!/bin/bash

# Quick Start Script for Development Environment
# Purpose: Start application and run analysis with MongoDB Atlas

echo "🚀 Quick Start for Development Environment"
echo "=========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# MongoDB Atlas URI
MONGODB_URI="mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true"

echo -e "${BLUE}📋 Configuration:${NC}"
echo "  Profile: development (dev)"
echo "  Database: MongoDB Atlas Cloud"
echo "  URI: ${MONGODB_URI:0:50}..."
echo ""

# Function to setup environment
setup_environment() {
    echo -e "${BLUE}🔧 Setting up environment...${NC}"
    export MONGODB_URI="$MONGODB_URI"
    export SPRING_PROFILES_ACTIVE=dev
    export SERVER_API_URL="http://localhost:8080"
    echo -e "${GREEN}✅ Environment variables set${NC}"
    echo ""
}

# Function to check if application is running
check_application() {
    echo -e "${BLUE}🔍 Checking if application is running...${NC}"
    
    if curl -s "http://localhost:8080/api/management/health" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Application is already running${NC}"
        return 0
    else
        echo -e "${YELLOW}⚠️  Application is not running${NC}"
        return 1
    fi
}

# Function to start application
start_application() {
    echo -e "${BLUE}🚀 Starting application...${NC}"
    echo -e "${YELLOW}This will take 30-60 seconds to start...${NC}"
    echo ""
    
    # Start application in background
    ./mvnw spring-boot:run -Dspring.profiles.active=dev &
    APP_PID=$!
    
    echo -e "${YELLOW}Application starting with PID: $APP_PID${NC}"
    echo -e "${YELLOW}Waiting for application to be ready...${NC}"
    
    # Wait for application to start
    for i in {1..60}; do
        if curl -s "http://localhost:8080/api/management/health" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ Application is ready!${NC}"
            return 0
        fi
        echo -n "."
        sleep 1
    done
    
    echo -e "${RED}❌ Application failed to start within 60 seconds${NC}"
    return 1
}

# Function to run analysis
run_analysis() {
    echo ""
    echo -e "${BLUE}📊 Running Storage Analysis...${NC}"
    echo "====================================="
    
    # Run storage baseline analysis
    echo -e "${YELLOW}1. Running storage baseline analysis...${NC}"
    ./analyze-storage-baseline-api.sh
    
    echo ""
    echo -e "${YELLOW}2. Testing MongoDB aggregation...${NC}"
    ./test-mongodb-direct-api.sh
    
    echo ""
    echo -e "${YELLOW}3. Testing query performance...${NC}"
    ./test-query-performance.sh
}

# Function to show available commands
show_commands() {
    echo ""
    echo -e "${BLUE}📋 Available Commands:${NC}"
    echo "============================="
    echo ""
    echo -e "${YELLOW}Analysis Scripts:${NC}"
    echo "  ./analyze-storage-baseline-api.sh    - Storage baseline analysis"
    echo "  ./test-mongodb-direct-api.sh         - MongoDB aggregation test"
    echo "  ./test-query-performance.sh          - Query performance test"
    echo "  ./test-mongodb-aggregation.sh        - Aggregation performance test"
    echo "  ./verify-embedded-structure.sh       - Embedded structure verification"
    echo ""
    echo -e "${YELLOW}Environment Scripts:${NC}"
    echo "  ./setup-dev-environment.sh           - Setup development environment"
    echo "  ./load-env.sh dev                    - Load development environment"
    echo ""
    echo -e "${YELLOW}Application Control:${NC}"
    echo "  ./mvnw spring-boot:run -Dspring.profiles.active=dev  - Start application"
    echo "  Ctrl+C                                 - Stop application"
    echo ""
}

# Main execution
main() {
    setup_environment
    
    if check_application; then
        echo -e "${GREEN}✅ Application is ready for analysis${NC}"
    else
        echo -e "${YELLOW}Starting application...${NC}"
        if ! start_application; then
            echo -e "${RED}❌ Failed to start application${NC}"
            exit 1
        fi
    fi
    
    # Ask user if they want to run analysis
    echo ""
    echo -e "${BLUE}Do you want to run analysis now? (y/n)${NC}"
    read -r response
    
    if [[ "$response" =~ ^[Yy]$ ]]; then
        run_analysis
    else
        echo -e "${YELLOW}Skipping analysis. You can run it manually later.${NC}"
    fi
    
    show_commands
    
    echo -e "${GREEN}✅ Quick start completed!${NC}"
    echo ""
    echo -e "${BLUE}Application is running at: http://localhost:8080${NC}"
    echo -e "${BLUE}API Documentation: http://localhost:8080/swagger-ui/index.html${NC}"
}

# Run main function
main 