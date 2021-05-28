package ua.strongBody.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.strongBody.models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static ua.strongBody.models.Customer.*;

@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest {

    private static final int ROW_NUM = 10;
    private static final UUID ID = UUID.randomUUID();
    private static final String EMAIL = "cus@bla.com";
    private static final String USERNAME = "cutouser";
    private static final String PASSWORD = "cus";
    private static final String FIRST_NAME = "Custo";
    private static final String LAST_NAME = "Mer";
    private static final String PHONE_NUMBER = "+99999999";

    @Mock
    private ResultSet resultSet;

    private final CustomerRowMapper testInstance = new CustomerRowMapper();

    @BeforeEach
    void setUp() throws SQLException {
        when(resultSet.getObject(ID_FIELD, UUID.class)).thenReturn(ID);
        when(resultSet.getString(EMAIL_FIELD)).thenReturn(EMAIL);
        when(resultSet.getString(FIRST_NAME_FIELD)).thenReturn(FIRST_NAME);
        when(resultSet.getString(LAST_NAME_FIELD)).thenReturn(LAST_NAME);
        when(resultSet.getString(PASSWORD_FIELD)).thenReturn(PASSWORD);
        when(resultSet.getString(USERNAME_FIELD)).thenReturn(USERNAME);
        when(resultSet.getString(PHONE_NUMBER_FIELD)).thenReturn(PHONE_NUMBER);
    }

    @Test
    void shouldMapRow() throws SQLException {
        Customer actual = testInstance.mapRow(resultSet, ROW_NUM);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(ID);
        assertThat(actual.getEmail()).isEqualTo(EMAIL);
        assertThat(actual.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(actual.getLastName()).isEqualTo(LAST_NAME);
        assertThat(actual.getPassword()).isEqualTo(PASSWORD);
        assertThat(actual.getUsername()).isEqualTo(USERNAME);
        assertThat(actual.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
    }
}
