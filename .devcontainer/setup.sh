#!/bin/bash

# GitHub Codespaces Setup Script
# This script sets up the development environment in Codespaces

echo "🚀 SETTING UP GITHUB CODESPACES"
echo "================================"

# Check for existing CODESPACE_ID
if [ -n "$CODESPACE_ID" ]; then
    echo "🔄 Reusing existing Codespace: $CODESPACE_ID"
    CODESPACE_NAME="$CODESPACE_ID"
elif [ -n "$CODESPACE_NAME" ]; then
    echo "🔄 Using current Codespace: $CODESPACE_NAME"
    CODESPACE_ID="$CODESPACE_NAME"
else
    echo "🆕 No existing Codespace detected, will create new one"
    CODESPACE_ID=""
fi

# Update system
echo "📦 Updating system packages..."
sudo apt-get update && sudo apt-get upgrade -y

# Install additional tools
echo "🔧 Installing additional tools..."
sudo apt-get install -y \
    curl \
    wget \
    git \
    vim \
    htop \
    tree \
    jq \
    unzip

# Setup environment variables for Codespaces
echo "🌐 Setting up environment variables..."
cat > ~/.bashrc.local << EOF
# Codespaces Environment Variables
export CODESPACE_ID="${CODESPACE_ID:-unknown}"
export CODESPACE_NAME="${CODESPACE_NAME:-$CODESPACE_ID}"
export CODESPACE_URL="https://${CODESPACE_ID:-unknown}-8080.app.github.dev"
export NODE_ENV=cloud
export SERVER_API_URL="https://${CODESPACE_ID:-unknown}-8080.app.github.dev/"
export SERVER_API_URL_WS="wss://${CODESPACE_ID:-unknown}-8080.app.github.dev/"
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

# Source the environment
source ~/.bashrc.local

# Install Node.js dependencies
echo "📦 Installing Node.js dependencies..."
npm ci

# Install Maven dependencies
echo "📦 Installing Maven dependencies..."
./mvnw dependency:resolve

# Build the application with cloud profile
echo "🔨 Building application with cloud profile..."
export SPRING_PROFILES_ACTIVE=cloud
export NODE_ENV=cloud
./mvnw clean compile -Pcloud

# Setup Git configuration
echo "🔧 Setting up Git configuration..."
git config --global user.name "GitHub Codespaces"
git config --global user.email "codespaces@github.com"

# Create development scripts
echo "📝 Creating development scripts..."

# Create deploy script for Codespaces
cat > deploy-codespaces.sh << EOF
#!/bin/bash

# Deploy to Codespaces Script
echo "🚀 DEPLOYING TO CODESPACES"
echo "=========================="

# Set cloud environment
export SPRING_PROFILES_ACTIVE=cloud
export NODE_ENV=cloud

# Get Codespaces URL
CODESPACE_URL="https://${CODESPACE_ID:-unknown}-8080.app.github.dev"
echo "🌐 Codespaces URL: \$CODESPACE_URL"

# Build with cloud profile
echo "🔨 Building with cloud profile..."
./mvnw clean compile -Pcloud

# Start the application
echo "🚀 Starting application..."
./mvnw spring-boot:run -Pcloud
EOF

chmod +x deploy-codespaces.sh

# Create quick start script
cat > quick-start.sh << EOF
#!/bin/bash

# Quick Start Script for Codespaces
echo "⚡ QUICK START FOR CODESPACES"
echo "============================="

# Set cloud environment
export SPRING_PROFILES_ACTIVE=cloud
export NODE_ENV=cloud

# Check environment
echo "🔍 Checking environment..."
./smart-env.sh --check

# Build frontend
echo "🔨 Building frontend..."
./smart-env.sh --build

# Start backend
echo "🚀 Starting backend..."
./mvnw spring-boot:run -Pcloud
EOF

chmod +x quick-start.sh

# Create status script
cat > status.sh << EOF
#!/bin/bash

# Status Script for Codespaces
echo "📊 CODESPACES STATUS"
echo "===================="

