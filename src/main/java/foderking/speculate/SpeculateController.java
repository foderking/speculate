package foderking.speculate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
//        Iterable<Laptop> tmp  repo.findAllByRatingGreaterThanEqual(f.getRating());
        Iterable<Laptop> tmp = repo.filterAllColumns(
                checkFloat(f.getWeight()), checkFloat(f.getThickness()), checkInt(f.getMax_temperature_load()),checkInt(f.getMax_temperature_idle())
        );
        model.addAttribute("laptops",tmp);
        return "template";
    }

    public Float checkFloat(Float f){
        return f==null ? Float.MAX_VALUE : f;
    }
    public Integer checkInt(Integer i){
        return i==null ? Integer.MAX_VALUE : i;
    }
}
