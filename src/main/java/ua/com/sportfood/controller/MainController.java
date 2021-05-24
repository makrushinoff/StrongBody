package ua.com.sportfood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.sportfood.models.forms.LoginForm;
import ua.com.sportfood.models.forms.RegistrationForm;

@Controller
@RequestMapping("/")
public class MainController {

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
    public String registrationPage(Model model) {
        model.addAttribute(new RegistrationForm());
        return "other/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("registrationForm") RegistrationForm registrationForm) {
        return "redirect:/login?regSuccess";
    }

}
