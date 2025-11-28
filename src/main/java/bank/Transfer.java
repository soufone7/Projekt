package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Diese Klasse gibt was in der Transfer gibt, sender, recipient und was in der Klasse Transaction gibt
 * Sie ist eine Unterklasse der Klasse Transaction
 */
public class Transfer extends Transaction{
    private String sender;
    private String recipient;

    public Transfer(String date, double amount, String description)throws TransactionAttributeException {
        super(date, amount, description);
    }



    /**
     *Nutzt vorheriger constructor für date, amount, description
     * @param sender wer sendet das Transfer
     * @param recipient für wem ist der Transfer
     */
    public Transfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date, amount, description);
        setAmount(amount);
        setSender(sender);
        setRecipient(recipient);
    }

    /**
     *
     * @param otherTransfer ist eine copy von einem existierende objekt der typ Transfer
     */
    public Transfer(Transfer otherTransfer)throws TransactionAttributeException {
        super(otherTransfer.getDate(), otherTransfer.getAmount(), otherTransfer.getDescription());
        setSender(otherTransfer.getSender());
        setRecipient(otherTransfer.getRecipient());
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    @Override
    public void setAmount(double amount)throws TransactionAttributeException {
        if (amount>=0){
            this.amount=amount;
        }
        else {
            System.out.println("Amount must be positive");
          throw new TransactionAttributeException("Amount must be a non-negative value for transaction: " + amount);

        }
    }
    @Override
    public double calculate() {
        return getAmount(); // Transfers sans intérêts
    }

    /**
     *
     * @return eine string im main(print)
     */
    @Override
    public String toString() {
        return super.toString() + " Sender: " + sender + " Recipient: " + recipient;
    }
}
