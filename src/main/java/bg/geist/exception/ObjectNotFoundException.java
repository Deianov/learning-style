package bg.geist.exception;

import java.io.Serial;


public class ObjectNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Not found object '%s' with id: %s";
    private final EntityName entityName;

    public ObjectNotFoundException(EntityName entityName, Long id) {
        super(String.format(MESSAGE, entityName, id));
        this.entityName = entityName;
    }

    public EntityName getEntityName() {
        return entityName;
    }
    private void setEntityName(EntityName entityName) {}
}