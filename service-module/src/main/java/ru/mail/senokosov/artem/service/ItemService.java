package ru.mail.senokosov.artem.service;

import org.springframework.data.domain.Page;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import java.util.List;

public interface ItemService {

    Page<ShowItemDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    List<ShowItemDTO> getAllItems();

    ShowItemDTO getItemById(Long id);

    void persist(AddItemDTO addItemDTO);

    boolean deleteItemById(Long id) throws SecurityException;

    boolean copyItemById(Long id) throws SecurityException;
}
