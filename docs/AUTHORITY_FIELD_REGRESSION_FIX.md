# Authority Field Regression Fix

## Problem

**Error:** `ERROR: relation "user_authority" does not exist`

**Impact:** User authentication failed on login

## Root Cause Analysis

### Timeline of Events

1. **Feb 5, 2026 - Commit `9271a35`**: ✅ **CORRECT FIX**
   - Removed `authorities` field from `RmsUser`
   - Removed `user_authority` table dependency
   - Reason: RMS uses `user_branch_role` system, not `user_authority`
   - Build: SUCCESS
   - User auth: WORKING

2. **Later - Commit `4c12cb3`**: ❌ **REGRESSION**
   - Title: "refactor: remove unnecessary @Transient annotations"
   - **ADDED BACK** the authorities field that was correctly removed!
   - Re-introduced `user_authority` table dependency
   - Build: SUCCESS (compiles fine)
   - Runtime: BROKEN (`user_authority` table doesn't exist)

3. **Today - Commit `06b4119`**: ⚠️ **MISSED THE REGRESSION**
   - Title: "fix: Use correct @jakarta.persistence.Transient"
   - Fixed `@Transient` annotations (correct)
   - But didn't notice authorities field was back
   - Build: SUCCESS
   - Runtime: STILL BROKEN

### What Went Wrong

**Commit `4c12cb3` silently undid the fix from `9271a35`**

The problem:
```java
// After 9271a35 (CORRECT):
// NOTE: We don't use authorities - removed to avoid non-existent user_authority table

// After 4c12cb3 (WRONG - ADDED BACK):
@JsonIgnore
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(name = "user_authority", ...)  // ← This table DOESN'T EXIST!
private Set<Authority> authorities = new HashSet<>();
```

## The Fix

### Reverted to Correct State

Restored `RmsUser.java` to the state from commit `9271a35`:

**Removed:**
```java
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
    name = "user_authority",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name")
)
private Set<Authority> authorities = new HashSet<>();

public Set<Authority> getAuthorities() { return authorities; }
public void setAuthorities(Set<Authority> authorities) { this.authorities = authorities; }
public RmsUser authorities(Set<Authority> authorities) { ... }
```

**Added Comments:**
```java
// NOTE: We don't use authorities collection - RMS uses user_branch_role system instead
// Removed @ManyToMany authorities field to avoid querying non-existent user_authority table
```

### Applied @Transient Fix

Then applied the correct `@Transient` annotation:

```java
@jakarta.persistence.Transient
private boolean isPersisted;

@jakarta.persistence.Transient
@Override
public boolean isNew() {
    return !this.isPersisted;
}
```

## Why This Database Table Doesn't Exist

**RMS uses a custom RBAC system:**
- ✅ `user_branch_role` - Assigns roles to users per branch
- ✅ `restaurant_role` - Defines roles at restaurant level
- ❌ `user_authority` - OLD JHipster table, NOT used in RMS

The `user_authority` table was part of JHipster's default security model, which RMS doesn't use.

## Lessons Learned

### For Future Development

1. **Review All File Changes** - Even "refactoring" commits can break working code
2. **Test Runtime Behavior** - Compilation success ≠ runtime success
3. **Check Git History** - Before "fixing" something, check if it was intentionally designed that way
4. **Document Intentional Removals** - Add comments explaining why code was removed
5. **Regression Testing** - Critical paths (like auth) need automated tests

### Why This Wasn't Caught

1. **Build Success**: The code compiles fine - Hibernate only fails at runtime
2. **Lazy Loading**: Error only occurs when trying to access the relationship
3. **Missing Tests**: No integration test for user provisioning on OAuth2 login

## Prevention

### Added Documentation

Added clear comments in `RmsUser.java`:
```java
// NOTE: We don't use authorities collection - RMS uses user_branch_role system instead
// Removed @ManyToMany authorities field to avoid querying non-existent user_authority table
```

### Build Verification

```bash
./mvnw clean compile -DskipTests
# BUILD SUCCESS ✅
```

### Runtime Verification

After deployment, check logs for:
```
✅ User provisioning succeeded
❌ NO ERROR: relation "user_authority" does not exist
```

## Files Changed

- `src/main/java/com/atparui/rmsservice/domain/RmsUser.java`
  - Removed: `authorities` field and methods
  - Fixed: `@Transient` annotations
  - Added: Comments explaining removal

## Related Commits

- `9271a35` - Original correct fix (Feb 5, 2026)
- `4c12cb3` - Regression that added authorities back
- `06b4119` - @Transient fix (didn't catch regression)
- **THIS COMMIT** - Final fix combining both corrections

## Deployment

```bash
cd /home/sivakumar/Shiva/Workspace/platform
docker-compose build rms-service
docker-compose up -d rms-service
sleep 30
docker logs --tail 50 rms-service | grep -i "error\|authority"
# Should see NO errors ✅
```

## Cost Impact

This regression cost significant debugging time and API calls due to:
1. Iterative failed fixes
2. Token-intensive file reading and editing
3. Multiple compilation cycles

**Root Cause:** Insufficient git history review before making changes

**Prevention:** Always check `git log` and `git blame` before "fixing" code that seems wrong
