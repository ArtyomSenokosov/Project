package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.RoleRepository;
import ru.mail.senokosov.artem.repository.model.Role;
import ru.mail.senokosov.artem.service.RoleService;
import ru.mail.senokosov.artem.service.converter.RoleConverter;
import ru.mail.senokosov.artem.service.model.show.ShowRoleDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    @Override
    @Transactional
    public List<ShowRoleDTO> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(roleConverter::convert)
                .collect(Collectors.toList());
    }
}
