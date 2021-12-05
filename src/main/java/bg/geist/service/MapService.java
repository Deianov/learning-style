package bg.geist.service;

import bg.geist.domain.model.service.MapModel;
import bg.geist.web.api.model.ExerciseIndexModel;

import java.util.Collection;

public interface MapService {
    Collection<ExerciseIndexModel> getIndex();
    MapModel getById(Long id);
}
