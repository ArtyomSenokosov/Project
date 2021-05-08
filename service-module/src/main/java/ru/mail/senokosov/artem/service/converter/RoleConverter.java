package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.enums.RoleEnum;

public interface RoleConverter {

    Role convert(RoleEnum roleEnum);
}
