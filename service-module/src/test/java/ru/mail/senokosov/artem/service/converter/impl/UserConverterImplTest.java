package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.repository.model.UserInfo;
import ru.mail.senokosov.artem.service.model.enums.RoleDTOEnum;
import ru.mail.senokosov.artem.service.model.add.AddUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserDTO;
import ru.mail.senokosov.artem.service.model.show.ShowUserInfoDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserConverterImplTest {

    @InjectMocks
    private UserConverterImpl userConverter;

    @Test
    void shouldConvertUserToShowUserDTOAndReturnRightId() {
        User user = new User();
        Long id = 1L;
        user.setId(id);
        ShowUserDTO showUserDTO = userConverter.convert(user);

        assertEquals(id, showUserDTO.getId());
    }

    @Test
    void shouldConvertUserToShowUserDTOAndReturnRightLastName() {
        User user = new User();
        String lastName = "test last name";
        user.setLastName(lastName);
        ShowUserDTO showUserDTO = userConverter.convert(user);

        assertEquals(lastName, showUserDTO.getLastName());
    }

    @Test
    void shouldConvertUserToShowUserDTOAndReturnRightFirstName() {
        User user = new User();
        String firstName = "test first name";
        user.setFirstName(firstName);
        ShowUserDTO showUserDTO = userConverter.convert(user);

        assertEquals(firstName, showUserDTO.getFirstName());
    }

    @Test
    void shouldConvertUserToShowUserDTOAndReturnRightMiddleName() {
        User user = new User();
        String middleName = "test middle name";
        user.setMiddleName(middleName);
        ShowUserDTO showUserDTO = userConverter.convert(user);

        assertEquals(middleName, showUserDTO.getMiddleName());
    }

    @Test
    void shouldConvertUserToShowUserDTOAndReturnRightEmail() {
        User user = new User();
        String email = "test email";
        user.setEmail(email);
        ShowUserDTO showUserDTO = userConverter.convert(user);

        assertEquals(email, showUserDTO.getEmail());
    }

    @Test
    void shouldConvertUserToShowUserDTOAndReturnRightRoleName() {
        Role role = new Role();
        String roleName = RoleDTOEnum.ADMINISTRATOR.name();
        role.setRoleName(roleName);
        User user = new User();
        user.setRole(role);
        ShowUserDTO showUserDTO = userConverter.convert(user);

        assertEquals(roleName, showUserDTO.getRoleName());
    }

    @Test
    void shouldConvertAddUserDTOToUserAndReturnRightLastName() {
        AddUserDTO addUserDTO = new AddUserDTO();
        String lastName = "test last name";
        addUserDTO.setLastName(lastName);
        User user = userConverter.convert(addUserDTO);

        assertEquals(lastName, user.getLastName());
    }

    @Test
    void shouldConvertAddUserDTOToUserAndReturnRightFirstName() {
        AddUserDTO addUserDTO = new AddUserDTO();
        String firstName = "test first name";
        addUserDTO.setFirstName(firstName);
        User user = userConverter.convert(addUserDTO);

        assertEquals(firstName, user.getFirstName());
    }

    @Test
    void shouldConvertAddUserDTOToUserAndReturnRightMiddleName() {
        AddUserDTO addUserDTO = new AddUserDTO();
        String middleName = "test middle name";
        addUserDTO.setMiddleName(middleName);
        User user = userConverter.convert(addUserDTO);

        assertEquals(middleName, user.getMiddleName());
    }

    @Test
    void shouldConvertAddUserDTOToUserAndReturnRightEmail() {
        AddUserDTO addUserDTO = new AddUserDTO();
        String email = "test email";
        addUserDTO.setEmail(email);
        User user = userConverter.convert(addUserDTO);

        assertEquals(email, user.getEmail());
    }

    @Test
    void shouldConvertAddUserDTOToUserAndReturnRightAddress() {
        AddUserDTO addUserDTO = new AddUserDTO();
        String address = "test address";
        addUserDTO.setAddress(address);
        User user = userConverter.convert(addUserDTO);

        assertEquals(address, user.getUserInfo().getAddress());
    }

    @Test
    void shouldConvertAddUserDTOToUserAndReturnRightTelephone() {
        AddUserDTO addUserDTO = new AddUserDTO();
        String telephone = "test telephone";
        addUserDTO.setTelephone(telephone);
        User user = userConverter.convert(addUserDTO);

        assertEquals(telephone, user.getUserInfo().getTelephone());
    }

    @Test
    void shouldConvertUserToUserDetailsDTOAndReturnRightId() {
        User user = new User();
        Long id = 1L;
        user.setId(id);
        ShowUserInfoDTO showUserInfoDTO = userConverter.convertUserToUserDetailsDTO(user);

        assertEquals(id, showUserInfoDTO.getId());
    }

    @Test
    void shouldConvertUserToUserDetailsDTOAndReturnRightFirstName() {
        User user = new User();
        String firstName = "test first name";
        user.setFirstName(firstName);
        ShowUserInfoDTO showUserInfoDTO = userConverter.convertUserToUserDetailsDTO(user);

        assertEquals(firstName, showUserInfoDTO.getFirstName());
    }

    @Test
    void shouldConvertUserToUserDetailsDTOAndReturnRightLastName() {
        User user = new User();
        String lastName = "test last name";
        user.setLastName(lastName);
        ShowUserInfoDTO showUserInfoDTO = userConverter.convertUserToUserDetailsDTO(user);

        assertEquals(lastName, showUserInfoDTO.getLastName());
    }

    @Test
    void shouldConvertUserToUserDetailsDTOAndReturnRightAddress() {
        UserInfo userInfo = new UserInfo();
        String address = "test address";
        userInfo.setAddress(address);
        User user = new User();
        user.setUserInfo(userInfo);
        ShowUserInfoDTO showUserInfoDTO = userConverter.convertUserToUserDetailsDTO(user);

        assertEquals(address, showUserInfoDTO.getAddress());
    }

    @Test
    void shouldConvertUserToUserDetailsDTOAndReturnRightTelephone() {
        UserInfo userInfo = new UserInfo();
        String telephone = "test telephone";
        userInfo.setTelephone(telephone);
        User user = new User();
        user.setUserInfo(userInfo);
        ShowUserInfoDTO showUserInfoDTO = userConverter.convertUserToUserDetailsDTO(user);

        assertEquals(telephone, showUserInfoDTO.getTelephone());
    }
}
