# RMS Entities – CRUD Context for UI

Use this as a quick reference for which entities exist, what they represent, and the expected CRUD surfaces. Routes follow standard JHipster patterns (`/api/<entity>` pluralized).

## Core Org
- Restaurant (`/api/restaurants`): top-level org.
- Branch (`/api/branches`): child of restaurant; location info.
- RmsUser (`/api/rms-users`): domain user profile.
- UserBranchRole (`/api/user-branch-roles`): user-to-branch role assignment.

## Seating / Operations
- BranchTable (`/api/branch-tables`): physical tables.
- TableAssignment (`/api/table-assignments`): table to customer/session.
- TableWaiterAssignment (`/api/table-waiter-assignments`): table to waiter.
- Shift (`/api/shifts`): staff shift windows.
- Table Roster (logical): use TableAssignment + TableWaiterAssignment for roster views.

## Menu & Catalog
- MenuCategory (`/api/menu-categories`)
- MenuItem (`/api/menu-items`)
- MenuItemVariant (`/api/menu-item-variants`)
- MenuItemAddon (`/api/menu-item-addons`)

## Inventory
- Inventory (`/api/inventories`): stock items.

## Customers & Loyalty
- Customer (`/api/customers`)
- CustomerLoyalty (`/api/customer-loyalties`)

## Ordering & KOT
- Order (`/api/orders`) [entity key: `jhi_order`]
- OrderItem (`/api/order-items`)
- OrderItemCustomization (`/api/order-item-customizations`)
- OrderStatusHistory (`/api/order-status-histories`)

## Pricing & Tax
- TaxConfig (`/api/tax-configs`)
- Discount (`/api/discounts`)

## Billing & Payments
- Bill (`/api/bills`)
- BillItem (`/api/bill-items`)
- BillTax (`/api/bill-taxes`)
- BillDiscount (`/api/bill-discounts`)
- PaymentMethod (`/api/payment-methods`)
- Payment (`/api/payments`)

## Navigation & Permissions (from new module)
- AppMenu (`/api/app-menus`)
- Permission (`/api/permissions`)
- RolePermission (`/api/role-permissions`)
- MenuPermission (`/api/menu-permissions`)
- Menu Tree (filtered): `/api/app-menus/tree?appKey=`

## Suggested UI Surfaces
- Org: Restaurant, Branch, RmsUser, UserBranchRole.
- Seating/Ops: Tables, Table Assignments, Waiter Assignments, Shifts, Roster view.
- Catalog: Menu Categories, Items, Variants, Addons.
- Inventory: Stock list/adjustments.
- Customers: Customers, Loyalty.
- Orders/KOT: Orders list/detail, items, status history.
- Pricing: Tax configs, Discounts.
- Billing: Bills, Bill items/taxes/discounts, Payments, Payment methods.
- AuthZ UI: App Menus, Permissions, Role↔Permission, Menu↔Permission; menu tree consumer.

## Notes
- All are standard CRUD endpoints; pagination on list endpoints.
- Some entities relate by FK; fetch detail to show linked names (e.g., Order → Customer, Branch, Table; Bill → Order/Items).
