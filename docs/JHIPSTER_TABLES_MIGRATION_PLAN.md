# JHipster Tables Migration Plan

## Current Status

The RMS service currently has **TWO parallel user management systems**:

### 1. Old JHipster System (Legacy - Still Active)
- **Table**: `jhi_user` → **Entity**: `User` → **Repository**: `UserRepository` → **Service**: `UserService`
- **Table**: `jhi_user_authority` (join table)
- **Table**: `authority` → **Entity**: `Authority` (already renamed from `jhi_authority`)
- **Used By**: OAuth2 user provisioning (`UserProvisioningAuthSuccessListener`)

### 2. New RMS System (Target - Partially Implemented)
- **Table**: `rms_user` → **Entity**: `RmsUser` → **Repository**: `RmsUserRepository` → **Service**: `RmsUserService`
- **Table**: `user_branch_role` → **Entity**: `UserBranchRole` (branch-tied roles)
- **Table**: `user_sync_log` → **Entity**: `UserSyncLog` (sync tracking)

##  Migration Requirements

Following the established pattern with `rms_user` and `user_branch_role`, we need to:

1. **Remove/Rename** `jhi_user` → `rms_user` (already exists)
2. **Remove/Rename** `jhi_user_authority` → Use `user_branch_role` instead
3. **Keep** `authority` table (already renamed, but may need review)

## Migration Steps

### Phase 1: Refactor OAuth2 User Provisioning

**Files to Update**:

1. **UserProvisioningAuthSuccessListener.java**
   - Change from `User` entity to `RmsUser` entity
   - Update to use `RmsUserRepository` instead of `UserRepository`
   - Store OAuth2 user data in `rms_user` table

2. **UserService.java**
   - Option A: Refactor to work with `RmsUser` (recommended)
   - Option B: Deprecate and migrate all logic to `RmsUserService`

3. **SecurityUtils.java** and other security utilities
   - Update to work with `RmsUser` instead of `User`

### Phase 2: Update Authority/Role Management

**Decision needed**: How to handle authorities/roles?

**Option A**: Branch-Tied Roles Only (Recommended)
- Remove global `authority` table
- Use only `user_branch_role` for all role assignments
- Each user has roles tied to specific branches
- Advantages: Cleaner, follows RMS pattern, branch-scoped security

**Option B**: Hybrid Approach
- Keep `authority` table for global roles (e.g., ROLE_ADMIN, ROLE_SUPER_USER)
- Use `user_branch_role` for branch-specific roles (e.g., ROLE_WAITER, ROLE_CHEF)
- Advantages: Some users need global access across all branches

### Phase 3: Liquibase Migrations

Create changeset to migrate existing data:

```xml
<!-- File: 20260206000001_migrate_jhi_user_to_rms_user.xml -->
<changeSet id="20260206000001-1" author="system">
    <comment>Migrate existing jhi_user data to rms_user if any exists</comment>
    
    <!-- Copy data from jhi_user to rms_user -->
    <sql>
        INSERT INTO rms_user (id, external_user_id, username, email, first_name, last_name, is_active, created_at, created_by)
        SELECT 
            gen_random_uuid() as id,
            id as external_user_id,
            login as username,
            email,
            first_name,
            last_name,
            activated as is_active,
            created_date as created_at,
            created_by
        FROM jhi_user
        WHERE NOT EXISTS (
            SELECT 1 FROM rms_user WHERE rms_user.external_user_id = jhi_user.id
        );
    </sql>
    
    <!-- Migrate authorities to user_branch_role -->
    <!-- This requires knowing which branch to assign to -->
    <!-- May need manual intervention or business logic -->
    
</changeSet>

<changeSet id="20260206000001-2" author="system">
    <comment>Drop old JHipster tables</comment>
    
    <dropTable tableName="jhi_user_authority"/>
    <dropTable tableName="jhi_user"/>
    
    <!-- Consider if authority table should be kept or dropped -->
    <!-- <dropTable tableName="authority"/> -->
</changeSet>
```

### Phase 4: Remove Old Code

1. Delete `User.java` entity
2. Delete `UserRepository.java` interface
3. Delete `UserRepositoryInternal.java` and implementation
4. Update or delete `UserService.java`
5. Remove all references to `jhi_user` in queries and code

### Phase 5: Update API Endpoints

Review and update REST endpoints that may still reference the old User entity:

```bash
# Find REST endpoints using User entity
grep -r "AdminUserDTO\|UserDTO" src/main/java/com/atparui/rmsservice/web/rest/
```

## Current Issues Caused by Dual System

1. **TransientPropertyValueException**: UserService tries to save User with unsaved Authority entities
2. **Confusion**: Two user entities exist, unclear which to use
3. **Data Duplication**: User data may exist in both tables
4. **Inconsistent Roles**: Roles in `authority` vs `user_branch_role`

## Recommended Approach

### Immediate Fix (Temporary)

The fix I already applied to UserService.java helps with the current error, but it's a band-aid solution.

### Long-Term Solution (Recommended)

**Complete the migration**:
1. Refactor `UserProvisioningAuthSuccessListener` to use `RmsUser`
2. Migrate `UserService` logic into `RmsUserService`
3. Create Liquibase migration to copy any existing data
4. Drop old JHipster tables
5. Remove old entity classes and repositories

## Testing Checklist

After migration:
- [ ] OAuth2 login creates users in `rms_user` table
- [ ] User roles are assigned via `user_branch_role`
- [ ] No references to `jhi_user`, `jhi_user_authority` in code
- [ ] Security filters work with `RmsUser`
- [ ] API endpoints return correct user data
- [ ] Tenant database schema created correctly

##Files to Review

```
src/main/java/com/atparui/rmsservice/
├── domain/
│   ├── User.java (DELETE after migration)
│   ├── RmsUser.java (KEEP - main user entity)
│   ├── Authority.java (REVIEW - may keep or integrate into user_branch_role)
│   └── UserBranchRole.java (KEEP - branch-tied roles)
├── repository/
│   ├── UserRepository.java (DELETE after migration)
│   ├── UserRepositoryInternal.java (DELETE after migration)
│   ├── UserRepositoryInternalImpl.java (DELETE after migration)
│   └── RmsUserRepository.java (KEEP)
├── service/
│   ├── UserService.java (MIGRATE logic to RmsUserService, then DELETE)
│   └── RmsUserService.java (KEEP and ENHANCE)
└── security/
    ├── UserProvisioningAuthSuccessListener.java (UPDATE to use RmsUser)
    └── SecurityUtils.java (UPDATE if needed)
```

## Decision Needed

**Question for Product Owner/Architect**:
- Should we keep the `authority` table for global roles, or use only `user_branch_role` for all role management?
- Is there existing production data in `jhi_user` that needs migration?
- What's the timeline for completing this migration?

## References

- Similar migration pattern established with `rms_user` and `user_branch_role` entities
- Liquibase changesets: `20251228051048_added_entity_RmsUser.xml`, `20251228051049_added_entity_UserBranchRole.xml`
