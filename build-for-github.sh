#!/bin/bash

# GitHub Actions Build Script
# This script is optimized for GitHub Actions environment

set -e

echo "🚀 Building for GitHub Actions..."

# Set environment variables for cloud deployment
export NODE_ENV=development
export SERVER_API_URL=https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev/
export APP_VERSION=GITHUB-ACTIONS

# Clean previous builds
rm -rf node_modules package-lock.json target/classes/static/

# Install dependencies
npm install --no-optional --no-audit --no-fund

# Build the application
npm run webapp:build

echo "✅ Build completed successfully!"
