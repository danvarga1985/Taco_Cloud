package daniel.varga.tacocloud.controllers;

import daniel.varga.tacocloud.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/current")
    public String orderForm(Model model) {
        model.addAttribute("order", new Order());
        //TODO: populate model with persisted data
        return "orderForm";
    }

    @PostMapping
    public String processOrder(Order order) {
        log.info("Order Submitted: " + order);
        return "redirect:/";
    }

}
