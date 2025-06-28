#!/bin/bash

# Environment Variable Loader Script
# This script loads environment variables for different deployment scenarios

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to display usage
show_usage() {
    echo -e "${BLUE}Environment Variable Loader for Warehouse Management System${NC}"
    echo ""
    echo "Usage: $0 [environment]"
    echo ""
    echo "Environments:"
    echo "  dev, development    - Load development environment"
    echo "  prod, production    - Load production environment"
    echo "  cloud               - Load cloud environment (GitHub Codespaces, AWS, etc.)"
    echo "  custom [file]       - Load custom environment file"
    echo ""
    echo "Examples:"
    echo "  $0 dev              - Load development environment"
    echo "  $0 cloud            - Load cloud environment"
    echo "  $0 custom my-env    - Load custom environment from my-env file"
    echo ""
    echo "Environment Variables:"
    echo "  SERVER_API_URL      - API server URL"
    echo "  NODE_ENV            - Node.js environment"
    echo "  CLOUD_DEPLOYMENT    - Cloud deployment flag"
    echo "  DEBUG               - Debug mode flag"
}

# Function to load environment file
load_env_file() {
    local env_file=$1
    local env_name=$2
    
    if [ -f "$env_file" ]; then
        echo -e "${GREEN}Loading $env_name environment from $env_file${NC}"
        
        # Export variables from file
        while IFS= read -r line; do
            # Skip comments and empty lines
            if [[ ! "$line" =~ ^[[:space:]]*# ]] && [[ -n "$line" ]]; then
                # Export the variable
                export "$line"
                echo -e "  ${YELLOW}Exported: $line${NC}"
            fi
        done < "$env_file"
        
        echo -e "${GREEN}✅ $env_name environment loaded successfully${NC}"
        echo ""
        
        # Display current environment
        echo -e "${BLUE}Current Environment:${NC}"
        echo "  SERVER_API_URL: ${SERVER_API_URL:-'not set'}"
        echo "  NODE_ENV: ${NODE_ENV:-'not set'}"
        echo "  CLOUD_DEPLOYMENT: ${CLOUD_DEPLOYMENT:-'not set'}"
        echo "  DEBUG: ${DEBUG:-'not set'}"
        echo ""
        
    else
        echo -e "${RED}❌ Environment file $env_file not found${NC}"
        exit 1
    fi
}

# Function to detect cloud environment
detect_cloud_environment() {
    echo -e "${BLUE}Detecting cloud environment...${NC}"
    
    # Check for GitHub Codespaces
    if [ -n "$CODESPACES" ]; then
        echo -e "${GREEN}GitHub Codespaces detected${NC}"
        export CLOUD_DEPLOYMENT=true
        export NODE_ENV=development
        export ENVIRONMENT=cloud
        
        # Try to get the codespace URL
        if [ -n "$GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN" ]; then
            local codespace_url="https://$GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN"
            echo -e "${YELLOW}Detected Codespace URL: $codespace_url${NC}"
            export SERVER_API_URL="$codespace_url/"
            echo -e "${GREEN}Set SERVER_API_URL to: $SERVER_API_URL${NC}"
        fi
        
        return 0
    fi
    
    # Check for other cloud environments
    if [ -n "$AWS_REGION" ] || [ -n "$AZURE_REGION" ] || [ -n "$GCP_REGION" ]; then
        echo -e "${GREEN}Cloud environment detected${NC}"
        export CLOUD_DEPLOYMENT=true
        return 0
    fi
    
    echo -e "${YELLOW}No cloud environment detected${NC}"
    return 1
}

# Main script logic
case "${1:-}" in
    "dev"|"development")
        load_env_file "env.development" "Development"
        ;;
    "prod"|"production")
        load_env_file "env.production" "Production"
        ;;
    "cloud")
        load_env_file "env.cloud" "Cloud"
        detect_cloud_environment
        ;;
    "custom")
        if [ -n "$2" ]; then
            load_env_file "$2" "Custom"
        else
            echo -e "${RED}❌ Custom environment file not specified${NC}"
            show_usage
            exit 1
        fi
        ;;
    "detect")
        detect_cloud_environment
        ;;
    "help"|"-h"|"--help"|"")
        show_usage
        ;;
    *)
        echo -e "${RED}❌ Unknown environment: $1${NC}"
        show_usage
        exit 1
        ;;
esac

# Export common variables
export APP_VERSION=${APP_VERSION:-"DEV"}

echo -e "${GREEN}🚀 Environment setup complete!${NC}"
echo ""
echo -e "${BLUE}To use these variables in your application:${NC}"
echo "  ./mvnw spring-boot:run"
echo "  npm start"
echo ""
echo -e "${BLUE}To verify the configuration:${NC}"
echo "  echo \$SERVER_API_URL"
echo "  echo \$NODE_ENV"
echo "  echo \$CLOUD_DEPLOYMENT" 