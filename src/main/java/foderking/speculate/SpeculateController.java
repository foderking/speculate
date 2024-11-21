package foderking.speculate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class SpeculateController {
    @GetMapping
    public Optional<Laptop> index(@RequestParam String link){
        return Laptop.create(link);
    }

    @GetMapping(value="/filter")
    public String getTemplate(Model model){
        model.addAttribute("filter", new Filter());
        return "filter";
    }
    @PostMapping(value="/filter")
    public String postTemplate(@ModelAttribute("filter") Filter f, Model model){
        model.addAttribute("version",f.getVersion());
        return "template";
    }
}
