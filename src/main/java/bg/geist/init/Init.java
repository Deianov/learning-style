package bg.geist.init;

import bg.geist.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class Init implements CommandLineRunner {
    private final static boolean DO_EXPORT_TO_JSON = true;
    private final static boolean DO_INIT = false;

    private final SeedDb seedDb;
    private final MarshalDb marshalDb;
    private final CategoryRepository categoryRepository;
    private final String version;
    private final Resource ymlFile;


    public Init(SeedDb seedDb, MarshalDb marshalDb, CategoryRepository categoryRepository,
                @Value("${myapp.version}") String version,
                @Value("classpath:application.yml") Resource ymlFile){
        this.seedDb = seedDb;
        this.marshalDb = marshalDb;
        this.categoryRepository = categoryRepository;
        this.version = version;
        this.ymlFile = ymlFile;
    }

    @Override
    public void run(String... args) throws Exception {

        showVersion();

        if (DO_INIT && categoryRepository.count() == 0) {
            seedDb.showVersion();
            seedDb.seedUsers();
            seedDb.seedCategories();
            seedDb.seedCards();
            seedDb.seedAnswersCollectionTemplates();
            seedDb.seedQuizzes();
            seedDb.seedMaps();
        }

        if (DO_EXPORT_TO_JSON) {
            marshalDb.prepareDirectories();
            marshalDb.marshalUsers();
            marshalDb.marshalCategories();
            marshalDb.marshalCards();
            marshalDb.marshalQuizzes();
            marshalDb.marshalMaps();
        }
    }

    public void showVersion() throws IOException {
        System.out.println(ymlFile.getFile().toString());
        System.out.printf("version: %s%n", version);
    }
}