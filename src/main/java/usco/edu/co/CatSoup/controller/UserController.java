package usco.edu.co.CatSoup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user/home")
    public String userHome() {
        return "user/home";  // 👈 busca templates/user/home.html
    }
}
