package foderking.speculate;

import jakarta.persistence.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
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

    public static String parseReviewer(Document doc) {
        // reviews below v8 are parsed differently
        Elements intro_author = doc.select(".intro-author");
        if (!intro_author.isEmpty()) {
            return intro_author
                    .first()
                    .text()
                    .split(",")[0]
                    .replace("\uD83D\uDC41","")
                    .strip();
            // v8 and above
        } else {
            return doc
                    .select("div.nbcintroelwide_abstract")
                    .first()
                    .childNode(3)
                    .outerHtml()
                    .replace("\uD83D\uDC41","")
                    .strip();
        }
    }
    public static String parseReviewDate(Document doc) {
        return doc
                .select(".intro-date > time:nth-child(1)")
                .attr("datetime");
    }
    public static String parsePicture(Document doc) {
        // reviews below v8 are parsed differently
        Elements orig = doc.select(".csc-textpic-above");
        if (!orig.isEmpty()) {
            return orig
                    .first()
                    .select("div:nth-child(1) > div:nth-child(1) > figure:nth-child(1) > img:nth-child(1)")
                    .attr("src");
        } else {
            return doc
                    .select(".news-teaser-image picture img")
                    .attr("src");
        }
    }
    public static String parseModel(Document doc) {
        return doc
                .select(".specs_header")
                .stream()
                .map(e ->
                    e
                    .text()
                    .split(" [(]")[0]
                )
                .findFirst()
                .orElse("");

    }
    public static float parseReviewVersion(Document doc) {
        return doc
                .select("#tspan4368")
                .stream()
                .map(e ->
                    Float.parseFloat(
                        e
                        .text()
                        .split("- v")[1]
                    )
                )
                .findFirst().orElse(-1f);
    }
    public static Map<String, String> parseInfo(Document doc) {
        return doc
                .select(".specs_whole > .specs_element")
                .stream()
                .filter(
                    e -> !e.hasAttr("style")
                )
                .map(
                    e -> e
                        .children()
                        .stream()
                        .map(Element::text)
                        .toArray(String[]::new)
                )
                .collect(
                    Collectors.toMap(
                        i -> i[0],
                        i -> (i.length >1 ? i[1] : ""))
                );
    }
    public static int parseRating(Document doc) {
        return
            doc
                .select("#tspan4350")
                .stream()
                .map(e ->
                    Integer.parseInt(
                        e
                        .text()
                        .split("%[)]")[0]
                        .split("[(]")[1]
                    )
                )
                .findFirst().orElse(-1);
    }
    public static float parseLength(Element dimension_svg){
        return  dimension_svg
                .select("rect")
                .eachAttr("width")
                .stream().map(Float::parseFloat)
                .max(Float::compareTo)
                .get();
    }
    public static float parseThickness(Element dimension_svg){
        return  dimension_svg
                .select("rect")
                .eachAttr("width")
                .stream().map(Float::parseFloat)
                .min(Float::compareTo)
                .get();
    }
    public static float parseWidth(Element dimension_svg){
        return Float.parseFloat(
            dimension_svg
                .select("rect")
                .eachAttr("height")
                .getFirst()
        );
    }
    public static float parseWeight(Element dimension_svg){
            return dimension_svg
                    .select("text[text-anchor]")
                    .eachText()
                    .stream()
                    .filter(e -> e.endsWith("kg"))
                    .findFirst()
                    .map(e ->
                        Float.parseFloat(
                            e.split(" ")[0]
                        )
                    )
                    .orElse(-1f);
    }
    public static float parseMaxTemperatureLoad(Map<String, String[]> temperature_info){
        if (!temperature_info.containsKey("Max. Load")) {
            return -1f;
        }
        return Arrays.stream(temperature_info.get("Max. Load"))
                .map(each -> each.split(" °C")[0].split(" ")[1])
                .map(Float::parseFloat)
                .max(Float::compareTo)
                .orElse(-1f);
    }
    public static float parseMaxTemperatureIdle(Map<String, String[]> temperature_info){
        if (!temperature_info.containsKey("Idle")) {
            return -1f;
        }
        return Arrays.stream(temperature_info.get("Idle"))
                .map(each -> each.split(" °C")[0].split(" ")[1])
                .map(Float::parseFloat)
                .max(Float::compareTo)
                .orElse(-1f);
    }
    public static float parseBattery(Map<String, String> compare_tables, Map<String, String> compare_bars, Map<String,String> bar_charts){
        // order is important
        List<String> table_keys = List.of(
            "Battery Runtime:WiFi v1.3",
            "Battery Runtime:WiFi Websurfing",
            "WiFi Websurfing"
        );
        List<String> bar_keys = List.of(
            "WiFi Websurfing",
            "Battery Runtime - WiFi",
            "Battery Runtime - WiFi (sort by value)",
            "Battery Runtime - WiFi Websurfing",
            "Battery Runtime - WiFi v1.3 (sort by value)",
            "Battery Runtime - WiFi Websurfing (sort by value)",
            "Battery Runtime:WiFi v1.3",
            "Battery Runtime:WiFi v1.3 (sort by value)"
        );
        List<String> chart_keys = List.of(
            "WiFi Websurfing",
            "WiFi Surfing"
        );
        for (String key: table_keys){
            Optional<String> value = extractTableValue(compare_tables, key);
            if (value.isPresent()){
                return Float.parseFloat(
                    value.get()
                );
            }
        }
        for (String key: bar_keys){
            Optional<String> value = extractTableValue(compare_tables, key);
            if (value.isPresent()){
                return Float.parseFloat(
                    value.get()
                );
            }
        }
        for (String key: chart_keys){
            Optional<String> value = extractTableValue(compare_tables, key);
            if (value.isPresent()){
                String[] tmp = value.get().split("h");
                float hours  = Float.parseFloat(tmp[0]);
                float minutes = Float.parseFloat(tmp[1].substring(1,3));
                return hours * 60 + minutes;
            }
        }
        for (String key: bar_charts.keySet()) {
            if (key.startsWith("WiFi Websurfing")) {
                String[] tmp = bar_charts.get(key).split("h");
                float hours  = Float.parseFloat(tmp[0]);
                float minutes = Float.parseFloat(tmp[1].split("m")[0]);
                return hours * 60 + minutes;
            }
        }
        return -1;
    }
    public static Optional<String> extractTableValue(Map<String,String> compare_tables, String key){
        if (compare_tables.containsKey(key) && !compare_tables.get(key).isEmpty()){
            return Optional.of(compare_tables.get(key));
        }
        else{
            return Optional.empty();
        }
    }
    public static float parseCoverageSRGB(Map<String,String> compare_tables, List<String> display_info){
        Optional<String> value = extractTableValue(compare_tables, "Display:sRGB Coverage");
        if (value.isPresent()){
            return Float.parseFloat(
                value.get()
            );
        }
        for (String info: display_info){
            if (info.endsWith("sRGB (Argyll 2.2.0 3D)")){
                return Float.parseFloat(
                    info.split("%")[0]
                );
            }
        }
        return -1f;
    }
    public static float parseCoverageAdobeRGB(Map<String,String> compare_tables, List<String> display_info){
        Optional<String> value = extractTableValue(compare_tables, "Display:AdobeRGB 1998 Coverage");
        if (value.isPresent()){
            return Float.parseFloat(
                value.get()
            );
        }
        for (String info: display_info){
            if (info.endsWith("AdobeRGB 1998 (Argyll 2.2.0 3D)") || info.endsWith("AdobeRGB 1998 (Argyll 1.6.3 3D)")){
                return Float.parseFloat(
                    info.split("%")[0]
                );
            }
        }
        return -1f;
    }
    public static float parseCoverageP3(Map<String,String> compare_tables, List<String> display_info){
        Optional<String> value = extractTableValue(compare_tables, "Display:Display P3 Coverage");
        if (value.isPresent()){
            return Float.parseFloat(
                value.get()
            );
        }
        for (String info: display_info){
            if (info.endsWith(" Display P3 (Argyll 2.2.0 3D)")){
                return Float.parseFloat(
                    info.split("%")[0]
                );
            }
        }
        return -1f;
    }
    public static float parsePWM(Map<String,String> compare_tables){
        if (compare_tables.containsKey("Response Times:PWM Frequency") && !compare_tables.get("Response Times:PWM Frequency").isEmpty()){
            return Float.parseFloat(
                compare_tables.get("Response Times:PWM Frequency")
            );
        }
        if (compare_tables.containsKey("Response Times:PWM Frequency") && compare_tables.get("Response Times:PWM Frequency").isEmpty()){
            // no pwm detected
            return Float.MAX_VALUE;
        }
        return -1f;
    }
    public static float parseBrightness(Map<String,String> compare_tables, List<String> display_info){
        Optional<String> value = extractTableValue(compare_tables, "Screen:Brightness");
        if (value.isPresent()){
            return Float.parseFloat(
                value.get()
            );
        }
        for (String info: display_info){
            if (info.startsWith("Maximum")){
                return Float.parseFloat(
                    info.split("Average: ")[1].split(" cd/m")[0]
                );
            }
        }
        return -1f;
    }
    public static float parseBrightnessDistribution(Map<String,String> compare_tables, List<String> display_info){
        Optional<String> value = extractTableValue(compare_tables, "Screen:Brightness Distribution");
        if (value.isPresent()){
            return Float.parseFloat(
                value.get()
            );
        }
        for (String info: display_info){
            if (info.startsWith("Brightness Distribution")){
                return Float.parseFloat(
                    info.substring(25).split(" %")[0]
                );
            }
        }
        return -1f;
    }
    public static float parseContrast(Map<String,String> compare_tables, List<String> display_info){
        String key = "Screen:Contrast";
        if (compare_tables.containsKey(key) && !compare_tables.get(key).isEmpty()){
            return Float.parseFloat(
                compare_tables.get(key)
            );
        }
        if (compare_tables.containsKey(key) && compare_tables.get(key).isEmpty()){
            return Float.MAX_VALUE; // infinite contrast (OLED)
        }
        for (String info: display_info) {
            if (info.startsWith("Contrast")) {
                return Float.parseFloat(
                    info.substring(10).split(":")[0]
                );
            }
        }
        return -1f;
    }
    public static float parseResponseBW(Map<String,String> compare_tables){
        Optional<String> value = extractTableValue(compare_tables, "Response Times:Response Time Black / White *");
        if (value.isPresent()){
            return Float.parseFloat(
                value.get()
            );
        }
        return -1f;
    }
    public static float parseResponseGG(Map<String,String> compare_tables){
        Optional<String> value = extractTableValue(compare_tables, "Response Times:Response Time Grey 50% / Grey 80% *");
        if (value.isPresent()){
            return Float.parseFloat(
                value.get()
            );
        }
        return -1f;
    }

    public static List<String> createDisplayInfo(Document doc){
        return doc
                .select("div.auto_analysis")
                .stream().findFirst()
                .map(e ->
                    e
                    .textNodes()
                    .stream()
                    .map(TextNode::text)
                    .toList()
                )
                .orElse(new ArrayList<>());
    }
    public static List<String> createLinksFromSearch(Document doc){
        return doc
                .select("table#search td > a")
                .eachAttr("href");
    }
    public static Map<String, String[]> createTemperatureInfo(Document doc){
        return doc
                    .select("div.nbc2rdisplay_smenu div[style=\";padding:5px;margin-bottom:5px;background-color:#ababab\"]")
                    .stream()
                    .collect(
                        Collectors.toMap(
                            e -> e.text(),
                            e ->
                                e
                                .parent()
                                .parent()
                                .select("td.nbc2rdisplay_avgmax")
                                .textNodes()
                                .stream()
                                .map(TextNode::text)
                                .toArray(String[]::new),
                                (key1, key2) -> key1 // fix duplicate key bug
                        )
                    );
    }
    public static Optional<Document> createDoc(String link) {
        try {
            return Optional.of(Jsoup
                    .connect(link)
                    .userAgent("Mozilla")
                    .timeout(0)
                    .maxBodySize(0)
                    .get());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
    public static Optional<Element> selectDimensionSVG(Document doc) {
        Elements comparison_labels = doc
                .select("div.csc-default div.nbc_element span[style] > label");
        return comparison_labels.stream()
                .filter(e -> e.childrenSize() == 2)
                .findFirst()
                .map( (Element e) -> {
                    String id = e.attr("for").split("_")[1];
                    String svg_id = "g#showDeviceSize_" + id;
                    return doc.selectFirst(svg_id);
                });
    }
    public static Map<String, String> createCompareTables(Document doc){
        HashMap<String, String> root_hashmap = new HashMap();
        Elements compared_tables = doc.select("table.comparetable");
        for (Element compared_table: compared_tables){
            String key = "";
            for(Element tr: compared_table.select("tr")){ // assumes order!
                if (tr.hasClass("tr-even") || tr.hasClass("tr-odd")){
                    root_hashmap
                        .put(
                            key + ":" + tr.child(0).text(),
                            tr.child(1).text().split(" ")[0]
                        );
                }
                else {
                    key = tr.child(0).text();
                }
            }
        }
        return root_hashmap;
    }
    public static Map<String, String> createCompareBars(Document doc){
        HashMap<String, String> root_hashmap = new HashMap();
        Elements compared_bars = doc.select("table.r_compare_bars");
        for (Element compared_bar: compared_bars){
            String prefix = compared_bar.selectFirst("td.prog_header").text();
            String key = prefix; // some don't have keys
            for (Element tr: compared_bar.select("tr")){
                if (tr.child(0).hasClass("settings_header")){
                    key = prefix + ":" + tr.child(0).text();
                }
                else if (tr.hasClass("referencespecs")) {
                    root_hashmap
                    .put(
                        key,
                        tr.selectFirst(" td.bar span.r_compare_bars_value").child(0).text()
                    );
                }
                else {}
            }
        }
        return root_hashmap;

    }
    public static Map<String, String> createBarCharts(Document doc){
        return doc
                .select("table.barcharts tr td.caption")
                .stream()
                .collect(
                    Collectors.toMap(
                        td -> td.text(),
                        td -> td.parent().parent().selectFirst("td.runtime").text(),
                        (key1, key2) -> key1 // fix duplicate key bug
                    )
                );
    }
    public static Elements selectStatsNode(Document doc){
        return doc.select("div.ttcl_0 div.nbc_element");
    }
    public static Optional<Laptop> create(String link) {
        return createDoc(link).map(doc -> {
            Optional<Element> svg_node = selectDimensionSVG(doc);
            Map<String, String[]> temperature_info = createTemperatureInfo(doc);
            Map<String, String> compare_tables = createCompareTables(doc);
            Map<String, String> compare_bars = createCompareBars(doc);
            Map<String, String> bar_charts = createBarCharts(doc);
            List<String> display_info = createDisplayInfo(doc);
            return new Laptop(
                    link,
                    Laptop.parseReviewer(doc),
                    parseReviewDate(doc),
                    parsePicture(doc),
                    parseModel(doc),
                    parseReviewVersion(doc),
                    parseInfo(doc),
                    parseRating(doc),
                    svg_node
                        .map(Laptop::parseLength)
                        .orElse(-1f),
                    svg_node
                        .map(Laptop::parseWidth)
                        .orElse(-1f),
                    svg_node
                        .map(Laptop::parseThickness)
                        .orElse(-1f),
                    svg_node
                        .map(Laptop::parseWeight)
                        .orElse(-1f),
                    parseMaxTemperatureLoad(temperature_info),
                    parseMaxTemperatureIdle(temperature_info),
                    parseBattery(compare_tables, compare_bars, bar_charts),
                    parseCoverageSRGB(compare_tables, display_info),
                    parseCoverageAdobeRGB(compare_tables, display_info),
                    parseCoverageP3(compare_tables, display_info),
                    parsePWM(compare_tables),
                    parseBrightness(compare_tables, display_info),
                    parseBrightnessDistribution(compare_tables, display_info),
                    parseContrast(compare_tables, display_info),
                    parseResponseBW(compare_tables),
                    parseResponseGG(compare_tables),
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
