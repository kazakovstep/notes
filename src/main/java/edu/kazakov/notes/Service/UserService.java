package edu.kazakov.notes.Service;


import edu.kazakov.notes.DTO.UserDTO;
import edu.kazakov.notes.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDto);

    User findUserByEmail(String email);

    List<UserDTO> findAllUsers();
}
