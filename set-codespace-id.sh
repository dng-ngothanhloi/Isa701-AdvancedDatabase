#!/bin/bash

# Set Codespace ID and Rebuild Script
# This script sets the CODESPACE_ID and rebuilds the application

echo "🆔 SETTING CODESPACE ID AND REBUILDING"
echo "======================================"

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

# Check if CODESPACE_ID is provided
if [ -z "$1" ]; then
    print_error "Please provide CODESPACE_ID"
    echo ""
    echo "Usage: ./set-codespace-id.sh <CODESPACE_ID>"
    echo "Example: ./set-codespace-id.sh super-broccoli-pj96jxxr4p7q3945r"
    echo ""
    echo "Available options:"
    echo "  super-broccoli-pj96jxxr4p7q3945r  - Your existing Codespace"
    echo "  <new-codespace-id>               - Any new Codespace ID"
    echo ""
    exit 1
fi

CODESPACE_ID="$1"
CODESPACE_URL="https://${CODESPACE_ID}-8080.app.github.dev"

print_status "Setting Codespace ID: $CODESPACE_ID"
print_status "Codespace URL: $CODESPACE_URL"

# Step 1: Set environment variables
print_status "Step 1: Setting environment variables..."
export CODESPACE_ID="$CODESPACE_ID"
export CODESPACE_NAME="$CODESPACE_ID"
export CODESPACE_URL="$CODESPACE_URL"
export SERVER_API_URL="$CODESPACE_URL/"
export SERVER_API_URL_WS="wss://${CODESPACE_ID}-8080.app.github.dev/"
export NODE_ENV=cloud
export SPRING_PROFILES_ACTIVE=cloud

# Step 2: Update .bashrc.local
print_status "Step 2: Updating .bashrc.local..."
cat > ~/.bashrc.local << EOF
# Codespaces Environment Variables
export CODESPACE_ID="$CODESPACE_ID"
export CODESPACE_NAME="$CODESPACE_ID"
export CODESPACE_URL="$CODESPACE_URL"
export NODE_ENV=cloud
export SERVER_API_URL="$CODESPACE_URL/"
export SERVER_API_URL_WS="wss://${CODESPACE_ID}-8080.app.github.dev/"
export SPRING_PROFILES_ACTIVE=cloud

# Aliases for easy development
alias dev='./mvnw spring-boot:run -Pcloud'
alias build='./mvnw clean compile -Pcloud'
alias test='./mvnw test -Pcloud'
alias frontend='npm run webapp:dev'
alias check='./smart-env.sh --check'
alias deploy='./deploy-codespaces.sh'
alias status='./status.sh'

# Load environment variables
source ~/.bashrc.local
EOF

# Step 3: Source the updated environment
print_status "Step 3: Loading updated environment..."
source ~/.bashrc.local

# Step 4: Verify environment
print_status "Step 4: Verifying environment..."
echo "CODESPACE_ID: $CODESPACE_ID"
echo "CODESPACE_URL: $CODESPACE_URL"
echo "SERVER_API_URL: $SERVER_API_URL"
echo "SERVER_API_URL_WS: $SERVER_API_URL_WS"
echo "NODE_ENV: $NODE_ENV"
echo "SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"

# Step 5: Clean and rebuild
print_status "Step 5: Cleaning and rebuilding application..."
./mvnw clean

# Step 6: Build with cloud profile
print_status "Step 6: Building with cloud profile..."
if ./mvnw clean compile -Pcloud; then
    print_success "Build completed successfully!"
else
    print_error "Build failed!"
    exit 1
fi

# Step 7: Build frontend
print_status "Step 7: Building frontend..."
if ./smart-env.sh --build; then
    print_success "Frontend build completed successfully!"
else
    print_error "Frontend build failed!"
    exit 1
fi

# Step 8: Display summary
echo ""
echo "🎉 CODESPACE ID SETUP COMPLETED!"
echo "================================"
print_success "Codespace ID: $CODESPACE_ID"
print_success "Application URL: $CODESPACE_URL"
print_success "Environment: Cloud"
print_success "Node Environment: Cloud"
print_success "Build: Completed"

echo ""
echo "🚀 Next steps:"
echo "1. Start the application: ./mvnw spring-boot:run -Pcloud"
echo "2. Or use quick start: ./quick-start.sh"
echo "3. Check status: ./status.sh"
echo "4. Verify environment: ./smart-env.sh --check"

echo ""
echo "🌐 Your application will be available at:"
echo "   $CODESPACE_URL"

print_success "Setup completed! 🎉" 