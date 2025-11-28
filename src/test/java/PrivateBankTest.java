import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für die Klasse {@link PrivateBank}.
 *
 * Diese Klasse überprüft alle Methoden der PrivateBank-Klasse:
 * Konstruktor, Copy-Konstruktor, createAccount, addTransaction,
 * removeTransaction, containsTransaction, getAccountBalance,
 * getTransactions, getTransactionsSorted, getTransactionsByType.
 *
 * Zusätzlich werden Exception-Fälle getestet sowie persistierte
 * JSON-Dateien nach jedem Test wieder gelöscht, um unabhängige Tests
 * sicherzustellen.
 *
 * assert = kontrolliert, ob ein bestimmtes Verhalten richtig ist.
 * Die Ergebnisse gehen automatisch an JUnit, nicht ins Terminal.
 * JUnit zeigt sie als grün/rot, damit du sofort siehst, ob alles funktioniert.
 */
public class PrivateBankTest {

    PrivateBank bank1;
    Payment payment ;
    Transfer transfer;
    IncomingTransfer incomingTransfer;
    OutgoingTransfer outgoingTransfer;

    /**
     * Standardkonstruktor der Testklasse.
     * Wird nicht aktiv verwendet, muss aber Exceptions deklarieren,
     * da JUnit Testklassen standardmäßig über Reflection erzeugt.
     */
    public PrivateBankTest() throws TransactionAttributeException, IOException {}

    /**
     * Initialisiert vor jedem Test neue Testobjekte
     * (PrivateBank + verschiedene Transaktionen).
     * Diese Methode stellt sicher, dass jeder Test mit identischen
     * Startbedingungen beginnt und keine Änderungen aus vorherigen
     * Tests übernommen werden.
     */
    @BeforeEach
    public void init() throws TransactionAttributeException, IOException {

        bank1 = new PrivateBank("KSK", 0.7, 0.7, "D:\\3 Semester\\os\\Praktikum\\Prak4\\testdatei");
        payment = new Payment("3.11.2025", 250, "Gehalt", 0.7, 0.2);
        transfer = new Transfer("3.11.2025", 200, "Aldi");
        outgoingTransfer = new OutgoingTransfer("3.11.2025",45,"Vinted Überweisung","Souf","Alex");
        incomingTransfer = new IncomingTransfer("3.11.2025",789,"Gehalt","Aldi","Souf");
    }

