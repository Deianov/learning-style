package bg.geist.service;

import bg.geist.domain.entity.Exercise;
import bg.geist.domain.entity.ExercisePlay;
import bg.geist.init.dto.ExerciseDto;
import bg.geist.domain.model.service.ExerciseModel;
import bg.geist.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExerciseServiceImpl implements ExerciseService {
    // todo: exerciseService.updateModel(exerciseEntity, exerciseModel)

    private final ExerciseRepository repository;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository repository) {
        this.repository = repository;
    }

    @Override
    public final ExerciseModel map(ExerciseDto dto){
        return
            new ExerciseModel(
                null,
                null,
                dto.exercise.get("name"),
                dto.exercise.get("category"),
                dto.exercise.get("description"),
                dto.exercise.get("source"),
                dto.exercise.get("sourceUrl"),
                dto.exercise.get("author"),
                dto.exercise.get("authorUrl"),
                dto.props == null ? null : dto.props.get("createdBy")
            );
    }

    @Override
    public final ExerciseModel map(Exercise exercise, ExercisePlay exercisePlay){
        return
            new ExerciseModel(
                exercisePlay.getId(),
                exercisePlay.getType().name().toLowerCase() + "/" + exercisePlay.getId(),
                exercise.getName(),
                exercisePlay.getCategory().getName(),
                exercise.getDescription(),
                exercise.getSource(),
                exercise.getSourceUrl(),
                exercise.getAuthor(),
                exercise.getAuthorUrl(),
                exercise.getCreatedBy()
            );
    }

    @Override
    public final Exercise seed(ExerciseModel model) {
        Exercise exercise = repository.findByName(model.getName()).orElse(null);
        if (exercise == null) {
            exercise = this.map(model);
            return repository.save(exercise);
        }
        return exercise;
    }

    private Exercise map(ExerciseModel model) {
        return
            new Exercise(
                model.getName(),
                model.getDescription(),
                model.getSource(),
                model.getSourceUrl(),
                model.getAuthor(),
                model.getAuthorUrl(),
                model.getCreatedBy()
            );
    }
}