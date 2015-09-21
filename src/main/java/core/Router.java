package core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ApiDef;
import spark.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by sai on 18/09/2015.
 */
public class Router {
    public static RoutingContext context(final Request rq, final List<ApiDef> availableDefinitions) {
        RoutingContext routingContext = new RoutingContext();

        String uri = rq.uri();
        String reqMethod = rq.requestMethod();


        ApiDef apiDef = matchingPathTokens(availableDefinitions, uri, reqMethod, rq.queryString())
                .orElseThrow(() -> new RuntimeException("No matching api definitions found to route this request"));

        // Map the path variables now.
        String[] apiUriElements = apiDef.getUri().split("/", -1);
        String[] rqUriElements = uri.split("/", -1);
        IntStream.range(0, apiUriElements.length).forEach(index -> {
            // This is a variable.
            if (apiUriElements[index].contains(":") && !apiUriElements[index].contains("?")) {
                routingContext.getUri().put(apiUriElements[index].replace(":", ""), rqUriElements[index]);
            }
        });

        if (apiDef.getResponseCookies() != null) {
            Stream.of(apiDef.getResponseCookies().split(",", -1))
                    .forEach(kv -> routingContext.getResponseCookies().put(kv.split("=")[0].trim(), kv.split("=")[1].trim()));
        }

        if (apiDef.getResponseHeaders() != null) {
            Stream.of(apiDef.getResponseHeaders().split(",", -1))
                    .forEach(kv -> routingContext.getResponseHeaders().put(kv.split("=")[0].trim(), kv.split("=")[1].trim()));
        }

        rq.headers().stream()
                .forEach(rqHeaderName -> routingContext.getRequestHeaders().put(rqHeaderName, rq.headers(rqHeaderName)));

        routingContext.getRequestCookies().putAll(rq.cookies());

        routingContext.setResponseStatus(apiDef.getResponseStatus());

        routingContext.setRoutingTemplateName(apiDef.getResponseTemplateFile());

        // Map the query string variables now.
        if (apiDef.getUri().contains("?") && !apiDef.getUri().endsWith("?")) {
            String[] apiQueryStringElements = apiDef.getUri().substring(apiDef.getUri().indexOf("?") + 1).split("&");
            String[] rqQueryStringElements = rq.queryString().split("&", -1);
            IntStream.range(0, apiQueryStringElements.length).forEach(index -> {
                // This is a variable.
                if (apiQueryStringElements[index].contains(":")) {
                    String[] kvApi = apiQueryStringElements[index].split("=");
                    String[] kvRq = rqQueryStringElements[index].split("=");
                    IntStream.range(0, kvApi.length)
                            .forEach(i -> {
                                if (kvApi[i].contains(":")) {
                                    routingContext.getUri().put(kvApi[i].replace(":", "").trim(), kvRq[i].trim());
                                }
                            });

                }
            });
        }

        // Parse body.
        if (rq.body() != null) {
            routingContext.getRequestBody().put("val", rq.body());
            if (rq.contentType() != null && rq.contentType().toUpperCase().contains("json".toUpperCase())) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    routingContext.getRequestBody().putAll(mapper.readValue(rq.body(),
                            new TypeReference<HashMap<String, String>>() {
                            }));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        return routingContext;
    }

    private static Optional<ApiDef> matchingPathTokens(final List<ApiDef> apiDefinitions, final String uri, final String verb, final String queryString) {
        List<String> uriElements = Stream.of(uri.split("/", -1)).collect(toList());
        return apiDefinitions
                .stream()
                .filter(apiDef -> apiDef.getHttpverb().equalsIgnoreCase(verb))
                .filter(definition -> definition.getUri().split("/", -1).length == uriElements.size())
                .filter(apiDef -> {
                    String[] apiUriElements = apiDef.getUri().split("/", -1);
                    String[] rqUriElements = uri.split("/", -1);
                    return IntStream.range(0, apiUriElements.length)
                            .allMatch(index -> {
                                // If the path element is not a variable, then it must match.
                                if (!apiUriElements[index].contains(":")) {
                                    return rqUriElements[index].trim().equals(apiUriElements[index].trim());
                                } else {
                                    return true;
                                }
                            });

                })
                .filter(apiDef -> {
                    if (queryString != null && apiDef.getUri().contains("?") && !apiDef.getUri().endsWith("?")) {
                        String[] queryParamPairInApiDef = apiDef.getUri().substring(apiDef.getUri().indexOf("?") + 1).split("&");
                        String[] queryParamPairQueryString = queryString.split("&");

                        if (queryParamPairInApiDef.length == queryParamPairQueryString.length) {
                            return IntStream.range(0, queryParamPairInApiDef.length)
                                    .allMatch(index -> {
                                        // If the path element is not a variable, then it must match.
                                        if (!queryParamPairInApiDef[index].contains(":")) {
                                            return queryParamPairQueryString[index].trim().equals(queryParamPairQueryString[index].trim());
                                        } else {
                                            return true;
                                        }
                                    });
                        } else {
                            return false;
                        }


                    } else {
                        return true;
                    }
                })
                .findFirst();
    }
}
