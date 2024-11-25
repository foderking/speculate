package foderking.speculate;

import java.io.Serializable;

public class Filter implements Serializable {
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private String version;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    private Integer rating;
    private float thickness;

    public float getWeight() {
        return weight;
    }

    private float weight;

    public float getMax_temperature_idle() {
        return max_temperature_idle;
    }

    public float getMax_temperature_load() {
        return max_temperature_load;
    }

    public float getThickness() {
        return thickness;
    }

    private float max_temperature_load;
    private float max_temperature_idle;

//    public Filter(String version_number) {
//        this.version_number = version_number;
//    }
}
