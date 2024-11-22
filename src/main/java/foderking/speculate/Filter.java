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
//    public Filter(String version_number) {
//        this.version_number = version_number;
//    }
}
