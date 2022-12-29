package edu.kazakov.notes.Service;

import edu.kazakov.notes.DTO.UserDTO;
import edu.kazakov.notes.model.Role;
import edu.kazakov.notes.model.User;
import edu.kazakov.notes.repository.RoleRepository;
import edu.kazakov.notes.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import net.bytebuddy.utility.RandomString;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDTO userDto, String url)
            throws MessagingException, UnsupportedEncodingException{
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        sendVerificationEmail(user, url);
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDTO mapToUserDto(User user){
        UserDTO userDto = new UserDTO();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "pizzatimegel@gmail.com";
        String senderName = "onlynotes.site";
        String subject = "Подтвердите свой электронный адрес";
        String content = "[[name]],<br>"
                + "Перейдите по ссылке внизу для подтверждения электронного адреса:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ПОДТВЕРДИТЬ</a></h3>"
                + "С уважением,<br>"
                + "onlynotes.site.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }
    }
}
