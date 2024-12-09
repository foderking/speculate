package foderking.speculate;

import org.springframework.data.domain.Sort;

import java.io.Serializable;

public class Filter implements Serializable {

    private Float weight;
    private Float thickness;
    private Float p3;
    private Float sRGB;
    private SortParameters sort;
    private Sort.Direction sort_dir;

    public Float getResponse() {
        return response;
    }

    public void setResponse(Float response) {
        this.response = response;
    }

    private Float response;

    public Float getBrightness() {
        return brightness;
    }

    public void setBrightness(Float brightness) {
        this.brightness = brightness;
    }

    private Float brightness;

    public Float getAdobeRGB() {
        return adobeRGB;
    }

    public void setAdobeRGB(Float adobeRGB) {
        this.adobeRGB = adobeRGB;
    }

    public Float getsRGB() {
        return sRGB;
    }

    public void setsRGB(Float sRGB) {
        this.sRGB = sRGB;
    }

    public Float getP3() {
        return p3;
    }

    public void setP3(Float p3) {
        this.p3 = p3;
    }

    private Float adobeRGB;

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    private Integer battery;

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

    public SortParameters getSort() {
        return sort;
    }

    public void setSort(SortParameters sort) {
        this.sort = sort;
    }

    public Sort.Direction getSort_dir() {
        return sort_dir;
    }

    public void setSort_dir(Sort.Direction sort_dir) {
        this.sort_dir = sort_dir;
    }
}
