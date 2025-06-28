#!/bin/bash

# 🌍 Quick Start Environment Selection Script
# Usage: ./quick-start-env.sh [dev|cloud|prod]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
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

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Function to check environment
check_environment() {
    print_header "Checking Current Environment"
    
    if [ -n "$NODE_ENV" ]; then
        print_status "NODE_ENV: $NODE_ENV"
    else
        print_warning "NODE_ENV: NOT SET"
    fi
    
    if [ -n "$SPRING_PROFILES_ACTIVE" ]; then
        print_status "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
    else
        print_warning "SPRING_PROFILES_ACTIVE: NOT SET"
    fi
    
    if [ -n "$SERVER_API_URL" ]; then
        print_status "SERVER_API_URL: $SERVER_API_URL"
    else
        print_warning "SERVER_API_URL: NOT SET"
    fi
}

# Function to start development environment
start_development() {
    print_header "Starting Development Environment"
    
    print_status "Loading development environment variables..."
    if [ -f "env.development" ]; then
        export $(cat env.development | grep -v '^#' | xargs)
        print_status "Development environment loaded"
    else
        print_warning "env.development file not found, using defaults"
        export NODE_ENV=development
        export SPRING_PROFILES_ACTIVE=dev
        export SERVER_API_URL=http://localhost:8080/
    fi
    
    check_environment
    
    print_status "Starting application with development profile..."
    ./mvnw spring-boot:run -Pwebapp
}

# Function to start cloud environment
start_cloud() {
    print_header "Starting Cloud Environment"
    
    print_status "Loading cloud environment variables..."
    if [ -f "env.cloud" ]; then
        export $(cat env.cloud | grep -v '^#' | xargs)
        print_status "Cloud environment loaded"
    else
        print_warning "env.cloud file not found, using defaults"
        export NODE_ENV=cloud
        export SPRING_PROFILES_ACTIVE=cloud
        export SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
    fi
    
    check_environment
    
    print_status "Starting application with cloud profile..."
    ./mvnw spring-boot:run -Pcloud
}

# Function to start production environment
start_production() {
    print_header "Starting Production Environment"
    
    print_status "Building production package..."
    ./mvnw clean package -Pprod
    
    if [ $? -eq 0 ]; then
        print_status "Production build successful"
        
        print_status "Starting production JAR..."
        java -jar target/*.jar --spring.profiles.active=prod
    else
        print_error "Production build failed"
        exit 1
    fi
}

# Function to show help
show_help() {
    echo "🌍 Quick Start Environment Selection Script"
    echo ""
    echo "Usage: $0 [dev|cloud|prod]"
    echo ""
    echo "Options:"
    echo "  dev     Start development environment (localhost)"
    echo "  cloud   Start cloud environment (Codespaces)"
    echo "  prod    Build and start production environment"
    echo "  check   Check current environment variables"
    echo "  help    Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 dev      # Start development environment"
    echo "  $0 cloud    # Start cloud environment"
    echo "  $0 prod     # Build and start production"
    echo "  $0 check    # Check current environment"
    echo ""
    echo "Environment Summary:"
    echo "  🏠 Development: ./mvnw spring-boot:run -Pwebapp"
    echo "  ☁️  Cloud:      ./mvnw spring-boot:run -Pcloud"
    echo "  🚀 Production:  ./mvnw clean package -Pprod"
}

# Main script logic
main() {
    # Check if Maven wrapper exists
    if [ ! -f "./mvnw" ]; then
        print_error "Maven wrapper (mvnw) not found. Please run this script from the project root."
        exit 1
    fi
    
    # Make Maven wrapper executable
    chmod +x ./mvnw
    
    # Parse command line arguments
    case "${1:-}" in
        "dev"|"development")
            start_development
            ;;
        "cloud"|"codespaces")
            start_cloud
            ;;
        "prod"|"production")
            start_production
            ;;
        "check"|"status")
            check_environment
            ;;
        "help"|"-h"|"--help"|"")
            show_help
            ;;
        *)
            print_error "Unknown environment: $1"
            echo ""
            show_help
            exit 1
            ;;
    esac
}

# Run main function with all arguments
main "$@" 