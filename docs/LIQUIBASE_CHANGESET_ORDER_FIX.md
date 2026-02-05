# Liquibase Changeset Order Fix

## Issue

During platform startup with `./scripts/up.sh dev`, the Liquibase migration for the `rms-rms-demo` tenant database was failing with this error:

```
ERROR: Exception Primary Reason: ERROR: relation "public.restaurant_role" does not exist
ERROR: Exception Primary Source: PostgreSQL 17.2 (Debian 17.2-1.pgdg120+1)

Unexpected error running Liquibase: Migration failed for changeset 
config/liquibase/changelog/20251228051049_added_entity_constraints_UserBranchRole.xml::20251228051049-2::rms-team:
     Reason: liquibase.exception.DatabaseException: ERROR: relation "public.restaurant_role" does not exist 
     [Failed SQL: (0) ALTER TABLE public.user_branch_role ADD CONSTRAINT fk_user_branch_role__role_name 
     FOREIGN KEY (role) REFERENCES public.restaurant_role (name) ON UPDATE CASCADE ON DELETE RESTRICT]
```

## Root Cause

The issue was caused by **incorrect changeset order** in the `master.xml` file:

1. **Line 47**: `20251228051049_added_entity_constraints_UserBranchRole.xml` - Tries to add FK constraint to `restaurant_role` table
2. **Line 76**: `20260205120000_added_entity_RestaurantRole.xml` - Creates the `restaurant_role` table

The foreign key constraint was being applied **before** the referenced table existed.

## Solution

Moved the `restaurant_role` table creation changeset to **before** the constraints section in `master.xml`:

### Before (Incorrect Order)
```xml
<include file="config/liquibase/changelog/20260123093000_seed_app_menu_permissions.xml" relativeToChangelogFile="false"/>
<!-- jhipster-needle-liquibase-add-changelog - JHipster will add liquibase changelogs here -->
<include file="config/liquibase/changelog/20251228051047_added_entity_constraints_Branch.xml" relativeToChangelogFile="false"/>
...
<include file="config/liquibase/changelog/20251228051049_added_entity_constraints_UserBranchRole.xml" relativeToChangelogFile="false"/>
...
<!-- Later in file (line 76) -->
<include file="config/liquibase/changelog/20260205120000_added_entity_RestaurantRole.xml" relativeToChangelogFile="false"/>
```

### After (Correct Order)
```xml
<include file="config/liquibase/changelog/20260123093000_seed_app_menu_permissions.xml" relativeToChangelogFile="false"/>

<!-- RESTAURANT ROLE: Define valid roles for restaurant staff (MUST be before constraints) -->
<include file="config/liquibase/changelog/20260205120000_added_entity_RestaurantRole.xml" relativeToChangelogFile="false"/>

<!-- jhipster-needle-liquibase-add-changelog - JHipster will add liquibase changelogs here -->
<include file="config/liquibase/changelog/20251228051047_added_entity_constraints_Branch.xml" relativeToChangelogFile="false"/>
...
<include file="config/liquibase/changelog/20251228051049_added_entity_constraints_UserBranchRole.xml" relativeToChangelogFile="false"/>
```

## Changes Made

1. **File Modified**: `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/master.xml`
   - Moved `20260205120000_added_entity_RestaurantRole.xml` include to line 46 (before constraints)
   - Removed duplicate include that was at line 76

2. **Docker Image Rebuilt**:
   ```bash
   cd /home/sivakumar/Shiva/Workspace/rms-service
   ./mvnw clean package -DskipTests -Pprod jib:dockerBuild
   docker push shivain22/rms-service:latest
   ```

## What the RestaurantRole Table Contains

The `restaurant_role` table defines valid roles for restaurant staff:
- MANAGER
- ASSISTANT_MANAGER
- CHEF
- SOUS_CHEF
- LINE_COOK
- WAITER
- HOST
- CASHIER
- BARTENDER
- BUSSER
- DELIVERY_DRIVER
- DISHWASHER

This table is referenced by the `user_branch_role` table via a foreign key constraint, ensuring that only valid roles can be assigned to users.

## Verification

After rebuilding and restarting the platform, the Liquibase migration should complete successfully:
- The `restaurant_role` table is created first
- Then the foreign key constraint in `user_branch_role` is added successfully

## Date Fixed

February 5, 2026

## Related Files

- `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/master.xml`
- `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/changelog/20260205120000_added_entity_RestaurantRole.xml`
- `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/changelog/20251228051049_added_entity_constraints_UserBranchRole.xml`
