package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.dao.OrderDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Customer;
import ua.strongBody.models.Order;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final UUID CUSTOMER_ID = UUID.randomUUID();

    private Order order;

    @Mock
    private OrderDAO orderDAO;

    @InjectMocks
    private OrderServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);

        order = new Order();
        order.setId(ORDER_ID);
        order.setCustomer(customer);
    }

    @Test
    void shouldFindAll() {
        testInstance.findAll();

        verify(orderDAO).findAll();
    }

    @Test
    void shouldSave() {
        testInstance.save(order);

        verify(orderDAO).save(order);
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(ORDER_ID, order);

        verify(orderDAO).updateById(ORDER_ID, order);
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(ORDER_ID);

        verify(orderDAO).deleteById(ORDER_ID);
    }

    @Test
    void shouldFindById() {
        Optional<Order> orderOptional = Optional.of(order);
        when(orderDAO.findById(ORDER_ID)).thenReturn(orderOptional);

        Order actual = testInstance.findById(ORDER_ID);

        assertThat(actual).isEqualTo(order);
    }

    @Test
    void shouldGenerateExceptionOnInvalidId() {
        Optional<Order> orderOptional = Optional.empty();
        when(orderDAO.findById(ORDER_ID)).thenReturn(orderOptional);

        assertThrows(FieldNotFoundException.class, () -> testInstance.findById(ORDER_ID));
    }

    @Test
    void findOrdersByCustomerId() {
        orderDAO.findOrdersByCustomerId(CUSTOMER_ID);

        verify(orderDAO).findOrdersByCustomerId(CUSTOMER_ID);
    }
}
