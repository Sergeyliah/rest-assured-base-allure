package utils;

import exception.PropertyFileLoadingException;
import exception.PropertyNotFoundException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.yaml.snakeyaml.Yaml;

@Slf4j
@UtilityClass
public class PropertiesUtils {
    private final Map<String, Object> PROPERTIES;

    static {
        Yaml yaml = new Yaml();
        String env = fetchEnvironmentName();
        String yamlFile = "config/res-" + env + "-automation.yml";
        log.info("Environment: [{}], Configuration File: [{}] ", env, yamlFile);

        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(yamlFile)){
            PROPERTIES = yaml.load(inputStream);
            if (PROPERTIES == null){
                throw new PropertyFileLoadingException("Property file: " + yamlFile + " could not be found or loaded.");
            }
        } catch (IOException e){
            throw new PropertyFileLoadingException("Error loading property file " + yamlFile, e);
        }
    }

    public String fetchProperty(String key){
        String[] keys = key.split("\\.");
        Object current = PROPERTIES;

        for (String k : keys) {
            current = ((Map<String, Object>) current).get(k);
            if (current == null){
                throw new PropertyNotFoundException("Property key " + key + " not found in the property file.");
            }
        }
        return current.toString();
    }

    public String fetchEnvironmentName(){
        final String env = System.getProperty("env") == null || System.getProperty("env").isBlank() ?
                "local" : System.getProperty("env");
        log.debug("fetchEnvironmentName()-> env = {}", env);
        return env;
    }


    public static Object fetchProperties(String key) throws IOException {
        FileInputStream file = new FileInputStream("./src/main/resources/config.properties");
        Properties property = new Properties();
        property.load(file);
        return property.get(key);
    }
}
