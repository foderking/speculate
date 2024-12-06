package foderking.speculate;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class Seeder implements CommandLineRunner {
    HttpClient client;
    LaptopRepository repo;
    int max_concurrent = 5;
    Logger logger = LoggerFactory.getLogger(Seeder.class);
    AtomicInteger success = new AtomicInteger(0);
    AtomicInteger error   = new AtomicInteger(0);
    AtomicInteger duplicate_count = new AtomicInteger(0);

    public Seeder(LaptopRepository repo, HttpClient client) {
        this.repo = repo;
        this.client = client;
    }

    @Override
    public void run(String... args){
        if (args.length == 1 && args[0].equals("create")) {
            logger.info("scraping all laptop reviews");
            parseAllReviews();
        }
        else{
            logger.info("running no seed");
        }
    }

    public <T, R> void concurrentExecutor(
        Iterable<T> iterable, Function<T, R> entity_creator,
        Consumer<R> entity_consumer, BiConsumer<Exception, T> error_consumer
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
                        error_consumer.accept(e, item);
                    }
                    finally {
                        semaphore.release();
                    }
                });
            }
        }
    }

    @Scheduled(fixedRate = 1000*3600*24) // executes every 24 hours
    public void update(){
        logger.info("updating review database");
        int current_year = Year.now().getValue();
        duplicate_count.set(0);

        while (duplicate_count.get() == 0) {
            logger.info("scraping reviews in " + current_year);
            Optional<List<String>> links =
                parseYear(current_year)
                .map(Jsoup::parse)
                .map(LaptopParser::createLinksFromSearch);
            if (links.isPresent()) {
                logger.info(links.get().size() + " links found");
                concurrentExecutor(
                    links.get(),
                    link -> Laptop.create(link),
                    laptop -> {
                        if (laptop.isPresent()) {
                            try{
                                if (!repo.existsByLink(laptop.get().getLink())){
                                    repo.save(laptop.get());
                                    success.incrementAndGet();
                                }
                                else{
                                    duplicate_count.incrementAndGet();
                                }
                            }
                            catch (Exception e){
                                error.incrementAndGet();
                            }
                        }
                        else {
                            error.incrementAndGet();
                        }
                    },
                    (e, link) -> {
                        System.out.println(link);
                        e.printStackTrace();
                    }
                );
                logger.info("updated: " + success.get() + ", errors: " + error.get() + ", duplicates: " + duplicate_count.get());
                current_year -= 1;
            }
            else{
                logger.info("failed to parse year: " + current_year);
                // current_year isn't decremented so to make sure a year is checked before proceeding to the next
            }
        }
        logger.info("finished updating database");
    }

    public void parseAllReviews() {
        int start_year = 2013; // earliest review
        int current_year = Year.now().getValue();
        for (int year = start_year; year <= current_year; year++) {
            error.set(0);
            success.set(0);
            logger.info("scraping reviews in " + year);
            Optional<List<String>> links =
                parseYear(year)
                .map(Jsoup::parse)
                .map(LaptopParser::createLinksFromSearch);
            if (links.isPresent()) {
                logger.info(links.get().size() + " links found");
                concurrentExecutor(
                    links.get(),
                    link -> Laptop.create(link),
                    laptop -> {
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
                    },
                    (e, link) -> {
                        System.out.println(link);
                        e.printStackTrace();
                    }
                );
                logger.info("success " + success.get() + ", error " + error.get());
            }
            else{
                logger.info("failed to parse year: " + year);
            }
        }
    }

    public Optional<String> parseYear(int year){
        try {
            var tmp = client.send(
                HttpRequest.newBuilder()
                    .uri(URI.create("https://www.notebookcheck.net/Laptop-Search.8223.0.html"))
                    .headers("Content-Type","application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                        String.format(
                            "manufacturer=&model=&lang=2&gpu=&gpu_manu=&gpu_architecture=&gpu_class=&cpu=&cpu_manu=&exclude_cpu_manu=&cpu_generation=&cpu_cores=&inch=&inch_from=&inch_to=&screen_ratio=&screen_resolution_x=&screen_resolution_y=&screen_glossy=&screen_panel_type=&screen_panel=&screen_refresh_rate=&class=&rating=&reviewcount=&dr_workmanship=&dr_display=&dr_emissions=&dr_ergonomy=&dr_performance=&dr_mobility=&dr_temperature=&dr_audio=&dr_camera=&ratingversion=&age=&min_age=&year_from=%s&year_till=%s&weight=&size_width=&size_length=&size_depth=&price=&min_price=&min_list_price=&list_price=&ram=&battery_capacity=&battery_capacity_mah=&hdd_size=&hdd_type=&odd_type=&lan_type=&wlan_type=&brightness_center=&de2000_colorchecker=&percent_of_srgb=&percent_of_adobergb=&pwm=&loudness_min=&loudness_load=&battery_wlan=&os_type=&tag_type=16&nbcReviews=1&archive=1&orderby=0&scatterplot_x=&scatterplot_y=&scatterplot_r=", year, year
                        )
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
