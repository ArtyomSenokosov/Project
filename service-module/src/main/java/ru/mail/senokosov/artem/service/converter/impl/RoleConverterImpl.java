package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.enums.RoleEnum;
import ru.mail.senokosov.artem.service.converter.RoleConverter;

@Component
public class RoleConverterImpl implements RoleConverter {
    @Override
    public Role convert(RoleEnum roleEnum) {
        Role role = new Role();
        role.setRole(roleEnum);
        return role;
    }
}
