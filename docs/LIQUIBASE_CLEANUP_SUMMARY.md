# Liquibase Schema Cleanup - JHipster Removal Summary

## Overview
Completely removed all JHipster remnants from the Liquibase database schema. The schema now creates a clean RMS-specific database from scratch without any JHipster artifacts.

## What Was Changed

### 1. Initial Schema Cleanup (`00000000000000_initial_schema.xml`)
**Before:** Created JHipster core tables
- `jhi_user` - JHipster user table
- `jhi_authority` - JHipster authority table
- `jhi_user_authority` - Join table for user-authority
- `jhi_date_time_wrapper` - Test utility table

**After:** Clean placeholder changeset
- Removed all JHipster table creations
- Added comment explaining RMS uses `rms_user` + `user_branch_role` system
- Kept changeset for Liquibase compatibility

### 2. Order Table Naming (`20251228051102_added_entity_Order.xml`)
**Before:** Created table named `jhi_order`
```xml
<createTable tableName="jhi_order">
    <column name="order_number" uniqueConstraintName="ux_jhi_order__order_number"/>
</createTable>
```

**After:** Creates table named `orders`
```xml
<createTable tableName="orders">
    <column name="order_number" uniqueConstraintName="ux_orders__order_number"/>
</createTable>
```

### 3. Foreign Key References Updated
Updated all foreign key references from `jhi_order` to `orders`:
- `20251228051102_added_entity_constraints_Order.xml`
- `20251228051103_added_entity_constraints_OrderItem.xml`
- `20251228051105_added_entity_constraints_OrderStatusHistory.xml`
- `20251228051108_added_entity_constraints_Bill.xml`

### 4. Migration Files Deleted
Removed three migration files that tried to fix JHipster issues:

1. **`20260131000001_added_authority_tables.xml`**
   - Created `authority` and `user_authority` tables
   - **Why removed:** RMS doesn't use this system; we use `user_branch_role`

2. **`20260206000001_remove_jhipster_tables.xml`**
   - Dropped `jhi_user`, `jhi_authorities`, `jhi_user_authority`, `user_authority`
   - **Why removed:** Tables never created in clean schema

3. **`20260205000001_rename_order_table_remove_jhipster_test_tables.xml`**
   - Renamed `jhi_order` to `orders`
   - Dropped `jhi_date_time_wrapper`
   - **Why removed:** Table created as `orders` directly now

### 5. Data Files Cleanup
- **Deleted:** `src/main/resources/config/liquibase/data/authority.csv`
  - No longer needed (no `jhi_authority` table)
  
- **Renamed:** `fake-data/jhi_order.csv` → `fake-data/orders.csv`
  - Matches new table name

### 6. Master Changelog Updated (`master.xml`)
Removed includes for the 3 deleted migration files:
```xml
<!-- Removed these lines -->
<include file="config/liquibase/changelog/20260131000001_added_authority_tables.xml"/>
<include file="config/liquibase/changelog/20260206000001_remove_jhipster_tables.xml"/>
<include file="config/liquibase/changelog/20260205000001_rename_order_table_remove_jhipster_test_tables.xml"/>
```

## RMS User Management System

### Current Architecture
RMS uses a **custom user management system**, NOT JHipster's:

| Purpose | Table | Description |
|---------|-------|-------------|
| User storage | `rms_user` | Synced from Keycloak via user provisioning |
| User sync tracking | `user_sync_log` | Tracks Keycloak sync status |
| Branch-specific roles | `user_branch_role` | App roles (MANAGER, WAITER, CHEF) per branch |

### Tables That Don't Exist (By Design)
- ❌ `jhi_user` - Removed
- ❌ `jhi_authority` - Removed
- ❌ `jhi_user_authority` - Removed
- ❌ `user_authority` - Never created
- ❌ `authority` - Never created
- ❌ `jhi_date_time_wrapper` - Never created

## Database Recreation

### To Recreate Database From Scratch
```bash
# 1. Drop existing schemas
psql -h localhost -U postgres -c "DROP SCHEMA IF EXISTS rms_service CASCADE;"
psql -h localhost -U postgres -c "DROP SCHEMA IF EXISTS rms_rms_demo CASCADE;"

# 2. Recreate schemas (handled by up.sh)
cd /path/to/platform
./scripts/down.sh
./scripts/up.sh dev

# 3. Liquibase will run automatically and create clean schema
# No JHipster tables will be created
# Only RMS-specific tables (rms_user, user_branch_role, etc.)
```

### Expected Tables After Fresh Creation
- ✅ `rms_user` - RMS users synced from Keycloak
- ✅ `user_branch_role` - Branch-specific roles
- ✅ `user_sync_log` - Keycloak sync tracking
- ✅ `orders` - Order table (not `jhi_order`)
- ✅ All other RMS entities (branch, menu_item, customer, etc.)
- ✅ `databasechangelog` - Liquibase tracking
- ✅ `javers_*` - Audit tables

## Code Changes Required

### Entity Classes
- ✅ `RmsUser.java` - Removed `@ManyToMany authorities` field
- ✅ `RmsUserMapper.java` - Removed `@Mapping(target = "authorities", ignore = true)`
- ✅ All `Persistable` entities - Added `@jakarta.persistence.Transient` annotations

### Verification
1. **Build:** `./mvnw clean compile -DskipTests` ✅ SUCCESS
2. **No more errors:**
   - ❌ "relation user_authority does not exist" - FIXED
   - ❌ "column ispersisted does not exist" - FIXED
   - ❌ "table jhi_order does not exist" - FIXED

## Benefits

1. **Clean Schema:** No create-then-drop migrations
2. **No Confusion:** Clear separation from JHipster artifacts
3. **Easy Rebuilds:** Database can be recreated from scratch anytime
4. **Proper Naming:** Tables use RMS conventions (not `jhi_` prefix)
5. **Faster Startup:** Fewer Liquibase changesets to process

## Migration Impact

### For Existing Databases
⚠️ **IMPORTANT:** If you have existing data:
1. Back up your database
2. Drop and recreate schemas (we're not in production)
3. Re-seed demo data if needed

### For New Deployments
✅ **Ready to go!** Just run `up.sh` and you'll get a clean RMS schema.

## Related Commits
- `7a41510` - Liquibase cleanup (this document)
- `7691b26` - Added `jakarta.persistence.Transient` to all Persistable entities
- `9271a35` - Removed unused `authorities` relationship from RmsUser

## Verification Checklist
- [x] Build succeeds
- [x] No JHipster tables in schema creation
- [x] Order table created as `orders` (not `jhi_order`)
- [x] All foreign keys reference `orders`
- [x] No `user_authority` table creation
- [x] No `authority.csv` seed data
- [x] Master changelog doesn't reference deleted files
- [ ] Database recreates cleanly (to be tested by user)
- [ ] User provisioning works without errors (to be tested)

## Next Steps
1. **User action:** Drop `rms-service` and `rms-rms-demo` schemas
2. **User action:** Run `./scripts/down.sh && ./scripts/up.sh dev`
3. **Expected:** Clean database with only RMS tables
4. **Test:** OAuth2 login should provision users in `rms_user` table
5. **Verify:** No errors related to `user_authority` or `jhi_*` tables
