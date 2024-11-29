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
            "https://www.notebookcheck.net/Dell-Precision-3510-Workstation-Review.169566.0.html",
            "https://www.notebookcheck.net/Apple-MacBook-Pro-15-Late-2016-2-6-GHz-450-Notebook-Review.185254.0.html",
            "https://www.notebookcheck.net/Acer-Aspire-ES1-731G-Notebook-Review.171097.0.html",
            "https://www.notebookcheck.net/Asus-ROG-GX800VH-Notebook-Preview.171399.0.html",
            "https://www.notebookcheck.net/Acer-Graphics-Dock-with-Nvidia-GTX-960M-Review.175429.0.html",
            "https://www.notebookcheck.net/Apple-MacBook-12-Early-2016-1-1-GHz-Review.164797.0.html",
            "https://www.notebookcheck.net/MIFcom-EG7-Clevo-N170RF-Notebook-Review.158477.0.html",
            "https://www.notebookcheck.net/Medion-S20-laptop-review-17-inch-multimedia-notebook-with-Intel-Core-Ultra-7.878056.0.html",
            "https://www.notebookcheck.net/GPD-Win-Mini-Zen-4-handheld-review-Solid-alternative-to-the-Asus-ROG-Ally.845030.0.html",
            "https://www.notebookcheck.net/Asus-P1511CEA-reviewed-An-affordable-office-laptop-for-school-office-leisure.686060.0.html",
            "https://www.notebookcheck.net/Lenovo-Legion-5-Pro-16-review-A-gaming-laptop-with-a-bright-165-Hz-display.554931.0.html",
            "https://www.notebookcheck.net/Lenovo-ThinkPad-X1-Extreme-G4-Review-The-best-Multimedia-Laptop-thanks-to-Core-i9-and-RTX-3080.588585.0.html",
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
        Document doc = LaptopParser.createDoc(link).get();
        String model = LaptopParser.parseModel(doc);
        assertThat(model).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingPicture(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        String picture = LaptopParser.parsePicture(doc);
        assertThat(picture).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingDate(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        String date  = LaptopParser.parseReviewDate(doc);
        assertThat(date).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingReviewer(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        String reviewer = LaptopParser.parseReviewer(doc);
        assertThat(reviewer).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingReviewVersion(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        float review_version = LaptopParser.parseReviewVersion(doc);
        assertThat(review_version).isGreaterThan(0);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingInfo(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> info = LaptopParser.parseInfo(doc);
        assertThat(info.size()).isGreaterThan(0);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingRating(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Integer rating = LaptopParser.parseRating(doc);
        assertThat(rating).isGreaterThan(0);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingLength(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Optional<Element> svg_node = LaptopParser.selectDimensionSVG(doc);
        // reviews below version 5 appear not to have dimensions
        if (svg_node.isPresent()) {
            assertThat(
                svg_node
                    .map(LaptopParser::parseLength)
                    .get()
            ).isGreaterThan(0f);
        }
        else {
            assertThat(LaptopParser.parseReviewVersion(doc)).isLessThan(5);
        }
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingThickness(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Optional<Element> svg_node = LaptopParser.selectDimensionSVG(doc);
        if (svg_node.isPresent()) {
            assertThat(
                svg_node
                    .map(LaptopParser::parseThickness)
                    .get()
            ).isGreaterThan(0f);
        }
        else {
            assertThat(LaptopParser.parseReviewVersion(doc)).isLessThan(5);
        }
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingWidth(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Optional<Element> svg_node = LaptopParser.selectDimensionSVG(doc);
        if (svg_node.isPresent()) {
            assertThat(
                svg_node
                    .map(LaptopParser::parseWidth)
                    .get()
            ).isGreaterThan(0f);
        }
        else {
            assertThat(LaptopParser.parseReviewVersion(doc)).isLessThan(5);
        }
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingWeight(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Optional<Element> svg_node = LaptopParser.selectDimensionSVG(doc);
        if (svg_node.isPresent()) {
            assertThat(
                svg_node
                    .map(LaptopParser::parseWeight)
                    .get()
            ).isGreaterThan(0f);
        }
        else {
            assertThat(LaptopParser.parseReviewVersion(doc)).isLessThan(5);
        }
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingDisplayInfo(String link){
        Document doc = LaptopParser.createDoc(link).get();
        List<String> display_infos = LaptopParser.createDisplayInfo(doc);
        assertThat(display_infos).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingTemperatureInfo(String link){
        Document doc = LaptopParser.createDoc(link).get();
        var temperature_info = LaptopParser.createTemperatureInfo(doc);
        assertThat(temperature_info).isNotEmpty();
        temperature_info.values().forEach(e -> assertThat(e.length).isGreaterThanOrEqualTo(4));
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingCompareBars(String link){
        Document doc = LaptopParser.createDoc(link).get();
        var dict = LaptopParser.createCompareBars(doc);
        assertThat(dict).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("data")
    void parsingCompareTables(String link){
        Document doc = LaptopParser.createDoc(link).get();
        var dict = LaptopParser.createCompareTables(doc);
        assertThat(dict).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingMaxTempLoad(String link){
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String[]> tmp = LaptopParser.createTemperatureInfo(doc);
        assertThat(LaptopParser.parseMaxTemperatureLoad(tmp)).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingMaxTempIdle(String link){
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String[]> tmp = LaptopParser.createTemperatureInfo(doc);
        assertThat(LaptopParser.parseMaxTemperatureIdle(tmp)).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingBattery(String link){
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        Map<String, String> tmp2 = LaptopParser.createCompareBars(doc);
        Map<String, String> tmp3 = LaptopParser.createBarCharts(doc);
        float battery = LaptopParser.parseBattery(tmp1,tmp2,tmp3);
        assertThat(battery).isGreaterThan(0);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingSRGB(String link){
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        List<String> tmp2 = LaptopParser.createDisplayInfo(doc);
        float tmp = LaptopParser.parseCoverageSRGB(tmp1,tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingAdobeRGB(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        List<String> tmp2 = LaptopParser.createDisplayInfo(doc);
        float tmp = LaptopParser.parseCoverageAdobeRGB(tmp1,tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingP3(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        List<String> tmp2 = LaptopParser.createDisplayInfo(doc);
        float tmp = LaptopParser.parseCoverageP3(tmp1,tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingPWM(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        float tmp = LaptopParser.parsePWM(tmp1);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingBrightnessDistribution(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        List<String> tmp2 = LaptopParser.createDisplayInfo(doc);
        float tmp = LaptopParser.parseBrightnessDistribution(tmp1, tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingContrast(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        List<String> tmp2 = LaptopParser.createDisplayInfo(doc);
        float tmp = LaptopParser.parseContrast(tmp1, tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingBrightness(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        List<String> tmp2 = LaptopParser.createDisplayInfo(doc);
        float tmp = LaptopParser.parseBrightness(tmp1, tmp2);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingResponseBW(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        float tmp = LaptopParser.parseResponseBW(tmp1);
        assertThat(tmp).isGreaterThan(0f);
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingResponseGG(String link) {
        Document doc = LaptopParser.createDoc(link).get();
        Map<String, String> tmp1 = LaptopParser.createCompareTables(doc);
        float tmp = LaptopParser.parseResponseGG(tmp1);
        assertThat(tmp).isGreaterThan(0f);
    }
}
