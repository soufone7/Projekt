package bank;

import bank.exceptions.TransactionAttributeException;

public abstract class Transaction implements CalculateBill {
    protected String date;
    protected double amount;
    protected String description;

    public Transaction(String date, double amount, String description)throws TransactionAttributeException {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount)throws TransactionAttributeException {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     *
     * @return return alle attribute als string
     */
    @Override
    public String toString() {
        return "\nDate: " + date + " Amount: " + calculate() + " Description: " + description;
    }

    /**
     *this: object auf dem die methode aufgerufen wird
     * @param obj object auf dem die methode aufgerufen wird
     * @return gibt true an, wenn das objekt ist gleich als das Objekt, auf dem die Methode aufgerufen wird, wenn nicht, dann false
     */
    @Override
    public boolean equals(Object obj) {
      
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;// wenn objekt null ist, oder sie haben nicht dieselbe klasse

        Transaction that = (Transaction) obj;//new "that" der datentyp transaction ist


        if (Double.compare(that.amount, amount) == 0) return false;
        if (!date.equals(that.date)) return false;
        return description.equals(that.description);
    }
}
