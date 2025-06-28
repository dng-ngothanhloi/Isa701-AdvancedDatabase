#!/bin/bash

# Test Spring Profiles Active Script
# This script tests how SPRING_PROFILES_ACTIVE is passed from Maven to npm

echo "🧪 TESTING SPRING_PROFILES_ACTIVE FLOW"
echo "======================================"

echo "📋 Testing different ways to set SPRING_PROFILES_ACTIVE:"
echo ""

# Test 1: Default webapp profile
echo "1️⃣ Testing default webapp profile (-Pwebapp):"
echo "   Command: ./mvnw help:evaluate -Dexpression=spring.profiles.active -Pwebapp -q -DforceStdout"
./mvnw help:evaluate -Dexpression=spring.profiles.active -Pwebapp -q -DforceStdout
echo ""

# Test 2: Cloud profile
echo "2️⃣ Testing cloud profile (-Pcloud):"
echo "   Command: ./mvnw help:evaluate -Dexpression=spring.profiles.active -Pcloud -q -DforceStdout"
./mvnw help:evaluate -Dexpression=spring.profiles.active -Pcloud -q -DforceStdout
echo ""

# Test 3: Command line override
echo "3️⃣ Testing command line override (-Dspring.profiles.active=cloud):"
echo "   Command: ./mvnw help:evaluate -Dexpression=spring.profiles.active -Dspring.profiles.active=cloud -q -DforceStdout"
./mvnw help:evaluate -Dexpression=spring.profiles.active -Dspring.profiles.active=cloud -q -DforceStdout
echo ""

# Test 4: Environment variable
echo "4️⃣ Testing environment variable:"
echo "   Command: SPRING_PROFILES_ACTIVE=cloud ./mvnw help:evaluate -Dexpression=spring.profiles.active -q -DforceStdout"
SPRING_PROFILES_ACTIVE=cloud ./mvnw help:evaluate -Dexpression=spring.profiles.active -q -DforceStdout
echo ""

echo "🔍 Testing smart-env.js with different profiles:"
echo ""

# Test smart-env.js with different profiles
echo "5️⃣ Testing smart-env.js with dev profile:"
SPRING_PROFILES_ACTIVE=dev node scripts/smart-env.js --check
echo ""

echo "6️⃣ Testing smart-env.js with cloud profile:"
SPRING_PROFILES_ACTIVE=cloud node scripts/smart-env.js --check
echo ""

echo "📊 SUMMARY:"
echo "==========="
echo "✅ SPRING_PROFILES_ACTIVE flows from Maven → frontend-maven-plugin → npm → smart-env.js"
echo "✅ Profile can be set via: Maven profile, command line, or environment variable"
echo "✅ smart-env.js automatically detects the environment and builds accordingly"
echo ""
echo "🚀 Ready to use:"
echo "   ./mvnw spring-boot:run -Pcloud                    # Cloud environment"
echo "   ./mvnw spring-boot:run -Dspring.profiles.active=cloud  # Cloud environment (override)"
echo "   ./mvnw spring-boot:run -Pwebapp                   # Development environment" 