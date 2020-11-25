package daniel.varga.tacocloud.controllers;//package daniel.varga.tacocloud.controllers;

import daniel.varga.tacocloud.domain.Ingredient;
import daniel.varga.tacocloud.domain.Ingredient.Type;
import daniel.varga.tacocloud.domain.Order;
import daniel.varga.tacocloud.domain.Taco;
import daniel.varga.tacocloud.repositories.IngredientRepository;
import daniel.varga.tacocloud.repositories.JdbcTacoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/design")
/*
    Ensures that an Order object is crated in the model. Unlike the Taco object, it has to be present across
    multiple requests -> multiple tacos can be added to it.
*/
@SessionAttributes("order")
public class DesignTacoController {

    private final IngredientRepository ingredientRepository;
    private JdbcTacoRepository tacoRepository;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepository, JdbcTacoRepository tacoRepository) {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
    }

    // Order and Taco objects will be created in the model
    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepository.findAll().forEach(ingredients::add);

        Type[] types = Ingredient.Type.values();
        for (Type t: types) {
            model.addAttribute(t.toString().toLowerCase(), filterByType(ingredients, t));
        }

        // The model consists of (A.) empty Taco object, (B.) all the distinct Types as Lists of Ingredients

        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
/*
         @ModelAttribute indicates that the Order comes from the model and that Spring MVC shouldn't bind
        request parameters to it.
*/
        if (errors.hasErrors()) {
            log.info(String.valueOf(errors.getAllErrors()));
            return "design";
        }

        Taco savedTaco = tacoRepository.save(design);
        order.addDesign(savedTaco);

         return "redirect:/orders/current";
    }


    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
