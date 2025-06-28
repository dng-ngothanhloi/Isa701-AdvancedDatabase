#!/bin/bash

# Development Environment Setup Script
# Purpose: Setup development environment with MongoDB Atlas connection

echo "🔧 Setting up Development Environment with MongoDB Atlas"
echo "========================================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# MongoDB Atlas URI for development
MONGODB_URI="mongodb+srv://Admin:Admin_1234@cluster0.bfpk1jw.mongodb.net/warehoure?retryWrites=true&w=majority&appName=Cluster0&tls=true"

echo -e "${BLUE}📋 Environment Configuration:${NC}"
echo "  Profile: development (dev)"
echo "  Database: MongoDB Atlas Cloud"
echo "  URI: ${MONGODB_URI:0:50}..."
echo ""

# Function to export environment variables
setup_environment() {
    echo -e "${BLUE}🔧 Setting up environment variables...${NC}"
    
    # Export MongoDB URI
    export MONGODB_URI="$MONGODB_URI"
    echo -e "${GREEN}✅ MONGODB_URI exported${NC}"
    
    # Export other development variables
    export SPRING_PROFILES_ACTIVE=dev
    export SERVER_API_URL="http://localhost:8080"
    export NODE_ENV=development
    
    echo -e "${GREEN}✅ Environment variables set${NC}"
    echo ""
    
    # Display current environment
    echo -e "${BLUE}Current Environment:${NC}"
    echo "  MONGODB_URI: ${MONGODB_URI:0:50}..."
    echo "  SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
    echo "  SERVER_API_URL: $SERVER_API_URL"
    echo "  NODE_ENV: $NODE_ENV"
    echo ""
}

# Function to check if application is ready to start
check_prerequisites() {
    echo -e "${BLUE}🔍 Checking prerequisites...${NC}"
    
    # Check if Maven is available
    if command -v mvn >/dev/null 2>&1; then
        echo -e "${GREEN}✅ Maven is available${NC}"
    else
        echo -e "${RED}❌ Maven not found. Please install Maven first.${NC}"
        return 1
    fi
    
    # Check if Java is available
    if command -v java >/dev/null 2>&1; then
        echo -e "${GREEN}✅ Java is available${NC}"
        java -version 2>&1 | head -1
    else
        echo -e "${RED}❌ Java not found. Please install Java first.${NC}"
        return 1
    fi
    
    # Check if curl is available
    if command -v curl >/dev/null 2>&1; then
        echo -e "${GREEN}✅ curl is available${NC}"
    else
        echo -e "${RED}❌ curl not found. Please install curl first.${NC}"
        return 1
    fi
    
    # Check if jq is available
    if command -v jq >/dev/null 2>&1; then
        echo -e "${GREEN}✅ jq is available${NC}"
    else
        echo -e "${YELLOW}⚠️  jq not found. JSON parsing will be limited.${NC}"
    fi
    
    echo ""
    return 0
}

# Function to provide startup instructions
show_startup_instructions() {
    echo -e "${BLUE}🚀 Startup Instructions:${NC}"
    echo "=================================="
    echo ""
    echo -e "${YELLOW}1. Start the application:${NC}"
    echo "   ./mvnw spring-boot:run -Dspring.profiles.active=dev"
    echo ""
    echo -e "${YELLOW}2. Wait for application to start (usually 30-60 seconds)${NC}"
    echo ""
    echo -e "${YELLOW}3. Run storage analysis:${NC}"
    echo "   ./analyze-storage-baseline-api.sh"
    echo ""
    echo -e "${YELLOW}4. Test MongoDB aggregation:${NC}"
    echo "   ./test-mongodb-direct-api.sh"
    echo ""
    echo -e "${YELLOW}5. Other available scripts:${NC}"
    echo "   ./test-query-performance.sh"
    echo "   ./test-mongodb-aggregation.sh"
    echo "   ./verify-embedded-structure.sh"
    echo ""
}

# Function to create environment file
create_env_file() {
    echo -e "${BLUE}📝 Creating environment file...${NC}"
    
    cat > env.development << EOF
# Development Environment Configuration
# MongoDB Atlas Cloud Connection
MONGODB_URI=$MONGODB_URI

# Application Configuration
SPRING_PROFILES_ACTIVE=dev
SERVER_API_URL=http://localhost:8080
NODE_ENV=development

# Database Configuration
SPRING_DATA_MONGODB_URI=$MONGODB_URI
SPRING_DATA_MONGODB_DATABASE=warehoure

# Logging Configuration
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_dtu.k30.msc.whm=DEBUG
EOF

    echo -e "${GREEN}✅ Environment file created: env.development${NC}"
    echo ""
}

# Main execution
main() {
    echo -e "${YELLOW}Setting up development environment...${NC}"
    echo ""
    
    if ! check_prerequisites; then
        echo -e "${RED}❌ Prerequisites check failed. Please install missing tools.${NC}"
        exit 1
    fi
    
    setup_environment
    create_env_file
    show_startup_instructions
    
    echo -e "${GREEN}✅ Development environment setup completed!${NC}"
    echo ""
    echo -e "${BLUE}Next steps:${NC}"
    echo "  1. Start the application with: ./mvnw spring-boot:run -Dspring.profiles.active=dev"
    echo "  2. Run analysis scripts to test MongoDB Atlas connection"
    echo ""
}

# Run main function
main 