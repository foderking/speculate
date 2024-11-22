package foderking.speculate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map;

@Entity
public class Laptop implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    private String link;
    private String reviewer;
    private String reviewDate;
    private String picture;
    private String model;
    private String reviewVersion;
    @ElementCollection
    private Map<String, String> info;
    private Integer rating;
    private float length;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Laptop(
            String link,
            String reviewer,
            String reviewDate,
            String picture,
            String model,
            String reviewVersion,
            Map<String, String> info,
            Integer rating,
            float length
    ) {
        this.link = link;
        this.reviewer = reviewer;
        this.reviewDate = reviewDate;
        this.picture = picture;
        this.model = model;
        this.reviewVersion = reviewVersion;
        this.info = info;
        this.rating = rating;
        this.length = length;
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
    public static String parseReviewVersion(Document doc) {
        return doc
                .select("#tspan4368")
                .text()
                .split(" ")[4];
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
                                .toArray()
                )
                .collect(
                        Collectors.toMap(i -> (String) i[0], i -> (String) (i.length>1 ? i[1] : ""))
                );
    }
    public static Integer parseRating(Document doc) {
        return Integer.parseInt(
            doc
                .select("#tspan4350")
                .text()
                .split("%[)]")[0]
                .split("[(]")[1]
        );
    }
    public static Float parseLength(Element dimension_svg){
        return  dimension_svg
                .select("rect")
                .eachAttr("width")
                .stream().map(Float::parseFloat)
                .max(Float::compareTo)
                .get();
    }

    public static Optional<Document> createDoc(String link) {
        try {
            return Optional.of(Jsoup
                    .connect(link)
                    .userAgent("Mozilla")
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
    public static Elements selectStatsNode(Document doc){
        return doc.select("div.ttcl_0 div.nbc_element");
    }
    public static Optional<Laptop> create(String link) {
        return createDoc(link).map(doc -> {
            Optional<Element> svg_node = selectDimensionSVG(doc);
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
                        .orElse(0f)
            );
        });
    }

    @Override
    public String toString() {
        return "Laptop[" +
                "link=" + link + ", " +
                "reviewer=" + reviewer + ", " +
                "reviewDate=" + reviewDate + ", " +
                "picture=" + picture + ", " +
                "model=" + model + ", " +
                "reviewVersion=" + reviewVersion + "," + info.toString();
    }

}
