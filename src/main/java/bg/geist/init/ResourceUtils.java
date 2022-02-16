package bg.geist.init;

import bg.geist.constant.Constants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


class ResourceUtils {
    private static final String JSON_FILENAME_PATTERN = Constants.PRJ.JSON_FILENAME_PATTERN;
    private static final String DEFAULT_FILTER = "/*.json";
    private final PathMatchingResourcePatternResolver resolver;

    ResourceUtils() {
        ClassLoader classLoader = MethodHandles.lookup().getClass().getClassLoader();
        resolver = new PathMatchingResourcePatternResolver(classLoader);
    }

    Resource getOne(String filePath){
        return resolver.getResource(filePath);
    }

    /** @param path - relative path in 'classpath' project directory Or null for home */
    Resource[] getAll(String path) throws IOException {
        String locationPattern = "classpath:" + (path == null ? "*" : path.contains("*") ? path : (path + DEFAULT_FILTER));

        return Arrays.stream(resolver.getResources(locationPattern))
            .filter(Objects::nonNull)
            .filter(Resource::isReadable) // skip directories
            .sorted(this::compareByName)
            .toArray(Resource[]::new);
    }

    String getContent(String filePath){
        return this.read(this.getOne(filePath));
    }

    String read(Resource r) {
        try (InputStream inputStream = r.getInputStream()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            return null;
        }
    }

    private int compareByName(Resource r1, Resource r2) {
        if (r1 == null || r2 == null) {
            return 0;
        }
        String a = r1.getFilename();
        String b = r2.getFilename();
        if (a == null || b == null) {
            return 0;
        }
        if (a.matches(JSON_FILENAME_PATTERN) && b.matches(JSON_FILENAME_PATTERN)) {
            int ai = Integer.parseInt(a.substring(0, a.lastIndexOf(".")));
            int bi = Integer.parseInt(b.substring(0, b.lastIndexOf(".")));
            return ai - bi;
        } else {
            return a.compareTo(b);
        }
    }
}