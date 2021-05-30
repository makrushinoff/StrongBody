package ua.strongBody.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.dao.CustomerDAO;
import ua.strongBody.exceptions.FieldNotFoundException;
import ua.strongBody.models.Customer;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    private static final UUID ID = UUID.randomUUID();
    private static final String USERNAME = "username";

    private Customer customer;

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private CustomerServiceImpl testInstance;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @Test
    void shouldFindByUsername() {
        Optional<Customer> customerOptional = Optional.of(customer);
        when(customerDAO.findFirstByUsername(USERNAME)).thenReturn(customerOptional);

        Customer actual = testInstance.findByUsername(USERNAME);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void shouldProcessExportExceptionOnUsername() {
        when(customerDAO.findFirstByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(FieldNotFoundException.class, () -> testInstance.findByUsername(USERNAME));
    }

    @Test
    void shouldFindAll() {
        testInstance.findAll();

        verify(customerDAO).findAll();
    }

    @Test
    void shouldSave() {
        testInstance.save(customer);

        verify(customerDAO).save(customer);
    }

    @Test
    void shouldUpdateById() {
        testInstance.updateById(ID, customer);

        verify(customerDAO).updateById(ID, customer);
    }

    @Test
    void shouldDeleteById() {
        testInstance.deleteById(ID);

        verify(customerDAO).deleteById(ID);
    }

    @Test
    void shouldFindById() {
        Optional<Customer> customerOptional = Optional.of(customer);
        when(customerDAO.findById(ID)).thenReturn(customerOptional);

        Customer actual = testInstance.findById(ID);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void shouldProcessExportExceptionOnInvalidId() {
        when(customerDAO.findById(ID)).thenReturn(Optional.empty());

        assertThrows(FieldNotFoundException.class, () -> testInstance.findById(ID));
    }
}
