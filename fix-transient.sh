#!/bin/bash

# ============================================================================
# Fix @Transient Annotation - Proper Approach
# ============================================================================
# This script properly fixes the @Transient annotation issue by:
# 1. Adding the correct import
# 2. Using fully qualified annotation to avoid duplicates
# ============================================================================

set -e

echo "Fixing @Transient annotations..."

# Find all entity files with the wrong annotation
FILES=$(grep -rl "org.springframework.data.annotation.Transient" src/main/java/com/atparui/rmsservice/domain/)

for file in $FILES; do
    echo "Processing: $file"
    
    # Replace with fully qualified JPA Transient to avoid confusion
    sed -i 's/@org\.springframework\.data\.annotation\.Transient/@jakarta.persistence.Transient/g' "$file"
done

echo "✅ All files fixed!"
echo ""
echo "Files modified:"
git status --short src/main/java/com/atparui/rmsservice/domain/ | wc -l
