package ru.mail.senokosov.artem.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.mail.senokosov.artem.service.model.enums.RoleDTOEnum;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;
import ru.mail.senokosov.artem.web.config.BaseIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.USERS_PATH;

class UserAPIControllerIT extends BaseIT {

    @Test
    void shouldAddUserAndReturn_201() {
        AddUserDTO addUserDTO = new AddUserDTO();
        addUserDTO.setLastName("Lastname");
        addUserDTO.setFirstName("Firstname");
        addUserDTO.setMiddleName("Middlename");
        addUserDTO.setEmail("test@gmail.com");
        addUserDTO.setRole(RoleDTOEnum.ADMINISTRATOR);
        addUserDTO.setAddress("Address");
        addUserDTO.setTelephone("66666666666");
        HttpEntity<AddUserDTO> httpEntity = new HttpEntity<>(addUserDTO);
        ResponseEntity<AddUserDTO> response = testRestTemplate
                .withBasicAuth("rest@gmail.com", "test")
                .exchange(
                        REST_API_USER_PATH + USERS_PATH,
                        HttpMethod.POST,
                        httpEntity,
                        AddUserDTO.class
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}