package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.model.ErrorDTO;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;

import javax.validation.Valid;

import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.USERS_PATH;

@RestController
@RequestMapping(REST_API_USER_PATH)
@RequiredArgsConstructor
public class UserAPIController {

    private final UserService userService;

    @PostMapping(value = USERS_PATH)
    public ResponseEntity<Object> addUser(@RequestBody @Valid AddUserDTO addUserDTO,
                                          BindingResult result) throws ServiceException {
        if (result.hasErrors()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrors(result.getFieldErrors());
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } else {
            userService.persist(addUserDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}