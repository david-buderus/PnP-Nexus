package de.pnp.manager.server.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

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
            SimpleMongoClientDatabaseFactory clientFactory = new SimpleMongoClientDatabaseFactory(mongo(), database);
            templateCache.put(database, new MongoTemplate(clientFactory, createMongoConverter(clientFactory)));
        }
        return templateCache.get(database);
    }

    /**
     * Returns a client to manipulate the collections of a universe database.
     */
    public MongoTemplate universeMongoTemplate(String universe) {
        return mongoTemplate(DatabaseConstants.UNIVERSE_PREFIX + universe);
    }

    private static MongoConverter createMongoConverter(MongoDatabaseFactory factory) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MongoCustomConversions conversions = new MongoCustomConversions(Collections.emptyList());

        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        mappingContext.afterPropertiesSet();
        mappingContext.setAutoIndexCreation(true);

        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
        converter.setCustomConversions(conversions);
        converter.setCodecRegistryProvider(factory);
        converter.afterPropertiesSet();

        return converter;
    }
}
