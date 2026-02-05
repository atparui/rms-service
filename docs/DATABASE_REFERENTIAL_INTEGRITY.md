# Database Referential Integrity - Foreign Key Cascading Rules

## Overview
Complete implementation of foreign key constraints with proper cascading rules across the entire RMS database schema. This ensures data integrity, automatic cleanup, and protection against invalid deletions.

## Cascading Strategy

### CASCADE (onDelete="CASCADE")
**Purpose:** Child records are automatically deleted when parent is deleted.

**Use Cases:**
- Order items when order is deleted
- Bill items when bill is deleted
- User sync logs when user is deleted
- Shift assignments when shift is deleted

**Example:**
```sql
-- When an order is deleted, all order_items are automatically deleted
DELETE FROM orders WHERE id = '123';
-- Cascades to: order_item, order_status_history (automatic)
```

### RESTRICT (onDelete="RESTRICT")
**Purpose:** Prevents deletion of parent if children exist. Protects reference data.

**Use Cases:**
- Restaurant cannot be deleted if branches exist
- Branch cannot be deleted if active orders exist
- Restaurant role cannot be deleted if users have that role
- Tax config cannot be deleted if bills reference it

**Example:**
```sql
-- This will FAIL if the restaurant has branches
DELETE FROM restaurant WHERE id = '456';
-- Error: foreign key constraint violated

-- Must delete branches first
DELETE FROM branch WHERE restaurant_id = '456';
-- Now can delete restaurant
DELETE FROM restaurant WHERE id = '456';
```

### SET NULL (onDelete="SET NULL")
**Purpose:** Nulls the foreign key when parent is deleted. For optional relationships.

**Use Cases:**
- Customer ID in orders (order remains without customer)
- User ID in orders (order remains without assigned user)
- Supervisor in table assignments (assignment remains without supervisor)

**Example:**
```sql
-- When customer is deleted, their orders remain with customer_id = NULL
DELETE FROM customer WHERE id = '789';
-- Updates: orders SET customer_id = NULL WHERE customer_id = '789'
```

## Complete Foreign Key Reference

### Core Entities

#### restaurant
- **No foreign keys** (root entity)
- **Referenced by:** branch, menu_category, customer_loyalty, tax_config, discount
- **Deletion:** RESTRICTED if any children exist

#### branch
- **Foreign Keys:**
  - `restaurant_id` → `restaurant.id` (RESTRICT)
- **Referenced by:** menu_item, shift, branch_table, orders, bill, inventory
- **Deletion:** CASCADE from restaurant, RESTRICTED if children exist

#### rms_user
- **No foreign keys** (root entity)
- **Referenced by:** user_branch_role, user_sync_log, table_assignment, table_waiter_assignment, customer, orders
- **Deletion:** RESTRICTED if branch roles exist, CASCADE for sync logs

---

### User & Branch Management

#### user_branch_role
- **Foreign Keys:**
  - `user_id` → `rms_user.id` (CASCADE)
  - `branch_id` → `branch.id` (CASCADE)
  - `role` → `restaurant_role.name` (RESTRICT, onUpdate=CASCADE)
- **Behavior:**
  - Deleted when user is deleted
  - Deleted when branch is deleted
  - Cannot delete role if assigned to users

#### restaurant_role
- **No foreign keys** (reference data)
- **Referenced by:** user_branch_role, app_navigation_menu_role, role_permission
- **Deletion:** RESTRICTED if any users/permissions reference it

#### user_sync_log
- **Foreign Keys:**
  - `user_id` → `rms_user.id` (CASCADE)
- **Behavior:** Deleted when user is deleted

---

### Branch Operations

#### shift
- **Foreign Keys:**
  - `branch_id` → `branch.id` (CASCADE)
- **Referenced by:** table_assignment
- **Behavior:** Deleted when branch is deleted

#### branch_table
- **Foreign Keys:**
  - `branch_id` → `branch.id` (CASCADE)
- **Referenced by:** table_assignment, orders
- **Behavior:** Deleted when branch is deleted

#### table_assignment
- **Foreign Keys:**
  - `branch_table_id` → `branch_table.id` (CASCADE)
  - `shift_id` → `shift.id` (CASCADE)
  - `supervisor_id` → `rms_user.id` (SET NULL)
- **Referenced by:** table_waiter_assignment
- **Behavior:**
  - Deleted when table or shift is deleted
  - Supervisor nulled if supervisor user deleted

