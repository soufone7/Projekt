package bank;

import bank.exceptions.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class PrivateBank implements Bank {
    /**
     * Namen der PrivateBank
     */
    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    private String directoryName; // Der Speicherort der Konten
    /**
     * jedem gespeicherten Konto 0 bis n Transaktionen zugeordnet werden können.
     */
    private Map<String, List<Transaction>> accountsToTransactions= new HashMap<>();

    public PrivateBank(String name, double incomingInterest, double outgoingInterest)throws TransactionAttributeException {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName)
            throws TransactionAttributeException, IOException {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        setDirectoryName(directoryName);

        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        readAccounts();
    }

    public PrivateBank(PrivateBank otherPrivateBank) {
        this.name = otherPrivateBank.name;
        this.incomingInterest = otherPrivateBank.incomingInterest;
        this.outgoingInterest = otherPrivateBank.outgoingInterest;
        this.accountsToTransactions = new HashMap<>(otherPrivateBank.accountsToTransactions);
        this.directoryName= otherPrivateBank.directoryName;

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

    public void setIncomingInterest(double incomingInterest)throws TransactionAttributeException  {
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

    public void setOutgoingInterest(double outgoingInterest)throws TransactionAttributeException {
      if (outgoingInterest >= 0 && outgoingInterest <= 1) {
        this.outgoingInterest = outgoingInterest;
      } else {
        System.out.println("Falsche Incoming Interest");
        throw new TransactionAttributeException("error");
      }
    }

    public void setDirectoryName(String directoryName) {
    this.directoryName = directoryName;
  }

    public String getDirectoryName() {
    return this.directoryName;
  }


    @Override
    public String toString() {
        return "PrivateBank{" +
                "  \nName='" + this.name + '\'' +
                ", \nincomingInterest=" + this.incomingInterest +
                ", \noutgoingInterest=" + this.outgoingInterest +
                ", \naccountsToTransaction=\n" + this.accountsToTransactions.toString() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PrivateBank that = (PrivateBank) obj;
        return Double.compare(that.incomingInterest, incomingInterest) == 0 && Double.compare(that.outgoingInterest, outgoingInterest) == 0 && Objects.equals(name, that.name) && Objects.equals(accountsToTransactions, that.accountsToTransactions);

    }

  /**
   *
   * @param account Name von dem neuen Account
   */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {

        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }

        accountsToTransactions.put(account, new ArrayList<>());
        this.writeAccount(account);
    }

    /**
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, IOException {

        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Account already exists: " + account);
        }

        List<Transaction> existingTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {

            if (existingTransactions.contains(transaction)) {
                throw new TransactionAlreadyExistException("Transaction already exists: " + transaction);
            }
            existingTransactions.add(transaction);
        }

        accountsToTransactions.put(account, transactions);
        this.writeAccount(account);

    }

    /**
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     *  im fall von Payment sollen income-outgoinginterrest mit die Werte von PrivateBank
     */
    @Override
    public void addTransaction(String account, Transaction transaction)
      throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {

        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account '" + account + "' does not exist.");
        }
        if (containsTransaction(account, transaction)) {
            throw new TransactionAlreadyExistException("Transaction already exists in account '" + account + "'.");
        }
        if (transaction.getAmount()==0 || transaction.date==null) {
            throw new TransactionAttributeException("Attributen check fail : " + transaction);
        }
        if (transaction instanceof Payment payment) {
            payment.setIncomingInterest(this.incomingInterest);
            payment.setOutgoingInterest(this.outgoingInterest);

        }

        List<Transaction> accountTransactions = this.accountsToTransactions.get(account);
        accountTransactions.add(transaction);
        this.accountsToTransactions.put(account, accountTransactions);
        this.writeAccount(account);

    }

    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {

        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account does not exist: " + account);
        }

        List<Transaction> transactions = accountsToTransactions.get(account);
        if (!transactions.remove(transaction)) {
            throw new TransactionDoesNotExistException("Transaction does not exist for the account: " + transaction);
        }

        // Datei für dieses Konto nach der Änderung neu schreiben
        this.writeAccount(account);
    }


    /**
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * gibt, ob in eine account eine Transaction gibt oder nicht.
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {

        if (!accountsToTransactions.containsKey(account)) {
            return false;
        }
        List<Transaction> transactions = accountsToTransactions.get(account);
        return transactions.contains(transaction);
    }

    @Override
    public double getAccountBalance(String account) {
        double balance = 0.0;
        // Annahme: accountsToTransactions ist eine Map, die Konten mit ihren Transaktionen verknüpft
        List<Transaction> transactions = accountsToTransactions.get(account);
        if (transactions != null) {
            for (Transaction transaction : transactions) {
                    balance += transaction.calculate(); // Hier erfolgt die Polymorphe Aufruf von calculate()
            }
        }
        return balance;
    }
    @Override
    public List<Transaction> getTransactions(String account) {

        if (!accountsToTransactions.containsKey(account)) {
            return new ArrayList<>();
        }
        List<Transaction> transactions = accountsToTransactions.get(account);
        return new ArrayList<>(transactions);
    }

  /**
   *
   * @param account the selected account
   * @param asc     asc negative dann absteigend, wenn positiv dann aufsteigend
   */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {

        List<Transaction> transactions = new ArrayList<>(getTransactions(account));
        Comparator<Transaction> comparator = Comparator.comparingDouble(Transaction::calculate);

        if (!asc) {
            comparator = comparator.reversed();  // Sortiere absteigend, falls 'asc' false ist
        }
        transactions.sort(comparator);// sort benötigt einen Comparator

        return transactions;

    }
  /**
   *
   * @param account  the selected account
   * @param positive if true dann  gibt die positive transaction, wenn nicht dann negative
   */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : getTransactions(account)) {
            if ((positive && transaction.calculate() >= 0) || (!positive && transaction.calculate() < 0)) {
                transactions.add(transaction);  // Füge passende Transaktion hinzu
            }
        }
        return transactions;
    }


  /**
   *deserialisation account in file Accounts
   */
    /**
     * Liest alle gespeicherten Kontodateien (.json) aus dem angegebenen Verzeichnis
     * und lädt sie in die HashMap accountsToTransactions.
     */
    private void readAccounts() throws IOException {

        // Verzeichnis, in dem die JSON-Dateien liegen
        File fileReader = new File(directoryName);

        // Prüfen, ob das Verzeichnis existiert und auch wirklich ein Verzeichnis ist
        if (!fileReader.exists() || !fileReader.isDirectory()) {
            throw new IOException("Das Ziel existiert nicht oder ist kein Verzeichnis." + fileReader.getAbsolutePath());
        }

        // Liste aller Dateien im Verzeichnis
        File[] ArrayOfFiles = fileReader.listFiles();

        // Falls Ordner leer oder null → nichts zu laden
        if (ArrayOfFiles == null || ArrayOfFiles.length == 0) {
            System.out.println("Keine Dateien und Unterverzeichnisse sind gefunden : " + fileReader.getAbsolutePath());
            return;
        }

        // Wird benutzt, um JSON aus Dateien zu parsen (veraltet, aber funktioniert)
        JsonParser jsonParser = new JsonParser();

        // Gson-Instanz zum Deserialisieren, MIT registriertem Custom-Deserializer
        Gson deserialize = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new Customserialisieren())
                .setPrettyPrinting()
                .create();

        // Typinformation für List<Transaction> (wegen Generics notwendig)
        Type typeList = new TypeToken<List<Transaction>>() {}.getType();

        // Durch alle Dateien im Verzeichnis iterieren
        for (File file : ArrayOfFiles) {

            // Nur .json-Dateien lesen
            if (file.isFile() && file.getName().endsWith(".json")) {

                try (FileReader reader = new FileReader(file)) {

                    // Dateiinhalt in ein JsonArray umwandeln
                    JsonArray transactionList = jsonParser.parse(reader).getAsJsonArray();

                    // Kontoname = Dateiname ohne .json
                    String fileName = file.getName().replace(".json", "");

                    // Liste von Transaction-Objekten auslesen (benutzt Customserialisieren)
                    List<Transaction> transactions = deserialize.fromJson(transactionList, typeList);

                    // In interne HashMap speichern
                    accountsToTransactions.put(fileName, transactions);

                } catch (IOException e) {
                    // Falls eine Datei nicht gelesen werden kann → nur diese Datei überspringen
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Alle gelesenen Konten : " + accountsToTransactions.keySet());
    }


    /**+
     * Speichert ein bestimmtes Konto als JSON-Datei im Dateisystem.
     * Es wird jeweils NUR dieses Konto gespeichert.
     */
    private void writeAccount(String account) throws IOException {

        // Nur speichern, wenn Konto existiert
        if (accountsToTransactions.containsKey(account)) {

            // Alle Transaktionen dieses Kontos holen
            List<Transaction> transactions = accountsToTransactions.get(account);

            // Liste von JsonElementen, die später gespeichert werden
            // Jeder Eintrag wird über deinen Customserialisieren erzeugt
            List<JsonElement> jsonElements = new ArrayList<>();
            Customserialisieren adapter = new Customserialisieren();

            // Jede Transaction manuell serialisieren
            for (Transaction transaction : transactions) {
                jsonElements.add(adapter.serialize(transaction, null, null));
            }

            // Gson nur zum Schreiben/Formatieren (Custom-Adapter wird HIER nicht benötigt)
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            // Speicherordner sicherstellen
            File dir = new File(directoryName);
            if (!dir.exists()) {
                dir.mkdirs(); // legt Verzeichnis an, falls nicht vorhanden
            }

            // Datei = <account>.json im Verzeichnis
            File file = new File(dir, account + ".json");

            // Datei schreiben (überschreibt alte Version)
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(jsonElements, writer);
            }
        }
    }}

