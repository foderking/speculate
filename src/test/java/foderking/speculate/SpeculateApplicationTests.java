package foderking.speculate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.stream.Stream;

@SpringBootTest
class SpeculateApplicationTests {
    private static Stream<String> data() {
        return Stream.of(
            "https://www.notebookcheck.net/LG-gram-Pro-2-in-1-16T90SP-review-Light-and-powerful.850220.0.html",
            "https://www.notebookcheck.net/Dell-XPS-17-9730-laptop-review-GeForce-RTX-4070-multimedia-monster.719622.0.html",
            "https://www.notebookcheck.net/Lenovo-ThinkPad-X1-Carbon-G6-2018-i5-8350U-Full-HD-Touch-256GB-Laptop-Review.331428.0.html",
            // v4
            "https://www.notebookcheck.net/Dell-Inspiron-17-5758-Notebook-Review.149952.0.html",
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
        var tmp = Laptop.createDoc(link).map(Laptop::parseModel);
        assertThat(tmp.get()).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingPicture(String link) {
        var tmp = Laptop.createDoc(link).map(Laptop::parsePicture);
        assertThat(tmp.get()).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingDate(String link) {
        var tmp = Laptop.createDoc(link).map(Laptop::parseReviewDate);
        assertThat(tmp.get()).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingReviewer(String link) {
        var tmp = Laptop.createDoc(link).map(Laptop::parseReviewer);
        assertThat(tmp.get()).isNotEmpty();
    }
    @ParameterizedTest
    @MethodSource("data")
    void parsingReviewVersion(String link) {
        var tmp = Laptop.createDoc(link).map(Laptop::parseReviewVersion);
        assertThat(tmp.get()).isNotEmpty();
    }
}
