package foderking.speculate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LaptopParser {
    public static float parsePWM(Map<String, String> compare_tables) {
        if (compare_tables.containsKey("Response Times:PWM Frequency") && !compare_tables.get("Response Times:PWM Frequency").isEmpty()) {
            return Float.parseFloat(
                    compare_tables.get("Response Times:PWM Frequency")
            );
        }
        if (compare_tables.containsKey("Response Times:PWM Frequency") && compare_tables.get("Response Times:PWM Frequency").isEmpty()) {
            // no pwm detected
            return Float.MAX_VALUE;
        }
        return -1f;
    }

    public static float parseBrightness(Map<String, String> compare_tables, List<String> display_info) {
        Optional<String> value = extractTableValue(compare_tables, "Screen:Brightness");
        if (value.isPresent()) {
            return Float.parseFloat(
                    value.get()
            );
        }
        for (String info : display_info) {
            if (info.startsWith("Maximum")) {
                return Float.parseFloat(
                        info.split("Average: ")[1].split(" cd/m")[0]
                );
            }
        }
        return -1f;
    }

    public static float parseBrightnessDistribution(Map<String, String> compare_tables, List<String> display_info) {
        Optional<String> value = extractTableValue(compare_tables, "Screen:Brightness Distribution");
        if (value.isPresent()) {
            return Float.parseFloat(
                    value.get()
            );
        }
        for (String info : display_info) {
            if (info.startsWith("Brightness Distribution")) {
                return Float.parseFloat(
                        info.substring(25).split(" %")[0]
                );
            }
        }
        return -1f;
    }

    public static float parseContrast(Map<String, String> compare_tables, List<String> display_info) {
        String key = "Screen:Contrast";
        if (compare_tables.containsKey(key) && !compare_tables.get(key).isEmpty()) {
            return Float.parseFloat(
                    compare_tables.get(key)
            );
        }
        if (compare_tables.containsKey(key) && compare_tables.get(key).isEmpty()) {
            return Float.MAX_VALUE; // infinite contrast (OLED)
        }
        for (String info : display_info) {
            if (info.startsWith("Contrast")) {
                return Float.parseFloat(
                        info.substring(10).split(":")[0]
                );
            }
        }
        return -1f;
    }

    public static float parseResponseBW(Map<String, String> compare_tables) {
        Optional<String> value = extractTableValue(compare_tables, "Response Times:Response Time Black / White *");
        if (value.isPresent()) {
            return Float.parseFloat(
                    value.get()
            );
        }
        return -1f;
    }

    public static float parseResponseGG(Map<String, String> compare_tables) {
        Optional<String> value = extractTableValue(compare_tables, "Response Times:Response Time Grey 50% / Grey 80% *");
        if (value.isPresent()) {
            return Float.parseFloat(
                    value.get()
            );
        }
        return -1f;
    }

    public static List<String> createDisplayInfo(Document doc) {
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

    public static List<String> createLinksFromSearch(Document doc) {
        return doc
                .select("table#search td > a")
                .eachAttr("href");
    }

    public static Map<String, String[]> createTemperatureInfo(Document doc) {
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
                .map((Element e) -> {
                    String id = e.attr("for").split("_")[1];
                    String svg_id = "g#showDeviceSize_" + id;
                    return doc.selectFirst(svg_id);
                });
    }

    public static Map<String, String> createCompareTables(Document doc) {
        HashMap<String, String> root_hashmap = new HashMap();
        Elements compared_tables = doc.select("table.comparetable");
        for (Element compared_table : compared_tables) {
            String key = "";
            for (Element tr : compared_table.select("tr")) { // assumes order!
                if (tr.hasClass("tr-even") || tr.hasClass("tr-odd")) {
                    root_hashmap
                            .put(
                                    key + ":" + tr.child(0).text(),
                                    tr.child(1).text().split(" ")[0]
                            );
                } else {
                    key = tr.child(0).text();
                }
            }
        }
        return root_hashmap;
    }

    public static Map<String, String> createCompareBars(Document doc) {
        HashMap<String, String> root_hashmap = new HashMap();
        Elements compared_bars = doc.select("table.r_compare_bars");
        for (Element compared_bar : compared_bars) {
            String prefix = compared_bar.selectFirst("td.prog_header").text();
            String key = prefix; // some don't have keys
            for (Element tr : compared_bar.select("tr")) {
                if (tr.child(0).hasClass("settings_header")) {
                    key = prefix + ":" + tr.child(0).text();
                } else if (tr.hasClass("referencespecs")) {
                    root_hashmap
                            .put(
                                    key,
                                    tr.selectFirst(" td.bar span.r_compare_bars_value").child(0).text()
                            );
                } else {
                }
            }
        }
        return root_hashmap;

    }

    public static Map<String, String> createBarCharts(Document doc) {
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

    public static Elements selectStatsNode(Document doc) {
        return doc.select("div.ttcl_0 div.nbc_element");
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
}
