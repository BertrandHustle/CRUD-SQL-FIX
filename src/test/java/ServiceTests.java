import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ServiceTests {

    Connection connection;
    Service service;

    @Before
    public void before() throws SQLException {
        // create a server
        Server server = Server.createTcpServer("-baseDir", "./data").start();

        // created our connection
        connection = DriverManager.getConnection("jdbc:h2:" + server.getURL() + "/test", "", null);

        service = new Service(connection);
    }

    /**
     * Given a User
     * When User is inserted and queried
     * Then User is returned
     * @throws SQLException
     */

    @Test
    public void whenUserInsertedReturnsUser() throws SQLException{
        //arrange
        service.initDatabase();
        User user = new User("Scott", "testPass");

        //act
        service.insertUser(user);
        User testUser = service.selectUser("Scott");

        //assert

        assertThat(testUser.getName(), is("Scott"));
    }

    @After
    public void after() throws SQLException {

        connection.close();

        File dataFolder = new File("data");
        if(dataFolder.exists()) {
            for(File file : dataFolder.listFiles()){
                if(file.getName().startsWith("test.h2.")) {
                    file.delete();
                }
            }
        }
    }
}
