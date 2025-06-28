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

// Get Spring profiles from environment variable
const springProfiles = process.env.SPRING_PROFILES_ACTIVE || '';
const nodeEnv = process.env.NODE_ENV || 'development';
const serverApiUrl = process.env.SERVER_API_URL || '';
const serverApiUrlWs = process.env.SERVER_API_URL_WS || '';

// Display current environment information
console.log('📋 Current Environment Variables:');
console.log(`NODE_ENV: ${nodeEnv}`);
console.log(`SPRING_PROFILES_ACTIVE: ${springProfiles}`);
console.log(`SERVER_API_URL: ${serverApiUrl}`);
console.log(`SERVER_API_URL_WS: ${serverApiUrlWs}`);
console.log('');

// Determine environment type
let environmentType = 'UNKNOWN';
let isCloudEnvironment = false;

if (springProfiles.includes('cloud') || nodeEnv === 'production') {
    environmentType = 'CLOUD';
    isCloudEnvironment = true;
} else if (springProfiles.includes('dev') || nodeEnv === 'development') {
    environmentType = 'DEVELOPMENT';
    isCloudEnvironment = false;
}

console.log(`🎯 Detected Environment: ${environmentType}`);
console.log(`🌐 Is Cloud Environment: ${isCloudEnvironment}`);
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
        console.log('🌐 API will call: ' + (serverApiUrl || 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev'));
        console.log('🔌 WebSocket will connect to: ' + (serverApiUrlWs || 'wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev'));
        console.log('📝 Suitable for: GitHub Codespaces, AWS, Azure, GCP');
    } else {
        console.log('🎯 Environment: DEVELOPMENT');
        console.log('🌐 API will call: ' + (serverApiUrl || 'http://localhost:8080'));
        console.log('🔌 WebSocket will connect to: ' + (serverApiUrlWs || 'ws://localhost:8080'));
        console.log('📝 Suitable for: Local development');
    }
    
    console.log('');
    console.log('🔧 Usage:');
    console.log('   node scripts/smart-env.js --check    # Check environment only');
    console.log('   node scripts/smart-env.js --build    # Build only');
    console.log('   node scripts/smart-env.js            # Check and build');
    process.exit(0);
}

// Build functionality
if (isBuildOnly || (!isCheckOnly && !isBuildOnly)) {
    console.log('🔨 Building frontend...');
    
    if (isCloudEnvironment) {
        console.log('🌐 Building for CLOUD environment');
        console.log('   - Using webpack.cloud.js');
        console.log('   - Setting NODE_ENV=production');
        console.log('   - Using cloud URLs');
        
        // Set environment variables for cloud build
        process.env.NODE_ENV = 'production';
        process.env.SERVER_API_URL = 'https://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev';
        process.env.SERVER_API_URL_WS = 'wss://super-broccoli-pj96jxxr4p7q3945r-8080.app.github.dev';
        
        // Run cloud build
        const result = spawnSync('npm', ['run', 'webapp:build:cloud'], {
            stdio: 'inherit',
            env: process.env,
            cwd: path.join(__dirname, '..')
        });
        
        if (result.status === 0) {
            console.log('✅ Cloud build completed successfully!');
        } else {
            console.log('❌ Cloud build failed!');
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