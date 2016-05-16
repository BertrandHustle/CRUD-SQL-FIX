import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.halt;

public class Main {

    //list of forms (used to get forms by identity)
    //static ArrayList<Form> formList = new ArrayList<>();
    //static HashMap<Integer, Form> formIntegerHashMap = new HashMap<>();

    public static void main (String[] args) throws SQLException{

        //creates server
        Server server = Server.createTcpServer("-baseDir", "./data").start();

        //creates connection
        String jdbcUrl = "jdbc:h2:" + server.getURL() + "/main";
        System.out.println(jdbcUrl);
        Connection connection = DriverManager.getConnection(jdbcUrl, "", null);

        //creates/configures web service
        Service service =  new Service(connection);

        //init database
        service.initDatabase();

        Spark.get(
                "/",
                (request, response) -> {

                        //contains user

                        HashMap hash = new HashMap();

                        if(!request.session().attributes().contains("userName")) {
                            //returns login page
                            return new ModelAndView(hash, "home.mustache");

                        } else {

                        String userName = request.session().attribute("userName");
                        ArrayList<User> users = service.selectAllUsers();
                        ArrayList<String> userNames = new ArrayList<String>();
                        for (User user : users){
                            userNames.add(user.getName());
                        }

                        //checks if user already exists in database
                        if (!userNames.contains(userName)) {
                            User user = new User(userName);
                            service.insertUser(user);
                        }

                        //returns new messages page
                        hash.put("userName", userName);
                        hash.put("formList", service.selectAllForms());

                        return new ModelAndView(hash, "form.mustache");
                    }
                },

            new MustacheTemplateEngine()

        );

        //login
        Spark.post(
                "/login",
                //this doesn't need password!

                (request, response) -> {

                    String userName = request.queryParams("loginName");

                    if (!request.session().attributes().contains("userName")) {

                        //puts current userName/Password in session
                        request.session().attribute("userName", userName);
                        /*
                        User user = new User(userName);
                        service.insertUser(user);
                        */

                        response.redirect("/?loggedIn=true");

                    } else {

                        response.redirect("/?loggedIn=false");

                    }

                    return "";

                }
        );

        //logout
        Spark.get(
                "/logout",
                (request, response) -> {
                    request.session().invalidate();
                    response.redirect("/");
                    halt();
                    return "";
                }
        );

        //main entry form
        Spark.post(
                "/form",
                (request, response) -> {

                    HashMap hash = new HashMap();

                    String title = request.queryParams("title");
                    String genre = request.queryParams("genre");
                    String system = request.queryParams("system");
                    //sets form id to user id
                    int userId = service.selectUser(request.session().attribute("userName")).getId();

                    //creates new form
                    Form form = new Form (title, genre, system, userId);
                    service.insertForm(form);

                    /*
                    if (formList.size() == 0){
                        userId = 0;
                    } else {
                        //sets ID to 1 plus the ID of the last entry in the list
                        userId = formList.get(formList.size()-1).getUserId()+1;
                    }
                    */

                    String userName = request.session().attribute("userName");

                    ArrayList<Form> formList = service.selectAllForms();

                    //this is what we pass into the mustache
                    hash.put("form", form);
                    hash.put("formList", formList);
                    hash.put("userName", userName);

                    //reloads page

                    return new ModelAndView(hash, "form.mustache");
                },

            new MustacheTemplateEngine()
        );

        Spark.get(
                "/edit",
                (request, response) -> {
                    HashMap hash = new HashMap();
                    int ID = Integer.parseInt(request.queryParams("number"));

                    //int index = 0;

                    Form form = service.selectForm(ID);

                    /*
                    try {
                        for (Form form : formList) {
                            if (form.getUserId() == ID){
                                index = formList.indexOf(form);
                            }
                        }
                    } catch (IndexOutOfBoundsException ioe){
                        response.redirect("/");
                    }

                    Form form = formList.get(index);
                    */

                    hash.put("form", form);

                    return new ModelAndView(hash, "edit.mustache");
                },
        new MustacheTemplateEngine()
        );

        Spark.post(
                "/edit",
                (request, response) -> {
                    HashMap hash = new HashMap();
                    int ID = Integer.parseInt(request.queryParams("change"));
                    int userId = service.selectUser(request.session().attribute("userName")).getId();


                    //int index = 0;

                    Form form = service.selectForm(ID);

                    /*
                    try {
                        for (Form form : formList) {
                            if (form.getUserId() == ID){
                                index = formList.indexOf(form);
                            }
                        }
                    } catch (IndexOutOfBoundsException ioe){
                        response.redirect("/");
                    }

                    Form form = formList.get(index);
                    */

                    String title = request.queryParams("title");
                    String genre = request.queryParams("genre");
                    String system = request.queryParams("system");

                    service.updateForm(ID, title, genre, system, userId);

                    //likely unnecessary
                    form.setTitle(title);
                    form.setGenre(genre);
                    form.setSystem(system);

                    hash.put("form", form);

                    response.redirect("/");
                    halt();
                    return "";
                }
        );

        Spark.post(
                "/delete",
                (request, response) -> {

                    //make this its own method

                    int ID = Integer.parseInt(request.queryParams("number"));
                    //int index = 0;

                    /*
                    try {
                        for (Form form : formList) {
                            if (form.getUserId() == ID){
                                index = formList.indexOf(form);
                            }
                        }
                    } catch (IndexOutOfBoundsException ioe){
                        response.redirect("/");
                    }

                    formList.remove(index);
                    */

                    service.deleteForm(ID);

                    response.redirect("/");
                    halt();
                    return "";

                }
        );

    }

}
