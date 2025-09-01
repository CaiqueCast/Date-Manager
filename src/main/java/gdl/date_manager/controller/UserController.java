package gdl.date_manager.controller;

import gdl.date_manager.data.UserRepository;
import gdl.date_manager.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public List<UserModel> getAllUser(){
        return userRepository.findAll();
    }

    @PostMapping("/user")
    public UserModel saveUser(@RequestBody UserModel user){
        return  userRepository.save(user);
    }

}
