import java.util.ArrayList;
import java.util.List;

public class PObject {
    public ArrayList<Double> coordinates;
    public String type;
    public int getType(String answer){
        if(type.equals(answer)) return 1;
        else return 0;
    }

    public PObject(ArrayList<Double> coordinates, String type) {
        this.coordinates = coordinates;
        this.type = type;
    }


}
