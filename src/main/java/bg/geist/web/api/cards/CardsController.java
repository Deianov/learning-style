package bg.geist.web.api.cards;

import bg.geist.domain.model.CardsModel;
import bg.geist.service.CardsService;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;


@RestController
@RequestMapping(path = "/api/cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardsController {

    private final CardsService cardsService;

    public CardsController(CardsService cardsService) {
        this.cardsService = cardsService;
    }

    @GetMapping
    public ResponseEntity<Collection<ExerciseIndexModel>> getIndex(@RequestParam(required = false) String lang) {
        // TODO: 25.03.2021 lang param
        Collection<ExerciseIndexModel> index = cardsService.getIndex();
        if (index.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(index, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardsModel getModel(@PathVariable Long id) {
        return cardsService.getById(id);
    }
}