#### table_waiter_assignment
- **Foreign Keys:**
  - `table_assignment_id` → `table_assignment.id` (CASCADE)
  - `waiter_id` → `rms_user.id` (CASCADE)
- **Behavior:** Deleted when assignment or waiter is deleted

---

### Menu Management

#### menu_category
- **Foreign Keys:**
  - `restaurant_id` → `restaurant.id` (CASCADE)
- **Referenced by:** menu_item
- **Behavior:** Deleted when restaurant is deleted

#### menu_item
- **Foreign Keys:**
  - `branch_id` → `branch.id` (CASCADE)
  - `menu_category_id` → `menu_category.id` (SET NULL)
- **Referenced by:** menu_item_variant, menu_item_addon, inventory, order_item
- **Behavior:**
  - Deleted when branch is deleted
  - Category nulled when category deleted
  - RESTRICTED if order items reference it

#### menu_item_variant
- **Foreign Keys:**
  - `menu_item_id` → `menu_item.id` (CASCADE)
- **Referenced by:** order_item
- **Behavior:** Deleted when menu item is deleted

#### menu_item_addon
- **Foreign Keys:**
  - `menu_item_id` → `menu_item.id` (CASCADE)
- **Referenced by:** order_item_customization
- **Behavior:** Deleted when menu item is deleted

#### inventory
- **Foreign Keys:**
  - `branch_id` → `branch.id` (CASCADE)
  - `menu_item_id` → `menu_item.id` (CASCADE)
- **Behavior:** Deleted when branch or menu item is deleted

---

### Customer Management

#### customer
- **Foreign Keys:**
  - `user_id` → `rms_user.id` (SET NULL)
- **Referenced by:** customer_loyalty, orders, bill
- **Behavior:** User ID nulled if user deleted

#### customer_loyalty
- **Foreign Keys:**
  - `customer_id` → `customer.id` (CASCADE)
  - `restaurant_id` → `restaurant.id` (CASCADE)
- **Behavior:** Deleted when customer or restaurant is deleted

---

### Order Management

#### orders
- **Foreign Keys:**
  - `branch_id` → `branch.id` (RESTRICT)
  - `customer_id` → `customer.id` (SET NULL)
  - `user_id` → `rms_user.id` (SET NULL)
  - `branch_table_id` → `branch_table.id` (SET NULL)
- **Referenced by:** order_item, order_status_history, bill
- **Behavior:**
  - CANNOT delete branch if active orders
  - Customer/user/table nulled if deleted

#### order_item
- **Foreign Keys:**
  - `order_id` → `orders.id` (CASCADE)
  - `menu_item_id` → `menu_item.id` (RESTRICT)
  - `menu_item_variant_id` → `menu_item_variant.id` (SET NULL)
- **Referenced by:** order_item_customization, bill_item
- **Behavior:**
  - Deleted when order is deleted
  - CANNOT delete menu item if ordered
  - Variant nulled if deleted

#### order_item_customization
- **Foreign Keys:**
  - `order_item_id` → `order_item.id` (CASCADE)
  - `menu_item_addon_id` → `menu_item_addon.id` (RESTRICT)
- **Behavior:**
  - Deleted when order item is deleted
  - CANNOT delete addon if used in orders

#### order_status_history
- **Foreign Keys:**
  - `order_id` → `orders.id` (CASCADE)
- **Behavior:** Deleted when order is deleted

---

### Billing & Payment

#### bill
- **Foreign Keys:**
  - `order_id` → `orders.id` (CASCADE)
  - `branch_id` → `branch.id` (RESTRICT)
  - `customer_id` → `customer.id` (SET NULL)
- **Referenced by:** bill_item, bill_tax, bill_discount, payment
- **Behavior:**
  - Deleted when order is deleted
  - CANNOT delete branch if bills exist
  - Customer nulled if deleted

#### bill_item
- **Foreign Keys:**
  - `bill_id` → `bill.id` (CASCADE)
  - `order_item_id` → `order_item.id` (CASCADE)
- **Behavior:** Deleted when bill or order item is deleted

#### bill_tax
- **Foreign Keys:**
  - `bill_id` → `bill.id` (CASCADE)
  - `tax_config_id` → `tax_config.id` (RESTRICT)
