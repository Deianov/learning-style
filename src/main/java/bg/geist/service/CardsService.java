package bg.geist.service;

import bg.geist.domain.model.service.CardsModel;
import bg.geist.web.api.model.ExerciseIndexModel;

import java.util.Collection;

public interface CardsService {
    Collection<ExerciseIndexModel> getIndex();
    CardsModel getById(Long id);
    int update(Long id, CardsModel model);
}
