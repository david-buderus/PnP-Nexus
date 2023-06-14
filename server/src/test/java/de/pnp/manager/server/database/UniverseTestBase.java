package de.pnp.manager.server.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base test class for every test that needs an existing universe in the database.
 */
@SpringBootTest
public abstract class UniverseTestBase {

  /**
   * Database name that should be used for testing
   */
  protected final String universe;


  protected UniverseTestBase() {
    universe = UUID.randomUUID().toString();
  }

  @BeforeEach
  void setup(@Autowired MongoClient client) {
    MongoDatabase database = client.getDatabase(universe);
    database.drop();
    database.createCollection("meta-information");
  }

  @AfterEach
  void tearDown(@Autowired MongoClient client) {
    MongoDatabase database = client.getDatabase(universe);
    database.drop();
  }
}
