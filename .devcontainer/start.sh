#!/bin/bash

# GitHub Codespaces Start Script
# This script runs when Codespaces starts

echo "🚀 STARTING GITHUB CODESPACES"
echo "============================="

# Load environment variables
source ~/.bashrc.local

# Display welcome message
echo "🎉 Welcome to Warehouse Management System!"
echo "🌐 Codespaces URL: $CODESPACE_URL"
echo "🔧 Environment: Cloud"
echo ""

# Check if this is first run
if [ ! -f ~/.codespaces-setup-complete ]; then
    echo "📦 First time setup detected..."
    echo "⏳ This may take a few minutes..."
    
    # Run setup
    bash .devcontainer/setup.sh
    
    # Mark setup as complete
    touch ~/.codespaces-setup-complete
    echo "✅ First time setup completed!"
else
    echo "✅ Environment already configured"
fi

# Display quick start guide
echo ""
echo "🚀 QUICK START GUIDE:"
echo "===================="
echo "1. Check environment: ./smart-env.sh --check"
echo "2. Build application: ./mvnw clean compile -Pcloud"
echo "3. Start application: ./mvnw spring-boot:run -Pcloud"
echo "4. Or use quick start: ./quick-start.sh"
echo "5. Check status: ./status.sh"
echo ""
echo "🌐 Your application will be available at:"
echo "   $CODESPACE_URL"
echo ""
echo "📚 Useful commands:"
echo "   ./quick-start.sh    - Quick start everything"
echo "   ./deploy-codespaces.sh - Deploy to Codespaces"
echo "   ./status.sh         - Check system status"
echo "   ./smart-env.sh --check - Check environment"
echo ""

# Auto-start application if requested
if [ "$AUTO_START" = "true" ]; then
    echo "🚀 Auto-starting application..."
    ./quick-start.sh
else
    echo "💡 To auto-start on next launch, set AUTO_START=true"
fi 