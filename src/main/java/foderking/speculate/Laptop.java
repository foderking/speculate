package foderking.speculate;

import jakarta.persistence.*;
import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(indexes = {@Index(columnList = "LINK")})
public class Laptop implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    private String link;
    private String reviewer;
    private String review_date;
    private String picture;
    private String model;
    private Float review_version;
    @ElementCollection
    @Column(columnDefinition="TEXT", length = 1000)
    private Map<String, String> info;
    private int rating;
    private float length;
    private float width;
    private float thickness;
    private float weight;
    private float max_temperature_load;
    private float max_temperature_idle;
    private float battery;
    private float coverage_srgb;
    private float coverage_adobergb;
    private float coverage_p3;
    private float pwm_freq;
    private float brightness;
    private float brightness_dist;
    private float contrast;
    private float response_bw;
    private float response_gg;

    @ElementCollection
    private List<String> display_info;
    @ElementCollection
    private Map<String, String> compare_tables;
    @ElementCollection
    private Map<String, String> compare_bars;
    @ElementCollection
    private Map<String, String> bar_charts;


    public float getBattery() {
        return battery;
    }
    public void setBattery(float battery) {
        this.battery = battery;
    }
    public float getMaxTemperatureLoad(){
        return max_temperature_load;
    }
    public void setMaxTemperatureLoad(float max_temperature_load){
        this.max_temperature_load = max_temperature_load;
    }
    public float getMaxTemperatureIdle(){
        return max_temperature_idle;
    }
    public void setMaxTemperatureIdle(float max_temperature_idle){
        this.max_temperature_idle = max_temperature_idle;
    }
    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public float getThickness() {
        return thickness;
    }
    public void setThickness(float thickness) {
        this.thickness = thickness;
    }
    public float getWidth() {
        return width;
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public float getLength() {
        return length;
    }
    public void setLength(float length) {
        this.length = length;
    }
    public Map<String, String> getInfo() {
        return info;
    }
    public void setInfo(Map<String, String> info) {
        this.info = info;
    }
    public float getReviewVersion() {
        return review_version;
    }
    public void setReviewVersion(float review_version) {
        this.review_version = review_version;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getReviewDate() {
        return review_date;
    }
    public void setReviewDate(String review_date) {
        this.review_date = review_date;
    }
    public String getReviewer() {
        return reviewer;
    }
    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public float getResponseGG(){
        return response_gg;
    }
    public void setResponseGG(float response_gg){
        this.response_gg = response_gg;
    }
    public float getResponseBw(){
        return response_bw;
    }
    public void setResponseBw(float bw){
        this.response_bw = bw;
    }
    public float getContrast(){
        return contrast;
    }
    public void setContrast(float contrast){
        this.contrast = contrast;
    }
    public float getBrightnessDist(){
        return brightness_dist;
    }
    public void setBrightnessDist(float brightness_dist){
        this.brightness_dist = brightness_dist;
    }
    public float getBrightness(){
        return brightness;
    }
    public void setBrightness(float brightness){
        this.brightness = brightness;
    }
    public float getPWMFrequency(){
        return pwm_freq;
    }
    public void setPWMFrequency(float pwm_freq){
        this.pwm_freq = pwm_freq;
    }
    public float getCoverageP3(){
        return coverage_p3;
    }
    public void setCoverageP3(float coverage_p3){
        this.coverage_p3 = coverage_p3;
    }
    public float getCoverageAdobeRGB(){
        return coverage_adobergb;
    }
    public void setCoverageAdobeRGB(float coverage_adobergb){
        this.coverage_adobergb = coverage_adobergb;
    }
    public float getCoverageSRGB(){
        return coverage_srgb;
    }
    public void setCoverageSRGB(float coverage_srgb){
        this.coverage_srgb = coverage_srgb;
    }

    public Map<String, String> getCompareBars() {
        return compare_bars;
    }
    public void setCompareBars(Map<String, String> compare_bars) {
        this.compare_bars = compare_bars;
    }
    public Map<String, String> getCompareTables() {
        return compare_tables;
    }
    public void setCompareTables(Map<String, String> compare_tables) {
        this.compare_tables = compare_tables;
    }
    public List<String> getDisplayInfo() {
        return display_info;
    }
    public void setDisplayInfo(List<String> display_info) {
        this.display_info = display_info;
    }
    public Map<String, String> getBarCharts() {
        return bar_charts;
    }
    public void setBarCharts(Map<String, String> bar_charts) {
        this.bar_charts = bar_charts;
    }

    public Laptop(
            String link,
            String reviewer,
            String review_date,
            String picture,
            String model,
            float review_version,
            Map<String, String> info,
            int rating,
            float length,
            float width,
            float thickness,
            float weight,
            float max_temperature_load,
            float max_temperature_idle,
            float battery,
            float coverage_srgb,
            float coverage_adobergb,
            float coverage_p3,
            float pwm_freq,
            float brightness,
            float brightness_dist,
            float contrast,
            float response_bw,
            float response_gg,
            Map<String, String> compare_tables,
            Map<String, String> compare_bars,
            Map<String, String> bar_charts,
            List<String> display_info
    ) {
        this.link = link;
        this.reviewer = reviewer;
        this.review_date = review_date;
        this.picture = picture;
        this.model = model;
        this.review_version = review_version;
        this.info = info;
        this.rating = rating;
        this.length = length;
        this.width = width;
        this.thickness = thickness;
        this.weight = weight;
        this.max_temperature_load = max_temperature_load;
        this.max_temperature_idle = max_temperature_idle;
        this.battery = battery;
        this.compare_tables = compare_tables;
        this.compare_bars = compare_bars;
        this.bar_charts = bar_charts;
        this.display_info = display_info;
        this.coverage_srgb = coverage_srgb;
        this.coverage_adobergb = coverage_adobergb;
        this.coverage_p3 = coverage_p3;
        this.pwm_freq = pwm_freq;
        this.brightness = brightness;
        this.brightness_dist = brightness_dist;
        this.contrast = contrast;
        this.response_bw = response_bw;
        this.response_gg = response_gg;
    }

    public Laptop() {
    }

    public static Optional<Laptop> create(String link) {
        return LaptopParser.createDoc(link).map(doc -> {
            Optional<Element> svg_node = LaptopParser.selectDimensionSVG(doc);
            Map<String, String[]> temperature_info = LaptopParser.createTemperatureInfo(doc);
            Map<String, String> compare_tables = LaptopParser.createCompareTables(doc);
            Map<String, String> compare_bars = LaptopParser.createCompareBars(doc);
            Map<String, String> bar_charts = LaptopParser.createBarCharts(doc);
            List<String> display_info = LaptopParser.createDisplayInfo(doc);
            return new Laptop(
                    link,
                    LaptopParser.parseReviewer(doc),
                    LaptopParser.parseReviewDate(doc),
                    LaptopParser.parsePicture(doc),
                    LaptopParser.parseModel(doc),
                    LaptopParser.parseReviewVersion(doc),
                    LaptopParser.parseInfo(doc),
                    LaptopParser.parseRating(doc),
                    svg_node
                        .map(LaptopParser::parseLength)
                        .orElse(-1f),
                    svg_node
                        .map(LaptopParser::parseWidth)
                        .orElse(-1f),
                    svg_node
                        .map(LaptopParser::parseThickness)
                        .orElse(-1f),
                    svg_node
                        .map(LaptopParser::parseWeight)
                        .orElse(-1f),
                    LaptopParser.parseMaxTemperatureLoad(temperature_info),
                    LaptopParser.parseMaxTemperatureIdle(temperature_info),
                    LaptopParser.parseBattery(compare_tables, compare_bars, bar_charts),
                    LaptopParser.parseCoverageSRGB(compare_tables, display_info),
                    LaptopParser.parseCoverageAdobeRGB(compare_tables, display_info),
                    LaptopParser.parseCoverageP3(compare_tables, display_info),
                    LaptopParser.parsePWM(compare_tables),
                    LaptopParser.parseBrightness(compare_tables, display_info),
                    LaptopParser.parseBrightnessDistribution(compare_tables, display_info),
                    LaptopParser.parseContrast(compare_tables, display_info),
                    LaptopParser.parseResponseBW(compare_tables),
                    LaptopParser.parseResponseGG(compare_tables),
                    compare_tables,
                    compare_bars,
                    bar_charts,
                    display_info
            );
        });
    }

    @Override
    public String toString() {
        return "Laptop[" +
                "link=" + link + ", " +
                "reviewer=" + reviewer + ", " +
                "reviewDate=" + review_date + ", " +
                "picture=" + picture + ", " +
                "model=" + model + ", " +
                "reviewVersion=" + review_version;
    }

}
