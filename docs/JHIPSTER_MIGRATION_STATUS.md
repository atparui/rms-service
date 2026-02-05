# JHipster to RMS Migration Status

## Executive Summary

The RMS service is in a **partial migration state**. Both old JHipster tables and new RMS tables coexist, causing:
1. `TransientPropertyValueException` errors in user provisioning
2. Confusion about which user system to use
3. Potential data inconsistency

**Action Required**: Complete the migration by refactoring code to use only RMS tables and removing JHipster tables.

## Current Database State (rms-rms-demo)

### ❌ Old JHipster Tables (To Be Removed)
```
jhi_user              → Old user table (replaced by rms_user)
jhi_user_authority    → Old user-role join table (replaced by user_branch_role)
jhi_authority         → Old authority table (renamed to authority, but should use user_branch_role)
user_authority        → Legacy table (unclear purpose, to be removed)
authority             → Renamed from jhi_authority (review if needed)
```

### ✅ New RMS Tables (Correct)
```
rms_user             → Main user table
user_branch_role     → Branch-tied role assignments
user_sync_log        → OAuth2/Keycloak sync tracking
```

## Code Migration Status

### ❌ Still Using Old System
**File**: `UserProvisioningAuthSuccessListener.java`
- Uses `User` entity (maps to `jhi_user`)
- Uses `UserRepository`
- Triggers `TransientPropertyValueException`

**File**: `UserService.java`
- Works with `User` entity and `jhi_user` table
- Manages `Authority` entities
- Uses `jhi_user_authority` join table

**File**: `User.java`
- Entity mapped to `jhi_user` table
- Has `@ManyToMany` relationship with `Authority`
- References `jhi_user_authority` join table

**File**: `Authority.java`
- Mapped to `authority` table (renamed from `jhi_authority`)
- Used in old user-authority relationship

### ✅ Already Using New System
**File**: `RmsUserService.java`
- Works with `RmsUser` entity
- Uses `rms_user` table
- Correctly implemented

**File**: `UserBranchRoleService.java`
- Manages branch-tied roles
- Uses `user_branch_role` table
- Correctly implemented

## Migration Tasks

### 1. Refactor OAuth2 User Provisioning (High Priority)

**File**: `/src/main/java/com/atparui/rmsservice/security/UserProvisioningAuthSuccessListener.java`

**Changes Needed**:
```java
// BEFORE (Current - uses jhi_user)
@Autowired
private UserService userService;

@EventListener(AuthenticationSuccessEvent.class)
@Transactional("transactionManager")
public void onAuthSuccess(AuthenticationSuccessEvent event) {
    AdminUserDTO userDTO = userService.getUserFromAuthentication(authToken);
    // ...
}

// AFTER (Target - uses rms_user)
@Autowired
private RmsUserService rmsUserService;

@EventListener(AuthenticationSuccessEvent.class)
@Transactional("transactionManager")
public void onAuthSuccess(AuthenticationSuccessEvent event) {
    RmsUserDTO userDTO = rmsUserService.syncUserFromIdP(authToken);
    // ...
}
```

### 2. Migrate UserService Logic (High Priority)

**Options**:

**Option A** (Recommended): Merge into `RmsUserService`
- Move all OAuth2 sync logic from `UserService` to `RmsUserService`
- Update to work with `RmsUser` entity
- Delete `UserService.java` after migration

**Option B**: Refactor `UserService` to use `RmsUser`
- Change all `User` references to `RmsUser`
- Update all methods to work with `rms_user` table
- Keep the service but make it work with new entities

### 3. Create Data Migration Changeset (Medium Priority)

