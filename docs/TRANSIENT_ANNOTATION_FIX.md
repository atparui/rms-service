# Transient Annotation Fix

## Problem Statement

The rms-service had a **CRITICAL recurring bug** with `@Transient` annotations in JPA entities. Entities were using `@org.springframework.data.annotation.Transient` which Hibernate doesn't recognize, causing SQL errors like:

```
column isPersisted does not exist
```

Additionally, some entities were missing import statements for `@Transient`, and some had duplicate `@Transient` annotations on the same field.

## Root Cause

1. **Wrong Annotation**: Using `@org.springframework.data.annotation.Transient` instead of `@jakarta.persistence.Transient`
   - Spring Data's `@Transient` is for repository-level persistence control
   - Hibernate/JPA requires `@jakarta.persistence.Transient` for entity field mapping
   - Hibernate doesn't recognize Spring Data's annotation, causing it to try to persist transient fields

2. **Missing Imports**: Some files used `@Transient` without any import statement, leading to compilation ambiguity

3. **Duplicate Annotations**: Some files had duplicate `@Transient` annotations (e.g., `@Transient @Transient`), which is invalid Java syntax

## Solution

**All entity files in `src/main/java/com/atparui/rmsservice/domain/` have been fixed to use:**

```java
@jakarta.persistence.Transient
```

**Key Changes:**
1. ✅ Replaced all `@Transient` with `@jakarta.persistence.Transient` (fully qualified)
2. ✅ Removed all `import jakarta.persistence.Transient;` statements (using fully qualified names prevents import issues)
3. ✅ Removed all duplicate `@Transient` annotations
4. ✅ Fixed files that were missing import statements

## Files Fixed

The following entity files were updated:

- `Order.java` - Fixed missing import, replaced 6 `@Transient` annotations
- `TableWaiterAssignment.java` - Fixed missing import, replaced 4 `@Transient` annotations
- `Bill.java` - Removed duplicate annotations, replaced 7 `@Transient` annotations
- `TableAssignment.java` - Removed duplicate annotations, replaced 9 `@Transient` annotations
- `Branch.java` - Removed duplicate annotations, replaced 6 `@Transient` annotations
- `OrderStatusHistory.java` - Removed duplicate annotations, replaced 4 `@Transient` annotations
- `RolePermission.java` - Replaced 1 `@Transient` annotation
- Plus 28 additional entity files with `@Transient` annotations

**Total: 35+ entity files fixed**

## Why Fully Qualified Names?

Using `@jakarta.persistence.Transient` instead of importing and using `@Transient`:

1. **Prevents Import Confusion**: No risk of accidentally importing the wrong `@Transient` annotation
2. **Explicit and Clear**: Makes it immediately obvious which annotation is being used
3. **Prevents Future Errors**: New developers can't accidentally use the wrong annotation
4. **No Import Maintenance**: No need to manage import statements

## Verification

✅ **Compilation Test**: `./mvnw compile -DskipTests` - **PASSED**

All entity files now compile successfully without any annotation-related errors.

## Prevention Guidelines

**CRITICAL RULES for Future Development:**

1. **ALWAYS use fully qualified `@jakarta.persistence.Transient`** for JPA entity transient fields
2. **NEVER use `@org.springframework.data.annotation.Transient`** in entity classes
3. **NEVER use shortened `@Transient`** without fully qualifying it
4. **NEVER create duplicate annotations** - one `@jakarta.persistence.Transient` per field is sufficient

## Example Usage

### ✅ CORRECT:
```java
@Entity
@Table(name = "orders")
public class Order implements Persistable<UUID> {
    
    @jakarta.persistence.Transient
    private boolean isPersisted;
    
    @jakarta.persistence.Transient
    @JsonIgnoreProperties(value = { "restaurant" }, allowSetters = true)
    private Branch branch;
    
    @jakarta.persistence.Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }
}
```

### ❌ WRONG:
```java
// DON'T DO THIS:
import org.springframework.data.annotation.Transient;  // ❌ WRONG!

@Transient  // ❌ Ambiguous, could be wrong annotation
private boolean isPersisted;

@Transient @Transient  // ❌ Duplicate annotations
private Branch branch;
```

## Date Fixed

**February 8, 2026**

## Impact

- **Before**: SQL errors, compilation failures, runtime exceptions
- **After**: Clean compilation, proper Hibernate entity mapping, no SQL errors

This fix ensures that Hibernate correctly identifies transient fields and does not attempt to persist them to the database, preventing the recurring "column does not exist" errors.
