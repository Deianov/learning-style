package bg.geist.schedules;

import bg.geist.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class ScheduleLogsClear {
    // second, minute, hour, day of month, month, day(s) of week
    private static final String cronExpression = "0 0 24 * * FRI";
    private static final String timeZone = "UTC";

    private final LogService logService;

    @Autowired
    public ScheduleLogsClear(LogService logService) {
        this.logService = logService;
    }

    @Async
//    @Scheduled(cron = cronExpression, zone = timeZone)
    @Scheduled(fixedDelay = 1000 * 15) // 15 sec.
    protected void clearLogs(){
        logService.clearBySchedule();
    }
}