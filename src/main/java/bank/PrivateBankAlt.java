package bank;

import bank.exceptions.*;

import java.util.*;

public class PrivateBankAlt implements Bank {
    /**
     * Namen der PrivateBank
     */
    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    /**
     * jedem gespeicherten Konto 0 bis n Transaktionen zugeordnet werden können.
     */
    private Map<String, List<Transaction>> accountsToTransactions= new HashMap<>();
    public PrivateBankAlt(String name, double incomingInterest, double outgoingInterest) {
        this.name = name;
        this.incomingInterest = incomingInterest;
        this.outgoingInterest = outgoingInterest;
    }
    public PrivateBankAlt(PrivateBankAlt otherPrivateBank) {
        this.name = otherPrivateBank.name;
        this.incomingInterest = otherPrivateBank.incomingInterest;
        this.outgoingInterest = otherPrivateBank.outgoingInterest;
        this.accountsToTransactions = new HashMap<>(otherPrivateBank.accountsToTransactions);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getIncomingInterest() {
        return incomingInterest;
    }
    public void setIncomingInterest(double incomingInterest) {
        this.incomingInterest = incomingInterest;
    }
    public double getOutgoingInterest() {
        return outgoingInterest;
    }
    public void setOutgoingInterest(double outgoingInterest) {
        this.outgoingInterest = outgoingInterest;
    }

    @Override
    public String toString() {
        return "PrivateBank{\n" +
                "\nName='" + this.name + '\'' +
                ", \nincomingInterest=" + this.incomingInterest +
                ", \noutgoingInterest=" + this.outgoingInterest +
                ", \naccountsToTransaction=" + this.accountsToTransactions.toString() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PrivateBankAlt that = (PrivateBankAlt) obj;
        return Double.compare(that.incomingInterest, incomingInterest) == 0 && Double.compare(that.outgoingInterest, outgoingInterest) == 0 && Objects.equals(name, that.name) && Objects.equals(accountsToTransactions, that.accountsToTransactions);

    }
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        // Vérifier si le compte existe déjà
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }
        // Créer une nouvelle liste de transactions pour le nouveau compte
        List<Transaction> newAccountTransactions = new ArrayList<>();
        // Ajouter le nouveau compte à la map
        accountsToTransactions.put(account, newAccountTransactions);
    }

    /**
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException {  // <- TransactionAttributeException weg
        // Vérifier si le compte existe déjà
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }

        // Utiliser un ensemble pour stocker temporairement les transactions existantes
        Set<Transaction> existingTransactions = new HashSet<>();

        for (Transaction transaction : transactions) {
            // Prüfen, ob Transaktion doppelt ist
            if (existingTransactions.contains(transaction)) {
                throw new TransactionAlreadyExistException("Transaction already exists: " + transaction);
            }

            // >>> HIER hast du vorher TransactionAttributeException geworfen
            if (transaction.getAmount() < 0) {
                // Option: als unchecked Exception (= muss nicht im throws stehen)
                throw new IllegalArgumentException(
                        "Amount must be a non-negative value for transaction: " + transaction);
            }

            existingTransactions.add(transaction);
        }

        // Konto mit Transaktionen anlegen
        accountsToTransactions.put(account, new ArrayList<>(transactions));
    }

    /**
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     *  im fall von Payment sollen incom-outgoinginterrest mit die Werte von PrivateBank
     */
    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        // Vérifier si le compte existe
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account '" + account + "' does not exist.");
        }

        // Vérifier si la transaction existe déjà
        if (containsTransaction(account, transaction)) {
            throw new TransactionAlreadyExistException("Transaction already exists in account '" + account + "'.");
        }
        // Valider les attributs de la transaction
        // Exemple de validation pour l'attribut amount
        if (transaction.getAmount() < 0) {
            throw new TransactionAttributeException("Amount must be a non-negative value for transaction: " + transaction);
        }

        // Si la transaction est de type Payment, mettre à jour les attributs incomingInterest et outgoingInterest
        if (transaction instanceof Payment payment) {
            payment.setIncomingInterest(this.incomingInterest);
            payment.setOutgoingInterest(this.outgoingInterest);
            List<Transaction> accountTransactions = this.accountsToTransactions.get(account);
            accountTransactions.add(transaction);
            this.accountsToTransactions.put(account, accountTransactions);
        }

        // Ajouter la transaction au compte
        /*List<Transaction> accountTransactions = this.accountsToTransactions.get(account);
        accountTransactions.add(transaction);
        this.accountsToTransactions.put(account, accountTransactions);*/
    }

    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException {
        // Assurez-vous que le compte existe
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account does not exist: " + account);
        }
        // Récupérer la liste des transactions pour le compte donné
        List<Transaction> transactions = accountsToTransactions.get(account);
        // Vérifier si la transaction spécifiée est présente dans la liste
        if (transactions.contains(transaction)) {
            // Supprimer la transaction de la liste
            transactions.remove(transaction);
        } else {
            // Gérer l'exception si la transaction n'est pas présente
            throw new TransactionDoesNotExistException("Transaction does not exist for the account: " + transaction);
        }
    }

    /**
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * gibt, ob in eine account eine Transaction gibt oder nicht.
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        // Assurez-vous que le compte existe
        if (!accountsToTransactions.containsKey(account)) {
            // Gérer l'exception ou retourner false selon vos besoins
            return false;
        }
        // Récupérer la liste des transactions pour le compte donné
        List<Transaction> transactions = accountsToTransactions.get(account);
        // Vérifier si la transaction spécifiée est présente dans la liste
        return transactions.contains(transaction);
    }

    @Override
    public double getAccountBalance(String account) {
        double balance = 0.0;
        List<Transaction> transactions = accountsToTransactions.get(account);

        if (transactions != null) {
            for (Transaction transaction : transactions) {
                if (transaction instanceof Transfer) {
                    Transfer transfer = (Transfer) transaction;
                    if (account.equals(transfer.getSender())) {
                        // Betrag abziehen
                        balance -= transfer.getAmount();
                    } else {
                        // Betrag hinzuaddieren
                        balance += transfer.getAmount();
                    }
                } else {
                    balance += transaction.calculate();
                }
            }
        }

        return balance;
    }
    @Override
    public List<Transaction> getTransactions(String account) {
        // Assurez-vous que le compte existe
        //return this.accountsToTransactions.get(account);
        if (!accountsToTransactions.containsKey(account)) {
            // Gérer l'exception ou retourner une liste vide selon vos besoins
            return new ArrayList<>();
        }

        // Récupérer la liste des transactions pour le compte donné
        List<Transaction> transactions = accountsToTransactions.get(account);

        // Créer une copie de la liste pour des raisons de sécurité (pour éviter la modification externe)
        return new ArrayList<>(transactions);
    }


    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        // Assurez-vous que le compte existe
        if (!accountsToTransactions.containsKey(account)) {

            return new ArrayList<>();
        }
        // Récupérer la liste des transactions pour le compte donné
        List<Transaction> transactions = accountsToTransactions.get(account);

        // Trier les transactions en fonction du paramètre 'asc'
        Comparator<Transaction> comparator = Comparator.comparingDouble(Transaction::calculate);

        if (!asc) {
            // Inverser l'ordre si 'asc' est false
            comparator = comparator.reversed();
        }

        // Trier les transactions en utilisant le comparateur
        transactions.sort(comparator);

        // Créer une copie de la liste triée pour éviter les modifications externes
        return new ArrayList<>(transactions);    }

    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        // Assurez-vous que le compte existe
        if (!accountsToTransactions.containsKey(account)) {
            // Gérer l'exception ou retourner une liste vide selon vos besoins
            return new ArrayList<>();
        }

        // Récupérer la liste des transactions pour le compte donné
        List<Transaction> transactions = accountsToTransactions.get(account);

        // Filtrer les transactions en fonction du paramètre 'positive'
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if ((positive && transaction.getAmount() >= 0) || (!positive && transaction.getAmount() < 0)) {
                filteredTransactions.add(transaction);
            }
        }

        // Créer une copie de la liste filtrée pour éviter les modifications externes
        return new ArrayList<>(filteredTransactions);    }
}