import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

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
        User user = new User("Scott");

        //act
        service.insertUser(user);
        User testUser = service.selectUser("Scott");

        //assert

        assertThat(testUser.getName(), is("Scott"));
    }

    /**
     * Given a Form
     * When Form is inserted and queried by id
     * Then Form is returned
     * @throws SQLException
     */

    @Test
    public void whenFormInsertedReturnsForm() throws SQLException{
        //arrange
        service.initDatabase();
        Form form = new Form ("Mario", "Platform", "NES", 1, 1);
        User user = new User ("name");

        //act
        service.insertForm(form);
        service.insertUser(user);
        Form testForm = service.selectForm(1);

        //assert
        assertThat(testForm.getUserId(), is(1));

    }

    /**
     * Given a table
     * When method is executed
     * Then arraylist of all entries owned by user is returned
     * @throws SQLException
     */

    @Test
    public void whenTableGivenThenFormArraylistReturned() throws SQLException {

        //arrange
        service.initDatabase();
        Form form = new Form ("Mario", "Platform", "NES", 1, 1);
        Form form2 = new Form ("Zelda", "RPG", "NES", 1, 2);
        User user = new User ("name");

        //act
        service.insertForm(form);
        service.insertForm(form2);
        service.insertUser(user);
        ArrayList<Form> testForms = service.selectAllForms(1);

        //assert
        assertThat(testForms.size(), is(2));

    }

    /**
     * Given a form
     * When method is run with form.values
     * Then form is updated properly
     * @throws SQLException
     */

    @Test
    public void whenUpdatedThenReturnsUpdatedForm() throws SQLException {

        //arrange
        service.initDatabase();
        Form form = new Form ("Mario", "Platform", "NES", 1, 1);
        Form form2 = new Form ("Zelda", "RPG", "NES", 2, 2);
        User user = new User ("name");

        //act
        service.insertForm(form);
        service.insertForm(form2);
        service.insertUser(user);
        service.updateForm(1, "Contra", "Action", "GG", 1);
        Form testForm = service.selectForm(1);

        //assert
        assertThat(testForm.getTitle(), is("Contra"));

    }

    /**
     * Given a form id
     * When method is executed
     * Then form is deleted from table
     * @throws SQLException
     */

    @Test
    public void whenIdGivenFormDeleted() throws SQLException {

        //arrange
        service.initDatabase();
        Form form = new Form ("Mario", "Platform", "NES", 1, 1);
        Form form2 = new Form ("Zelda", "RPG", "NES", 2, 2);
        User user = new User ("name");

        //act
        service.insertForm(form);
        service.insertForm(form2);
        service.insertUser(user);
        service.deleteForm(2);
        ArrayList<Form> testForms = service.selectAllForms(1);

        //assert
        assertThat(testForms.size(), is (1));


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
