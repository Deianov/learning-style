package bg.geist.service;

import bg.geist.domain.model.CardsModel;
import bg.geist.web.api.exercise.ExerciseIndexModel;

import java.util.Collection;
import java.util.Optional;

public interface CardsService {
    Collection<ExerciseIndexModel> getIndex();
    CardsModel getById(Long id);
}
