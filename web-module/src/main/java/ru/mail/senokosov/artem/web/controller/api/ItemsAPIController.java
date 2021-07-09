package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.model.ErrorDTO;
import ru.mail.senokosov.artem.service.model.add.AddItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static ru.mail.senokosov.artem.web.constant.PathConstant.ITEMS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@RestController
@RequestMapping(REST_API_USER_PATH)
@RequiredArgsConstructor
@Log4j2
public class ItemsAPIController {

    private final ItemService itemService;

    @GetMapping(value = ITEMS_PATH)
    public List<ShowItemDTO> getItems() {
        return itemService.getItems();
    }

    @SneakyThrows
    @GetMapping(value = ITEMS_PATH + "/{id}")
    public ResponseEntity<ShowItemDTO> getItemById(@PathVariable Long id) throws ServiceException {
        ShowItemDTO itemById = itemService.getItemById(id);
        if (Objects.nonNull(itemById)) {
            return new ResponseEntity<>(itemById, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = ITEMS_PATH)
    public ResponseEntity<Object> addItem(@RequestBody @Valid AddItemDTO addItemDTO,
                                          BindingResult result) {
        if (result.hasErrors()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.getErrors().addAll(result.getFieldErrors());
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } else {
            itemService.persist(addItemDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @DeleteMapping(value = ITEMS_PATH + "/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        boolean deleteById = itemService.isDeleteById(id);
        if (deleteById) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}