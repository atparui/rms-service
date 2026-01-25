package com.atparui.rmsservice.web.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for querying audit history using JaVers.
 * 
 * Provides endpoints to:
 * - Get complete change history for any entity
 * - Get snapshots (state at specific points in time)
 * - Get changes by author
 * - Get changes within date ranges
 */
@RestController
@RequestMapping("/api/audit")
public class AuditResource {

    private static final Logger log = LoggerFactory.getLogger(AuditResource.class);

    private final Javers javers;

    public AuditResource(Javers javers) {
        this.javers = javers;
    }

    /**
     * Get audit history for a specific entity by class and ID.
     * 
     * @param entityClass the entity class name (e.g., "Order", "Restaurant")
     * @param entityId the entity ID
     * @param limit maximum number of changes to return (default: 100)
     * @return list of changes
     */
    @GetMapping("/changes/{entityClass}/{entityId}")
    public ResponseEntity<List<CdoSnapshot>> getEntityChanges(
        @PathVariable String entityClass,
        @PathVariable String entityId,
        @RequestParam(defaultValue = "100") int limit
    ) {
        try {
            Class<?> clazz = Class.forName("com.atparui.rmsservice.domain." + entityClass);
            
            List<CdoSnapshot> changes = javers
                .findSnapshots(QueryBuilder.byInstanceId(entityId, clazz).limit(limit).build());
            
            log.debug("Found {} changes for {} with id {}", changes.size(), entityClass, entityId);
            return ResponseEntity.ok(changes);
        } catch (ClassNotFoundException e) {
            log.error("Entity class not found: {}", entityClass, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all changes for a specific entity type.
     * 
     * @param entityClass the entity class name
     * @param limit maximum number of changes to return (default: 100)
     * @return list of changes
     */
    @GetMapping("/changes/{entityClass}")
    public ResponseEntity<List<CdoSnapshot>> getAllChangesForEntity(
        @PathVariable String entityClass,
        @RequestParam(defaultValue = "100") int limit
    ) {
        try {
            Class<?> clazz = Class.forName("com.atparui.rmsservice.domain." + entityClass);
            
            List<CdoSnapshot> changes = javers
                .findSnapshots(QueryBuilder.byClass(clazz).limit(limit).build());
            
            log.debug("Found {} changes for {}", changes.size(), entityClass);
            return ResponseEntity.ok(changes);
        } catch (ClassNotFoundException e) {
            log.error("Entity class not found: {}", entityClass, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get changes made by a specific author.
     * 
     * @param author the username/author
     * @param limit maximum number of changes to return (default: 100)
     * @return list of changes
     */
    @GetMapping("/changes/by-author/{author}")
    public ResponseEntity<List<CdoSnapshot>> getChangesByAuthor(
        @PathVariable String author,
        @RequestParam(defaultValue = "100") int limit
    ) {
        List<CdoSnapshot> changes = javers
            .findSnapshots(QueryBuilder.anyDomainObject().byAuthor(author).limit(limit).build());
        
        log.debug("Found {} changes by author {}", changes.size(), author);
        return ResponseEntity.ok(changes);
    }

    /**
     * Get changes within a date range.
     * 
     * @param from start date (ISO format)
     * @param to end date (ISO format)
     * @param limit maximum number of changes to return (default: 100)
     * @return list of changes
     */
    @GetMapping("/changes/by-date")
    public ResponseEntity<List<CdoSnapshot>> getChangesByDateRange(
        @RequestParam String from,
        @RequestParam String to,
        @RequestParam(defaultValue = "100") int limit
    ) {
        try {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            LocalDateTime toDate = LocalDateTime.parse(to);
            
            List<CdoSnapshot> changes = javers
                .findSnapshots(
                    QueryBuilder
                        .anyDomainObject()
                        .from(fromDate)
                        .to(toDate)
                        .limit(limit)
                        .build()
                );
            
            log.debug("Found {} changes between {} and {}", changes.size(), from, to);
            return ResponseEntity.ok(changes);
        } catch (Exception e) {
            log.error("Error parsing dates: from={}, to={}", from, to, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get shadows (historical states) for a specific entity.
     * Shadows reconstruct the complete object state at each point in history.
     * 
     * @param entityClass the entity class name
     * @param entityId the entity ID
     * @param limit maximum number of shadows to return (default: 100)
     * @return list of historical states
     */
    @GetMapping("/shadows/{entityClass}/{entityId}")
    public ResponseEntity<List<Object>> getEntityShadows(
        @PathVariable String entityClass,
        @PathVariable String entityId,
        @RequestParam(defaultValue = "100") int limit
    ) {
        try {
            Class<?> clazz = Class.forName("com.atparui.rmsservice.domain." + entityClass);
            
            List<Shadow<Object>> shadows = javers
                .findShadows(QueryBuilder.byInstanceId(entityId, clazz).limit(limit).build());
            
            List<Object> objects = shadows.stream()
                .map(Shadow::get)
                .collect(Collectors.toList());
            
            log.debug("Found {} shadows for {} with id {}", objects.size(), entityClass, entityId);
            return ResponseEntity.ok(objects);
        } catch (ClassNotFoundException e) {
            log.error("Entity class not found: {}", entityClass, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get the latest snapshot for a specific entity.
     * 
     * @param entityClass the entity class name
     * @param entityId the entity ID
     * @return the latest snapshot
     */
    @GetMapping("/latest/{entityClass}/{entityId}")
    public ResponseEntity<CdoSnapshot> getLatestSnapshot(
        @PathVariable String entityClass,
        @PathVariable String entityId
    ) {
        try {
            Class<?> clazz = Class.forName("com.atparui.rmsservice.domain." + entityClass);
            
            List<CdoSnapshot> snapshots = javers
                .findSnapshots(QueryBuilder.byInstanceId(entityId, clazz).limit(1).build());
            
            if (snapshots.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(snapshots.get(0));
        } catch (ClassNotFoundException e) {
            log.error("Entity class not found: {}", entityClass, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
