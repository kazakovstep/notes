package edu.kazakov.notes.Service;


import edu.kazakov.notes.DTO.UserDTO;
import edu.kazakov.notes.model.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDto, String url) throws MessagingException, UnsupportedEncodingException;

    User findUserByEmail(String email);

    List<UserDTO> findAllUsers();

    boolean verify(String verificationCode);
}
