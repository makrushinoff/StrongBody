package ua.strongBody.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.facade.OrderFacade;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Order;
import ua.strongBody.models.forms.LoginForm;
import ua.strongBody.models.forms.RegistrationForm;
import ua.strongBody.services.CustomerService;
import ua.strongBody.services.RegistrationService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    private final RegistrationService registrationService;
    private final CustomerService customerService;
    private final OrderFacade orderFacade;

    public MainController(RegistrationService registrationService, CustomerService customerService, OrderFacade orderFacade) {
        this.registrationService = registrationService;
        this.customerService = customerService;
        this.orderFacade = orderFacade;
    }

    @GetMapping("/")
    public String indexPage() {
        return "other/index";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "regSuccess", required = false) String regSuccess,
                            Model model) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("regSuccess", regSuccess != null);
        return "other/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@RequestParam(value = "regFail", required = false) String regFail,
                                   Model model) {
        model.addAttribute(new RegistrationForm());
        model.addAttribute("regFail", regFail != null);
        return "other/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("registrationForm") RegistrationForm registrationForm) {
        if (!registrationService.register(registrationForm)) {
            return "redirect:/registration?regFail";
        }
        return "redirect:/login?regSuccess";
    }

    @GetMapping("/profile")
    public String profilePage(Principal principal, Model model) {
        try {
            String username = principal.getName();
            Customer customer = customerService.findByUsername(username);
            List<Order> orders = orderFacade.getOrdersByCustomerUsername(username);

            model.addAttribute("customer", customer);
            model.addAttribute("orders", orders);
        } catch (FieldNotFoundException ex) {
            return "redirect:/";
        }

        return "other/profile";
    }
}
