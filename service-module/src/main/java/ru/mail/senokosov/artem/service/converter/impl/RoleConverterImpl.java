package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.enums.RoleEnum;
import ru.mail.senokosov.artem.service.converter.RoleConverter;
import ru.mail.senokosov.artem.service.model.show.ShowRoleDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleConverterImpl implements RoleConverter {

    @Override
    public ShowRoleDTO convert(Role role) {
        ShowRoleDTO showRoleDTO = new ShowRoleDTO();
        Long id = role.getId();
        showRoleDTO.setId(id);
        String roleStatus = String.valueOf(role.getRole());
        showRoleDTO.setRole(roleStatus);
        return showRoleDTO;
    }
}