**File**: Create `src/main/resources/config/liquibase/changelog/20260206000001_migrate_remove_jhipster_tables.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">

    <changeSet id="20260206000001-1" author="system">
        <comment>Migrate any existing jhi_user data to rms_user</comment>
        
        <sql>
            -- Only migrate if jhi_user has data that's not in rms_user
            INSERT INTO rms_user (
                id, 
                external_user_id, 
                username, 
                email, 
                first_name, 
                last_name, 
                is_active, 
                created_at, 
                created_by
            )
            SELECT 
                gen_random_uuid(),
                id,
                login,
                email,
                first_name,
                last_name,
                activated,
                created_date,
                created_by
            FROM jhi_user
            WHERE id NOT IN (SELECT external_user_id FROM rms_user WHERE external_user_id IS NOT NULL);
        </sql>
    </changeSet>

    <changeSet id="20260206000001-2" author="system">
        <comment>Drop old JHipster tables</comment>
        
        <dropTable tableName="jhi_user_authority" cascadeConstraints="true"/>
        <dropTable tableName="jhi_user" cascadeConstraints="true"/>
        <dropTable tableName="jhi_authority" cascadeConstraints="true"/>
        <dropTable tableName="user_authority" cascadeConstraints="true"/>
        
        <!-- Review if authority table is still needed -->
        <!-- If using only user_branch_role, can drop this too -->
        <comment>Note: authority table kept for now, review if needed</comment>
    </changeSet>

</databaseChangeLog>
```

### 4. Remove Old Code (Low Priority - After Above Complete)

**Files to Delete**:
- `src/main/java/com/atparui/rmsservice/domain/User.java`
- `src/main/java/com/atparui/rmsservice/repository/UserRepository.java`
- `src/main/java/com/atparui/rmsservice/repository/UserRepositoryInternal.java`
- `src/main/java/com/atparui/rmsservice/repository/UserRepositoryInternalImpl.java`
- `src/main/java/com/atparui/rmsservice/service/UserService.java` (if logic merged to RmsUserService)

**Files to Review/Update**:
- Security utilities that may reference `User` entity
- REST controllers that return `UserDTO` or `AdminUserDTO`
- Any other services that inject `UserService`

### 5. Update Authority/Role Strategy (Design Decision Needed)

**Question**: How should roles be managed?

**Option A**: Branch-Scoped Roles Only (Recommended)
- Remove `authority` table entirely
- Use ONLY `user_branch_role` for all role assignments
- All roles are tied to specific branches
- Pros: Clean, consistent, follows RMS domain model
- Cons: Super admins also need branch assignment

**Option B**: Hybrid Approach  
- Keep `authority` for global roles (ROLE_SUPER_ADMIN, ROLE_PLATFORM_ADMIN)
- Use `user_branch_role` for branch-specific roles (ROLE_WAITER, ROLE_CHEF, ROLE_MANAGER)
- Pros: Some users need global access
- Cons: Two role systems to maintain

## Testing Plan

After completing migration:

1. **OAuth2 Login Test**
   - Login via Keycloak
   - Verify user created in `rms_user` (not `jhi_user`)
   - Verify `user_sync_log` entry created

2. **Role Assignment Test**
   - Assign roles to users via `user_branch_role`
   - Verify security checks work correctly
   - Test branch-scoped access control

3. **Database Verification**
   - Confirm `jhi_user`, `jhi_user_authority`, `jhi_authority` tables dropped
   - Confirm no code references to old tables
   - Run full application test suite

4. **API Endpoint Test**
   - Test all user-related API endpoints
   - Verify correct data returned
   - Check for any broken references

## Rollback Plan

If issues occur:
1. Revert Liquibase changeset (restore old tables)
2. Revert code changes
3. Restart services

Note: Data migration is one-way. If rolling back, may need to manually sync any new users created during migration window.

## Timeline Estimate

- **Phase 1** (Refactor OAuth2 provisioning): 4-6 hours
- **Phase 2** (Migrate UserService): 4-6 hours  
- **Phase 3** (Create migrations): 2-3 hours
- **Phase 4** (Remove old code): 2-3 hours
- **Phase 5** (Authority strategy): 3-4 hours
- **Testing**: 4-6 hours

**Total**: 2-3 days

## Next Steps

1. **Decision**: Choose authority/role management strategy (Option A or B)
2. **Implementation**: Start with Phase 1 (OAuth2 provisioning refactor)
3. **Testing**: Test each phase in dev environment before proceeding
4. **Migration**: Run Liquibase migrations on all tenant databases
5. **Cleanup**: Remove old code after successful migration

## Current Workaround

The fix applied to `UserService.java` (reloading authorities as managed entities) is a **temporary workaround** that allows the old system to function without errors. However, it does NOT complete the migration to the new RMS system.

The proper solution is to complete the migration as outlined in this document.
