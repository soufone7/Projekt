import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferTest {
    OutgoingTransfer transfer1;
    IncomingTransfer transfer2;

    /**
     * Creates new test objects for transfer
     */
    @BeforeEach
    public void init() throws TransactionAttributeException {
        transfer1 = new OutgoingTransfer("3.11.2025",45,"Vinted Überweisung","Souf","Ronnie");
        transfer2 = new IncomingTransfer("3.11.2025",789,"Gehalt","Aldi","Souf");
    }

    /**
     * Tests constructor
     */
    @Test
    public void construcorTest(){
        Assertions.assertEquals(transfer1.getDate(),"3.11.2025");
        Assertions.assertEquals(transfer1.getAmount(),45);
        Assertions.assertEquals(transfer1.getDescription(),"Vinted Überweisung");
        Assertions.assertEquals(transfer1.getSender(),"Souf");
        Assertions.assertEquals(transfer1.getRecipient(),"Ronnie");
    }

    /**
     * Tests Copy-Contructor
     */
    @Test
    public void copyContructorTest() throws TransactionAttributeException {
        OutgoingTransfer transferTest = new OutgoingTransfer(transfer1);
        Assertions.assertEquals(transfer1,transferTest);
    }

    /**
     * Tests Calculate
     */
    @Test
    public void calculateTest(){
        Assertions.assertEquals(transfer1.calculate(),-45);
        Assertions.assertEquals(transfer2.calculate(),789);
    }

    /**
     * Tests equals
     */
    @Test
    public void equalsTest() throws TransactionAttributeException {
        Assertions.assertEquals(transfer1,new OutgoingTransfer("3.11.2025",45,"Vinted Überweisung","Souf","Ronnie"));
    }

    /**
     * Tests toString
     */
    @Test
    public void toStringTest(){
        Assertions.assertEquals(transfer1.toString(),"\nDate: 3.11.2025 Amount: -45.0 Description: Vinted Überweisung Sender: Souf Recipient: Ronnie");
    }

}
