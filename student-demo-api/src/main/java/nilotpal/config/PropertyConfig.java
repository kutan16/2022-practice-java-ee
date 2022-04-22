package nilotpal.config;

import java.io.IOException;
import java.util.Properties;

public class PropertyConfig {
    private final Properties properties;

    protected PropertyConfig() throws IOException {

        properties = new Properties();
        properties.load(getClass().getResourceAsStream("student-demo.properties"));

    }

    public String get(String key) {
        return properties.getProperty(key);
    }

}
