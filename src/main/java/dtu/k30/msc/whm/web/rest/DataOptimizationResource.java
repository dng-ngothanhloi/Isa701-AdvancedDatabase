package dtu.k30.msc.whm.web.rest;

import dtu.k30.msc.whm.service.DataCompressionService;
import dtu.k30.msc.whm.service.DataArchivalService;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for managing data optimization operations.
 * Provides endpoints for compression and archival management.
 */
@RestController
@RequestMapping("/api/data-optimization")
@CrossOrigin(origins = "*")
public class DataOptimizationResource {

    private static final Logger log = LoggerFactory.getLogger(DataOptimizationResource.class);

    @Autowired
    private DataCompressionService compressionService;

    @Autowired
    private DataArchivalService archivalService;

    /**
     * Configure compression for all collections.
     */
    @PostMapping("/compression/configure")
    public ResponseEntity<Map<String, Object>> configureCompression() {
        log.info("REST request to configure compression");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            compressionService.configureCompression();
            compressionService.optimizeIndexesForCompression();
            
            response.put("success", true);
            response.put("message", "Compression configured successfully");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to configure compression", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get compression statistics and metrics.
     */
    @GetMapping("/compression/stats")
    public ResponseEntity<Document> getCompressionStats() {
        log.info("REST request to get compression statistics");
        
        try {
            Document stats = compressionService.getCompressionStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Failed to get compression stats", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Analyze compression effectiveness.
     */
    @GetMapping("/compression/analysis")
    public ResponseEntity<Document> analyzeCompression() {
        log.info("REST request to analyze compression effectiveness");
        
        try {
            Document analysis = compressionService.analyzeCompressionEffectiveness();
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            log.error("Failed to analyze compression", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Monitor compression performance.
     */
    @PostMapping("/compression/monitor")
    public ResponseEntity<Map<String, Object>> monitorCompression() {
        log.info("REST request to monitor compression performance");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            compressionService.monitorCompressionPerformance();
            
            response.put("success", true);
            response.put("message", "Compression monitoring completed");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to monitor compression", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Archive old documents.
     */
    @PostMapping("/archival/execute")
    public ResponseEntity<Map<String, Object>> executeArchival() {
        log.info("REST request to execute archival");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            archivalService.archiveOldPhieuNhapXuat();
            archivalService.archiveOldChiTietNhapXuat();
            
            response.put("success", true);
            response.put("message", "Archival executed successfully");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to execute archival", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Configure compression for archived collections.
     */
    @PostMapping("/archival/configure-compression")
    public ResponseEntity<Map<String, Object>> configureArchivedCompression() {
        log.info("REST request to configure archived collections compression");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            archivalService.configureArchivedCollectionsCompression();
            
            response.put("success", true);
            response.put("message", "Archived collections compression configured");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to configure archived compression", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get archival statistics.
     */
    @GetMapping("/archival/stats")
    public ResponseEntity<Document> getArchivalStats() {
        log.info("REST request to get archival statistics");
        
        try {
            Document stats = archivalService.getArchivalStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Failed to get archival stats", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Monitor archival performance.
     */
    @PostMapping("/archival/monitor")
    public ResponseEntity<Map<String, Object>> monitorArchival() {
        log.info("REST request to monitor archival performance");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            archivalService.monitorArchivalPerformance();
            
            response.put("success", true);
            response.put("message", "Archival monitoring completed");
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to monitor archival", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Restore archived document.
     */
    @PostMapping("/archival/restore")
    public ResponseEntity<Map<String, Object>> restoreArchivedDocument(
            @RequestParam String documentId,
            @RequestParam String collectionType) {
        log.info("REST request to restore archived document: {} from {}", documentId, collectionType);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            archivalService.restoreArchivedDocuments(documentId, collectionType);
            
            response.put("success", true);
            response.put("message", "Document restored successfully");
            response.put("documentId", documentId);
            response.put("collectionType", collectionType);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to restore archived document", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("documentId", documentId);
            response.put("collectionType", collectionType);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Get combined optimization statistics.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCombinedStats() {
        log.info("REST request to get combined optimization statistics");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Document compressionStats = compressionService.getCompressionStats();
            Document archivalStats = archivalService.getArchivalStats();
            
            response.put("compression", compressionStats);
            response.put("archival", archivalStats);
            response.put("timestamp", System.currentTimeMillis());
            
            // Calculate combined metrics
            double totalDataSizeMB = compressionStats.getDouble("totalDataSizeMB");
            double totalStorageSizeMB = compressionStats.getDouble("totalStorageSizeMB");
            double totalSavingsMB = compressionStats.getDouble("totalSavingsMB");
            
            response.put("totalDataSizeMB", totalDataSizeMB);
            response.put("totalStorageSizeMB", totalStorageSizeMB);
            response.put("totalSavingsMB", totalSavingsMB);
            response.put("overallCompressionRatio", compressionStats.getDouble("overallCompressionRatio"));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get combined stats", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Execute full optimization (compression + archival).
     */
    @PostMapping("/execute-full")
    public ResponseEntity<Map<String, Object>> executeFullOptimization() {
        log.info("REST request to execute full optimization");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Step 1: Configure compression
            compressionService.configureCompression();
            compressionService.optimizeIndexesForCompression();
            
            // Step 2: Configure archived collections compression
            archivalService.configureArchivedCollectionsCompression();
            
            // Step 3: Execute archival
            archivalService.archiveOldPhieuNhapXuat();
            archivalService.archiveOldChiTietNhapXuat();
            
            // Step 4: Monitor performance
            compressionService.monitorCompressionPerformance();
            archivalService.monitorArchivalPerformance();
            
            response.put("success", true);
            response.put("message", "Full optimization completed successfully");
            response.put("steps", new String[]{
                "Compression configured",
                "Indexes optimized",
                "Archived collections configured",
                "Archival executed",
                "Performance monitored"
            });
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to execute full optimization", e);
            
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 