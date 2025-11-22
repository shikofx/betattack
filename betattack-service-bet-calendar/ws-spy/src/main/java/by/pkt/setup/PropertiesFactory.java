package by.pkt.setup;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class PropertiesFactory {
    Properties currentProperties = new Properties();

    Properties getCurrentProperties(String propertiesFile) throws IOException {
        FileInputStream
                inputStream =
                new FileInputStream(System.getProperty("user.dir") + "/ws-spy/src/main/resources/" + propertiesFile);
        currentProperties.load(inputStream);
        inputStream.close();
        return currentProperties;
    }

    protected String getProperties(String propertyKey, String propertiesFile) throws IOException {
        if (!currentProperties.containsKey(propertyKey)) {
            currentProperties = getCurrentProperties(propertiesFile);
        }
        return currentProperties.getProperty(String.valueOf(propertyKey), null);
    }
}
