package bg.geist.domain.model.service;



public class CardsModel extends ExerciseParentModel {
    private String[][] data;


    public CardsModel() { }

    public String[][] getData() {
        return data;
    }
    public void setData(String[][] data) {
        this.data = data;
    }
}
