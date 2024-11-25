package foderking.speculate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

@Component
public class Seeder implements CommandLineRunner {
    @Autowired
    LaptopRepository repo;
    @Autowired
    HttpClient client;
    Logger logger = LoggerFactory.getLogger(Seeder.class);

    AtomicInteger success = new AtomicInteger(0);
    AtomicInteger error   = new AtomicInteger(0);

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

//    public Optional<String> parseMinimal(){
//        try {
//            var tmp = client.send(
//                HttpRequest.newBuilder()
//                    .uri(URI.create("https://www.notebookcheck.net/Laptop-Search.8223.0.html"))
//                    .headers("Content-Type","application/x-www-form-urlencoded")
//                    .POST(HttpRequest.BodyPublishers.ofString("manufacturer=&model=&lang=2&gpu=&gpu_manu=&gpu_architecture=&gpu_class=&cpu=&cpu_manu=&exclude_cpu_manu=&cpu_generation=&cpu_cores=&inch=&inch_from=&inch_to=&screen_ratio=&screen_resolution_x=&screen_resolution_y=&screen_glossy=&screen_panel_type=&screen_panel=&screen_refresh_rate=&class=&rating=92&reviewcount=&dr_workmanship=&dr_display=&dr_emissions=&dr_ergonomy=&dr_performance=&dr_mobility=&dr_temperature=&dr_audio=&dr_camera=&ratingversion=&age=&min_age=&year_from=&year_till=&weight=&size_width=&size_length=&size_depth=&price=&min_price=&min_list_price=&list_price=&ram=&battery_capacity=&battery_capacity_mah=&hdd_size=&hdd_type=&odd_type=&lan_type=&wlan_type=&brightness_center=&de2000_colorchecker=&percent_of_srgb=&percent_of_adobergb=&pwm=&loudness_min=&loudness_load=&battery_wlan=&os_type=&tag_type=16&nbcReviews=1&orderby=0&scatterplot_x=&scatterplot_y=&scatterplot_r="))
//                    .build(),
//                HttpResponse.BodyHandlers.ofString()
//            );
//            return Optional.of(tmp.body());
//        } catch(Exception e){
//            return Optional.empty();
//        }
//    }

    public void populateDB(){
        Semaphore semaphore = new Semaphore(5); // limits concurrent requests to prevent timeout
        logger.info("scraping all laptop reviews");
        int current_year = Year.now().getValue();

        for (int year = 2013; year <= current_year; year++) {
            logger.info("Year: " + year);
            success.set(0);
            error.set(0);

            Optional<List<String>> links = parseYear(year)
                        .map(Jsoup::parse)
                        .map(Laptop::createLinksFromSearch);
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
                            catch (InterruptedException e){
                                logger.error("Error " + e.getMessage());
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
