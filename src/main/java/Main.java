import bank.*;
import bank.exceptions.*;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws  AccountAlreadyExistsException, TransactionAttributeException, AccountDoesNotExistException, TransactionDoesNotExistException {




      try {
        PrivateBank Sparkasse = new PrivateBank("Sparkasse", 0.02, 0.1, "D:\\3 Semester\\os\\Praktikum\\Prak4\\src\\main\\java\\bank\\");

        Payment p1 = new Payment("10.11.2024", 100, "1. Payment", 0.02, 0.1);
        Payment p2 = new Payment("12.11.1884", 200, "2. Payment", 0.02, 0.1);
        Payment p3 = new Payment("14.11.2009", 300, "3. Payment", 0.02, 0.1);

        IncomingTransfer t1 = new IncomingTransfer("17.01.2007", 15, "Miete", "Ronaldo", "Messi");
        IncomingTransfer t2 = new IncomingTransfer("15.01.2007", 100, "GEhalt", "SouF", "Ronaldo");
        IncomingTransfer t3 = new IncomingTransfer("16.01.2007", 1000, "Schulden", "Messi", "SouF");

        OutgoingTransfer t4 = new OutgoingTransfer("18.07.2007", 20, "kauf", "Ronaldo", "SouF");
        OutgoingTransfer t5 = new OutgoingTransfer("19.07.2007", 25, "Spiel", "SouF", "Messi");
        OutgoingTransfer t6 = new OutgoingTransfer("20.07.2007", 30, "kauf", "Messi", "Ronaldo");
        ArrayList<Transaction> list1 = new ArrayList<>();
        ArrayList<Transaction> list2 = new ArrayList<>();
        ArrayList<Transaction> list3 = new ArrayList<>();

        list1.add(p1);list1.add(t1);list1.add(t4);
        list2.add(p2);list2.add(t2);list2.add(t5);
        list3.add(p3);list3.add(t3);list3.add(t6);

        Sparkasse.createAccount("Account Ronaldo",list1);
        Sparkasse.createAccount("Account SouF",list2);
        Sparkasse.createAccount("Account Messi",list3);
        Payment p7= new Payment("19.11.2024",200,"4.Payment",0.02,0.01);
        Sparkasse.addTransaction("Account Ronaldo",p7);
        //Transfer t10 = new OutgoingTransfer("28.11.2022",-23,"Paypal Überweisung","Ich","Du");

      //System.out.println(t10.toString());
      } catch (AccountAlreadyExistsException | TransactionAttributeException | TransactionAlreadyExistException | IOException e) {
        e.printStackTrace();
      }

    }
}

