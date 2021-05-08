package ru.mail.senokosov.artem.service.impl;

import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.repository.model.enums.RoleEnum;
import ru.mail.senokosov.artem.service.RoleService;
import ru.mail.senokosov.artem.service.converter.RoleConverter;

import javax.transaction.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    public RoleServiceImpl(RoleRepository roleRepository, RoleConverter roleConverter) {
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
    }

    @Override
    @Transactional
    public void addRole(RoleEnum roleEnum) {
        Role role = roleConverter.convert(roleEnum);
        roleRepository.persist(role);
    }
}
