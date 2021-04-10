package bg.geist.service;

import bg.geist.domain.entity.Log;


public interface LogService {
    void createLog(Log.Level level, String tags, String action);
    void clearBySchedule();
}
