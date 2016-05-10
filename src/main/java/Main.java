import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.halt;

public class Main {

    //list of forms (used to get forms by identity)
    static ArrayList<Form> formList = new ArrayList<>();

    static int currentID;

    public static void main (String[] args){

        Spark.get(
                "/",
                (request, response) -> {

                        //contains user

                        HashMap hash = new HashMap();

                        if(!request.session().attributes().contains("userName")) {
                            //returns login page
                            return new ModelAndView(hash, "home.mustache");

                        } else {

                            //returns new messages page
                            String userName = request.session().attribute("userName");
                            hash.put("userName", userName);
                            hash.put("formList", formList);
                            hash.put("number", request.session().attribute("ID"));

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

                    if (!request.session().attributes().contains("userName")){

                        //puts current userName/Password in session
                        request.session().attribute("userName", userName);

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

        //may need a get request for form.mustache here

        //main entry form
        Spark.post(
                "/form",
                (request, response) -> {

                    HashMap hash = new HashMap();

                    //creates new form

                    String title = request.queryParams("title");
                    String genre = request.queryParams("genre");
                    String system = request.queryParams("system");
                    int ID;

                    if (formList.size() == 0){
                        ID = 0;
                    } else {
                        //sets ID to 1 plus the ID of the last entry in the list
                        ID = formList.get(formList.size()-1).getID()+1;
                    }

                    //currentID = ID;

                    String userName = request.session().attribute("userName");

                    //puts new form into arraylist (in string form)
                    Form form = new Form(title, genre, system, ID);
                    formList.add(form);

                    request.session().attribute("ID", ID);

                    //this is the ID we pass into the mustache
                    hash.put("number", ID);
                    hash.put("formList", formList);

                    //reloads page

                    return new ModelAndView(hash, "form.mustache");
                },

            new MustacheTemplateEngine()
        );

        Spark.get(
                "/edit",
                (request, response) -> {
                    HashMap hash = new HashMap();
                    int ID = request.session().attribute("ID");
                    Form form = formList.get(ID);
                    hash.put("form", form);

                    return new ModelAndView(hash, "edit.mustache");
                },
        new MustacheTemplateEngine()
        );

        Spark.post(
                "/edit",
                (request, response) -> {
                    HashMap hash = new HashMap();
                    int ID = request.session().attribute("ID");
                    Form form = formList.get(ID);

                    (formList.get(ID)).setTitle(request.queryParams("title"));
                    (formList.get(ID)).setGenre(request.queryParams("genre"));
                    (formList.get(ID)).setSystem(request.queryParams("system"));

                    hash.put("form", form);

                    response.redirect("/");
                    halt();
                    return "";
                }
        );

        //make this a get?

        Spark.post(
                "/delete",
                (request, response) -> {

                    try {
                        formList.remove(formList.get(Integer.parseInt(request.queryParams("number"))));
                    } catch (IndexOutOfBoundsException ioobe){

                    }

                    response.redirect("/");
                    halt();
                    return "";

                }
        );

    }

}
