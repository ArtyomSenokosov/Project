package ru.mail.senokosov.artem.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.mail.senokosov.artem.repository.model.*;
import ru.mail.senokosov.artem.service.converter.OrderConverter;
import ru.mail.senokosov.artem.service.model.show.ShowOrderDTO;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Component
public class OrderConverterImpl implements OrderConverter {

    @Override
    public ShowOrderDTO convert(Order order) {
        ShowOrderDTO showOrderDTO = new ShowOrderDTO();
        Long id = order.getId();
        showOrderDTO.setId(id);
        UUID numberOfOrder = order.getNumberOfOrder();
        showOrderDTO.setNumberOfOrder(numberOfOrder);
        OrderStatus orderStatus = order.getOrderStatus();
        String status = orderStatus.getStatus();
        showOrderDTO.setOrderStatus(status);
        Item item = order.getItem();
        showOrderDTO.setTitle(item.getTitle());
        Long numberOfItems = order.getNumberOfItems();
        showOrderDTO.setNumberOfItems(numberOfItems);
        BigDecimal totalPrice = order.getTotalPrice();
        showOrderDTO.setTotalPrice(totalPrice);
        OrderInfo orderInfo = order.getOrderInfo();
        if (Objects.nonNull(orderInfo)) {
            User user = orderInfo.getUser();
            if (Objects.nonNull(user)) {
                String lastName = user.getLastName();
                showOrderDTO.setLastName(lastName);
                UserInfo userInfo = (UserInfo) user.getUserInfo();
                String telephone = userInfo.getTelephone();
                showOrderDTO.setTelephone(telephone);
            }
        }
        return showOrderDTO;
    }
}