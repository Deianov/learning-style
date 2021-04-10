package bg.geist.service;

import bg.geist.domain.entity.Log;
import bg.geist.domain.model.service.LogModel;
import bg.geist.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {
    private static final String ANONYMOUS_AUTHENTICATION = "anonymous";
    private static final int SAVE_LAST_N_DAYS = 0;
    private static final String TAGS_CONTROLLERS = Log.Tag.CONTROLLERS.toString();
    private static final String CLEAR_MSG = "SCHEDULED LOGS CLEARING: %d logs";

    private final LogRepository repository;

    @Autowired
    public LogServiceImpl(LogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createLog(Log.Level level, String tags, String action) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? ANONYMOUS_AUTHENTICATION : authentication.getName();

        Log log = new Log(username, level, tags, action);
        repository.save(log);
    }

    @Override
    public void clearBySchedule() {
        Date saveDate = getDateBefore(SAVE_LAST_N_DAYS);
        Collection<Log> logs = repository.findByTimeStampBeforeAndTags(saveDate, TAGS_CONTROLLERS);
        repository.deleteAll(logs);
        createLog(Log.Level.INFO, TAGS_CONTROLLERS, String.format(CLEAR_MSG, logs.size()));
    }

    private Collection<LogModel> getLogs() {
        return repository.findAll().stream().map(this::map).collect(Collectors.toCollection(ArrayList::new));
    }

    private LogModel map(Log log) {
        return new LogModel(log.getId(),
                log.getUsername(),
                log.getLevel().toString(),
                log.getTags(),
                log.getAction(),
                log.getTimeStamp()
        );
    }

    private Date getDateBefore(int days) {
        Instant instant = Instant.now();
        // instant.toEpochMilli() - (days * 86400 seconds * 1000)
        return Date.from(instant.minus(days, ChronoUnit.DAYS));
    }
}