package com.slandow.cycleganmanager

import com.mongodb.MongoClient
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import groovy.transform.TypeChecked
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

@Configuration
@TypeChecked
class IntegrationTestConfig extends AbstractMongoConfiguration {

    public static final int PORT = 5001
    public static final String HOST = "localhost"


    IntegrationTestConfig() {
        final starter = MongodStarter.getDefaultInstance()
        final config = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(HOST, PORT, Network.localhostIsIPv6()))
                .build()
        final executable = starter.prepare(config)
        executable.start()
    }

    @Value('${spring.data.mongodb.database}')
    private String mongoDbName

    @Override
    protected String getDatabaseName() {
        return mongoDbName
    }

    @Override
    MongoClient mongoClient() {
        return new MongoClient(HOST, PORT)
    }
}
