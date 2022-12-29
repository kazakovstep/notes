package edu.kazakov.notes.DTO;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO
{
    private Long id;

    @NotEmpty(message = "Имя пользователя не должно быть пустым")
    private String name;

    @Email
    @NotEmpty(message = "E-mail не должен быть пустым")
    private String email;
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.;?,!:]).{6,30})",
            message = "Введите корректный пароль")
    private String password;
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String passwordConfirm;

    private Boolean passwordsEquals;

    public Boolean isPasswordsEquals() {
        return password != null && password.equals(passwordConfirm);
    }

    @AssertTrue(message = "Пароли не совпадают.")
    public Boolean getPasswordsEquals(){
        return isPasswordsEquals();
    }
}