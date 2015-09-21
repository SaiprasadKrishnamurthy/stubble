package main;

import core.Repository;
import core.Router;
import core.RoutingContext;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;

/**
 * Created by sai on 03/08/2015.
 */
public class Main {

    private static final Repository REPOSITORY = new Repository(Main.class.getClassLoader().getResourceAsStream("api-defs.yml"));

    public static void main(String[] args) throws Exception {
        get("/*", Main::getModelAndView, new HandlebarsTemplateEngine());
        put("/*", Main::getModelAndView, new HandlebarsTemplateEngine());
        post("/*", Main::getModelAndView, new HandlebarsTemplateEngine());
        delete("/*", Main::getModelAndView, new HandlebarsTemplateEngine());
        head("/*", Main::getModelAndView, new HandlebarsTemplateEngine());
    }

    private static ModelAndView getModelAndView(final Request req, final Response res) {
        RoutingContext routingContext = getRoutingContext(req, res);
        String viewTemplateName = routingContext.getRoutingTemplateName();
        return new ModelAndView(routingContext, viewTemplateName);
    }

    private static RoutingContext getRoutingContext(final Request req, final Response res) {
        RoutingContext routingContext = Router.context(req, REPOSITORY.apiDefinitions());
        routingContext.getResponseHeaders().forEach(res::header);
        routingContext.getResponseCookies().forEach(res::cookie);
        res.status(routingContext.getResponseStatus());
        res.type(routingContext.getResponseContentType());
        return routingContext;
    }
}