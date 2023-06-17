package de.pnp.manager.server.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Configuration for MongoDB.
 */
@Configuration
public class MongoConfig {

  private final Map<String, MongoTemplate> templateCache = new HashMap<>();

  /**
   * Returns a client to manipulate the backend database.
   */
  @Bean
  public MongoClient mongo() {
    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
    MongoCredential credential = MongoCredential.createScramSha1Credential("admin", "admin",
        "admin".toCharArray());
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .credential(credential)
        .build();

    return MongoClients.create(mongoClientSettings);
  }

  /**
   * Returns a client to manipulate the collections of a specific database.
   */
  public MongoTemplate mongoTemplate(String database) {
    if (!templateCache.containsKey(database)) {
      templateCache.put(database, new MongoTemplate(mongo(), database));
    }
    return templateCache.get(database);
  }
}