echo "🆔 Codespace ID: ${CODESPACE_ID:-unknown}"
echo "🌐 Codespaces URL: https://${CODESPACE_ID:-unknown}-8080.app.github.dev"
echo "🔧 Environment: \$(./smart-env.sh --check | grep "Environment:" | tail -1)"
echo "📦 Node.js: \$(node --version)"
echo "☕ Java: \$(java --version | head -1)"
echo "📋 Maven: \$(./mvnw --version | head -1)"
echo "🌐 Spring Profile: \${SPRING_PROFILES_ACTIVE:-cloud}"
echo "📦 Node Environment: \${NODE_ENV:-cloud}"

# Check if application is running
if pgrep -f "spring-boot" > /dev/null; then
    echo "✅ Application is running"
else
    echo "❌ Application is not running"
fi

echo ""
echo "🚀 Quick commands:"
echo "  ./quick-start.sh    - Quick start"
echo "  ./deploy-codespaces.sh - Deploy to Codespaces"
echo "  ./status.sh         - Check status"
echo "  ./smart-env.sh --check - Check environment"
EOF

chmod +x status.sh

# Create environment update script
cat > update-codespace-env.sh << EOF
#!/bin/bash

# Update Codespace Environment Script
echo "🔄 UPDATING CODESPACE ENVIRONMENT"
echo "================================"

# Check if CODESPACE_ID is provided
if [ -z "\$1" ]; then
    echo "❌ Please provide CODESPACE_ID"
    echo "Usage: ./update-codespace-env.sh <CODESPACE_ID>"
    echo "Example: ./update-codespace-env.sh super-broccoli-pj96jxxr4p7q3945r"
    exit 1
fi

CODESPACE_ID="\$1"
echo "🆔 Setting Codespace ID: \$CODESPACE_ID"

# Update environment variables
export CODESPACE_ID="\$CODESPACE_ID"
export CODESPACE_URL="https://\$CODESPACE_ID-8080.app.github.dev"
export SERVER_API_URL="https://\$CODESPACE_ID-8080.app.github.dev/"
export SERVER_API_URL_WS="wss://\$CODESPACE_ID-8080.app.github.dev/"
export SPRING_PROFILES_ACTIVE=cloud
export NODE_ENV=cloud

# Update .bashrc.local
cat > ~/.bashrc.local << 'ENVEOF'
# Codespaces Environment Variables
export CODESPACE_ID="\$CODESPACE_ID"
export CODESPACE_NAME="\$CODESPACE_ID"
export CODESPACE_URL="https://\$CODESPACE_ID-8080.app.github.dev"
export NODE_ENV=cloud
export SERVER_API_URL="https://\$CODESPACE_ID-8080.app.github.dev/"
export SERVER_API_URL_WS="wss://\$CODESPACE_ID-8080.app.github.dev/"
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
ENVEOF

# Source the updated environment
source ~/.bashrc.local

echo "✅ Environment updated successfully!"
echo "🌐 New URL: \$CODESPACE_URL"
echo "🔄 Please restart your application: ./mvnw spring-boot:run -Pcloud"
EOF

chmod +x update-codespace-env.sh

echo "✅ Setup completed successfully!"
echo ""
if [ -n "$CODESPACE_ID" ]; then
    echo "🆔 Codespace ID: $CODESPACE_ID"
    echo "🌐 Your Codespaces URL: https://$CODESPACE_ID-8080.app.github.dev"
else
    echo "🆕 New Codespace will be created when you open Codespaces"
fi
echo "🔧 Environment: Cloud (SPRING_PROFILES_ACTIVE=cloud, NODE_ENV=cloud)"
echo "🚀 Quick start: ./quick-start.sh"
echo "📊 Check status: ./status.sh"
echo "🔄 Update environment: ./update-codespace-env.sh <CODESPACE_ID>"
echo ""
echo "🎉 Ready to develop!"

# Test environment
./test-codespace-env.sh

# Set Codespace ID (nếu cần)
./set-codespace-id.sh super-broccoli-pj96jxxr4p7q3945r

# Quick start
./quick-start.sh

# Check status
./status.sh 