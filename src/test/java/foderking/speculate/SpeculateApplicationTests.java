package foderking.speculate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest()
class SpeculateApplicationTests {
    private static Stream<String> data() {
        return Stream.of(
                "https://www.notebookcheck.net/Apple-MacBook-Pro-16-2021-M1-Pro-in-Review-The-best-Multimedia-Laptop-for-Content-Creators.579013.0.html",
            "https://www.notebookcheck.net/Apple-MacBook-Pro-16-2021-M1-Max-Laptop-Review-Full-Performance-without-Throttling.581437.0.html",
            "https://www.notebookcheck.net/Acer-Aspire-V3-572PG-604M-Notebook-Review.128525.0.html",
            "https://www.notebookcheck.net/Apple-MacBook-Air-2020-Review-Should-you-get-the-more-powerful-version-of-the-M1-processor.511529.0.html",
            "https://www.notebookcheck.net/Asus-VivoBook-S13-S330UA-i7-FHD-Laptop-Review.376761.0.html",

            "https://www.notebookcheck.net/LG-gram-Pro-2-in-1-16T90SP-review-Light-and-powerful.850220.0.html",
            "https://www.notebookcheck.net/Dell-XPS-17-9730-laptop-review-GeForce-RTX-4070-multimedia-monster.719622.0.html",
            "https://www.notebookcheck.net/Lenovo-ThinkPad-X1-Carbon-G6-2018-i5-8350U-Full-HD-Touch-256GB-Laptop-Review.331428.0.html",
            // v3 (archived)
            "https://www.notebookcheck.net/Review-Lenovo-ThinkPad-S540-20B30059GE-Ultrabook.111259.0.html",
            "https://www.notebookcheck.net/Review-Acer-Aspire-E1-470P-6659-Notebook.108284.0.html",
            // v4
            "https://www.notebookcheck.net/Dell-Inspiron-17-5758-Notebook-Review.149952.0.html",
            "https://www.notebookcheck.net/Acer-Aspire-E13-ES1-311-Notebook-Review.138907.0.html", // archived
            // v6
            "https://www.notebookcheck.net/Eurocom-Nightsky-RX15-Clevo-PB51RF-Core-i9-4K-OLED-Laptop-Review.431869.0.html",
            "https://www.notebookcheck.net/MSI-GP65-Leopard-9SE-Laptop-Review-The-best-screen-on-a-mid-tier-gaming-laptop.431863.0.html",
            // v7
            "https://www.notebookcheck.net/Lenovo-Yoga-Pro-9-16IMH9-laptop-review-75-W-GeForce-RTX-4050-overperforms.842762.0.html",
            "https://www.notebookcheck.net/Asus-ROG-Strix-Scar-15-G533zm-review-Modern-RTX-3060-gaming-laptop-with-many-strengths.910967.0.html",
            // v8
            "https://www.notebookcheck.net/Apple-MacBook-Pro-16-2024-review-Enormous-battery-life-and-better-performance-of-the-M4-Pro.917793.0.html",
            "https://www.notebookcheck.net/HP-OmniBook-Ultra-Flip-14-convertible-review-Lighter-than-expected.914608.0.html"
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void parsingModel(String link) {
        Document doc = Laptop.createDoc(link).get();
        String model = Laptop.parseModel(doc);
        assertThat(model).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingPicture(String link) {
        Document doc = Laptop.createDoc(link).get();
        String picture = Laptop.parsePicture(doc);
        assertThat(picture).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingDate(String link) {
        Document doc = Laptop.createDoc(link).get();
        String date  = Laptop.parseReviewDate(doc);
        assertThat(date).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingReviewer(String link) {
        Document doc = Laptop.createDoc(link).get();
        String reviewer = Laptop.parseReviewer(doc);
        assertThat(reviewer).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingReviewVersion(String link) {
        Document doc = Laptop.createDoc(link).get();
        int review_version = Laptop.parseReviewVersion(doc);
        assertThat(review_version).isGreaterThan(0);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingInfo(String link) {
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> info = Laptop.parseInfo(doc);
        assertThat(info.size()).isGreaterThan(0);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingRating(String link) {
        Document doc = Laptop.createDoc(link).get();
        Integer rating = Laptop.parseRating(doc);
        assertThat(rating).isGreaterThan(0);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingLength(String link) {
        Document doc = Laptop.createDoc(link).get();
        Optional<Element> svg_node = Laptop.selectDimensionSVG(doc);
        // reviews below version 5 appear not to have dimensions
        if (svg_node.isPresent()) {
            assertThat(
                svg_node
                    .map(Laptop::parseLength)
                    .get()
            ).isGreaterThan(0f);
        }
        else {
            assertThat(Laptop.parseReviewVersion(doc)).isLessThan(5);
        }
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingThickness(String link) {
        Document doc = Laptop.createDoc(link).get();
        Optional<Element> svg_node = Laptop.selectDimensionSVG(doc);
        if (svg_node.isPresent()) {
            assertThat(
                svg_node
                    .map(Laptop::parseThickness)
                    .get()
            ).isGreaterThan(0f);
        }
        else {
            assertThat(Laptop.parseReviewVersion(doc)).isLessThan(5);
        }
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingWidth(String link) {
        Document doc = Laptop.createDoc(link).get();
        Optional<Element> svg_node = Laptop.selectDimensionSVG(doc);
        if (svg_node.isPresent()) {
            assertThat(
                svg_node
                    .map(Laptop::parseWidth)
                    .get()
            ).isGreaterThan(0f);
        }
        else {
            assertThat(Laptop.parseReviewVersion(doc)).isLessThan(5);
        }
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingWeight(String link) {
        Document doc = Laptop.createDoc(link).get();
        Optional<Element> svg_node = Laptop.selectDimensionSVG(doc);
        if (svg_node.isPresent()) {
            assertThat(
                svg_node
                    .map(Laptop::parseWeight)
                    .get()
            ).isGreaterThan(0f);
        }
        else {
            assertThat(Laptop.parseReviewVersion(doc)).isLessThan(5);
        }
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingDisplayInfo(String link){
        Document doc = Laptop.createDoc(link).get();
        List<String> display_infos = Laptop.createDisplayInfo(doc);
        assertThat(display_infos).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingTemperatureInfo(String link){
        Document doc = Laptop.createDoc(link).get();
        var temperature_info = Laptop.createTemperatureInfo(doc);
        assertThat(temperature_info).isNotEmpty();
        temperature_info.values().forEach(e -> assertThat(e.length).isGreaterThanOrEqualTo(4));
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingCompareBars(String link){
        Document doc = Laptop.createDoc(link).get();
        var dict = Laptop.createCompareBars(doc);
        assertThat(dict).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("data")
    void parsingCompareTables(String link){
        Document doc = Laptop.createDoc(link).get();
        var dict = Laptop.createCompareTables(doc);
        assertThat(dict).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingMaxTempLoad(String link){
        Document doc = Laptop.createDoc(link).get();
        Map<String, String[]> tmp = Laptop.createTemperatureInfo(doc);
        assertThat(Laptop.parseMaxTemperatureLoad(tmp)).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingMaxTempIdle(String link){
        Document doc = Laptop.createDoc(link).get();
        Map<String, String[]> tmp = Laptop.createTemperatureInfo(doc);
        assertThat(Laptop.parseMaxTemperatureIdle(tmp)).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingBattery(String link){
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> tmp1 = Laptop.createCompareTables(doc);
        Map<String, String> tmp2 = Laptop.createCompareBars(doc);
        Map<String, String> tmp3 = Laptop.createBarCharts(doc);
        int battery = Laptop.parseBattery(tmp1,tmp2,tmp3);
        assertThat(battery).isGreaterThan(0);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingSRGB(String link){
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> tmp1 = Laptop.createCompareTables(doc);
        List<String> tmp2 = Laptop.createDisplayInfo(doc);
        float tmp = Laptop.parseCoverageSRGB(tmp1,tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingAdobeRGB(String link) {
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> tmp1 = Laptop.createCompareTables(doc);
        List<String> tmp2 = Laptop.createDisplayInfo(doc);
        float tmp = Laptop.parseCoverageAdobeRGB(tmp1,tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingP3(String link) {
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> tmp1 = Laptop.createCompareTables(doc);
        List<String> tmp2 = Laptop.createDisplayInfo(doc);
        float tmp = Laptop.parseCoverageP3(tmp1,tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingPWM(String link) {
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> tmp1 = Laptop.createCompareTables(doc);
        float tmp = Laptop.parsePWM(tmp1);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingBrightnessDistribution(String link) {
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> tmp1 = Laptop.createCompareTables(doc);
        List<String> tmp2 = Laptop.createDisplayInfo(doc);
        float tmp = Laptop.parseBrightnessDistribution(tmp1, tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingContrast(String link) {
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> tmp1 = Laptop.createCompareTables(doc);
        List<String> tmp2 = Laptop.createDisplayInfo(doc);
        float tmp = Laptop.parseContrast(tmp1, tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingBrightness(String link) {
        Document doc = Laptop.createDoc(link).get();
        Map<String, String> tmp1 = Laptop.createCompareTables(doc);
        List<String> tmp2 = Laptop.createDisplayInfo(doc);
        float tmp = Laptop.parseBrightness(tmp1, tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
}
