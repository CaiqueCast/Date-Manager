package gdl.date_manager.controller;

import gdl.date_manager.data.UserRepository;
import gdl.date_manager.model.UserModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder enc;

    public AuthController(UserRepository userRepository, BCryptPasswordEncoder enc) {
        this.userRepository = userRepository;
        this.enc = enc;
    }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserModel());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user") UserModel user, Model model) {
        user.setPassword(enc.encode(user.getPassword()));
        userRepository.save(user);
        model.addAttribute("msg","Usuário criado! Faça login.");
        return "auth/login";
    }
}
