package ru.mail.senokosov.artem.service.converter.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.converter.OrderConverter;
import ru.mail.senokosov.artem.service.model.OrderDTO;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderConverterImpl implements OrderConverter {

    private final ModelMapper modelMapper;

    @Override
    public OrderDTO convert(Order order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

        UUID numberOfOrder = order.getUuidOfOrder();
        orderDTO.setUuidOfOrder(numberOfOrder);

        OrderStatus orderStatus = order.getOrderStatus();
        String status = orderStatus.getStatus();
        orderDTO.setOrderStatus(status);

        Item item = order.getItem();
        orderDTO.setTitle(item.getTitle());

        Long numberOfItems = order.getNumberOfItems();
        orderDTO.setNumberOfItems(numberOfItems);

        BigDecimal totalPrice = order.getTotalPrice();
        orderDTO.setTotalPrice(totalPrice);

        User user = order.getUser();
        if (Objects.nonNull(user)) {
            String lastName = user.getLastName();
            orderDTO.setLastName(lastName);
            UserInfo userInfo = user.getUserInfo();
            if (Objects.nonNull(userInfo)) {
                String telephone = userInfo.getTelephone();
                orderDTO.setTelephone(telephone);
            } else {
                orderDTO.setTelephone("N/A");
            }
        }

        return orderDTO;
    }
}