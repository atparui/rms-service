# RMS-Demo Manual Setup Plan

## Goal

Stabilise **rms-demo** by manually creating its tenant database and tables (bypassing the broken tenant-creation flow). Once rms-demo works end-to-end, the full tenant lifecycle can be tested later (create tenant → provision DB → run Liquibase).

---

## 1. Naming convention (all tenants)

- **Database name:** `{3-letter platform prefix}-{tenant_key}` (hyphen, not underscore).  
  For rms-demo: **`rms-rms-demo`**.
- **Schema:** `public` (default).
- **DB user (role):** Use a quoted identifier for hyphenated names, e.g. **`rms-rms-demo`** (in SQL: `"rms-rms-demo"`).

---

## 2. Default rms-service database (out of scope for this step)

- The **default** rms-service database (e.g. `rms_service` or similar) is only for bootstrap/default use.
- You have removed all `jhi_`-prefixed tables; that default DB will be **reconstructed later** as needed.
- This plan does **not** change the default DB.

---

## 3. Fix rms-service TransactionManager (done)

- **Issue:** `NoUniqueBeanDefinitionException` – two beans: `transactionManager`, `jdbcTransactionManager`.
- **Fix:** Mark the JPA `transactionManager` bean as `@Primary` in `JdbcRoutingDataSourceConfig` so `@Transactional` (e.g. in `UserProvisioningAuthSuccessListener`) uses it.  
  This is already applied.

---

## 4. Create rms-rms-demo database and user (manual)

Run as a **superuser** (e.g. `postgres`) against the Postgres instance that rms-service and TMS use (e.g. `db:5432` in Docker, or `localhost` in dev).

**Option A – SQL script (run in psql or pgAdmin):**

See **`rms-service/scripts/create-rms-rms-demo-database.sql`**. It:

1. Creates user **`rms-rms-demo`** (quoted identifier) with password matching platform env (e.g. `AzBy791833!`; change in production).
2. Creates database **`rms-rms-demo`** with owner `rms-rms-demo`.
3. Connects to `rms-rms-demo` and grants schema `public` so Liquibase can create tables.

Run it with `psql -U postgres -f scripts/create-rms-rms-demo-database.sql` (or from pgAdmin: run the CREATE USER and CREATE DATABASE, then connect to `rms-rms-demo` and run the GRANT statements).

**Option B – One-liner (adjust password):**

```sql
CREATE USER "rms-rms-demo" WITH PASSWORD 'AzBy791833!';
CREATE DATABASE "rms-rms-demo" OWNER "rms-rms-demo";
-- Then connect to "rms-rms-demo" and run the GRANTs and Liquibase (step 5).
```

---

## 5. Create tables in rms-rms-demo (Liquibase)

After the database and user exist:

1. Point Liquibase at the new DB (same changelogs as rms-service, no `jhi_` removal for now so that `UserService` / `UserProvisioningAuthSuccessListener` keep working with `jhi_user`).

2. **From rms-service project** (with DB admin or `rms-rms-demo` credentials):

   ```bash
   cd /path/to/rms-service
   # Override URL to target rms-rms-demo (change host/port if needed)
   mvn liquibase:update \
     -Dliquibase-plugin.url=jdbc:postgresql://db:5432/rms-rms-demo \
     -Dliquibase-plugin.username=rms-rms-demo \
     -Dliquibase-plugin.password=AzBy791833!
   ```

   Or set in `application.yml` / env and run the app once so Liquibase runs on that URL (if you use a profile that points to `rms-rms-demo`).

3. **Alternative – run Liquibase from this chat:**  
   If you prefer not to run Maven, we can add a **standalone SQL script** that creates the same tables as the Liquibase changelogs (no `jhi_` if you prefer). That would be a larger script and must be kept in sync with Liquibase. Recommended: run Liquibase against `jdbc:postgresql://host:5432/rms-rms-demo` as above.

---

## 6. Align tenant-management-service with rms-rms-demo

TMS must point the **rms-demo** tenant at the new database:

- **database_name:** `rms-rms-demo`
- **database_url:** `jdbc:postgresql://db:5432/rms-rms-demo`
- **database_username:** `rms-rms-demo`
- **database_password:** match platform env (e.g. `AzBy791833!`)
- **schema_name:** `public`

If the rms-demo tenant row already exists (e.g. from seed) with `rms_demo` / `rms_demo`, update it to the above. Run the one-off SQL in **`tenant-management-service/docs/UPDATE_RMS_DEMO_TO_RMS_RMS_DEMO.sql`** against the TMS database (e.g. `tenant-service`), or run that UPDATE manually in pgAdmin.

---

## 7. Order of operations (summary)

| Step | Action |
|------|--------|
| 1 | Fix TransactionManager in rms-service (**done**). |
| 2 | Create DB and user: run `create-rms-rms-demo-database.sql` (or equivalent) as Postgres superuser. |
| 3 | Run Liquibase for rms-service against `jdbc:postgresql://host:5432/rms-rms-demo` with user `rms-rms-demo`. |
| 4 | Update TMS tenant row for rms-demo to use `rms-rms-demo` DB and `rms-rms-demo` user. |
| 5 | Restart rms-service and tenant-management-service; test rms-demo (login, menu, etc.). |
| 6 | Later: drop rms-demo tenant and re-test full tenant-creation flow. |

---

## 8. Clarifications (if needed)

- **Host for DB:** In Docker it’s usually `db` (service name); locally it may be `localhost`. Use the same host TMS and rms-service use.
- **Passwords:** Replace the dev password with a strong one in non-dev.
- **jhi_ tables:** Kept in tenant DB for now so existing user-provisioning and auth listener keep working. When you reconstruct the default DB and/or drop `jhi_` everywhere, we can add a tenant-only Liquibase master that omits `00000000000000_initial_schema.xml` and add a separate path for tenant schemas.
