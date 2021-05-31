package ru.mail.senokosov.artem.service.converter;

import ru.mail.senokosov.artem.repository.model.OrderStatus;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.enums.RoleEnum;
import ru.mail.senokosov.artem.service.model.show.ShowOrderStatusDTO;
import ru.mail.senokosov.artem.service.model.show.ShowRoleDTO;

import java.util.List;

public interface RoleConverter {

    ShowRoleDTO convert(Role role);
}
