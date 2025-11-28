import bank.Payment;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    Payment p1, p2;

    @BeforeEach
    void init() throws TransactionAttributeException {
        p1 = new Payment("10.11.2024", 100, "TestPayment", 0.05, 0.10);
        p2 = new Payment(p1); // Copy-Konstruktor
    }

    @Test
    void testConstructor() {
        assertEquals("10.11.2024", p1.getDate());
        assertEquals(100, p1.getAmount());
        assertEquals("TestPayment", p1.getDescription());
    }

    @Test
    void testCopyConstructor() {
        assertEquals(p1, p2);
        assertNotSame(p1, p2);
    }

    @ParameterizedTest
    @ValueSource(doubles = { 100, 200, 300 })
    void testCalculatePositive(double amount) throws TransactionAttributeException {
        Payment temp = new Payment("01.01.2025", amount, "Test", 0.05, 0.1);
        double expected = amount * (1 - 0.05);
        assertEquals(expected, temp.calculate());
    }

    @Test
    void testCalculateNegativeOutgoing() throws TransactionAttributeException {
        Payment temp = new Payment("01.01.2025", -200, "Outgoing", 0.05, 0.10);
        double expected = -200 * (1 + 0.10);
        assertEquals(expected, temp.calculate());
    }

    @Test
    void testEquals() throws TransactionAttributeException {
        assertEquals(p1, p2);
        Payment p3= new Payment("10.21.2024", 100, "TestPayment", 0.05, 0.10);
        assertNotEquals(p1, p3);
    }

    @Test
    void testToString() {
        assertTrue(p1.toString().contains("TestPayment"));
        assertTrue(p1.toString().contains("10.11.2024"));
    }

    @Test
    void testInvalidAttributes() {
        assertThrows(TransactionAttributeException.class, () ->
                new Payment("15.02.2025", 0, "Fehler", 0.02, 2.0));
    }

}

