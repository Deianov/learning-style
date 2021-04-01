package bg.geist.exception;

import java.io.Serial;

public class FieldAlreadyExistsException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Field '%s' already exists";
    private final String fieldName;


    public FieldAlreadyExistsException(final String fieldName){
        super(String.format(MESSAGE, fieldName));
        this.fieldName = fieldName;
    }

    public String getField(){
        return this.fieldName;
    }
}