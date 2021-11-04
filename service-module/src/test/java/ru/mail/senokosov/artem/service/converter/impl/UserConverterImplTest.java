package ru.mail.senokosov.artem.service.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.repository.model.UserInfo;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserConverterImplTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserConverterImpl userConverter;

    private User user;
    private UserDTO userDTO;
    private UserInfoDTO userInfoDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Alice");
        user.setLastName("Smith");
        user.setMiddleName("Elizabeth");
        Role userRole = new Role();
        userRole.setRoleName("User");
        user.setRole(userRole);

        userDTO = new UserDTO();
        userDTO.setFirstName("Alice");
        userDTO.setLastName("Smith");
        userDTO.setMiddleName("Elizabeth");
        userDTO.setRoleName("User");

        userInfoDTO = new UserInfoDTO();
        userInfoDTO.setFirstName("Alice");
        userInfoDTO.setLastName("Smith");
    }

    @Test
    void shouldCorrectlyConvertUserToUserDTO() {
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO resultDTO = userConverter.convert(user);

        assertEquals("Alice", resultDTO.getFirstName());
        assertEquals("Smith", resultDTO.getLastName());
        assertEquals("Elizabeth", resultDTO.getMiddleName());
        assertEquals("User", resultDTO.getRoleName());
    }

    @Test
    void shouldCorrectlyConvertUserDTOToUser() {
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        User resultUser = userConverter.convert(userDTO);

        assertEquals("Alice", resultUser.getFirstName());
        assertEquals("Smith", resultUser.getLastName());
        assertEquals("Elizabeth", resultUser.getMiddleName());
    }

    @Test
    void shouldCorrectlyConvertUserToUserInfoDTO() {
        when(modelMapper.map(user, UserInfoDTO.class)).thenReturn(userInfoDTO);
        UserInfoDTO resultInfoDTO = userConverter.convertUserToUserInfoDTO(user);

        assertEquals("Alice", resultInfoDTO.getFirstName());
        assertEquals("Smith", resultInfoDTO.getLastName());
    }

    @Test
    void shouldHandleNullMiddleNameWhenConvertingToUserDTO() {
        user.setMiddleName(null);

        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setFirstName(user.getFirstName());
        expectedDTO.setLastName(user.getLastName());
        expectedDTO.setMiddleName(null);

        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedDTO);

        UserDTO resultDTO = userConverter.convert(user);

        assertNull(resultDTO.getMiddleName(), "Middle name should be null when it's null in the source");
    }

    @Test
    void shouldHandleEmptyMiddleNameWhenConvertingToUserDTO() {
        user.setMiddleName("");
        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setFirstName(user.getFirstName());
        expectedDTO.setLastName(user.getLastName());
        expectedDTO.setMiddleName(null);

        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedDTO);

        UserDTO resultDTO = userConverter.convert(user);

        assertNull(resultDTO.getMiddleName(), "Middle name should be null when it's empty");
    }

    @Test
    void shouldHandleNullRoleWhenConvertingToUserDTO() {
        user.setRole(null);

        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setFirstName(user.getFirstName());
        expectedDTO.setLastName(user.getLastName());
        expectedDTO.setRoleName(null);

        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedDTO);

        UserDTO resultDTO = userConverter.convert(user);

        assertNull(resultDTO.getRoleName(), "Role name should be null when role is null");
    }

    @Test
    void shouldSetMiddleNameToNullInUserWhenConvertingFromUserDTO() {
        userDTO.setMiddleName(null);
        when(modelMapper.map(userDTO, User.class)).thenCallRealMethod();
        User resultUser = userConverter.convert(userDTO);

        assertNull(resultUser.getMiddleName(), "Middle name in User should be null if it's null in UserDTO");
    }

    @Test
    void shouldSetMiddleNameToNullInUserWhenMiddleNameIsEmpty() {
        userDTO.setMiddleName("");
        when(modelMapper.map(userDTO, User.class)).thenCallRealMethod();
        User resultUser = userConverter.convert(userDTO);

        assertNull(resultUser.getMiddleName(), "Middle name in User should be null if it's empty in UserDTO");
    }

    @Test
    void shouldHandleNullUserWhenConvertingToUserDTO() {
        assertThrows(NullPointerException.class, () -> userConverter.convert((User) null),
                "Converting null User should throw NullPointerException");
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldHandleNullUserDTOWhenConvertingToUser() {
        assertThrows(NullPointerException.class, () -> userConverter.convert((UserDTO) null),
                "Converting null UserDTO should throw NullPointerException");
    }

    @Test
    void shouldHandleNullUserWhenConvertingToUserInfoDTO() {
        assertThrows(NullPointerException.class, () -> userConverter.convertUserToUserInfoDTO(null),
                "Converting null User to UserInfoDTO should throw NullPointerException");
    }

    @Test
    void shouldCorrectlySetAddressAndTelephoneWhenUserInfoIsNotNull() {
        UserInfo userInfo = new UserInfo();
        userInfo.setAddress("123 Main St");
        userInfo.setTelephone("555-1234");
        user.setUserInfo(userInfo);

        UserInfoDTO expectedDTO = new UserInfoDTO();
        expectedDTO.setAddress("123 Main St");
        expectedDTO.setTelephone("555-1234");

        when(modelMapper.map(user, UserInfoDTO.class)).thenReturn(expectedDTO);

        UserInfoDTO resultDTO = userConverter.convertUserToUserInfoDTO(user);

        assertEquals("123 Main St", resultDTO.getAddress(), "Address should be correctly set when userInfo is not null");
        assertEquals("555-1234", resultDTO.getTelephone(), "Telephone should be correctly set when userInfo is not null");
    }

    @Test
    void shouldNotSetAddressAndTelephoneWhenUserInfoIsNull() {
        user.setUserInfo(null);

        UserInfoDTO expectedDTO = new UserInfoDTO();

        when(modelMapper.map(user, UserInfoDTO.class)).thenReturn(expectedDTO);

        UserInfoDTO resultDTO = userConverter.convertUserToUserInfoDTO(user);

        assertNull(resultDTO.getAddress(), "Address should be null when userInfo is null");
        assertNull(resultDTO.getTelephone(), "Telephone should be null when userInfo is null");
    }
}