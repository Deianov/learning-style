package bg.geist.util;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public final class FilesUtil {
    private static final Charset charset = StandardCharsets.UTF_8;
    private static final File[] NO_FILES = {};

    public static String read(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String result = Files.readString(path, charset);
        return result;
    }

    public static void write(String content, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, Collections.singleton(content), charset);
    }

    public static boolean delete(File directory) {
        for (File f : listFiles(directory)) {
            delete(f);
        }
        return directory.delete();
    }

    public static File[] listFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                return files;
            }
        }
        return NO_FILES;
    }

    public static File[] listFiles(String path) {
        return listFiles(new File(path));
    }
}