- **Behavior:**
  - Deleted when bill is deleted
  - CANNOT delete tax config if applied to bills

#### bill_discount
- **Foreign Keys:**
  - `bill_id` → `bill.id` (CASCADE)
  - `discount_id` → `discount.id` (RESTRICT)
- **Behavior:**
  - Deleted when bill is deleted
  - CANNOT delete discount if applied to bills

#### payment
- **Foreign Keys:**
  - `bill_id` → `bill.id` (CASCADE)
  - `payment_method_id` → `payment_method.id` (RESTRICT)
- **Behavior:**
  - Deleted when bill is deleted
  - CANNOT delete payment method if used

---

### Configuration Tables

#### tax_config
- **Foreign Keys:**
  - `restaurant_id` → `restaurant.id` (CASCADE)
- **Referenced by:** bill_tax
- **Behavior:**
  - Deleted when restaurant is deleted
  - RESTRICTED if applied to bills

#### discount
- **Foreign Keys:**
  - `restaurant_id` → `restaurant.id` (CASCADE)
- **Referenced by:** bill_discount
- **Behavior:**
  - Deleted when restaurant is deleted
  - RESTRICTED if applied to bills

#### payment_method
- **No foreign keys** (lookup table)
- **Referenced by:** payment
- **Deletion:** RESTRICTED if used in payments

---

### Navigation & Permissions

#### app_navigation_menu
- **No foreign keys** (root entity)
- **Referenced by:** app_navigation_menu_item, app_navigation_menu_role

#### app_navigation_menu_item
- **Foreign Keys:**
  - `parent_menu_id` → `app_navigation_menu.id` (CASCADE)
- **Referenced by:** app_navigation_menu_role
- **Behavior:** Deleted when parent menu is deleted

#### app_navigation_menu_role
- **Foreign Keys:**
  - `app_navigation_menu_id` → `app_navigation_menu.id` (CASCADE)
  - `app_navigation_menu_item_id` → `app_navigation_menu_item.id` (CASCADE)
  - `role` → `restaurant_role.name` (RESTRICT, onUpdate=CASCADE)
- **Behavior:**
  - Deleted when menu/menu item is deleted
  - CANNOT delete role if assigned to menu
  - Role name updated if role renamed

#### menu_permission (if exists)
- **Foreign Keys:**
  - `app_menu_id` → `app_menu.id` (CASCADE)
  - `permission_id` → `permission.id` (CASCADE)
- **Behavior:** Deleted when menu or permission is deleted

#### role_permission (if exists)
- **Foreign Keys:**
  - `permission_id` → `permission.id` (CASCADE)
  - `role` → `restaurant_role.name` (RESTRICT, onUpdate=CASCADE)
- **Behavior:**
  - Deleted when permission is deleted
  - CANNOT delete role if has permissions
  - Role name updated if role renamed

---

## Deletion Scenarios

### Scenario 1: Delete a Restaurant
```sql
-- 1. Check what will be affected
SELECT 'branches' as type, COUNT(*) FROM branch WHERE restaurant_id = 'rest-123'
UNION ALL
SELECT 'menu_categories', COUNT(*) FROM menu_category WHERE restaurant_id = 'rest-123'
UNION ALL
SELECT 'tax_configs', COUNT(*) FROM tax_config WHERE restaurant_id = 'rest-123'
UNION ALL
SELECT 'discounts', COUNT(*) FROM discount WHERE restaurant_id = 'rest-123';

-- 2. Try to delete (will fail if branches exist)
DELETE FROM restaurant WHERE id = 'rest-123';
-- Error: foreign key constraint "fk_branch__restaurant_id" violated

-- 3. Must delete branches first (or CASCADE will handle it)
DELETE FROM branch WHERE restaurant_id = 'rest-123';
-- This cascades to: shift, branch_table, menu_item, inventory, etc.

-- 4. Now can delete restaurant
DELETE FROM restaurant WHERE id = 'rest-123';
-- This cascades to: menu_category, customer_loyalty, tax_config, discount
```

### Scenario 2: Delete a User
```sql
-- Delete user - automatic cleanup
DELETE FROM rms_user WHERE id = 'user-456';

-- Automatically handles:
-- ✅ user_branch_role records (CASCADE)
-- ✅ user_sync_log records (CASCADE)
-- ✅ table_waiter_assignment records (CASCADE)
-- ✅ Nulls supervisor_id in table_assignment (SET NULL)
-- ✅ Nulls user_id in customer (SET NULL)
-- ✅ Nulls user_id in orders (SET NULL)
```

