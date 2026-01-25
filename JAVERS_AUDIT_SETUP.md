# JaVers Auditing Setup - RMS Service Project

## Overview

JaVers auditing has been successfully configured in the rms-service project to provide comprehensive audit trails for all entity changes. This implementation tracks:

- **What changed**: Field-level changes with before/after values
- **Who made the change**: User identification via Spring Security
- **When it changed**: Timestamp of each change
- **Complete history**: Full audit trail with snapshots at each point in time

## Components Added/Updated

### 1. Dependencies (`pom.xml`)

Updated JaVers to version **7.10.0** (compatible with Spring Boot 4.1.0 and Java 25):

```xml
<javers.version>7.10.0</javers.version>

<dependency>
    <groupId>org.javers</groupId>
    <artifactId>javers-spring-boot-starter-sql</artifactId>
    <version>${javers.version}</version>
</dependency>
<dependency>
    <groupId>org.javers</groupId>
    <artifactId>javers-core</artifactId>
    <version>${javers.version}</version>
</dependency>
```

### 2. Configuration Files

#### `SpringSecurityAuditorAware.java` (NEW)
- Implements `AuditorAware<String>` for JPA auditing
- Integrates with Spring Security to capture current user
- Used by both JPA auditing and JaVers

#### `JaversAuthorProvider.java` (UPDATED)
- Already existed but was updated to work properly
- Provides current user for JaVers audit entries
- Falls back to "system" if no user is authenticated

#### `JaversConfiguration.java` (NEW)
- Configures JaVers with custom AuthorProvider
- Makes JaversAuthorProvider the primary bean
- Spring Boot starter auto-configures the rest

#### `JdbcRoutingDataSourceConfig.java` (UPDATED)
- Added `@EnableJpaRepositories` annotation
- Added `@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")`
- Enables automatic population of audit fields

### 3. Domain Entities

#### `AbstractAuditingEntity.java` (UPDATED)
Enhanced with JPA auditing annotations:
- `@MappedSuperclass` - Makes it a JPA mapped superclass
- `@EntityListeners(AuditingEntityListener.class)` - Enables JPA auditing
- `@CreatedBy` and `@LastModifiedBy` - Auto-populated by Spring Security
- `@CreatedDate` and `@LastModifiedDate` - Auto-populated timestamps
- Kept `@DiffIgnore` annotation for JaVers

#### Repositories with JaVers Auditing
The following repositories are annotated with `@JaversSpringDataAuditable`:
- `OrderRepository`
- `RestaurantRepository`
- `MenuItemRepository`
- `UserRepository`

All save/update/delete operations on these repositories are automatically audited.

### 4. Database Schema

#### Liquibase Changelog: `20250125000001_create_javers_tables.xml`

Creates four JaVers tables:

1. **`jv_global_id`**: Stores entity identifiers
   - `global_id_pk`: Primary key
   - `local_id`: Entity ID
   - `type_name`: Entity class name
   - `fragment`: For value objects
   - `owner_id_fk`: For owned entities

2. **`jv_commit`**: Stores commit metadata
   - `commit_pk`: Primary key
   - `author`: Username who made the change
   - `commit_date`: Timestamp
   - `commit_id`: Sequential commit ID

3. **`jv_commit_property`**: Stores additional commit properties
   - `commit_fk`: Foreign key to jv_commit
   - `property_name`: Property key
   - `property_value`: Property value

4. **`jv_snapshot`**: Stores entity state snapshots
   - `snapshot_pk`: Primary key
   - `type`: Snapshot type (INITIAL, UPDATE, etc.)
   - `version`: Version number
   - `state`: JSON representation of entity state
   - `changed_properties`: List of changed properties
   - `global_id_fk`: Foreign key to jv_global_id
   - `commit_fk`: Foreign key to jv_commit

### 5. REST API (`AuditResource.java`)

Provides endpoints to query audit history:

#### Get Entity Changes
```
GET /api/audit/changes/{entityClass}/{entityId}?limit=100
```
Example: `GET /api/audit/changes/Order/123e4567-e89b-12d3-a456-426614174000`

Returns all changes for a specific entity.

#### Get All Changes for Entity Type
```
GET /api/audit/changes/{entityClass}?limit=100
```
Example: `GET /api/audit/changes/Restaurant`

Returns all changes for all entities of a type.

#### Get Changes by Author
```
GET /api/audit/changes/by-author/{author}?limit=100
```
Example: `GET /api/audit/changes/by-author/admin`

Returns all changes made by a specific user.

#### Get Changes by Date Range
```
GET /api/audit/changes/by-date?from=2025-01-01T00:00:00&to=2025-01-31T23:59:59&limit=100
```

Returns changes within a date range.

#### Get Historical States (Shadows)
```
GET /api/audit/shadows/{entityClass}/{entityId}?limit=100
```
Example: `GET /api/audit/shadows/Order/123e4567-e89b-12d3-a456-426614174000`

Returns complete historical states of an entity at each point in time.

#### Get Latest Snapshot
```
GET /api/audit/latest/{entityClass}/{entityId}
```
Example: `GET /api/audit/latest/Restaurant/123e4567-e89b-12d3-a456-426614174000`

Returns the most recent snapshot of an entity.

### 6. Application Configuration (`application.yml`)

