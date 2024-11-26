package foderking.speculate;

import java.io.Serializable;

public class Filter implements Serializable {

    private Float weight;
    private Float thickness;

    public void setMax_temperature_idle(Integer max_temperature_idle) {
        this.max_temperature_idle = max_temperature_idle;
    }

    public void setMax_temperature_load(Integer max_temperature_load) {
        this.max_temperature_load = max_temperature_load;
    }

    public void setThickness(Float thickness) {
        this.thickness = thickness;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    private Integer max_temperature_load;
    private Integer max_temperature_idle;

    public Float getWeight() {
        return weight;
    }

    public Integer getMax_temperature_idle() {
        return max_temperature_idle;
    }

    public Integer getMax_temperature_load() {
        return max_temperature_load;
    }

    public Float getThickness() {
        return thickness;
    }
    public String toString() {
        return String.format("w=%f,t_idle=%d,t_load=%d", weight, max_temperature_idle, max_temperature_load);
    }
}
