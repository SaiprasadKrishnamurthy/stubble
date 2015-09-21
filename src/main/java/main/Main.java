package main;

import core.HandleBarsTemplateEngine;
import core.Repository;
import core.Router;
import core.RoutingContext;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static spark.Spark.*;

/**
 * Created by sai on 03/08/2015.
 */
public class Main {

    private static Repository REPOSITORY;

    @Option(required = true, name = "-definitionsRootDir", aliases = "--definitionsRootDir", usage = "Path to the directory containing api definitions yml file(s).")
    private String definitions;

    @Option(required = true, name = "-templatesRootDir", aliases = "--templatesRootDir", usage = "Path to the directory containing the handlebar template (.hbs) file(s).")
    private String templatesDirectory;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.doMain(args);
    }

    private void doMain(final String[] args) throws Exception {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            List<InputStream> ymlDefinitions = Files.list(Paths.get(definitions))
                    .filter(path -> path.toString().endsWith(".yml") || path.toString().endsWith(".yaml"))
                    .map(path -> {
                        try {
                            return new FileInputStream(path.toString());
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(toList());

            REPOSITORY = new Repository(ymlDefinitions);
            configure();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java Main [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.exit(1);
        }
    }

    private void configure() {
        get("/*", this::getModelAndView, new HandleBarsTemplateEngine(templatesDirectory));
        put("/*", this::getModelAndView, new HandleBarsTemplateEngine(templatesDirectory));
        post("/*", this::getModelAndView, new HandleBarsTemplateEngine(templatesDirectory));
        delete("/*", this::getModelAndView, new HandleBarsTemplateEngine(templatesDirectory));
        head("/*", this::getModelAndView, new HandleBarsTemplateEngine(templatesDirectory));
    }

    private ModelAndView getModelAndView(final Request req, final Response res) {
        RoutingContext routingContext = getRoutingContext(req, res);
        String viewTemplateName = routingContext.getRoutingTemplateName();
        return new ModelAndView(routingContext, viewTemplateName);
    }

    private RoutingContext getRoutingContext(final Request req, final Response res) {
        RoutingContext routingContext = Router.context(req, REPOSITORY.apiDefinitions());
        routingContext.getResponseHeaders().forEach(res::header);
        routingContext.getResponseCookies().forEach(res::cookie);
        res.status(routingContext.getResponseStatus());
        res.type(routingContext.getResponseContentType());
        return routingContext;
    }
}
