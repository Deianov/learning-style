package bg.geist.service;


import bg.geist.domain.entity.Exercise;
import bg.geist.domain.entity.ExercisePlay;
import bg.geist.init.dto.ExerciseDto;
import bg.geist.domain.model.service.ExerciseModel;


public interface ExerciseService {
    Exercise seed(ExerciseModel model);
    ExerciseModel map(ExerciseDto dto);
    ExerciseModel map(Exercise exercise, ExercisePlay exercisePlay);
}