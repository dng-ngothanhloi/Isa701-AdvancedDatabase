#!/bin/bash

# Debug Classpath Script
# This script debugs classpath issues and checks for missing dependencies

echo "🔍 DEBUGGING CLASSPATH ISSUES"
echo "============================="

# Check if logstash classes are available
echo "📋 Checking for logstash dependencies..."

# Check in Maven dependencies
echo "1️⃣ Checking Maven dependencies:"
if ./mvnw dependency:tree | grep -i logstash; then
    echo "✅ Logstash dependencies found in Maven"
else
    echo "❌ No logstash dependencies found in Maven"
fi

echo ""

# Check in compiled classes
echo "2️⃣ Checking compiled classes:"
if find target/classes -name "*.class" | grep -i logstash; then
    echo "✅ Logstash classes found in compiled output"
else
    echo "❌ No logstash classes found in compiled output"
fi

echo ""

# Check if the problematic class exists
echo "3️⃣ Checking for JsonProviders class:"
if find target -name "*.jar" -exec jar -tf {} \; 2>/dev/null | grep -i "JsonProviders"; then
    echo "✅ JsonProviders class found in JAR files"
else
    echo "❌ JsonProviders class NOT found in JAR files"
fi

echo ""

# Check JHipster logging configuration
echo "4️⃣ Checking JHipster logging configuration:"
if find target/classes -name "*.yml" -exec grep -l "logstash" {} \; 2>/dev/null; then
    echo "✅ Logstash configuration found in YAML files"
else
    echo "❌ No logstash configuration found in YAML files"
fi

echo ""

# Test class loading
echo "5️⃣ Testing class loading:"
java -cp "$(find target -name '*.jar' | tr '\n' ':')" -e "
try {
    Class.forName('net.logstash.logback.composite.JsonProviders');
    System.out.println('✅ JsonProviders class can be loaded');
} catch (ClassNotFoundException e) {
    System.out.println('❌ JsonProviders class cannot be loaded: ' + e.getMessage());
}
" 2>/dev/null || echo "❌ Failed to test class loading"

echo ""

# Check application properties
echo "6️⃣ Checking application properties:"
echo "Cloud profile logstash enabled:"
grep -A 5 -B 5 "logstash" src/main/resources/config/application-cloud.yml || echo "No logstash config found"

echo ""

# Provide solutions
echo "🔧 SOLUTIONS:"
echo "============="
echo "1. Disable logstash in cloud profile (already done)"
echo "2. Add @ConditionalOnProperty to LoggingConfiguration (already done)"
echo "3. Create CloudLoggingConfiguration (already done)"
echo "4. Disable JSON logging in cloud (already done)"
echo ""
echo "🚀 Try running: ./test-cloud-startup.sh" 