package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public static String getProperty(String filename, String key) {
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream
                ("src/test/properties" + File.separator + filename)) {
            properties.load(fileInputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            System.out.println("created exception");
            e.printStackTrace();
            return null;
        }
    }
}
