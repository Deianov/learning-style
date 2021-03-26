package bg.geist.domain.entity;


public abstract class BaseEntityUUID {

    private static final long serialVersionUID = 1L;
    private String id;

    public BaseEntityUUID() { }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
