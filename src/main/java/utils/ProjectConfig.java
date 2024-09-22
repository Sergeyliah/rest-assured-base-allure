package utils;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {
    boolean logging();
    String baseUrl();


}
