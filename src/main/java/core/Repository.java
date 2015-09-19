package core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ApiDef;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by sai on 18/09/2015.
 */
public final class Repository {

    private final InputStream apiDefsYml;
    private final List<ApiDef> apiDefinitions = new ArrayList<>();

    public Repository(final InputStream apiDefsYml) {
        this.apiDefsYml = apiDefsYml;
        Yaml yml = new Yaml();
        List<Map<String, String>> apiDefsAsMaps = (List) yml.load(apiDefsYml);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        apiDefinitions.addAll(apiDefsAsMaps.stream().map(def -> mapper.convertValue(def, ApiDef.class)).collect(toList()));
    }

    public List<ApiDef> apiDefinitions() {
        return apiDefinitions;
    }
}
