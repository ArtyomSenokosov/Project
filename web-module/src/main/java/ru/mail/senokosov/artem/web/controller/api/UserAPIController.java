package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mail.senokosov.artem.service.UserService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.UserDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.USERS_PATH;

@Slf4j
@RestController
@RequestMapping(REST_API_USER_PATH)
@RequiredArgsConstructor
public class UserAPIController {

    private final UserService userService;

    @PostMapping(value = USERS_PATH)
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDTO userDTO, BindingResult result) {
        log.info("Attempting to add user: {}", userDTO.getEmail());
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
        }
        try {
            UserDTO createdUser = userService.addUser(userDTO);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (ServiceException exception) {
            return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}