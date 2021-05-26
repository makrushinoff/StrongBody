package ua.strongBody.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.strongBody.models.forms.LoginForm;
import ua.strongBody.models.forms.RegistrationForm;
import ua.strongBody.services.RegistrationService;

@Controller
@RequestMapping("/")
public class MainController {

    private final RegistrationService registrationService;

    public MainController(RegistrationService registrationService) {
        this.registrationService = registrationService;
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

}
