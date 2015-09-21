package stubble.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;
import stubble.model.ApiDefinition;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Created by sai on 18/09/2015.
 */
public final class Repository {

    private final List<InputStream> apiDefsYmls;
    private final List<ApiDefinition> apiDefinitions = new ArrayList<>();

    public Repository(final List<InputStream> apiDefsYmls) {
        this.apiDefsYmls = apiDefsYmls;

        List<ApiDefinition> parsedDefinitions = apiDefsYmls.stream()
                .flatMap(in -> {
                    Yaml yml = new Yaml();
                    List<Map<String, String>> apiDefsAsMaps = (List) yml.load(in);
                    return apiDefsAsMaps.stream();
                })
                .map(map -> {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    return mapper.convertValue(map, ApiDefinition.class);
                })
                .collect(toList());

        apiDefinitions.addAll(parsedDefinitions);
        validate(apiDefinitions);
    }

    private void validate(final List<ApiDefinition> apiDefinitions) {
        Optional<Map.Entry<String, Long>> invaidPathDefinition = apiDefinitions.stream()
                .map(apiDef -> apiDef.getUri().contains("?") ? apiDef.getHttpverb() + ":" + apiDef.getUri().substring(0, apiDef.getUri().indexOf("?")) : apiDef.getHttpverb() + ":" + apiDef.getUri())
                .map(uri -> Stream.of(uri.split("/", -1)).filter(pathToken -> !pathToken.startsWith(":")).collect(joining("|")))
                .collect(groupingBy(key -> key, counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .findFirst();

        if (invaidPathDefinition.isPresent()) {
            throw new IllegalStateException("Ambiguous path(s) found in api definitions for: VERB: "
                    + invaidPathDefinition.get().getKey().substring(0, invaidPathDefinition.get().getKey().indexOf(":")).toUpperCase()
                    + ", URI Part: " + invaidPathDefinition.get().getKey().substring(invaidPathDefinition.get().getKey().indexOf("|") + 1).replace("|", "/"));
        }
    }

    public List<ApiDefinition> apiDefinitions() {
        return apiDefinitions;
    }
}
