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
        return "index";
    }
    @GetMapping(value="/filter")
    public String getTemplate(Model model){
        model.addAttribute("filter", new Filter());
        return "filter";
    }
    @PostMapping(value="/filter")
    public String postTemplate(@ModelAttribute("filter") Filter f, Model model){
        Iterable<Laptop> tmp = repo.filterAllColumns(
                maxFloat(f.getWeight()), maxFloat(f.getThickness()),
                maxInt(f.getMax_temperature_load()), maxInt(f.getMax_temperature_idle()),
                minInt(f.getBattery()), minFloat(f.getsRGB()), minFloat(f.getAdobeRGB()),
                minFloat(f.getP3()), minFloat(f.getBrightness()), maxFloat(f.getResponse())
        );
        model.addAttribute("laptops",tmp);
        return "template";
    }

    public Float maxFloat(Float f){
        return f==null ? Float.MAX_VALUE : f;
    }
    public Float minFloat(Float f){
        return f==null ? Float.MIN_VALUE : f;
    }
    public Integer maxInt(Integer i){
        return i==null ? Integer.MAX_VALUE : i;
    }
    public Integer minInt(Integer i){
        return i==null ? Integer.MIN_VALUE : i;
    }
}
