package com.example.demo.Controllers;

import java.util.ArrayList;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
// import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.Models.Auth;
import com.example.demo.Models.LoginSchema;
import com.example.demo.Models.SignUpSchema;
import com.example.demo.Models.User;
import com.example.demo.Services.UserService;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Models.Candidate;
import com.example.demo.Services.CandidateService;

// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/usersignup")
    public ResponseEntity<String> SignUpUser(@RequestBody SignUpSchema user) {
        
        // call some helper function from repository to save in mongodb
        userService.SaveUser(new User(user.name, user.name, user.password));
        String message = "User Signed Up. Saved Data!";
        return ResponseEntity.ok(message);
    }
    
    @PostMapping("/userlogin")
    public ResponseEntity<Auth> LoginUser(@RequestBody LoginSchema loginUser) {
        System.out.println("====>"+loginUser);
        User user = userService.GetUser(loginUser.username);
        System.out.println(">"+loginUser.password+"<>"+user.getPassword()+"<");
        if(user.getPassword().equals(loginUser.password)){
            System.out.println("authenticated!");
            Auth auth = new Auth(true);
            return ResponseEntity.ok(auth);
        } else {
            System.out.println("nopeeee");
            Auth auth = new Auth(false);
            return ResponseEntity.ok(auth);
        }
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("something", "hallo uncle roger from the controller!");
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("something", "hallo uncle roger from the controller!");
        return "signup";
    }
}
