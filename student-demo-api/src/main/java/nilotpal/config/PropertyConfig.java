package nilotpal.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Responsible to loading properties from the properties file
 */
public class PropertyConfig {
    private static PropertyConfig instance = null;
    private final Properties properties;

    protected PropertyConfig() throws IOException {

        properties = new Properties();
        properties.load(getClass().getResourceAsStream("/student-demo.properties"));

    }

    /**
     * @return The singleton instance of the {@link PropertyConfig}
     */
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

    /**
     * @param key The key of the property whose value is needed
     * @return The String represented value that corresponds to the key
     */
    public String get(String key) {
        return properties.getProperty(key);
    }
}
