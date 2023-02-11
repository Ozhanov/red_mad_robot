package kz.test.tz.pogo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static kz.test.tz.costants.AppConstants.EMAIL_OWASP_REGEXP;

@Data
public class NewUserRequest {
    @NotBlank(message = "email is mandatory field")
    @Size(min = 3, max = 255)
    @Email(regexp = EMAIL_OWASP_REGEXP, message = "must be a well-formed email address")
    private String email;
    @NotBlank(message = "password is mandatory field")
    @Size(min = 8, max = 255, message = "password field size con not be less that 8")
    private String password;
    @NotNull
    @NotBlank(message = "firstName is mandatory field")
    @Size(min = 3, max = 255, message = "firstName field size can not be less than 3")
    private String firstName;
    @NotNull
    @NotBlank(message = "lastName is mandatory field")
    @Size(min = 3, max = 255, message = "lastName field size can not be less than 3")
    private String lastName;
}
