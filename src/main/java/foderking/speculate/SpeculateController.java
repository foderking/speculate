package foderking.speculate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class SpeculateController {
    @Autowired
    LaptopRepository repo;

    @GetMapping
    public String index(Model model){
        model.addAttribute("filter", new Filter());
        return "filter";
    }
    @GetMapping(value="/filter")
    public String getTemplate(Model model){
        model.addAttribute("filter", new Filter());
        return "filter";
    }
    @PostMapping(value="/filter")
    public String postTemplate(@ModelAttribute("filter") Filter f, Model model){
        Iterable<Laptop> tmp = repo.findAllByRatingGreaterThanEqual(f.getRating());
        model.addAttribute("laptops",tmp);
        return "template";
    }
}
