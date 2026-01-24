# App Menu & Permissions – Frontend Brief

## API
- `GET /api/app-menus/tree?appKey=` (appKey optional; current seed uses no appKey filter).
- Returns only permitted items; includes children sorted and `requiredPermissions` for transparency.

## Menu Model (backend)
- Table `app_menu`: `id`, `parent_id` (tree), `menu_key`, `label`, `route_path`, `type` (route), `icon` (opt), `sort_order`, `is_active`, `permission_logic` (ANY/ALL), `app_key` (opt).
- Table `menu_permission`: links menus to one or more permissions (visibility).
- Table `permission`: codes like `orders:view`, `orders:create`, etc.
- Table `role_permission`: roles map to permission ids.

## Seeded Menus (top-level)
1) Restaurant (`/restaurant`)
2) Branch (`/branches`)
3) Customers (`/customers`)
4) Users (`/users`)
5) Menu Categories (`/menu/categories`)
6) Menu Items (`/menu/items`)
7) Inventory (`/inventory`)
8) Billing & Payments (`/billing`)
9) Orders (`/orders`)
10) Table Management (`/tables`)
11) Table Roster (`/table-roster`)

All are `type=route`, `is_active=true`, `permission_logic=ANY`.

## Seeded Permissions (per menu scope)
- `view`, `create`, `edit`, `delete` for each scope: restaurant, branch, customers, users, menu_category, menu_item, inventory, billing, orders, table_management, table_roster.

## Default Role Grants
- `ROLE_ADMIN`: all permissions.
- `ROLE_MANAGER`: view/create/edit across most areas; no deletes; includes users view/create/edit.
- `ROLE_WAITER`: orders view/create/edit; table_roster view; menu_item view.
- `ROLE_CHEF`: orders view/edit; menu_item view.
- `ROLE_CASHIER`: orders view/create/edit; billing view/create/edit.
- `ROLE_CUSTOMER`: menu_item view; orders view/create.

## Client Expectations
- Call the tree once after login (or token refresh) and cache client-side; reuse for web and mobile (hamburger).
- Use `label` for display, `route_path` for navigation, `icon` if desired.
- If a menu later has multiple permissions and `permission_logic=ALL`, the user must have all to see it; current seed uses ANY.
- Gate page-level and button-level actions with the same permission codes (e.g., `orders:edit`).

## Extending
- New menu: insert into `app_menu` (set `menu_key`, `parent_id` if nested, `route_path`) and map view permission via `menu_permission`.
- New actions: add `permission` code (e.g., `orders:approve`), map in `role_permission`, attach to menu if it controls visibility.
- Multi-app: set `app_key` on menus and pass `appKey` in the tree call to filter per app.
