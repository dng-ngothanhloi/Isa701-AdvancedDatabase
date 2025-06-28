#!/bin/bash

# Test Archival Operations Script
# This script tests archival operations step by step

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}==========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}==========================================${NC}"
}

# Function to check if application is compiled
check_compilation() {
    if [ ! -f "target/warehouse-mgmt-*.jar" ]; then
        print_error "Application JAR file not found. Please compile the application first:"
        echo "  ./mvnw clean package -DskipTests"
        exit 1
    fi
}

# Function to get JAR file path
get_jar_path() {
    JAR_FILE=$(find target -name "warehouse-mgmt-*.jar" | head -1)
    if [ -z "$JAR_FILE" ]; then
        print_error "No JAR file found in target directory"
        exit 1
    fi
    echo "$JAR_FILE"
}

# Function to wait for user confirmation
wait_for_confirmation() {
    echo
    read -p "Press Enter to continue to the next step..."
    echo
}

print_header "TESTING ARCHIVAL OPERATIONS"

print_status "Step 1: Checking application compilation..."
check_compilation
JAR_FILE=$(get_jar_path)
print_status "✓ Application compiled successfully"
print_status "JAR file: $JAR_FILE"

wait_for_confirmation

print_status "Step 2: Showing initial archival statistics..."
java -jar "$JAR_FILE" --archive-stats

wait_for_confirmation

print_status "Step 3: Configuring archived collections compression..."
java -jar "$JAR_FILE" --archive-config
print_status "✓ Compression configured successfully"

wait_for_confirmation

print_status "Step 4: Running data archival process..."
print_warning "This will archive data older than 3 years"
echo "Note: Since this is a test environment, there might not be any data to archive"
java -jar "$JAR_FILE" --archive

wait_for_confirmation

print_status "Step 5: Showing final archival statistics..."
java -jar "$JAR_FILE" --archive-stats

print_header "ARCHIVAL OPERATIONS TEST COMPLETED"

print_status "All archival operations tested successfully!"
print_status "You can now use the following commands:"
echo
echo "  ./run-archival.sh archive     - Run archival process"
echo "  ./run-archival.sh stats       - Show archival statistics"
echo "  ./run-archival.sh config      - Configure compression"
echo "  ./run-archival.sh restore <id> <type> - Restore archived document"
echo
print_status "For production use, consider:"
echo "  - Setting up scheduled archival (already configured in DataArchivalService)"
echo "  - Monitoring archival performance"
echo "  - Regular backup of archived collections" 