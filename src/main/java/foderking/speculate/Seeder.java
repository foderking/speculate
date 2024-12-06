package foderking.speculate;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class Seeder implements CommandLineRunner {
    LaptopRepository repo;
    HttpClient client;

    Logger logger = LoggerFactory.getLogger(Seeder.class);
    AtomicInteger success = new AtomicInteger(0);
    AtomicInteger error   = new AtomicInteger(0);

    public Seeder(LaptopRepository repo, HttpClient client) {
        this.repo = repo;
        this.client = client;
    }

    @Override
    public void run(String... args){
        logger.info("Started seeder");
        if (args.length == 1 && args[0].equals("manual")) {
            logger.info("running manual seed");
            List<String> links = List.of(
                    "https://www.notebookcheck.net/Gigabyte-G5-KF5-2024-laptop-review-RTX-4060-gaming-at-a-bargain-price-is-the-deal-worth-it.906622.0.html",
                    "https://www.notebookcheck.net/MSI-Sword-16-HX-Laptop-Review-Gaming-powerhouse-stifled-by-an-unimpressive-screen.888247.0.html"
            );
            links
                    .stream()
                    .map(Laptop::create)
                    .forEach(each -> {
                        if (each.isPresent()) {
                            repo.save(each.get());
                        } else {
                            System.out.printf("Error: %s", each);
                        }
                    });
            System.out.println("boobs");
        }
        else if (args.length == 1 && args[0].equals("update")) {
            populateDB();
        }
        else{
            logger.info("running no seed");
        }
    }

    public void populateDB(){
        int max_concurrent = 5;
        int start_year = 2013; // earliest review
        int current_year = Year.now().getValue();
        Semaphore semaphore = new Semaphore(max_concurrent); // prevent read timeout

        logger.info("scraping all laptop reviews");

        for (int year = start_year; year <= current_year; year++) {
            logger.info("Year: " + year);
            success.set(0);
            error.set(0);
            Optional<List<String>> links =
                parseYear(year)
                .map(Jsoup::parse)
                .map(LaptopParser::createLinksFromSearch);
            if (links.isPresent()) {
                logger.info(links.get().size() + " links found");
                try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                    for (String link : links.get()) {
                        executor.submit(() -> {
                            try {
                                semaphore.acquire();
                                Optional<Laptop> laptop = Laptop.create(link);
                                saveParsedLaptoptoDB(laptop);
                            }
                            catch (Exception e){
                                System.out.println(link);
                                e.printStackTrace();
                            }
                            finally {
                                semaphore.release();
                            }
                        });
                    }

                }
                logger.info("success " + success.get() + ", error " + error.get());
            }
            else{
                logger.info("failed to parse year: " + year);
            }
        }
    }

    public <T, R> void concurrentExecutor(
        int max_concurrent, Iterable<T> iterable, Function<T, R> entity_creator,
        Consumer<R> entity_consumer, Consumer<Exception> error_consumer
    ){
        Semaphore semaphore = new Semaphore(max_concurrent); // prevent read timeout
        try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (T item : iterable) {
                executor.submit(() -> {
                    try {
                        semaphore.acquire();
                        entity_consumer.accept(
                            entity_creator.apply(item)
                        );
                    }
                    catch (Exception e){
                        error_consumer.accept(e);
                    }
                    finally {
                        semaphore.release();
                    }
                });
            }
        }
    }

    public void saveParsedLaptoptoDB(Optional<Laptop> laptop){
        if (laptop.isPresent()) {
            try{
                repo.save(laptop.get());
                success.incrementAndGet();
            }
            catch (Exception e){
                error.incrementAndGet();
            }
        }
        else {
            error.incrementAndGet();
        }
    }

    public Optional<String> parseYear(int year){
        logger.info("Parsing reviews from " + year);
        try {
            var tmp = client.send(
                HttpRequest.newBuilder()
                    .uri(URI.create("https://www.notebookcheck.net/Laptop-Search.8223.0.html"))
                    .headers("Content-Type","application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            String.format("manufacturer=&model=&lang=2&gpu=&gpu_manu=&gpu_architecture=&gpu_class=&cpu=&cpu_manu=&exclude_cpu_manu=&cpu_generation=&cpu_cores=&inch=&inch_from=&inch_to=&screen_ratio=&screen_resolution_x=&screen_resolution_y=&screen_glossy=&screen_panel_type=&screen_panel=&screen_refresh_rate=&class=&rating=&reviewcount=&dr_workmanship=&dr_display=&dr_emissions=&dr_ergonomy=&dr_performance=&dr_mobility=&dr_temperature=&dr_audio=&dr_camera=&ratingversion=&age=&min_age=&year_from=%s&year_till=%s&weight=&size_width=&size_length=&size_depth=&price=&min_price=&min_list_price=&list_price=&ram=&battery_capacity=&battery_capacity_mah=&hdd_size=&hdd_type=&odd_type=&lan_type=&wlan_type=&brightness_center=&de2000_colorchecker=&percent_of_srgb=&percent_of_adobergb=&pwm=&loudness_min=&loudness_load=&battery_wlan=&os_type=&tag_type=16&nbcReviews=1&archive=1&orderby=0&scatterplot_x=&scatterplot_y=&scatterplot_r=", year, year)
                    ))
                    .build(),
                HttpResponse.BodyHandlers.ofString()
            );
            return Optional.of(tmp.body());
        } catch(Exception e){
            return Optional.empty();
        }
    }
}
