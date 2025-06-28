#!/usr/bin/env node

const { spawnSync } = require('child_process');
const fs = require('fs');
const path = require('path');

// Get command line arguments
const args = process.argv.slice(2);
const isCheckOnly = args.includes('--check') || args.includes('-c');
const isBuildOnly = args.includes('--build') || args.includes('-b');

console.log('🔍 SMART ENVIRONMENT MANAGER');
console.log('============================');

// Get environment variables
const springProfiles = process.env.SPRING_PROFILES_ACTIVE || '';
const nodeEnv = process.env.NODE_ENV || 'development';
const serverApiUrl = process.env.SERVER_API_URL || '';
const serverApiUrlWs = process.env.SERVER_API_URL_WS || '';
const codespaceId = process.env.CODESPACE_ID || process.env.CODESPACE_NAME || '';

// Determine cloud URL based on CODESPACE_ID
let cloudApiUrl = 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev';
let cloudWsUrl = 'wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev';

if (codespaceId && codespaceId !== 'unknown') {
    cloudApiUrl = `https://${codespaceId}-8080.app.github.dev`;
    cloudWsUrl = `wss://${codespaceId}-8080.app.github.dev`;
}

// Display current environment information
console.log('📋 Current Environment Variables:');
console.log(`NODE_ENV: ${nodeEnv}`);
console.log(`SPRING_PROFILES_ACTIVE: ${springProfiles}`);
console.log(`CODESPACE_ID: ${codespaceId || 'not set'}`);
console.log(`SERVER_API_URL: ${serverApiUrl}`);
console.log(`SERVER_API_URL_WS: ${serverApiUrlWs}`);
console.log('');

// Determine environment type
let environmentType = 'UNKNOWN';
let isCloudEnvironment = false;

if (springProfiles.includes('cloud') || nodeEnv === 'cloud') {
    environmentType = 'CLOUD';
    isCloudEnvironment = true;
} else if (nodeEnv === 'production' && !springProfiles.includes('cloud')) {
    environmentType = 'PRODUCTION';
    isCloudEnvironment = false;
} else if (springProfiles.includes('dev') || nodeEnv === 'development') {
    environmentType = 'DEVELOPMENT';
    isCloudEnvironment = false;
}

console.log(`🎯 Detected Environment: ${environmentType}`);
console.log(`🌐 Is Cloud Environment: ${isCloudEnvironment}`);
if (isCloudEnvironment && codespaceId) {
    console.log(`🆔 Codespace ID: ${codespaceId}`);
    console.log(`🌐 Cloud URL: ${cloudApiUrl}`);
}
console.log('');

// Test webpack configuration
console.log('🧪 Testing webpack configuration:');
try {
    const webpackEnv = require('../webpack/environment.js');
    console.log('✅ webpack/environment.js loaded successfully');
    console.log(`   SERVER_API_URL: ${webpackEnv.SERVER_API_URL}`);
    console.log(`   SERVER_API_URL_WS: ${webpackEnv.SERVER_API_URL_WS}`);
} catch (error) {
    console.log('❌ Error loading webpack/environment.js:', error.message);
}
console.log('');

// Check environment files
console.log('📁 Environment files:');
const envFiles = ['env.development', 'env.cloud'];
envFiles.forEach(file => {
    if (fs.existsSync(path.join(__dirname, '..', file))) {
        console.log(`✅ ${file} exists`);
    } else {
        console.log(`❌ ${file} not found`);
    }
});
console.log('');

