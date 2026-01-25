# JHipster JDL Files Usage Guide

## Overview

This directory contains JHipster Domain Language (JDL) files that define all entities for the Restaurant Management System. These files are used to generate:

- **Entity Classes**: Domain models with R2DBC annotations
- **Repository Interfaces**: Reactive R2DBC repositories
- **Service Interfaces & Implementations**: Business logic layer
- **REST Controllers**: Reactive WebFlux endpoints
- **DTOs**: Data Transfer Objects with MapStruct mappers
- **Liquibase Changelogs**: Database migration scripts
- **Tests**: Unit and integration tests

## File Structure

```
jdl/
├── rms-entities.jdl          # Main JDL file (all entities in one file)
├── 01-restaurant-branch.jdl   # Restaurant & Branch entities
├── 02-user-management.jdl    # User management entities
├── 03-table-management.jdl  # Table management entities
├── 04-menu-management.jdl   # Menu management entities
├── 05-customer-management.jdl # Customer management entities
├── 06-order-management.jdl   # Order management entities
├── 07-billing-payments.jdl   # Billing & payment entities
└── JDL_USAGE_GUIDE.md        # This file
```

## Prerequisites

1. **JHipster CLI** installed globally:

   ```bash
   npm install -g generator-jhipster
   ```

2. **Node.js** (v18 or higher)

3. **Java 25** and **Maven** configured

4. **PostgreSQL** database running (for Liquibase migrations)

## Usage

### Option 1: Import All Entities at Once (Recommended)

Use the main JDL file that contains all entities:

```bash
cd <project-root>
jhipster import-jdl jdl/rms-entities.jdl
```

This will:

- Generate all 28 entities
- Create all relationships
- Generate DTOs, services, controllers
- Create Liquibase changelogs
- Generate tests

### Option 2: Import Entities by Module

If you want to generate entities incrementally:

```bash
# Step 1: Restaurant & Branch
jhipster import-jdl jdl/01-restaurant-branch.jdl

# Step 2: User Management
jhipster import-jdl jdl/02-user-management.jdl

# Step 3: Table Management
jhipster import-jdl jdl/03-table-management.jdl

# Step 4: Menu Management
jhipster import-jdl jdl/04-menu-management.jdl

# Step 5: Customer Management
jhipster import-jdl jdl/05-customer-management.jdl

# Step 6: Order Management
jhipster import-jdl jdl/06-order-management.jdl

# Step 7: Billing & Payments
jhipster import-jdl jdl/07-billing-payments.jdl
```

**Note**: When importing incrementally, make sure to import in order as entities have dependencies.

### Option 3: Generate Single Entity

To generate a single entity:

```bash
jhipster entity <EntityName>
```

Then follow the interactive prompts.

## What Gets Generated

For each entity, JHipster generates:

### 1. Domain Entity (`domain/EntityName.java`)

- R2DBC annotations (`@Table`, `@Column`, `@Id`)
- Extends `AbstractAuditingEntity` (if applicable)
- Implements `Persistable<UUID>`
- Validation annotations
- Getters/setters
- `equals()` and `hashCode()`

### 2. Repository (`repository/EntityNameRepository.java`)

- Extends `ReactiveCrudRepository<EntityName, UUID>`
- Custom query methods (if needed)

### 3. DTO (`service/dto/EntityNameDTO.java`)

- Data Transfer Object for API responses
- MapStruct annotations

### 4. Mapper (`service/mapper/EntityNameMapper.java`)

- MapStruct mapper interface
- Converts between Entity and DTO

### 5. Service Interface (`service/EntityNameService.java`)

- Business logic interface
- Reactive methods returning `Mono` or `Flux`

### 6. Service Implementation (`service/impl/EntityNameServiceImpl.java`)

- Service implementation
- Uses reactive repositories

### 7. REST Controller (`web/rest/EntityNameResource.java`)

- Reactive WebFlux controller
- CRUD endpoints
- Pagination support
- Search endpoints (if Elasticsearch enabled)

### 8. Liquibase Changelog (`resources/config/liquibase/changelog/YYYYMMDDHHMMSS_added_entity_EntityName.xml`)

- Database table creation
- Indexes
- Constraints
- Foreign keys

### 9. Tests

- `EntityNameTest.java` - Entity unit tests
- `EntityNameResourceIT.java` - Integration tests
- `EntityNameServiceIT.java` - Service integration tests

## Generated Features

### DTOs with MapStruct

All entities have DTOs with MapStruct mappers for:

- Entity → DTO conversion
- DTO → Entity conversion
- List conversions

### Service Layer

All entities have:

