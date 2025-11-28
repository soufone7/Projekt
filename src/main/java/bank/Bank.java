package bank;

import bank.exceptions.*;
import java.io.IOException;
import java.util.List;

public interface Bank {

    void createAccount(String account)
            throws AccountAlreadyExistsException, IOException;

    void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, IOException;

    void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException,
            TransactionAttributeException, IOException;

    void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException;

    boolean containsTransaction(String account, Transaction transaction);

    double getAccountBalance(String account);

    List<Transaction> getTransactions(String account);

    List<Transaction> getTransactionsSorted(String account, boolean asc);

    List<Transaction> getTransactionsByType(String account, boolean positive);
}
