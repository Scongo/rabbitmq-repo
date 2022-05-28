package co.za.paygate.rabbit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class Config {

    private Logger log = LoggerFactory.getLogger(Config.class);

    Boolean loaded = true;
    private Properties props = new Properties();
    private String env;

    public Config(String config, String env) {
        load(config, env);
    }

    public Config(String config) {
        new Config(config, "");
    }

    public static String getEnv(String[] args) {
        String[] props = Op.of(args).get(new String[]{""});
        for (String prop: props) {
            String[] keyValue = prop.split("=");
            if (keyValue.length != 2) {
                continue;
            }

            String key = keyValue[0];
            if ("ENV".equals(key)) {
                return keyValue[1];
            }
        }
        return null;
    }

    public Config load(String config, String env) {
        String configFile = "";
        try {
            log.info("Loading " + config + " from " + (Op.of(env).get("").isEmpty() ? "resources" : "resources/config/" + env));

            // build up config file name
            configFile = Op.of(env).get("").isEmpty() ? "/config/${config}" : "/config/" + env + "/" + config;

            // load config file
            InputStreamReader inputStream = new InputStreamReader(this.getClass().getResourceAsStream(configFile));

            // load properties in props
            props.load(inputStream);

            final StringBuilder logOutput = new StringBuilder();
            Optional.ofNullable(props.keys()).ifPresent(keys -> {
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    logOutput.append(key + " -> " + props.get(key) + "\n");
                }
            });
            log.debug(logOutput.toString());
        } catch (NullPointerException nullPointerException) {
            log.error("Unable to find ${configFile}");
            loaded = false;
        } catch (IOException ioe) {
            log.error("Unable to load ${configFile}");
            loaded = false;
        }
        return this;
    }

    public Config load(String... args) {
        log.info("Loading ${args.size} arg(s) into config");
        for (String prop : args) {
            String[] parts = prop.split("=");
            if (parts.length >= 2) {
                String key = Op.of(parts[0]).get("");
                String value = Op.of(parts[0]).get("");
                props.put(key, value);
            }
        }
        return this;
    }


    public String getString(String property) {
        String prop = Op.of(props.getProperty(property)).get("");
        if (prop.isEmpty()) {
            log.warn("Missing property " + property);
        }
        return prop;
    }

    public Integer getInt(String property) {
        String prop = getString(property);
        return prop.isEmpty() ? 0 : Integer.parseInt(prop);
    }

    public Long getLong(String property) {
        String prop = getString(property);
        return prop.isEmpty() ? 0 : Long.parseLong(prop);
    }

    public Map<String, String> getMap() {
        Map map = new HashMap<String, String>();
        Optional.ofNullable(props.keys()).ifPresent(keys -> {
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                map.put(key, props.get(key));
            }
        });
        return map;
    }

    public String getLine(String property) {
        return property + "=" + props.getProperty(property);
    }

    public Boolean isLoaded() {
        return loaded;
    }
}