    /**
     * Löscht nach jedem Test alle JSON-Dateien im Testverzeichnis,
     * damit die Persistenz keinen Test beeinflusst.
     */
    @AfterEach
    void cleanup() {
        File directory = new File("D:\\3 Semester\\os\\Praktikum\\Prak4\\testdatei");
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    // file.delete();   // Wieder aktivieren, sobald nötig
                }
            }
        }
    }

    /**
     * Testet Konstruktor der Klasse PrivateBank.
     * Überprüft, ob die gesetzten Werte korrekt übernommen werden.
     */
    @Test
    public void constrocturTest() {
        assertEquals(bank1.getName(), "KSK");
        assertEquals(bank1.getIncomingInterest(), 0.7);
        assertEquals(bank1.getOutgoingInterest(), 0.7);
        assertEquals(bank1.getDirectoryName(), "D:\\3 Semester\\os\\Praktikum\\Prak4\\testdatei");
    }

    /**
     * Testet den Copy-Konstruktor.
     * Überprüft, ob alle Attribute korrekt kopiert wurden.
     */
    @Test
    public void CopyKonstruktTest()  {
        PrivateBank bank2 = new PrivateBank(bank1);
        assertEquals(bank1.getName(), bank2.getName());
        assertEquals(bank1.getIncomingInterest(), bank2.getIncomingInterest());
        assertEquals(bank1.getOutgoingInterest(), bank2.getOutgoingInterest());
        assertEquals(bank1.getDirectoryName(), bank2.getDirectoryName());
        assertEquals(bank1.getOutgoingInterest(), bank2.getOutgoingInterest());
    }

    /**
     * Testet createAccount(Account + List<Transaction>).
     * Testet sowohl erfolgreichen Kontoanlegung als auch:
     * - Transaktion wird korrekt gebunden
     * - Fehlermeldung bei erneutem Anlegen
     */
    @Test
    public void createAccountTest() {

        List<Transaction> list_Souf = new ArrayList<Transaction>();
        list_Souf.add(payment);

        try {
            bank1.createAccount("Souf", list_Souf);
        } catch (AccountAlreadyExistsException | TransactionAlreadyExistException | IOException e) {
            e.printStackTrace();
        }

        //@Contract(value = "false -> fail", pure = true)
        assertTrue(bank1.containsTransaction("Souf", payment));
        //@Contract(value = "true -> fail", pure = true
        assertFalse(bank1.containsTransaction("tarik", payment));
        assertThrows(AccountAlreadyExistsException.class, () -> bank1.createAccount("Souf"));
    }

    /**
     * Testet addTransaction():
     * - erfolgreiche Hinzufügung
     * - doppelte Transaktion
     * - Konto existiert nicht
     */
    @Test
    public void addTransactionTest() throws TransactionAttributeException {

        Payment payment2 = new Payment("4.11.2025", 120, "AOK", 0.4, 0.01);
        try {
            bank1.createAccount("tarik");
            bank1.addTransaction("tarik", payment2);
            bank1.addTransaction("tarik", outgoingTransfer );
            bank1.addTransaction("tarik", incomingTransfer);
        } catch (TransactionAlreadyExistException | AccountAlreadyExistsException | TransactionAttributeException |
                 AccountDoesNotExistException | IOException e) {
            e.printStackTrace();
        }

        assertTrue(bank1.containsTransaction("tarik", payment2));
        //Beim Ausführen dieses Codes MUSS die Exception X geworfen werden.
        //Wenn NICHT diese Exception kommt → Test schlägt fehl.
        //Wenn eine ANDERE Exception kommt → Test schlägt fehl.
        assertThrows(TransactionAlreadyExistException.class, () -> bank1.addTransaction("tarik", payment2));
        assertThrows(AccountDoesNotExistException.class, () -> bank1.addTransaction("SouF", payment2));
    }

    /**
     * Testet removeTransaction():
     * - erfolgreiche Entfernung
     * - Entfernen einer nicht vorhandenen Transaktion
     */
    @Test
    public void removeTransactionTest() throws TransactionAttributeException {
        Payment payment3 = new Payment("5.12.2025", 300, "c&a", 0.5, 0.01);
        try {
            bank1.createAccount("ines");
            bank1.addTransaction("ines", payment3);
            bank1.addTransaction("ines", outgoingTransfer );
            bank1.addTransaction("ines", incomingTransfer);
            bank1.removeTransaction("ines", payment3);
        } catch (AccountDoesNotExistException | TransactionDoesNotExistException | TransactionAttributeException |
                 AccountAlreadyExistsException | IOException e) {
            e.printStackTrace();
        } catch (TransactionAlreadyExistException e) {
            throw new RuntimeException(e);
        }

        assertFalse(bank1.containsTransaction("ines", payment3));
        assertThrows(TransactionDoesNotExistException.class, () -> bank1.removeTransaction("ines", payment3));
    }

    /**
     * Testet containsTransaction() mit validen und invaliden Accounts.
     */
    @Test
    public void ContainsTransactionTest() throws TransactionAttributeException {
        Payment payment4 = new Payment("6.12.2025", 400, "Asos", 0.5, 0.01);
        try {
            bank1.createAccount("zaka");
            bank1.addTransaction("zaka", payment4);
            bank1.addTransaction("zaka", outgoingTransfer );
            bank1.addTransaction("zaka", incomingTransfer);
        } catch (AccountAlreadyExistsException | TransactionAlreadyExistException | AccountDoesNotExistException |
                 TransactionAttributeException | IOException e) {
            e.printStackTrace();
        }

        assertTrue(bank1.containsTransaction("zaka", payment4));
    }

    /**
     * Testet getAccountBalance().
     * Überprüft korrekte Berechnung inklusive Payment, Transfer,
     * IncomingTransfer und OutgoingTransfer.
     */
    @Test
    public void AccountBalanceTest() throws TransactionAttributeException {
        Payment payment5 = new Payment("7.11.2025", 900, "uber", 0.6, 0.01);
        try {
            bank1.createAccount("messi");
            bank1.addTransaction("messi", payment5);
            bank1.addTransaction("messi", transfer);
            bank1.addTransaction("messi", outgoingTransfer );
            bank1.addTransaction("messi", incomingTransfer);
        } catch (AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException |
                 AccountDoesNotExistException | IOException e) {
            e.printStackTrace();
        }

        assertEquals(bank1.getAccountBalance("messi"), 1214.0);
        assertNotEquals(bank1.getAccountBalance("messi"), 1430.0);
    }

    /**
     * Testet getTransactions():
     * Rückgabe der vollständigen Transaktionsliste.
     */
    @Test
    public void getTransactionTest() throws TransactionAttributeException {
        Payment payment5 = new Payment("8.11.2025", 150, "Danke für das Ausleihen ", 0.6, 0.01);
        List<Transaction> list = new ArrayList<Transaction>();

        try {
            bank1.createAccount("ronaldo");
            bank1.addTransaction("ronaldo", payment5);
            bank1.addTransaction("ronaldo", outgoingTransfer );
            bank1.addTransaction("ronaldo", incomingTransfer);
            list.add(payment5);
            list.add(outgoingTransfer);
            list.add(incomingTransfer);
        } catch (AccountAlreadyExistsException | TransactionAlreadyExistException | TransactionAttributeException |
                 AccountDoesNotExistException | IOException e) {
            e.printStackTrace();
        }

        assertEquals(bank1.getTransactions("ronaldo"), list);
    }

    /**
     * Testet die Sortierfunktion getTransactionsSorted():
     * sowohl aufsteigend als auch absteigend.
     */
    @Test
    public void getTransactionSort() throws TransactionAttributeException {
        Payment payment5 = new Payment("9.11.2025", 160, "Einkaufen", 0.6, 0.01);

        try {
            bank1.createAccount("salah");
            bank1.addTransaction("salah", payment5);
            bank1.addTransaction("salah", outgoingTransfer );
            bank1.addTransaction("salah", incomingTransfer);
            bank1.addTransaction("salah", transfer);
        } catch (AccountAlreadyExistsException | TransactionAlreadyExistException | AccountDoesNotExistException |
                 IOException e) {
            e.printStackTrace();
        }

        List<Transaction> descendingTransactions = bank1.getTransactionsSorted("salah", false);
        List<Transaction> expectedDescendingTransactions = Arrays.asList(incomingTransfer, transfer, payment5, outgoingTransfer);
        assertIterableEquals(expectedDescendingTransactions, descendingTransactions);

        List<Transaction> actualAscendingTransactions = bank1.getTransactionsSorted("salah", true);
        List<Transaction> expectedAscendingTransactions = Arrays.asList(outgoingTransfer, payment5, transfer, incomingTransfer);
        assertIterableEquals(expectedAscendingTransactions, actualAscendingTransactions);
    }

    /**
     * Testet die Filterung nach positiven und negativen Transaktionen.
     */
    @Test
    public void getTransactionByTypeTest() throws TransactionAttributeException {
        Payment payment5 = new Payment("10.11.2025", 200, "Nike Partnerschaft", 0.3, 0.01);
        Payment payment6 = new Payment("10.11.2025", -120, "Adidas Shoes", 0.5, 0.01);

        try {
            bank1.createAccount("lamin");
            bank1.addTransaction("lamin", payment5);
            bank1.addTransaction("lamin", payment6);
            bank1.addTransaction("lamin", incomingTransfer );
            bank1.addTransaction("lamin", outgoingTransfer );
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Transaction> actualPositiveTransactions = bank1.getTransactionsByType("lamin", true);
        List<Transaction> actualNegativeTransactions = bank1.getTransactionsByType("lamin", false);

        List<Transaction> expectedPositiveTransactions = List.of(payment5, incomingTransfer);
        List<Transaction> expectedNegativeTransactions = List.of(payment6, outgoingTransfer);

        assertIterableEquals(expectedPositiveTransactions, actualPositiveTransactions);
        assertIterableEquals(expectedNegativeTransactions, actualNegativeTransactions);
    }

}
