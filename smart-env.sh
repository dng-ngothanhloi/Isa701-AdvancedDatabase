#!/bin/bash

# Smart Environment Manager Wrapper
# This script provides easy access to the smart environment manager

echo "🔍 SMART ENVIRONMENT MANAGER"
echo "============================"

# Check if scripts directory exists
if [ ! -d "scripts" ]; then
    echo "📁 Creating scripts directory..."
    mkdir -p scripts
fi

# Check if smart-env.js exists
if [ ! -f "scripts/smart-env.js" ]; then
    echo "❌ scripts/smart-env.js not found!"
    echo "Please ensure the smart-env.js file exists in the scripts directory."
    exit 1
fi

# Make the script executable
chmod +x scripts/smart-env.js

# Pass all arguments to the Node.js script
node scripts/smart-env.js "$@" 