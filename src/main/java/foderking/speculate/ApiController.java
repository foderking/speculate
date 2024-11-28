package foderking.speculate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ApiController {
    @GetMapping(value="/api/")
    public String rand(){
        return "hallo";
    }
    @GetMapping(value = "/api/query")
    public Optional<Laptop> index(@RequestParam String link){
        return Laptop.create(link);
    }
}