// If check-only mode, exit here
if (isCheckOnly) {
    console.log('📊 ENVIRONMENT SUMMARY:');
    if (isCloudEnvironment) {
        console.log('🎯 Environment: CLOUD');
        console.log('🌐 API will call: ' + (serverApiUrl || cloudApiUrl));
        console.log('🔌 WebSocket will connect to: ' + (serverApiUrlWs || cloudWsUrl));
        if (codespaceId) {
            console.log(`🆔 Using Codespace ID: ${codespaceId}`);
        }
        console.log('📝 Suitable for: GitHub Codespaces, Cloud Development');
    } else if (environmentType === 'PRODUCTION') {
        console.log('🎯 Environment: PRODUCTION');
        console.log('🌐 API will call: ' + (serverApiUrl || 'Production URL'));
        console.log('🔌 WebSocket will connect to: ' + (serverApiUrlWs || 'Production WebSocket URL'));
        console.log('📝 Suitable for: Production Deployment');
    } else {
        console.log('🎯 Environment: DEVELOPMENT');
        console.log('🌐 API will call: ' + (serverApiUrl || 'http://localhost:8080'));
        console.log('🔌 WebSocket will connect to: ' + (serverApiUrlWs || 'ws://localhost:8080'));
        console.log('📝 Suitable for: Local Development');
    }
    
    console.log('');
    console.log('🔧 Usage:');
    console.log('   node scripts/smart-env.js --check    # Check environment only');
    console.log('   node scripts/smart-env.js --build    # Build only');
    console.log('   node scripts/smart-env.js            # Check and build');
    console.log('');
    console.log('🆔 To set Codespace ID:');
    console.log('   export CODESPACE_ID=super-broccoli-pj96jxxr4p7q3945r');
    console.log('   ./update-codespace-env.sh super-broccoli-pj96jxxr4p7q3945r');
    process.exit(0);
}

// Build functionality
if (isBuildOnly || (!isCheckOnly && !isBuildOnly)) {
    console.log('🔨 Building frontend...');
    
    if (isCloudEnvironment) {
        console.log('🌐 Building for CLOUD environment');
        console.log('   - Using webpack.cloud.js');
        console.log('   - Setting NODE_ENV=cloud');
        console.log('   - Using cloud URLs');
        
        if (codespaceId) {
            console.log(`   - Using Codespace ID: ${codespaceId}`);
            console.log(`   - Cloud URL: ${cloudApiUrl}`);
        }
        
        // Set environment variables for cloud build
        process.env.NODE_ENV = 'cloud';
        process.env.SERVER_API_URL = serverApiUrl || cloudApiUrl;
        process.env.SERVER_API_URL_WS = serverApiUrlWs || cloudWsUrl;
        
        // Run cloud build
        const result = spawnSync('npm', ['run', 'webapp:build:cloud'], {
            stdio: 'inherit',
            env: process.env,
            cwd: path.join(__dirname, '..')
        });
        
        if (result.status === 0) {
            console.log('✅ Cloud build completed successfully!');
            if (codespaceId) {
                console.log(`🌐 Application will be available at: ${cloudApiUrl}`);
            }
        } else {
            console.log('❌ Cloud build failed!');
        }
        
        process.exit(result.status);
    } else if (environmentType === 'PRODUCTION') {
        console.log('🚀 Building for PRODUCTION environment');
        console.log('   - Using webpack.prod.js');
        console.log('   - Setting NODE_ENV=production');
        console.log('   - Using production URLs');
        
        // Set environment variables for production build
        process.env.NODE_ENV = 'production';
        
        // Run production build
        const result = spawnSync('npm', ['run', 'webapp:build:prod'], {
            stdio: 'inherit',
            env: process.env,
            cwd: path.join(__dirname, '..')
        });
        
        if (result.status === 0) {
            console.log('✅ Production build completed successfully!');
        } else {
            console.log('❌ Production build failed!');
        }
        
        process.exit(result.status);
    } else {
        console.log('🏠 Building for DEVELOPMENT environment');
        console.log('   - Using webpack.dev.js');
        console.log('   - Setting NODE_ENV=development');
        console.log('   - Using localhost URLs');
        
        // Set environment variables for dev build
        process.env.NODE_ENV = 'development';
        
        // Run dev build
        const result = spawnSync('npm', ['run', 'webapp:build:dev'], {
            stdio: 'inherit',
            env: process.env,
            cwd: path.join(__dirname, '..')
        });
        
        if (result.status === 0) {
            console.log('✅ Development build completed successfully!');
        } else {
            console.log('❌ Development build failed!');
        }
        
        process.exit(result.status);
    }
} 