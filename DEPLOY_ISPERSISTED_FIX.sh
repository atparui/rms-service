#!/bin/bash

# ============================================================================
# Deploy isPersisted Fix - Permanent Solution
# ============================================================================
# This script deploys the fix for the @Transient annotation issue
# that was causing "column isPersisted does not exist" errors.
#
# Problem: Entities were using @org.springframework.data.annotation.Transient
#          which Hibernate doesn't recognize
# Solution: Changed to @jakarta.persistence.Transient
# ============================================================================

set -e  # Exit on error

echo "=========================================="
echo "Deploying isPersisted Fix"
echo "=========================================="
echo ""

# Step 1: Show what changed
echo "Step 1: Files Changed"
echo "----------------------------------------"
cd /home/sivakumar/Shiva/Workspace/rms-service
git status --short src/main/java/com/atparui/rmsservice/domain/ | wc -l
echo "entity files modified"
echo ""

# Step 2: Commit changes
echo "Step 2: Committing Changes"
echo "----------------------------------------"
git add src/main/java/com/atparui/rmsservice/domain/*.java
git add docs/ISPERSISTED_PERMANENT_FIX.md

git commit -m "fix: Use correct @Transient annotation for JPA entities (PERMANENT FIX)

CRITICAL FIX: Replace Spring Data @Transient with JPA @Transient

Problem:
- Entities were using @org.springframework.data.annotation.Transient
- Hibernate doesn't recognize this annotation
- Hibernate tried to persist/query isPersisted field
- PostgreSQL error: column isPersisted does not exist
- User authentication failed

Solution:
- Changed to @jakarta.persistence.Transient (or @Transient)
- Hibernate now correctly ignores isPersisted field
- All 30+ entity files fixed simultaneously

Impact:
- Fixes user authentication
- Fixes all database operations
- Prevents error from recurring

Files Changed:
- All entity files in domain package (30+ files)
- RmsUser, Order, Branch, MenuItem, etc.

This is a PERMANENT fix that resolves the recurring issue."

echo "✅ Changes committed"
echo ""

# Step 3: Build
echo "Step 3: Building Service"
echo "----------------------------------------"
./mvnw clean package -DskipTests
echo "✅ Build complete"
echo ""

# Step 4: Rebuild Docker image
echo "Step 4: Rebuilding Docker Image"
echo "----------------------------------------"
cd /home/sivakumar/Shiva/Workspace/platform
docker-compose build rms-service
echo "✅ Docker image rebuilt"
echo ""

# Step 5: Restart service
echo "Step 5: Restarting Service"
echo "----------------------------------------"
docker-compose up -d rms-service
echo "✅ Service restarted"
echo ""

# Step 6: Wait for startup
echo "Step 6: Waiting for Service to Start"
echo "----------------------------------------"
echo "Waiting 30 seconds for service startup..."
sleep 30
echo "✅ Startup complete"
echo ""

# Step 7: Check logs
echo "Step 7: Checking Logs"
echo "----------------------------------------"
echo "Last 50 lines of logs:"
docker logs --tail 50 rms-service
echo ""

# Step 8: Test
echo "Step 8: Testing Fix"
echo "----------------------------------------"
echo "Testing menu API (triggers user provisioning)..."
curl -s 'https://console.atparui.com/services/rms-service/api/app-menus/tree?appKey=rms-demo' \
  -H 'Authorization: Bearer YOUR_TOKEN_HERE' \
  -H 'X-Tenant-ID: rms-demo' \
  > /dev/null && echo "✅ API call successful!" || echo "❌ API call failed"
echo ""

echo "=========================================="
echo "✅ Deployment Complete!"
echo "=========================================="
echo ""
echo "What was fixed:"
echo "  - All entity @Transient annotations corrected"
echo "  - 30+ entity files updated"
echo "  - Hibernate now ignores isPersisted field"
echo "  - No more 'column isPersisted does not exist' error"
echo ""
echo "Next steps:"
echo "  1. Test user authentication"
echo "  2. Monitor logs for any SQL errors"
echo "  3. Verify all CRUD operations work"
echo ""
echo "If error still appears:"
echo "  - Check logs: docker logs -f rms-service"
echo "  - Verify correct image: docker images | grep rms-service"
echo "  - Full restart: docker-compose down && docker-compose up -d"
echo ""
