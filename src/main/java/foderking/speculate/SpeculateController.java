package foderking.speculate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    @GetMapping(value="/laptop")
    public String getLaptops(Model model, Filter filter){
        Iterable<Laptop> laptops;
        if (filter.getSort() != null) {
            laptops = repo.filterAllColumns(
                maxFloat(filter.getWeight()), maxFloat(filter.getThickness()),
                maxInt(filter.getMax_temperature_load()), maxInt(filter.getMax_temperature_idle()),
                minInt(filter.getBattery()), minFloat(filter.getsRGB()), minFloat(filter.getAdobeRGB()),
                minFloat(filter.getP3()), minFloat(filter.getBrightness()), maxFloat(filter.getResponse()),
                Sort.by(filter.getSort_dir(),filter.getSort().toString())
            );
        }
        else {
            laptops = repo.findAll();
        }
        model.addAttribute("laptops",laptops);
        return "filter-template";
    }
    @GetMapping(value="/laptop/{id}")
    public String getLaptop(Model model, @PathVariable UUID id){
        Laptop laptop = repo.findById(id).orElse(new Laptop());
        model.addAttribute("laptop", laptop);
        return "laptop-template";
    }
    @GetMapping(value="/compare")
    public String getCompare(Model model){
        return "compare";
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
