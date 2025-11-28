package bank;

import bank.exceptions.TransactionAttributeException;

import java.util.Objects;

/**
 * Payment ist eine Unterklasse, der Klasse Transaction
 * Sie rechnet das Gehalt mit Incoming-oder-OutgoingInterest
 */
public class Payment extends Transaction  {
    private double incomingInterest;
    private double outgoingInterest;

    /**
     *
     * @param date datum der Payment
     * @param amount Wie viel Geld im Payment
     * @param description Was ist Ziel dieses Payment
     */
    public Payment(String date, double amount, String description)throws TransactionAttributeException {
        super(date, amount, description);
    }

    /**
     *Constructor nutzt vorheriger constructor un gibt incoming und outgoing-interest
     * @param incomingInterest muss zwischen 0 und 1
     * @param outgoingInterest muss zwischen 0 und 1
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest) throws TransactionAttributeException {
        super(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     *
     * @param otherPayment ist eine copy von eine existierende objekt von Datentyp Payment
     */
    public Payment(Payment otherPayment) throws TransactionAttributeException {
        this(otherPayment.getDate(),otherPayment.getAmount(),otherPayment.getDescription(),otherPayment.getIncomingInterest(), otherPayment.getOutgoingInterest());


    }

    public double getIncomingInterest() {
        return incomingInterest;
    }

    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest >= 0 && incomingInterest <= 1) {
            this.incomingInterest = incomingInterest;
        } else {
            System.out.println("Falsche Incoming Interest");
          throw new TransactionAttributeException("error");
        }
    }

    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     *
     * @param outgoingInterest muss zwischen 0 und 1 sein
     */
    public void setOutgoingInterest(double outgoingInterest)throws TransactionAttributeException {
        if (outgoingInterest >= 0 && outgoingInterest <= 1) {
            this.outgoingInterest = outgoingInterest;
        } else {
            System.out.println("Falsche outgoing interest");
            throw new TransactionAttributeException("error");
        }
    }

    /**
     *
     * @return gibt das Amount an nach rechnung von Interrest
     */
    @Override
    public double calculate() {
        if (getAmount() >= 0) {
            return getAmount() * (1 - incomingInterest);
        } else {
            return getAmount() * (1 + outgoingInterest);
        }
    }

    /**
     *
     * @return gibt das Payment als string im Compiler
     */
    @Override
    public String toString() {
        return super.toString() + " Incoming Interest: " + incomingInterest + " Outgoing Interest: " + outgoingInterest;
    }
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    Payment payment = (Payment) obj;

    // Utilisation de Objects.equals pour comparer les champs
    return Objects.equals(date, payment.date) &&
      Objects.equals(amount, payment.amount) &&
      Objects.equals(description, payment.description) &&
      Objects.equals(incomingInterest, payment.incomingInterest) &&
      Objects.equals(outgoingInterest, payment.outgoingInterest);
  }
}
