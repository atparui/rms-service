# Restaurant Role Management System

## Overview
The Restaurant Role Management System provides a database-backed, type-safe approach to managing staff roles in the RMS application. This replaces the previous string-based system with proper referential integrity and validation.

## Architecture

### Database Schema

```sql
CREATE TABLE restaurant_role (
    name VARCHAR(50) PRIMARY KEY,
    display_name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Foreign key constraint ensures only valid roles can be assigned
ALTER TABLE user_branch_role 
    ADD CONSTRAINT fk_user_branch_role__role_name 
    FOREIGN KEY (role) REFERENCES restaurant_role(name)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;
```

### Entity Structure

```java
@Entity
@Table(name = "restaurant_role")
public class RestaurantRole {
    @Id
    private String name;              // Primary key: MANAGER, WAITER, etc.
    
    @NotNull
    private String displayName;        // Human-readable: "Manager", "Waiter"
    
    private String description;        // Role description for UI
    
    @NotNull
    private Boolean isActive;          // Can be assigned?
    
    @NotNull
    private Integer sortOrder;         // Display order in lists
    
    @NotNull
    private Instant createdAt;         // Audit timestamp
    
    private Instant updatedAt;         // Last update timestamp
}
```

## Standard Roles

### Management Roles
1. **MANAGER** (sort: 1)
   - Display: "Manager"
   - Full access to branch operations
   - Can view reports, manage staff, configure settings

2. **ASSISTANT_MANAGER** (sort: 2)
   - Display: "Assistant Manager"
   - Elevated privileges, second-in-command
   - Can perform most manager functions

### Kitchen Staff
3. **CHEF** (sort: 3)
   - Display: "Chef"
   - Head chef, responsible for kitchen operations
   - Manages menu execution, quality control

4. **SOUS_CHEF** (sort: 4)
   - Display: "Sous Chef"
   - Second-in-command in kitchen
   - Supervises line cooks

5. **LINE_COOK** (sort: 5)
   - Display: "Line Cook"
   - Station-specific cook
   - Prepares dishes for specific station

12. **DISHWASHER** (sort: 12)
    - Display: "Dishwasher"
    - Cleans dishes, utensils, kitchen equipment
    - Maintains sanitation standards

### Front-of-House Staff
6. **WAITER** (sort: 6)
   - Display: "Waiter/Waitress"
   - Takes orders, serves customers
   - Primary customer interaction role

7. **HOST** (sort: 7)
   - Display: "Host/Hostess"
   - Greets and seats customers
   - Manages reservations, wait lists

9. **BARTENDER** (sort: 9)
   - Display: "Bartender"
   - Prepares and serves beverages
   - Manages bar inventory

10. **BUSSER** (sort: 10)
    - Display: "Busser"
    - Cleans and resets tables
    - Assists waiters

### Support Staff
8. **CASHIER** (sort: 8)
   - Display: "Cashier"
   - Handles payments and cash register
   - Processes transactions

11. **DELIVERY_DRIVER** (sort: 11)
    - Display: "Delivery Driver"
    - Delivers orders to customers
    - Manages delivery logistics

## REST API

### Endpoints

#### List All Roles
```http
GET /api/restaurant-roles
GET /api/restaurant-roles?activeOnly=true
```

**Response:**
```json
[
  {
    "name": "MANAGER",
    "displayName": "Manager",
    "description": "Restaurant manager with full access to branch operations",
    "isActive": true,
    "sortOrder": 1,
    "createdAt": "2026-02-05T14:00:00Z",
    "updatedAt": null
  },
  {
    "name": "WAITER",
    "displayName": "Waiter/Waitress",
    "description": "Server responsible for taking orders and serving customers",
    "isActive": true,
    "sortOrder": 6,
    "createdAt": "2026-02-05T14:00:00Z",
    "updatedAt": null
  }
]
```

#### Get Single Role
```http
GET /api/restaurant-roles/MANAGER
```

**Response:**
```json
{
  "name": "MANAGER",
  "displayName": "Manager",
  "description": "Restaurant manager with full access to branch operations",
  "isActive": true,
  "sortOrder": 1,
  "createdAt": "2026-02-05T14:00:00Z",
  "updatedAt": null
}
```

#### Create New Role
```http
POST /api/restaurant-roles
Content-Type: application/json

{
  "name": "SUPERVISOR",
  "displayName": "Supervisor",
  "description": "Floor supervisor monitoring operations",
  "isActive": true,
  "sortOrder": 3
}
```

**Response:** `201 Created` with role object

#### Update Role
```http
PUT /api/restaurant-roles/SUPERVISOR
Content-Type: application/json

{
  "name": "SUPERVISOR",
  "displayName": "Floor Supervisor",
  "description": "Supervises front-of-house and back-of-house operations",
  "isActive": true,
  "sortOrder": 3
}
```

**Response:** `200 OK` with updated role

#### Partial Update
```http
PATCH /api/restaurant-roles/SUPERVISOR
Content-Type: application/json

{
  "isActive": false
}
```

**Response:** `200 OK` with updated role

#### Delete Role
```http
DELETE /api/restaurant-roles/SUPERVISOR
```

**Response:** `204 No Content`

**Note:** Cannot delete if users are assigned this role (foreign key constraint)

## Usage in User Assignment

### Assigning Role to User

```java
// Create user-branch-role assignment
UserBranchRoleDTO assignment = new UserBranchRoleDTO();
assignment.setUserId(userId);
assignment.setBranchId(branchId);
assignment.setRole("WAITER");  // Must match restaurant_role.name
assignment.setIsActive(true);
assignment.setAssignedAt(Instant.now());
assignment.setAssignedBy(currentUser);

userBranchRoleService.save(assignment);
```

### Foreign Key Enforcement