```yaml
javers:
  sqlSchemaManagementEnabled: false  # Liquibase manages schema
  prettyPrint: true                  # Pretty JSON in snapshots
  typeSafeValues: false
  commitIdGenerator: synchronized_sequence
  packagesToScan: com.atparui.rmsservice.domain
  listCompareAlgorithm: LEVENSHTEIN_DISTANCE
  mappingStyle: FIELD
  newObjectSnapshot: true            # Capture initial state
```

## How It Works

### Automatic Auditing

1. **Repository Operations**: Any save/update/delete on annotated repositories is automatically audited
2. **User Tracking**: Spring Security context is used to identify the user
3. **Change Detection**: JaVers compares entity states and stores only what changed
4. **Snapshot Storage**: Complete entity state is stored in JSON format

### Example Flow

```java
// User updates an order
Order order = orderRepository.findById(orderId).get();
order.setStatus(OrderStatus.COMPLETED);
orderRepository.save(order);  // Automatically audited!
```

JaVers will:
1. Capture the current user from Spring Security
2. Compare old and new order state
3. Create a commit record with author and timestamp
4. Store a snapshot with changed fields
5. Link everything together in the database

### Querying Audit History

```java
// In a service or controller
@Autowired
private Javers javers;

// Get all changes for an order
List<CdoSnapshot> changes = javers.findSnapshots(
    QueryBuilder.byInstanceId(orderId, Order.class).build()
);

// Get changes by user
List<CdoSnapshot> userChanges = javers.findSnapshots(
    QueryBuilder.anyDomainObject().byAuthor("admin").build()
);

// Get historical state
List<Shadow<Order>> history = javers.findShadows(
    QueryBuilder.byInstanceId(orderId, Order.class).build()
);
```

## Audited Entities

The following key entities are automatically audited:
- ✅ Order
- ✅ Restaurant
- ✅ MenuItem
- ✅ User
- ✅ All entities extending AbstractAuditingEntity

Additional repositories can be annotated with `@JaversSpringDataAuditable` to enable auditing.

## Multi-Tenancy Support

JaVers works seamlessly with the multi-tenant architecture:
- Each tenant's audit data is stored in their own database
- Tenant context is automatically handled by the routing datasource
- Audit queries are isolated per tenant

## Testing the Setup

### 1. Start the Application
```bash
./mvnw spring-boot:run
```

### 2. Verify Tables Created
Check that JaVers tables exist in your database:
```sql
SELECT * FROM jv_commit;
SELECT * FROM jv_snapshot;
SELECT * FROM jv_global_id;
```

### 3. Make a Change
Create or update an order through the API.

### 4. Query Audit History
```bash
# Get changes for order with specific UUID
curl http://localhost:8081/api/audit/changes/Order/123e4567-e89b-12d3-a456-426614174000

# Get all changes by admin user
curl http://localhost:8081/api/audit/changes/by-author/admin

# Get historical states
curl http://localhost:8081/api/audit/shadows/Order/123e4567-e89b-12d3-a456-426614174000
```

## Benefits

1. **Compliance**: Full audit trail for regulatory requirements (PCI-DSS, GDPR, etc.)
2. **Debugging**: See exactly what changed and when
3. **Accountability**: Track who made each change
4. **Recovery**: Restore previous states if needed
5. **Analytics**: Analyze change patterns over time
6. **Dispute Resolution**: Resolve order/payment disputes with complete history

## Performance Considerations

- JaVers stores snapshots efficiently using JSON
- Indexes on key columns ensure fast queries
- Only changed fields are tracked
- Works efficiently with UUID primary keys
- Automatic cleanup strategies can be configured if needed

## Version Compatibility

- **JaVers**: 7.10.0 (latest)
- **Spring Boot**: 4.1.0
- **Java**: 25
- **PostgreSQL**: 12+

## Future Enhancements

1. **UI Dashboard**: Create a web interface to visualize audit history
2. **Notifications**: Alert on specific changes (e.g., order cancellations)
3. **Rollback**: Implement entity state restoration
4. **Export**: Generate audit reports in PDF/Excel
5. **Retention Policy**: Auto-archive old audit data
6. **Real-time Audit Stream**: WebSocket notifications for changes

## Troubleshooting

### Issue: No audit records created
- Verify repository has `@JaversSpringDataAuditable` annotation
- Check that user is authenticated (Spring Security context)
- Ensure Liquibase migrations ran successfully
- Check application logs for errors

### Issue: Author is null
- Verify `SpringSecurityAuditorAware` bean is registered
- Check Spring Security configuration
- Ensure user is authenticated before making changes
- Verify `JaversAuthorProvider` is working

### Issue: Performance degradation
- Add database indexes on frequently queried columns
- Consider archiving old audit data
- Adjust `limit` parameter in queries
- Review query patterns and optimize

### Issue: Multi-tenant data leakage
- Verify tenant context is properly set
- Check routing datasource configuration
- Ensure tenant isolation in queries

## References

- [JaVers Documentation](https://javers.org/documentation/)
- [Spring Boot Integration](https://javers.org/documentation/spring-boot-integration/)
- [JaVers GitHub](https://github.com/javers/javers)
- [JaVers Query Examples](https://javers.org/documentation/repository-examples/)

## Support

For issues or questions about JaVers auditing:
1. Check the JaVers documentation
2. Review the configuration files
3. Check application logs for errors
4. Verify database schema is correct
5. Test with simple entities first before complex ones