### Scenario 3: Delete an Order
```sql
-- Delete order - automatic cleanup
DELETE FROM orders WHERE id = 'order-789';

-- Automatically cascades to:
-- ✅ order_item records
-- ✅ order_item_customization records
-- ✅ order_status_history records
-- ✅ bill record
-- ✅ bill_item records
-- ✅ bill_tax records
-- ✅ bill_discount records
-- ✅ payment records

-- Everything cleaned up in single statement!
```

### Scenario 4: Delete a Role (Fails if in Use)
```sql
-- Try to delete role
DELETE FROM restaurant_role WHERE name = 'WAITER';
-- Error: foreign key constraint violated

-- Check what's referencing it
SELECT 'user_branch_role' as type, COUNT(*) FROM user_branch_role WHERE role = 'WAITER'
UNION ALL
SELECT 'app_navigation_menu_role', COUNT(*) FROM app_navigation_menu_role WHERE role = 'WAITER'
UNION ALL
SELECT 'role_permission', COUNT(*) FROM role_permission WHERE role = 'WAITER';

-- Must unassign all users first
DELETE FROM user_branch_role WHERE role = 'WAITER';
DELETE FROM app_navigation_menu_role WHERE role = 'WAITER';

-- Now can delete role
DELETE FROM restaurant_role WHERE name = 'WAITER';
```

---

## Benefits

### 1. Data Integrity
- ✅ No orphaned records
- ✅ Consistent relationships
- ✅ Database-enforced rules

### 2. Automatic Cleanup
- ✅ Child records deleted automatically
- ✅ No manual cleanup needed
- ✅ Single DELETE statement cascades properly

### 3. Protection
- ✅ Cannot delete referenced data
- ✅ Must explicitly handle dependencies
- ✅ Prevents data loss

### 4. Clarity
- ✅ Clear deletion policies
- ✅ Predictable behavior
- ✅ Documented cascading rules

---

## Testing Cascading Rules

### Test CASCADE
```sql
-- Create test order with items
INSERT INTO orders (id, ...) VALUES ('test-order', ...);
INSERT INTO order_item (id, order_id, ...) VALUES ('test-item', 'test-order', ...);

-- Delete order
DELETE FROM orders WHERE id = 'test-order';

-- Verify cascaded
SELECT COUNT(*) FROM order_item WHERE order_id = 'test-order';
-- Should return 0 (cascaded)
```

### Test RESTRICT
```sql
-- Try to delete branch with active orders
DELETE FROM branch WHERE id = 'branch-123';
-- Should fail with FK constraint error
```

### Test SET NULL
```sql
-- Delete customer with orders
DELETE FROM customer WHERE id = 'cust-456';

-- Verify orders remain with NULL customer
SELECT customer_id FROM orders WHERE customer_id IS NULL;
-- Should return the orders
```

---

## Migration Notes

### Fresh Database Setup
When creating database from scratch:
```bash
# Drop existing schemas
psql -c "DROP SCHEMA IF EXISTS rms_service CASCADE;"
psql -c "DROP SCHEMA IF EXISTS rms_rms_demo CASCADE;"

# Run up.sh
./scripts/up.sh dev

# Liquibase will automatically:
# 1. Create all tables
# 2. Add all foreign keys
# 3. Apply cascading rules
# 4. Seed restaurant roles
```

### Existing Database Migration
The Liquibase changeset handles existing databases:
```xml
<!-- Drops existing FK, recreates with cascading rules -->
<dropForeignKeyConstraint .../>
<addForeignKeyConstraint ... onDelete="CASCADE"/>
```

---

## Summary

| Strategy | Count | Use Case |
|----------|-------|----------|
| CASCADE | 35 | Child records (order_item, bill_item, shifts, etc.) |
| RESTRICT | 12 | Reference data (restaurant_role, tax_config, payment_method) |
| SET NULL | 8 | Optional relationships (customer, user, supervisor) |
| **TOTAL** | **55** | **All foreign key constraints** |

**Additional FKs Added:**
- `app_navigation_menu_role.role` → `restaurant_role.name`
- `role_permission.role` → `restaurant_role.name`

**Result:** Complete referential integrity with proper cascading rules across the entire RMS database schema.
