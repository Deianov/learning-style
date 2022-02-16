package bg.geist.init;

import bg.geist.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
class Init implements CommandLineRunner {
    private static final boolean DO_EXPORT_TO_JSON = false;
    private static final boolean DO_INIT = true;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final SeedDb seedDb;
    private final MarshalDb marshalDb;
    private final CategoryRepository categoryRepository;
    private final String version;


    Init(@Value("${myapp.version}") String version, SeedDb seedDb, MarshalDb marshalDb, CategoryRepository categoryRepository){
        this.seedDb = seedDb;
        this.marshalDb = marshalDb;
        this.categoryRepository = categoryRepository;
        this.version = version;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.debug(String.format("%n***%n%s version: %s%n -init: run", "learning-style", version));

        if (DO_INIT && categoryRepository.count() == 0) {
            seedDb.readJsonExample();
            seedDb.seedUsers();
            seedDb.seedCategories();
            seedDb.seedCards();
            seedDb.seedAnswerTemplates();
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
}