package dtu.k30.msc.whm.web.rest;

import dtu.k30.msc.whm.config.dbmigrations.ManualMigrationScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing database migrations.
 */
@RestController
@RequestMapping("/api/migration")
@CrossOrigin(origins = "*")  // Allow all origins for migration endpoint
public class MigrationResource {

    private final Logger log = LoggerFactory.getLogger(MigrationResource.class);

    @Autowired
    private ManualMigrationScript manualMigrationScript;

    /**
     * POST /api/migration/dbref-to-embedded : Run DBRef to Embedded migration.
     *
     * @return the ResponseEntity with status 200 (OK) and migration result
     */
    @PostMapping("/dbref-to-embedded")
    public ResponseEntity<Map<String, Object>> runDBRefToEmbeddedMigration() {
        log.info("REST request to run DBRef to Embedded migration");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            manualMigrationScript.migrateDBRefToEmbedded();
            
            result.put("success", true);
            result.put("message", "Migration completed successfully");
            result.put("timestamp", java.time.Instant.now());
            
            log.info("Migration completed successfully");
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Migration failed: {}", e.getMessage(), e);
            
            result.put("success", false);
            result.put("message", "Migration failed: " + e.getMessage());
            result.put("timestamp", java.time.Instant.now());
            
            return ResponseEntity.internalServerError().body(result);
        }
    }
} 