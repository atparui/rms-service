# Liquibase Migration Fixes

## Summary of Issues Fixed

This document describes three related Liquibase issues that were fixed to enable stable database migrations for the RMS service.

## Issue 1: Missing Table (restaurant_role)

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

## Issue 2: Role Value Mismatch

After fixing Issue 1, a new error appeared:

```
ERROR: insert or update on table "user_branch_role" violates foreign key constraint "fk_user_branch_role__role_name"
Detail: Key (role)=(ROLE_MANAGER) is not present in table "restaurant_role".
```

### Root Cause

Data mismatch between tables:
- **Existing `user_branch_role` data**: `ROLE_MANAGER`, `ROLE_CHEF`, etc. (with `ROLE_` prefix)
- **New `restaurant_role` table**: `MANAGER`, `CHEF`, etc. (without `ROLE_` prefix)

When the FK constraint was added, it validated existing data and found mismatches.

### Solution

Created a migration changeset (`20260205115000_migrate_user_branch_role_values.xml`) to:

1. Update existing `user_branch_role` data to remove `ROLE_` prefix:
   ```sql
   UPDATE user_branch_role SET role = 'MANAGER' WHERE role = 'ROLE_MANAGER';
   UPDATE user_branch_role SET role = 'CHEF' WHERE role = 'ROLE_CHEF';
   -- etc.
   ```

2. Add this changeset to `master.xml` **after** `restaurant_role` creation but **before** FK constraints are added

## Issue 3: Missing Table Checks (app_navigation_menu_item)

After fixing Issue 2, another error appeared:

```
ERROR: relation "public.app_navigation_menu_item" does not exist
```

### Root Cause

The cascading rules changeset (`20260205130000_add_cascading_rules_to_all_foreign_keys.xml`) was trying to modify FK constraints on optional tables that may not exist in all databases.

### Solution

Added `preConditions` to changesets 25 and 26 to skip if tables don't exist:

```xml
<changeSet id="20260205130000-25" author="rms-team">
    <preConditions onFail="MARK_RAN">
        <tableExists tableName="app_navigation_menu_item"/>
    </preConditions>
    <comment>Add cascading rules to AppNavigationMenuItem FK (if table exists)</comment>
    ...
</changeSet>
```

## Date Fixed

February 5, 2026

## Related Files

- `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/master.xml`
- `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/changelog/20260205120000_added_entity_RestaurantRole.xml`
- `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/changelog/20260205115000_migrate_user_branch_role_values.xml`
- `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/changelog/20251228051049_added_entity_constraints_UserBranchRole.xml`
- `/home/sivakumar/Shiva/Workspace/rms-service/src/main/resources/config/liquibase/changelog/20260205130000_add_cascading_rules_to_all_foreign_keys.xml`

## Final Changeset Order in master.xml

```xml
<!-- 1. Create restaurant_role table first -->
<include file="config/liquibase/changelog/20260205120000_added_entity_RestaurantRole.xml" relativeToChangelogFile="false"/>

<!-- 2. Migrate existing data to match new format -->
<include file="config/liquibase/changelog/20260205115000_migrate_user_branch_role_values.xml" relativeToChangelogFile="false"/>

<!-- 3. Add FK constraints (now data is compatible) -->
<include file="config/liquibase/changelog/20251228051047_added_entity_constraints_Branch.xml" relativeToChangelogFile="false"/>
<include file="config/liquibase/changelog/20251228051049_added_entity_constraints_UserBranchRole.xml" relativeToChangelogFile="false"/>
...
```
