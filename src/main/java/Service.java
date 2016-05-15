import java.sql.*;
import java.util.ArrayList;

public class Service {

    private final Connection connection;

    public Service (Connection connection) {
        this.connection = connection;
    }

    //init database
    public void initDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS form (id IDENTITY, title VARCHAR , genre VARCHAR, system VARCHAR, userId INT)");
        statement.execute("CREATE TABLE IF NOT EXISTS user (id IDENTITY, name VARCHAR , password VARCHAR)");

    }

    public void insertUser(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user VALUES(NULL, ?, ?)");
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.executeUpdate();

        //sets user.id to autogenerated id from SQL
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        user.setId(resultSet.getInt(1));
    }

    public User selectUser(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE name=?");
        statement.setString(1, name);

        ResultSet resultSet = statement.executeQuery();
        ArrayList<User> selectedUser = new ArrayList<>();
        while(resultSet.next()){

            User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("password")
            );
            selectedUser.add(user);
        }
        return selectedUser.get(0);
    }

    public void insertForm(Form form) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO form VALUES(NULL, ?, ?, ?, ?)");
        preparedStatement.setString(1, form.getTitle());
        preparedStatement.setString(2, form.getGenre());
        preparedStatement.setString(3, form.getSystem());
        preparedStatement.setInt(4, form.getUserId());
        preparedStatement.executeUpdate();

    }

    public Form selectForm(int id) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM form INNER JOIN user ON user.id = form.Id WHERE user.Id = ?");
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();
        ArrayList<Form> selectedForm = new ArrayList<>();
        while(resultSet.next()){

            Form form = new Form(
                    resultSet.getString("title"),
                    resultSet.getString("genre"),
                    resultSet.getString("system"),
                    resultSet.getInt("userId")
            );

            selectedForm.add(form);
        }
        return selectedForm.get(0);

    }

    public ArrayList<Form> selectAllForms() throws SQLException {

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM form INNER JOIN user ON user.ID");

        ResultSet resultSet = statement.executeQuery();
        ArrayList<Form> forms = new ArrayList<>();
        while(resultSet.next()){

            Form form = new Form(
                    resultSet.getString("title"),
                    resultSet.getString("genre"),
                    resultSet.getString("system"),
                    resultSet.getInt("userId")
            );

            forms.add(form);
        }
        return forms;
    }

    public void updateForm(int id, String title, String genre, String system, int userId) throws SQLException {

            PreparedStatement statement = connection.prepareStatement("UPDATE form SET title = ?, genre = ?, system = ?, userId = ? WHERE id = ?");
            statement.setString(1, title);
            statement.setString(2, genre);
            statement.setString(3, system);
            statement.setInt(4, userId);
            statement.setInt(5, id);
            statement.executeUpdate();
        }

    public void deleteForm(int id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM form WHERE id = ?");
        statement.setInt(1, id);
        statement.executeUpdate();

    }

}


