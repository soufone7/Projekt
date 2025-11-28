package bank;

import bank.exceptions.TransactionAttributeException;

public class IncomingTransfer extends Transfer {
    public IncomingTransfer(String data, double amount, String description) throws TransactionAttributeException {
        super(data, amount, description);
    }

    public IncomingTransfer(String date, double amount, String description, String sender, String recipient)throws TransactionAttributeException {
        super(date, amount, description, sender, recipient);
    }


}
