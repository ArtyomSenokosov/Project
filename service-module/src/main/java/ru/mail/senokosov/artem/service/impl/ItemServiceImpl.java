package ru.mail.senokosov.artem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.mail.senokosov.artem.repository.ItemRepository;
import ru.mail.senokosov.artem.repository.PagingAndSortingRepository;
import ru.mail.senokosov.artem.repository.model.Item;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemConverter itemConverter;
    private final ThreadLocal<PagingAndSortingRepository> pagingAndSortingRepository = new ThreadLocal<PagingAndSortingRepository>();

    @Override
    @Transactional
    public Page<ShowItemDTO> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.pagingAndSortingRepository.get().findAll(pageable);
    }

    @Override
    @Transactional
    public List<ShowItemDTO> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(itemConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShowItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id);
        return itemConverter.convert(item);
    }

    @Override
    @Transactional
    public void persist(AddItemDTO addItemDTO) {
        Item item = itemConverter.convert(addItemDTO);
        itemRepository.persist(item);
    }

    @Override
    @Transactional
    public boolean deleteItemById(Long id) throws SecurityException {
        Item byId = itemRepository.findById(id);
        itemRepository.remove(byId);
        return true;
    }

    @Override
    @Transactional
    public boolean copyItemById(Long id) throws SecurityException {
        Item byId = itemRepository.findById(id);
        itemRepository.merge(byId);
        return true;
    }
}
