import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.halt;

public class Main {

    //list of forms (used to get forms by identity)
    static ArrayList<Form> formList = new ArrayList<>();

    //list of forms in string form
    static ArrayList<String> formStrings = new ArrayList<>();

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
                            hash.put("formStrings", formStrings);
                            return new ModelAndView(hash, "form.mustache");
                        }

                    },

                    new MustacheTemplateEngine()

        );

        //login
        Spark.post(
                "/login",
                //this doesnt need password!

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
                    String userName = request.session().attribute("userName");

                    //puts new form into arraylist (in string form)
                    Form form = new Form(title, genre, system);
                    formList.add(form);
                    formStrings.add(form.toString());
                    System.out.println((formStrings.get(0)));

                    //reloads page
                    response.redirect("/");
                    halt();

                    return "";
                }


        );


    }

}
