package ru.mail.senokosov.artem.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.mail.senokosov.artem.service.model.enums.OrderStatusDTOEnum;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldPassValidationWhenOrderDTOIsValid() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUuidOfOrder(UUID.randomUUID());
        orderDTO.setOrderStatus(OrderStatusDTOEnum.NEW.name());
        orderDTO.setTotalPrice(new BigDecimal("100.00"));
        orderDTO.setTelephone("+12345678901");

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(orderDTO);

        assertThat(violations).isEmpty();
    }
}