#!/bin/bash

# Safe Archival Testing Script
# This script tests archival operations safely without actually archiving data

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
        echo "  ./mvnw clean package -DskipTests -Dmodernizer.skip=true"
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

print_header "SAFE ARCHIVAL OPERATIONS TESTING"

print_status "Step 1: Checking application compilation..."
check_compilation
JAR_FILE=$(get_jar_path)
print_status "✓ Application compiled successfully"
print_status "JAR file: $JAR_FILE"

echo
print_status "Step 2: Testing MigrationRunner help command..."
echo "Running: java -jar $JAR_FILE --help"
echo

java -jar "$JAR_FILE" --help

echo
print_status "Step 3: Testing archival statistics (safe operation)..."
echo "Running: java -jar $JAR_FILE --archive-stats"
echo

java -jar "$JAR_FILE" --archive-stats

echo
print_status "Step 4: Testing compression configuration (safe operation)..."
echo "Running: java -jar $JAR_FILE --archive-config"
echo

java -jar "$JAR_FILE" --archive-config

print_header "SAFE TESTING COMPLETED"

print_status "✓ All safe operations completed successfully!"
echo
print_status "Available archival operations:"
echo "  ./run-archival.sh stats       - Show archival statistics"
echo "  ./run-archival.sh config      - Configure compression"
echo "  ./run-archival.sh archive     - Run archival process (WARNING: archives old data)"
echo "  ./run-archival.sh restore <id> <type> - Restore archived document"
echo
print_warning "IMPORTANT NOTES:"
echo "  - Archival threshold is set to 3 years (data older than 3 years)"
echo "  - Archival process moves data from main collections to archived collections"
echo "  - Archived data is compressed for storage optimization"
echo "  - Restore operation is available for testing/debugging"
echo
print_status "For production use:"
echo "  - Scheduled archival runs automatically at 2:00 AM on 1st day of each month"
echo "  - Monitor archival performance regularly"
echo "  - Backup archived collections separately" 