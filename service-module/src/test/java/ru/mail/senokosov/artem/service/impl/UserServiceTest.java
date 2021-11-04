package ru.mail.senokosov.artem.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.UserRepository;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.User;
import ru.mail.senokosov.artem.repository.model.UserInfo;
import ru.mail.senokosov.artem.service.converter.UserConverter;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.PageDTO;
import ru.mail.senokosov.artem.service.model.UserDTO;
import ru.mail.senokosov.artem.service.model.UserInfoDTO;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnUsersPageWhenRequested() {
        when(userRepository.getCount()).thenReturn(10L);
        when(userRepository.findAll(anyInt(), anyInt())).thenReturn(Collections.singletonList(new User()));
        when(userConverter.convert(any(User.class))).thenReturn(new UserDTO());

        PageDTO result = userService.getUsersByPage(1);

        assertNotNull(result);
        assertEquals(1, result.getUsers().size());

        verify(userRepository, times(1)).getCount();
        verify(userRepository, times(1)).findAll(anyInt(), anyInt());
    }

    @Test
    void shouldSuccessfullyAddUserAndReturnUserDTO() throws ServiceException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        userDTO.setRoleName("USER");

        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        when(roleRepository.findByRoleName(anyString())).thenReturn(new Role());
        when(userConverter.convert(any(UserDTO.class))).thenReturn(new User());
        when(userConverter.convert(any(User.class))).thenReturn(new UserDTO());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        UserDTO result = userService.addUser(userDTO);

        assertNotNull(result);
        verify(userRepository, times(1)).persist(any(User.class));
    }

    @Test
    void shouldThrowServiceExceptionWhenEmailAlreadyExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");

        when(userRepository.findUserByEmail(anyString())).thenReturn(new User());

        assertThrows(ServiceException.class, () -> userService.addUser(userDTO));
    }

    @Test
    void shouldThrowServiceExceptionWhenUserRoleNotFound() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        userDTO.setRoleName("NON_EXISTENT_ROLE");

        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        when(roleRepository.findByRoleName(anyString())).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.addUser(userDTO));
    }

    @Test
    void shouldDeleteUserByIdAndReturnTrue() {
        Long userId = 1L;

        when(userRepository.findById(anyLong())).thenReturn(new User());

        boolean result = userService.isDeleteById(userId);

        assertTrue(result);
        verify(userRepository, times(1)).removeById(anyLong());
    }

    @Test
    void shouldReturnFalseWhenExceptionOccursOnDeleteById() {
        Long userId = 1L;

        when(userRepository.findById(anyLong())).thenThrow(new RuntimeException("Database error"));

        boolean result = userService.isDeleteById(userId);

        verify(userRepository, times(1)).findById(userId);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenUserNotFoundOnDeleteById() {
        Long userId = 1L;

        when(userRepository.findById(anyLong())).thenReturn(null);

        boolean result = userService.isDeleteById(userId);

        assertFalse(result);
    }

    @Test
    void shouldChangeUserRoleByIdWithoutThrowingException() {
        Long userId = 1L;
        String roleName = "ADMIN";

        when(roleRepository.findByRoleName(anyString())).thenReturn(new Role());
        when(userRepository.findById(anyLong())).thenReturn(new User());

        assertDoesNotThrow(() -> userService.changeRoleById(roleName, userId));
        verify(userRepository, times(1)).merge(any(User.class));
    }

    @Test
    void shouldThrowServiceExceptionWhenRoleNotFoundOnChangeRoleById() {
        Long userId = 1L;
        String roleName = "NON_EXISTENT_ROLE";

        when(roleRepository.findByRoleName(anyString())).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.changeRoleById(roleName, userId));
    }

    @Test
    void shouldThrowServiceExceptionWhenUserNotFoundOnChangeRoleById() {
        Long userId = 1L;
        String roleName = "ADMIN";

        when(roleRepository.findByRoleName(anyString())).thenReturn(new Role());
        when(userRepository.findById(anyLong())).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.changeRoleById(roleName, userId));
    }

    @Test
    void shouldResetUserPasswordByIdSuccessfully() throws ServiceException {
        Long userId = 1L;
        User mockUser = new User();
        UserDTO mockUserDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(mockUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userConverter.convert(mockUser)).thenReturn(mockUserDTO);

        userService.resetPassword(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).merge(mockUser);
        assertNotEquals("oldPassword", mockUser.getPassword());
    }

    @Test
    void shouldThrowServiceExceptionWhenUserNotFoundOnResetPassword() {
        Long userId = 1L;

        when(userRepository.findById(anyLong())).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.resetPassword(userId));
    }

    @Test
    void shouldReturnUserInfoDTOWhenUserFoundByEmail() throws ServiceException {
        String userName = "user@example.com";
        User user = new User();
        user.setEmail(userName);
        UserInfoDTO expectedUserInfoDTO = new UserInfoDTO();

        when(userRepository.findUserByEmail(anyString())).thenReturn(user);
        when(userConverter.convertUserToUserInfoDTO(any(User.class))).thenReturn(expectedUserInfoDTO);

        UserInfoDTO actualUserInfoDTO = userService.getUserByUserName(userName);

        assertNotNull(actualUserInfoDTO);
    }

    @Test
    void shouldThrowServiceExceptionWhenUserNotFoundByEmail() {
        String userName = "nonexistent@example.com";

        when(userRepository.findUserByEmail(anyString())).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.getUserByUserName(userName));
    }

    @Test
    void shouldUpdateUserParametersByIdSuccessfully() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(1L);
        userInfoDTO.setFirstName("NewFirstName");
        userInfoDTO.setLastName("NewLastName");
        userInfoDTO.setAddress("NewAddress");
        userInfoDTO.setTelephone("NewPhone");
        userInfoDTO.setOldPassword("oldPassword");
        userInfoDTO.setNewPassword("newPassword");

        User user = new User();
        UserInfo userInfo = new UserInfo();
        user.setId(1L);
        user.setUserInfo(userInfo);
        user.setPassword("oldPasswordEncoded");

        when(userRepository.findById(anyLong())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newPasswordEncoded");

        assertDoesNotThrow(() -> userService.changeParameterById(userInfoDTO));
        verify(userRepository, times(1)).merge(any(User.class));
        assertEquals("NewFirstName", user.getFirstName());
        assertEquals("NewLastName", user.getLastName());
    }

    @Test
    void shouldThrowServiceExceptionWhenUserIdIsNullOnUpdateParameters() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(null);

        assertThrows(ServiceException.class, () -> userService.changeParameterById(userInfoDTO),
                "User id is required for updating profile");
    }

    @Test
    void shouldThrowServiceExceptionWhenUserNotFoundOnUpdateParameters() {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(null);

        assertThrows(ServiceException.class, () -> userService.changeParameterById(userInfoDTO));
    }
}