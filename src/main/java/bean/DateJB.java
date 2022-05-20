package bean;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DateJB {
    private String sunCal;
    private String animalSign;
    private String shadowCal;
    private String constellation;
    public String toString(){
        return sunCal + "  " + animalSign + "  " + shadowCal + "   " + constellation;
    }
    public static String[] header = new String[]{
            "sunCal","animalSign","shadowCal","constellation"
    };
    public String[] toStringList() {
        return new String[]{
                sunCal,animalSign,shadowCal,constellation
        };
    }
    public String getMonth_day(){
        return sunCal.substring(5);
    }
}
