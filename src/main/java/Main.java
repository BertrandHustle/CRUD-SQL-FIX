import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

import static spark.Spark.halt;

public class Main {

    public static void main (String[] args){

         HashMap<String, User> userList = new HashMap<>();
         HashMap<User, Form> forms = new HashMap();

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

                    if (request.session().attributes().contains("userName")){

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

                    //creates new form

                    String title = request.queryParams("title");
                    String genre = request.queryParams("genre");
                    String year = request.queryParams("year");
                    String system = request.queryParams("system");
                    String userName = request.session().attribute("userName");

                    //puts new form into hashmap

                    Form form = new Form(title, genre, year, system);
                    forms.put(userList.get(userName), form);

                    //reloads page
                    response.redirect("/form");
                    halt();
                    return "";
                }
        );

    }

}
