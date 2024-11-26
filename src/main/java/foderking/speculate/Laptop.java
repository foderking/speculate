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
    private int review_version;
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
    private int battery;

    @ElementCollection
    private List<String> display_info;
    @ElementCollection
    private Map<String, String> compare_tables;
    @ElementCollection
    private Map<String, String> compare_bars;


    public int getBattery() {
        return battery;
    }
    public void setBattery(int battery) {
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
    public int getReviewVersion() {
        return review_version;
    }
    public void setReviewVersion(int review_version) {
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

    public Laptop(
            String link,
            String reviewer,
            String review_date,
            String picture,
            String model,
            int review_version,
            Map<String, String> info,
            int rating,
            float length,
            float width,
            float thickness,
            float weight,
            float max_temperature_load,
            float max_temperature_idle,
            int battery,
            Map<String, String> compare_tables,
            Map<String, String> compare_bars,
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
        this.display_info = display_info;
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
                .first()
                .text()
                .split(" [(]")[0];
    }
    public static int parseReviewVersion(Document doc) {
        return Integer.parseInt(
            doc
            .select("#tspan4368")
            .text()
            .split(" ")[4]
            .substring(1)
        );
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
        return Integer.parseInt(
            doc
                .select("#tspan4350")
                .text()
                .split("%[)]")[0]
                .split("[(]")[1]
        );
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
        return Float.parseFloat(
                dimension_svg
                    .select("text[text-anchor]")
                    .eachText()
                    .stream()
                    .filter(e -> e.endsWith("kg"))
                    .findFirst()
                    .get()
                    .split(" ")[0]
        );
    }
    public static float parseMaxTemperatureLoad(Map<String, String[]> temperature_info){
            return Arrays.stream(temperature_info.get("Max. Load"))
                    .map(each -> each.split(" °C")[0].split(" ")[1])
                    .map(Float::parseFloat)
                    .max(Float::compareTo)
                    .orElse(-1f);
    }
    public static float parseMaxTemperatureIdle(Map<String, String[]> temperature_info){
        return Arrays.stream(temperature_info.get("Idle"))
                .map(each -> each.split(" °C")[0].split(" ")[1])
                .map(Float::parseFloat)
                .max(Float::compareTo)
                .orElse(-1f);
    }
    public static int parseBattery(Map<String, String> compare_tables){
        return Integer.parseInt(
                compare_tables.getOrDefault("Battery Runtime:WiFi v1.3","-1")
        );
    }

    public static List<String> parseDisplayInfo(Document doc){
        return doc
                .select("div.auto_analysis")
                .getFirst()
                .textNodes()
                .stream().map(TextNode::text).toList();
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
    public static Elements selectStatsNode(Document doc){
        return doc.select("div.ttcl_0 div.nbc_element");
    }
    public static Optional<Laptop> create(String link) {
        return createDoc(link).map(doc -> {
            Optional<Element> svg_node = selectDimensionSVG(doc);
            Map<String, String[]> temperature_info = createTemperatureInfo(doc);
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
                    1,
                    createCompareTables(doc),
                    createCompareBars(doc),
                    parseDisplayInfo(doc)
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
