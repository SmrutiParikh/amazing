package forbes.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyUtils.class);

    private static Properties properties = new Properties();

    public static void initProperties() {
        try (InputStream input = PropertyUtils.class.getClassLoader().getResourceAsStream(Constants.APP_PROPERTIES)) {
            if (input == null) {
                LOGGER.info("Sorry, unable to find app.properties");
                return;
            }
            //load a properties file from class path, inside static method
            properties.load(input);
        } catch (IOException ex) {
            LOGGER.error("Exception at Main : {}", ex.getMessage(), ex);
        }
    }

    public static Properties getProperties() {
        return properties;
    }

    public static String getProperty(String property){
        return properties.getProperty(property);
    }

    public static String getProperty(String property, String defaultValue){
        return properties.getProperty(property, defaultValue);
    }
}
