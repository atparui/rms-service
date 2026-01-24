# App Menu & Permission CRUD – Routes for UI

## Entities & Endpoints
- App menus: `/api/app-menus`
- Permissions: `/api/permissions`
- Role↔Permission map: `/api/role-permissions`
- Menu↔Permission map: `/api/menu-permissions`
- Menu tree (filtered by user roles/permissions): `/api/app-menus/tree?appKey=`

## DTO Shapes (high level)
- AppMenuDTO: `id`, `menuKey`, `label`, `type`, `routePath`, `icon?`, `sortOrder?`, `isActive?`, `permissionLogic` (ANY/ALL), `appKey?`, `parentId?`
- PermissionDTO: `id`, `code` (e.g., `orders:view`), `description?`, `scope?`, `isActive?`
- RolePermissionDTO: `id`, `role`, `permissionId`, `isActive?`
- MenuPermissionDTO: `id`, `appMenuId`, `permissionId`

## CRUD Routes
- App menus:
  - `GET /api/app-menus?page=&size=` list (paged)
  - `GET /api/app-menus/{id}` detail
  - `POST /api/app-menus` create
  - `PUT /api/app-menus/{id}` update
  - `PATCH /api/app-menus/{id}` partial update
  - `DELETE /api/app-menus/{id}` delete

- Permissions:
  - `GET /api/permissions?page=&size=`
  - `GET /api/permissions/{id}`
  - `POST /api/permissions`
  - `PUT /api/permissions/{id}`
  - `PATCH /api/permissions/{id}`
  - `DELETE /api/permissions/{id}`

- Role permissions:
  - `GET /api/role-permissions?page=&size=`
  - `GET /api/role-permissions/{id}`
  - `POST /api/role-permissions`
  - `PUT /api/role-permissions/{id}`
  - `PATCH /api/role-permissions/{id}`
  - `DELETE /api/role-permissions/{id}`

- Menu permissions:
  - `GET /api/menu-permissions?page=&size=`
  - `GET /api/menu-permissions/{id}`
  - `POST /api/menu-permissions`
  - `PUT /api/menu-permissions/{id}`
  - `PATCH /api/menu-permissions/{id}`
  - `DELETE /api/menu-permissions/{id}`

- Menu tree for rendering:
  - `GET /api/app-menus/tree?appKey=` (appKey optional; filters by current user permissions)

## UI Page Ideas
- Menus screen:
  - List + tree view of `app_menu` (sort by `sortOrder`); show parent/child; actions: create/edit/delete; set `permissionLogic` (ANY/ALL), `appKey`, `parentId`, `routePath`, `icon`.
- Permissions screen:
  - CRUD `permission`; show code, scope, active flag.
- Role→Permission screen:
  - For each role, list mapped permission codes; add/remove mappings.
- Menu→Permission screen:
  - For each menu item, attach one or more permissions (controls visibility); set logic on menu if multiple.
- Menu Tree consumer:
  - Call `/tree` once after login; cache; build sidebar/hamburger from `children`; hide anything not returned.

## Notes
- Backend already filters menu tree by current user’s permissions.
- Use permission codes (e.g., `orders:edit`) to gate page actions/buttons client-side.
- Seeded routes are placeholders; adjust UI routes if your paths differ.***
