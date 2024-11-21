package foderking.speculate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

public record Laptop(
        String link,
        String reviewer,
        String reviewDate,
        String picture,
        String model,
        String reviewVersion,
        Map<String, String> info
) {
    public static String getReviewer(Document doc){
        // reviews below v8 are parsed differently
        Elements intro_author = doc.select(".intro-author");
        if (intro_author.size() > 0) {
            return intro_author
                    .first()
                    .text()
                    .split(",")[0];
        // v8 and above
        } else {
            String tmp= doc.select("div.nbcintroelwide_abstract").first().childNode(3).outerHtml().strip();
            return tmp;
        }
    }
    public static String getReviewDate(Document doc){
        return doc
                .select(".intro-date > time:nth-child(1)")
                .attr("datetime");
    }
    public static String getPicture(Document doc){
        // reviews below v8 are parsed differently
        Elements orig = doc.select(".csc-textpic-above");
        if (orig.size() > 0) {
            return orig
                    .first()
                    .select("div:nth-child(1) > div:nth-child(1) > figure:nth-child(1) > img:nth-child(1)")
                    .attr("src");
        }
        else{
            return doc
                    .select(".news-teaser-image picture img")
                    .attr("src");
        }
    }
    public static String getModel(Document doc){
        return doc
                .select(".specs_header")
                .first()
                .text()
                .split(" [(]")[0];
    }
    public static String getReviewVersion(Document doc){
        return doc
                .select("#tspan4368")
                .text()
                .split(" ")[4];
    }
    public static Map<String, String> getInfo(Document doc){
        return doc
                .select(".specs_whole .specs_element")
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
                    Collectors.toMap(i -> (String) i[0], i -> (String)i[1])
                );
    }
    public static Optional<Document> parse(String link){
        try {
            return Optional.of(Jsoup
                    .connect(link)
                    .userAgent("Mozilla")
                    .get());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
    public static Optional<Laptop> create(String link){
        return parse(link).map(doc ->
            new Laptop(
                link,
                Laptop.getReviewer(doc),
                getReviewDate(doc),
                getPicture(doc),
                getModel(doc),
                "",
                getInfo(doc)
            )
        );
    }
}
