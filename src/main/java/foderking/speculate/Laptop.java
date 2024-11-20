package foderking.speculate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeFilter;

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
        Map<String, String> info
) {
    public static String getReviewer(Document doc){
            return doc
                    .select("div.intro-author")
                    .first()
                    .text()
                    .split(",")[0];
    }
    public static String getReviewDate(Document doc){
        return doc
                .select(".intro-date > time:nth-child(1)")
                .attr("datetime");
    }
    public static String getPicture(Document doc){
        return doc
                .select(".csc-textpic-above > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > figure:nth-child(1) > img:nth-child(1)")
                .attr("src");
    }
    public static String getModel(Document doc){
        return doc
                .select(".specs_header")
                .first()
                .text()
                .split(" [(]")[0];
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



    public static Optional<Laptop> create(String link){
        Document doc;
        try {
            doc = Jsoup
                    .connect(link)
                    .userAgent("Mozilla")
                    .get();
            return Optional.of(
                new Laptop(
                    link,
                    getReviewer(doc),
                    getReviewDate(doc),
                    getPicture(doc),
                    getModel(doc),
                    getInfo(doc)
                )
            );
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
