package bg.geist.web.api.cards;

import bg.geist.domain.model.service.CardsModel;
import bg.geist.domain.model.service.UserProfileModel;
import bg.geist.service.CardsService;
import bg.geist.service.UserService;
import bg.geist.web.api.model.ExerciseIndexModel;
import bg.geist.web.api.model.RequestModel;
import bg.geist.web.api.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping(path = "/api/cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardsController {

    private final CardsService cardsService;
    private final UserService userService;

    @Autowired
    public CardsController(CardsService cardsService, UserService userService) {
        this.cardsService = cardsService;
        this.userService = userService;
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

    // todo: fast home fix
    @PostMapping("/{id}")
    public ResponseEntity<ResponseModel> patchDictionary(@PathVariable Long id, @RequestBody RequestModel model) {
        final String MSG_UPDATED = "UPDATED: %d";
        final String MSG_ADMIN = "Administrator required.";
        final String MSG_NO_CHANGES = "NOT FOUND CHANGES!";

        if (id == null || model == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        String username = model.getUsername();
        if (username != null && !username.isEmpty()) {
            UserProfileModel profile = userService.profile(model.getUsername(), model.getUsername());
            if (profile != null && profile.isAdmin()) {

                CardsModel cardsModel = new CardsModel();
                cardsModel.setData(model.getData());
                int updated = cardsService.update(id, cardsModel);

                String msg = updated > 0 ? String.format(MSG_UPDATED, updated) : MSG_NO_CHANGES;
                String [][] data = cardsService.getById(id).getData();

                ResponseModel response = new ResponseModel(msg, HttpStatus.OK, cardsService.getById(id).getData());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseModel(MSG_ADMIN, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }
}