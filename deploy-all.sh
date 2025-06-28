#!/bin/bash

# Deploy to All Free Platforms Script
# This script deploys the application to GitHub Pages, Vercel, and Netlify

echo "🚀 DEPLOYING TO ALL FREE PLATFORMS"
echo "=================================="

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

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    print_error "Not in a git repository. Please run this script from the project root."
    exit 1
fi

# Get repository info
REPO_NAME=$(basename -s .git $(git config --get remote.origin.url))
USERNAME=$(git config --get remote.origin.url | sed -n 's/.*github.com[:/]\([^/]*\).*/\1/p')

print_status "Repository: $USERNAME/$REPO_NAME"

# Step 1: Build the application
print_status "Step 1: Building application with cloud profile..."
if ./mvnw clean compile -Pcloud; then
    print_success "Build completed successfully"
else
    print_error "Build failed"
    exit 1
fi

# Step 2: Deploy to GitHub Pages
print_status "Step 2: Deploying to GitHub Pages..."
if command -v gh &> /dev/null; then
    if gh pages deploy target/classes/static --repo "$USERNAME/$REPO_NAME"; then
        print_success "GitHub Pages deployment successful"
        echo "🌐 GitHub Pages URL: https://$USERNAME.github.io/$REPO_NAME/"
    else
        print_warning "GitHub Pages deployment failed (you can still deploy via GitHub Actions)"
    fi
else
    print_warning "GitHub CLI not installed. Skipping GitHub Pages deployment."
    print_status "You can deploy via GitHub Actions by pushing to main branch"
fi

# Step 3: Deploy to Vercel
print_status "Step 3: Deploying to Vercel..."
if command -v vercel &> /dev/null; then
    if vercel --prod --yes; then
        print_success "Vercel deployment successful"
        echo "🚀 Vercel URL: https://$REPO_NAME.vercel.app"
    else
        print_warning "Vercel deployment failed"
    fi
else
    print_warning "Vercel CLI not installed. Skipping Vercel deployment."
    print_status "Install with: npm i -g vercel"
fi

# Step 4: Deploy to Netlify
print_status "Step 4: Deploying to Netlify..."
if command -v netlify &> /dev/null; then
    if netlify deploy --prod --dir=target/classes/static; then
        print_success "Netlify deployment successful"
        echo "🌐 Netlify URL: https://$REPO_NAME.netlify.app"
    else
        print_warning "Netlify deployment failed"
    fi
else
    print_warning "Netlify CLI not installed. Skipping Netlify deployment."
    print_status "Install with: npm i -g netlify-cli"
fi

# Step 5: Create Codespaces (if not already in one)
if [ -z "$CODESPACE_NAME" ]; then
    print_status "Step 5: Creating GitHub Codespace..."
    if command -v gh &> /dev/null; then
        print_status "Creating Codespace for $USERNAME/$REPO_NAME..."
        gh codespace create --repo "$USERNAME/$REPO_NAME" --branch main
        print_success "Codespace creation initiated"
        echo "🌐 Codespaces URL: https://github.com/$USERNAME/$REPO_NAME/codespaces"
    else
        print_warning "GitHub CLI not installed. Skipping Codespace creation."
        print_status "You can create Codespace manually at: https://github.com/$USERNAME/$REPO_NAME/codespaces"
    fi
else
    print_success "Already running in Codespace: $CODESPACE_NAME"
    echo "🌐 Current Codespace URL: https://$CODESPACE_NAME-8080.app.github.dev"
fi

# Summary
echo ""
echo "🎉 DEPLOYMENT SUMMARY"
echo "===================="
echo "✅ Build: Completed"
echo "🌐 GitHub Pages: https://$USERNAME.github.io/$REPO_NAME/"
echo "🚀 Vercel: https://$REPO_NAME.vercel.app"
echo "🌐 Netlify: https://$REPO_NAME.netlify.app"
echo "💻 Codespaces: https://github.com/$USERNAME/$REPO_NAME/codespaces"

if [ ! -z "$CODESPACE_NAME" ]; then
    echo "🔧 Current Codespace: https://$CODESPACE_NAME-8080.app.github.dev"
fi

echo ""
echo "📋 Next steps:"
echo "1. Test all deployed URLs"
echo "2. Check GitHub Actions for automated deployments"
echo "3. Configure custom domains if needed"
echo "4. Set up monitoring and alerts"

print_success "Deployment completed! 🚀" 