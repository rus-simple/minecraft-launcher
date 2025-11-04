import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MinecraftLauncher {

    public static void main(String[] args) throws Exception {
        // Загружаем конфигурацию
        Properties config = ConfigLoader.loadConfig();

        String GAME_DIR = config.getProperty("gameDir");
        String VERSION = config.getProperty("version");

        System.out.println("Запуск Minecraft " + VERSION + "...");

        // Собираем classpath
        List<URL> classpathUrls = new ArrayList<>();

        // Добавляем основной JAR игры
        File gameJar = new File(GAME_DIR + "/versions/" + VERSION + "/" + VERSION + ".jar");
        classpathUrls.add(gameJar.toURI().toURL());
        System.out.println("Добавлен: " + gameJar.getName());

        // Добавляем все библиотеки из папки libraries
        File librariesDir = new File(GAME_DIR + "/libraries");
        addLibraries(librariesDir, classpathUrls);

        System.out.println("Всего библиотек: " + (classpathUrls.size() - 1));

        // Создаем classloader с собранными путями (с try-with-resources)
        try (URLClassLoader classLoader = new URLClassLoader(
                classpathUrls.toArray(new URL[0]),
                ClassLoader.getSystemClassLoader()
        )) {
            // Загружаем главный класс Minecraft
            Class<?> mainClass = classLoader.loadClass("net.minecraft.client.main.Main");

            // Подготавливаем аргументы из конфига
            String[] minecraftArgs = {
                    "--username", config.getProperty("username"),
                    "--version", VERSION,
                    "--gameDir", GAME_DIR,
                    "--assetsDir", GAME_DIR + "/assets",
                    "--assetIndex", "26",
                    "--uuid", config.getProperty("uuid"),
                    "--accessToken", config.getProperty("accessToken"),
                    "--clientId", config.getProperty("clientId"),
                    "--xuid", config.getProperty("xuid"),
                    "--userType", "OFFLINE",
                    "--versionType", "release"
            };

            // Устанавливаем системные свойства
            System.setProperty("java.library.path", GAME_DIR + "/versions/" + VERSION + "/natives");
            System.setProperty("jna.tmpdir", GAME_DIR + "/versions/" + VERSION + "/natives");
            System.setProperty("org.lwjgl.system.SharedLibraryExtractPath", GAME_DIR + "/versions/" + VERSION + "/natives");
            System.setProperty("io.netty.native.workdir", GAME_DIR + "/versions/" + VERSION + "/natives");

            // Запускаем Minecraft
            Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) minecraftArgs);
        } // classLoader автоматически закроется здесь
    }

    private static void addLibraries(File dir, List<URL> urls) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        addLibraries(file, urls);
                    } else if (file.getName().endsWith(".jar")) {
                        try {
                            urls.add(file.toURI().toURL());
                        } catch (Exception e) {
                            System.err.println("Ошибка добавления: " + file.getPath());
                        }
                    }
                }
            }
        }
    }
}