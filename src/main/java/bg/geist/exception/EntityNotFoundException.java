package bg.geist.exception;

import bg.geist.domain.enums.EntityName;
import java.io.Serial;


public class EntityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Not found entity '%s' with id: %s";
    private final EntityName entityName;

    public EntityNotFoundException(EntityName entityName, Long id) {
        super(String.format(MESSAGE, entityName, id));
        this.entityName = entityName;
    }

    public EntityName getEntityName() {
        return entityName;
    }
    private void setEntityName(EntityName entityName) {}
}