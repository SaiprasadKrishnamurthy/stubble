package stubble;

import stubble.core.HandleBarsTemplateEngine;
import stubble.core.Repository;
import stubble.core.Router;
import stubble.core.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * <p>
     * Runs the API stub app as defined in the api definitions file in the apiDefinitionsRootDirectory.
     * By default, the http port is: 4567. If you need to override it, do so by using -Dport=[port]
     * </p>
     *
     * @param apiDefinitionsRootDirectory Root directory where the api definitions yml files are stored.
     * @param templatesRootDirectory      Root directory where the handlebar templates are stored.
     */
    public void run(final String apiDefinitionsRootDirectory, final String templatesRootDirectory) {
        this.definitions = apiDefinitionsRootDirectory;
        this.templatesDirectory = templatesRootDirectory;
    }

    private void doMain(final String[] args) throws Exception {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            configure();
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java Main [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.exit(1);
        }
    }

    private void configure() throws IOException {
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
        configureRoutes();
    }

    private void configureRoutes() {
        if (StringUtils.isNumeric(System.getProperty("port"))) {
            port(Integer.parseInt(System.getProperty("port").trim()));
        }
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
