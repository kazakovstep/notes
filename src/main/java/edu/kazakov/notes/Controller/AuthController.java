package edu.kazakov.notes.Controller;

import edu.kazakov.notes.DTO.UserDTO;
import edu.kazakov.notes.model.User;
import edu.kazakov.notes.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class AuthController {
    private UserService userService;
    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }
    public AuthController(UserService userService){this.userService=userService;}
    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        return "registration";
    }
    @PostMapping("/registration/save")
    public String registration(@Valid @ModelAttribute("user") UserDTO userDto,
                               BindingResult result,
                               Model model, HttpServletRequest req) throws MessagingException, UnsupportedEncodingException {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "Этот E-mail уже зарегистрирован");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/registration";
        }

        userService.saveUser(userDto, getSiteURL(req));
        return "/verificationCheck";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        System.out.println(siteURL.replace(request.getServletPath(), ""));
        return siteURL.replace(request.getServletPath(), "");
    }
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "login";
        } else {
            return "verificationFail";
        }
    }

}
