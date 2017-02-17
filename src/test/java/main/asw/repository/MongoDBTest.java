package main.asw.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import main.asw.user.User;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by MIGUEL on 16/02/2017.
 *
 * This class runs tests against a temporal database
 */
public class MongoDBTest {

    private static final String MONGO_HOST = "localhost";
    private static final int MONGO_PORT = 27777;
//    private static final String IN_MEM_CONNECTION_URL = MONGO_HOST + ":" + MONGO_PORT;

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoClient mongoClient;

    /**
     * Deploys an in-memory database for simple testing
     * @throws Exception
     */
    @Before
    public void setup() throws Exception {
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        mongodExe = runtime.prepare(new MongodConfig(Version.V2_0_5, MONGO_PORT, Network.localhostIsIPv6()));
        mongod = mongodExe.start();
        mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT);
    }

    /**
     * Shuts down the in-memory DB
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        if (mongod != null) {
            mongod.stop();
            mongodExe.stop();
        }
    }

    /**
     * Tests user insertion on DB
     */
    @Test
    public void testUserInsertion() {
        MongoDatabase db = mongoClient.getDatabase("test");
        MongoCollection<Document> coll = db.getCollection("user");
        User u = new User("Miguel", "García", "mg@email.com", new Date(), "c/ street", "España", "11122233A");
        Document doc = new Document("name", u.getName())
                .append("surname", u.getSurname())
                .append("email", u.getEmail())
                .append("nationality", u.getNationality())
                .append("address", u.getNationality())
                .append("dni", u.getDni())
                .append("date", u.getDate())
                .append("password", u.getPassword());
        coll.insertOne(doc);

        assertEquals(1, coll.count());
        assertEquals("Miguel", coll.find().first().get("name"));
        assertEquals(doc.toJson(), coll.find().first().toJson());
    }

}