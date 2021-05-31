package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.converter.ItemConverter;
import ru.mail.senokosov.artem.service.converter.OrderConverter;
import ru.mail.senokosov.artem.service.model.*;
import ru.mail.senokosov.artem.service.model.add.AddOrderDTO;
import ru.mail.senokosov.artem.service.model.show.ShowItemDTO;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor
public class OrderConverterImpl implements OrderConverter {

    private final ItemConverter itemConverter;

    @Override
    public ShowOrderDTO convert(Order order) {
        ShowOrderDTO showOrderDTO = new ShowOrderDTO();
        Long id = order.getId();
        showOrderDTO.setId(id);
        String orderStatus = String.valueOf(order.getOrderStatus());
        showOrderDTO.setStatus(orderStatus);
        Set<Item> items = Collections.singleton(order.getItem());
        if (!items.isEmpty()) {
            List<ShowItemDTO> itemsDTO = items.stream()
                    .map(itemConverter::convert)
                    .collect(Collectors.toList());
            showOrderDTO.getItems().addAll(itemsDTO);
        }
        Long quantity = order.getQuantity();
        showOrderDTO.setQuantity(quantity);
        Long totalPrice = order.getTotalPrice();
        showOrderDTO.setTotalPrice(totalPrice);
        User user = order.getUser();
        if (Objects.nonNull(user)) {
            String email = user.getEmail();
            showOrderDTO.setEmail(email);
        }
        UserInfo userInfo = order.getUserInfo();
        if (Objects.nonNull(userInfo)) {
            String telephone = userInfo.getTelephone();
            showOrderDTO.setTelephone(telephone);
        }
        return showOrderDTO;
    }

    @Override
    public Order convert(AddOrderDTO addOrderDTO) {
        Order order = new Order();
        Long quantity = addOrderDTO.getQuantity();
        order.setQuantity(quantity);
        Long totalPrice = addOrderDTO.getTotalPrice();
        order.setTotalPrice(totalPrice);
        return order;
    }

    @Override
    public OrderDTO convertToChange(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        Long id = order.getId();
        orderDTO.setId(id);
        OrderStatus status = order.getOrderStatus();
        orderDTO.setOrderStatus(status);
        return orderDTO;
    }
}
