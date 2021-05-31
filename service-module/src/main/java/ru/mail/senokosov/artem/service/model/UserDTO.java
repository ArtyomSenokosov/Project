package ru.mail.senokosov.artem.service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import ru.mail.senokosov.artem.repository.model.Role;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private UUID uuid;
    @NonNull
    @Size(min = 2, max = 40, message = "characters count should be in the range between 2 and 40")
    private String secondname;
    @NonNull
    @Size(min = 2, max = 20, message = "characters count should be in the range between 2 and 20")
    private String firstname;
    @NonNull
    @Size(min = 2, max = 40, message = "characters count should be in the range between 2 and 40")
    private String middlename;
    @NonNull
    @Pattern(regexp = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\/)+[A-z](2,4)$")
    private String email;
    private String password;
    private String roleName;

}
