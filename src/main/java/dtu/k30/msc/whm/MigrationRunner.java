package dtu.k30.msc.whm;

import dtu.k30.msc.whm.config.dbmigrations.ManualMigrationScript;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Standalone migration runner that can be executed independently.
 * This class runs only the migration script without starting the full web application.
 */
@SpringBootApplication
public class MigrationRunner {

    public static void main(String[] args) {
        System.out.println("Starting migration runner...");
        
        try (ConfigurableApplicationContext context = SpringApplication.run(MigrationRunner.class, args)) {
            System.out.println("Spring context started successfully");
            
            ManualMigrationScript migrationScript = context.getBean(ManualMigrationScript.class);
            System.out.println("Migration script bean retrieved");
            
            System.out.println("Starting DBRef to Embedded migration...");
            migrationScript.migrateDBRefToEmbedded();
            
            System.out.println("Migration completed successfully!");
            System.out.println("Exiting migration runner...");
            
        } catch (Exception e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 