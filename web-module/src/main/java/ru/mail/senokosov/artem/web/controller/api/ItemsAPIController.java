package ru.mail.senokosov.artem.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mail.senokosov.artem.service.ItemService;
import ru.mail.senokosov.artem.service.exception.ServiceException;
import ru.mail.senokosov.artem.service.model.ItemDTO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.mail.senokosov.artem.web.constant.PathConstant.ITEMS_PATH;
import static ru.mail.senokosov.artem.web.constant.PathConstant.REST_API_USER_PATH;

@Slf4j
@RestController
@RequestMapping(REST_API_USER_PATH)
@RequiredArgsConstructor
public class ItemsAPIController {

    private final ItemService itemService;

    @GetMapping(value = ITEMS_PATH)
    public ResponseEntity<List<ItemDTO>> getItems() {
        try {
            List<ItemDTO> itemDTOs = itemService.getAllItems();
            if (itemDTOs.isEmpty()) {
                log.info("No items found.");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(itemDTOs, HttpStatus.OK);
        } catch (Exception exception) {
            log.error("Error retrieving items: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = ITEMS_PATH + "/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        try {
            ItemDTO itemById = itemService.getItemById(id);
            return new ResponseEntity<>(itemById, HttpStatus.OK);
        } catch (ServiceException exception) {
            log.warn("Item with id: {} was not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            log.error("Error retrieving item by id: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = ITEMS_PATH)
    public ResponseEntity<?> addItem(@RequestBody @Valid ItemDTO itemDTO, BindingResult result) {
        log.info("Attempting to add item: {}", itemDTO.getTitle());
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(Map.of("errors", errors), HttpStatus.BAD_REQUEST);
        }
        try {
            ItemDTO createdItem = itemService.addItem(itemDTO);
            log.info("Successfully added item with title: '{}'", createdItem.getTitle());
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        } catch (ServiceException exception) {
            log.error("Error adding item with title '{}': {}", itemDTO.getTitle(), exception.getMessage());
            return new ResponseEntity<>(Map.of("error", exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = ITEMS_PATH + "/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            boolean isDeleted = itemService.isDeleteById(id);
            if (isDeleted) {
                String successMessage = "Item with id " + id + " was successfully deleted.";
                log.info(successMessage);
                return ResponseEntity.ok(Map.of("message", successMessage));
            } else {
                String notFoundMessage = "Item with id " + id + " was not found.";
                log.warn(notFoundMessage);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", notFoundMessage));
            }
        } catch (Exception exception) {
            String errorMessage = "Error deleting item with id " + id + ": " + exception.getMessage();
            log.error(errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMessage));
        }
    }
}