package bank;

import bank.exceptions.TransactionAttributeException;

public class OutgoingTransfer extends Transfer {
    public OutgoingTransfer(String data, double amount, String description)throws TransactionAttributeException {
        super(data, amount, description);
    }
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient)throws TransactionAttributeException {
        super(date, amount, description, sender, recipient);
    }
    public OutgoingTransfer(Transfer transfr) throws TransactionAttributeException {
      super(transfr);
    }
  @Override
    public double calculate() {
        return -super.calculate();}
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    OutgoingTransfer that = (OutgoingTransfer) obj;

    return Double.compare(that.getAmount(), getAmount()) == 0 &&
      getDate().equals(that.getDate()) &&
      getDescription().equals(that.getDescription()) &&
      getSender().equals(that.getSender()) &&
      getRecipient().equals(that.getRecipient());
  }
}
