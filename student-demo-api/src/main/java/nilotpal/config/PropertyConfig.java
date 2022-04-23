package nilotpal.config;

import java.io.IOException;
import java.util.Properties;

public class PropertyConfig {
    private static PropertyConfig instance = null;
    private final Properties properties;

    protected PropertyConfig() throws IOException {

        properties = new Properties();
        properties.load(getClass().getResourceAsStream("/student-demo.properties"));

    }

    public static PropertyConfig getInstance() {
        if(instance == null) {
            try {
                instance = new PropertyConfig();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
