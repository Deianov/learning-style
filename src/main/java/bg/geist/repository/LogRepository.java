package bg.geist.repository;

import bg.geist.domain.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    Collection<Log> findByTimeStampBeforeAndTags(Date date, String tags);
}