```java
// This WILL work (role exists)
assignment.setRole("MANAGER");

// This will FAIL (referential integrity violation)
assignment.setRole("INVALID_ROLE");
// Exception: foreign key constraint "fk_user_branch_role__role_name"
```

### Loading Role Metadata

```java
// UserBranchRole has transient field for role lookup
UserBranchRole userRole = userBranchRoleRepository.findById(id);

// Load role metadata separately if needed
RestaurantRole role = restaurantRoleRepository.findById(userRole.getRole());
System.out.println(role.getDisplayName());  // "Manager"
System.out.println(role.getDescription());   // Full description
```

## Benefits

### 1. Type Safety
- ✅ Only valid roles can be assigned
- ✅ Typos caught at database level
- ✅ No silent failures with invalid roles

### 2. Referential Integrity
- ✅ Cannot assign non-existent role
- ✅ Cannot delete role if users assigned
- ✅ Database enforces consistency

### 3. Extensibility
- ✅ Add new roles via API/admin UI
- ✅ No code deployment needed
- ✅ Tenant-specific customization possible

### 4. Metadata Support
- ✅ Display names for UI
- ✅ Descriptions for help text
- ✅ Sort order for consistent lists
- ✅ Active/inactive management

### 5. Audit Trail
- ✅ Created timestamp on all roles
- ✅ Updated timestamp tracks changes
- ✅ Can track who added custom roles

## Migration from String-Based System

### Before (String-Based)
```java
// No validation - any string accepted
userBranchRole.setRole("WAITER");      // OK
userBranchRole.setRole("WITER");       // Typo accepted!
userBranchRole.setRole("anything");    // Also accepted!
```

### After (Database-Backed)
```java
// Validated by foreign key constraint
userBranchRole.setRole("WAITER");      // ✅ OK (role exists)
userBranchRole.setRole("WITER");       // ❌ FK constraint violation
userBranchRole.setRole("anything");    // ❌ FK constraint violation
```

## Database Recreation

When recreating the database from scratch:

```bash
# 1. Drop schemas
psql -c "DROP SCHEMA IF EXISTS rms_service CASCADE;"
psql -c "DROP SCHEMA IF EXISTS rms_rms_demo CASCADE;"

# 2. Run up.sh
cd /path/to/platform && ./scripts/up.sh dev

# 3. Liquibase will automatically:
#    - Create restaurant_role table
#    - Seed 12 standard roles
#    - Add foreign key constraint to user_branch_role
```

## Customization

### Adding Custom Roles (Per Tenant)

Each tenant can add custom roles specific to their operations:

```http
POST /api/restaurant-roles
{
  "name": "SHIFT_LEADER",
  "displayName": "Shift Leader",
  "description": "Manages staff during specific shift",
  "isActive": true,
  "sortOrder": 3
}
```

### Deactivating Roles

Instead of deleting (which fails if users assigned), deactivate:

```http
PATCH /api/restaurant-roles/LINE_COOK
{
  "isActive": false
}
```

## Best Practices

### 1. Role Naming Convention
- Use UPPER_SNAKE_CASE for role names
- Keep names short but descriptive
- Use underscores for multi-word roles (ASSISTANT_MANAGER)

### 2. Display Names
- Use proper capitalization
- Human-readable format
- Can include special characters, slashes (Host/Hostess)

### 3. Sort Order
- Use gaps (1, 2, 3, 10, 20, 30) to allow insertions
- Group related roles (1-2: management, 3-5: kitchen, 6-11: front-of-house)

### 4. Descriptions
- Keep under 500 characters
- Describe key responsibilities
- Useful for role selection UI

### 5. Deletion
- Avoid deleting standard roles
- Deactivate instead of delete
- Only delete custom roles with no assignments

## Security Considerations

### 1. Role Creation Permissions
- Only admins/managers should create roles
- Restrict DELETE operations to system admins
- Regular users can only view active roles

### 2. Validation
- Validate role name format (uppercase, underscores only)
- Prevent duplicate role names
- Ensure sort_order is unique per tenant (if multi-tenant)

### 3. Audit
- Log all role creation/modification
- Track who added custom roles
- Monitor deletion attempts

## Future Enhancements

1. **Role Permissions Matrix**
   - Link roles to specific permissions
   - Fine-grained access control
   - `role_permission` join table

2. **Role Hierarchy**
   - Define parent-child relationships
   - Inherited permissions
   - `parent_role_name` field

3. **Tenant-Specific Roles**
   - Add `tenant_id` column
   - Each tenant manages their roles
   - Isolate role definitions per tenant

4. **Role Templates**
   - Quick setup for new tenants
   - Copy standard roles
   - Customize as needed

## Related Tables

- `user_branch_role` - Assigns roles to users per branch
- `rms_user` - User accounts
- `branch` - Restaurant branches
- (Future) `role_permission` - Role-based permissions

## Troubleshooting

### Cannot Delete Role
**Error:** "foreign key constraint violation"
**Solution:** Deactivate instead of delete, or first unassign all users

### Role Assignment Fails
**Error:** "fk_user_branch_role__role_name constraint violated"
**Solution:** Check role exists and is spelled correctly (case-sensitive)

### Role Not Appearing in List
**Cause:** `isActive = false`
**Solution:** Use `GET /api/restaurant-roles` without `?activeOnly=true`

## Summary

The Restaurant Role Management System provides a robust, database-backed foundation for managing staff roles with:
- ✅ 12 pre-seeded standard roles
- ✅ Full CRUD REST API
- ✅ Foreign key constraints for data integrity
- ✅ Extensible for custom tenant-specific roles
- ✅ Active/inactive role management
- ✅ Audit timestamps for tracking

This system replaces the previous string-based approach and provides proper type safety and referential integrity for the RMS application.