- Service interfaces
- Service implementations
- Reactive methods (`Mono`, `Flux`)

### Pagination

The following entities have pagination enabled:

- Restaurant
- Branch
- RmsUser
- UserBranchRole
- BranchTable
- Shift
- MenuCategory
- MenuItem
- Customer
- Order
- Bill
- Payment

### Elasticsearch Search

The following entities have Elasticsearch search enabled:

- Restaurant
- Branch
- RmsUser
- BranchTable
- MenuCategory
- MenuItem
- Customer
- Order
- Bill
- Payment

## Important Notes

### 1. Entity Naming

- `RmsUser` is used instead of `User` to avoid conflict with JHipster's built-in User entity
- All entity names use PascalCase

### 2. Field Naming

- Database columns use snake_case (e.g., `created_at`)
- Java fields use camelCase (e.g., `createdAt`)
- JHipster handles the conversion automatically

### 3. Relationships

- All relationships are `ManyToOne` (R2DBC limitation)
- Junction tables (like `UserBranchRole`) are separate entities
- Foreign keys are managed through `@Column` annotations

### 4. Audit Fields

- `createdAt`, `updatedAt`, `createdBy`, `updatedBy` are inherited from `AbstractAuditingEntity`
- These are NOT defined in JDL (handled by base class)

### 5. UUID Primary Keys

- All entities use UUID as primary key
- JHipster generates this automatically for R2DBC entities

### 6. Liquibase Changelogs

- Changelogs are generated automatically
- They are tracked in `src/main/resources/config/liquibase/changelog/`
- Master changelog is updated automatically

## Customization After Generation

After generating entities, you may need to:

### 1. Add Custom Methods to Repositories

```java
public interface EntityNameRepository extends ReactiveCrudRepository<EntityName, UUID> {
  Flux<EntityName> findByBranchIdAndIsActive(UUID branchId, Boolean isActive);
}

```

### 2. Add Business Logic to Services

```java
public Mono<EntityName> customBusinessMethod(UUID id) {
  // Custom logic
}

```

### 3. Add Custom Endpoints to Controllers

```java
@GetMapping("/custom-endpoint")
public Mono<ResponseEntity<EntityNameDTO>> customEndpoint() {
  // Custom endpoint
}

```

### 4. Modify Liquibase Changelogs

- Add custom indexes
- Add check constraints
- Add triggers (if needed)

## Regenerating Entities

To regenerate an entity after modifying the JDL:

```bash
jhipster import-jdl jdl/rms-entities.jdl
```

JHipster will:

- Ask if you want to overwrite existing files
- Preserve custom code if you choose not to overwrite
- Update Liquibase changelogs

## Troubleshooting

### Issue: Entity not found after import

**Solution**: Make sure you imported entities in the correct order (dependencies first)

### Issue: Foreign key relationships not working

**Solution**: R2DBC doesn't support JPA-style relationships. Use manual joins in repositories.

### Issue: Liquibase errors

**Solution**:

1. Check database connection
2. Verify Liquibase changelog master file
3. Check for duplicate changelog entries

### Issue: MapStruct compilation errors

**Solution**:

1. Run `mvn clean compile`
2. Ensure MapStruct processor is configured in `pom.xml`

## Best Practices

1. **Version Control**: Commit JDL files to version control
2. **Incremental Generation**: Generate entities incrementally for large projects
3. **Review Generated Code**: Always review generated code before committing
4. **Custom Code**: Place custom code in separate methods/files to avoid overwrites
5. **Testing**: Run tests after generating entities
6. **Documentation**: Update API documentation after adding new entities

## Next Steps

After generating entities:

1. **Review Generated Code**: Check entity classes, DTOs, services
2. **Run Tests**: Execute `mvn test` to verify generation
3. **Update Application Properties**: Configure database, Elasticsearch if needed
4. **Create Initial Data**: Add Liquibase data changelogs for reference data
5. **Implement Business Logic**: Add custom business methods to services
6. **Add Security**: Configure role-based access control for endpoints
7. **API Documentation**: Update Swagger/OpenAPI documentation

## Gateway Integration

Since the gateway creates database schemas using Liquibase API:

1. **Export Liquibase Changelogs**: The generated changelogs can be exported
2. **Gateway API**: Gateway will call Liquibase API to create schema
3. **Service Ready**: Once schema is created, service can use entities

## References

- [JHipster JDL Documentation](https://www.jhipster.tech/jdl/)
- [JHipster Creating an Entity](https://www.jhipster.tech/creating-an-entity/)
- [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc)
- [MapStruct Documentation](https://mapstruct.org/documentation/stable/reference/html/)
