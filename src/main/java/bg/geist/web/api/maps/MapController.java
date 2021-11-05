package bg.geist.web.api.maps;

import bg.geist.service.MapService;
import bg.geist.web.api.exercise.ExerciseIndexModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path="/api/maps", produces = MediaType.APPLICATION_JSON_VALUE)
public class MapController {

    private final MapService service;

    @Autowired
    public MapController(MapService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Collection<ExerciseIndexModel>> getIndex(@RequestParam(required = false) String lang) {
        // TODO: 25.03.2021 lang param
        Collection<ExerciseIndexModel> index = service.getIndex();
        if (index.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(index, HttpStatus.OK);
    }
}
