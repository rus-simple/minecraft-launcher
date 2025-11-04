import java.io.*;
import java.util.Properties;

public class ConfigLoader {
    private static final String CONFIG_FILE = "config.properties";
    private static final String EXAMPLE_CONFIG_FILE = "config.example.properties";

    public static Properties loadConfig() throws IOException {
        Properties props = new Properties();

        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            // Создаем пример конфига, если его нет
            createExampleConfig();
            throw new IOException("Config file not found. Created " + EXAMPLE_CONFIG_FILE +
                    ". Please copy it to " + CONFIG_FILE + " and configure.");
        }

        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
        }

        return props;
    }

    private static void createExampleConfig() throws IOException {
        Properties example = new Properties();
        example.setProperty("username", "Player");
        example.setProperty("uuid", "00000000-0000-0000-0000-000000000000");
        example.setProperty("accessToken", "0");
        example.setProperty("clientId", "0");
        example.setProperty("xuid", "0");
        example.setProperty("gameDir", "C:/path/to/your/.minecraft");
        example.setProperty("version", "1.21.8");

        try (FileOutputStream output = new FileOutputStream(EXAMPLE_CONFIG_FILE)) {
            example.store(output, "Minecraft Launcher Configuration - EXAMPLE");
        }
    }
}