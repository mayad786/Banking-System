import java.io.*;

public class BankAccount implements Serializable {
    private String accNumber;
    private String pin;
    private String accHolderName;
    private int balance;
    private boolean isAdmin;

    public BankAccount(String accNumber, String pin, String accHolderName, int balance, boolean isAdmin) {
        this.accNumber = accNumber;
        this.pin = pin;
        this.accHolderName = accHolderName;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    public String getAccNumber() { return accNumber; }
    public String getPin() { return pin; }
    public String getAccHolderName() { return accHolderName; }
    public int getBalance() { return balance; }
    public boolean isAdmin() { return isAdmin; }

    public void setPin(String pin) { this.pin = pin; }
    public void setAccHolderName(String name) { this.accHolderName = name; }
    public void setBalance(int balance) {
        if (balance >= 0) this.balance = balance;
    }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    public void deposit(int amount) {
        if (amount > 0) balance += amount;
    }

    public boolean withdraw(int amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return accNumber + "," + pin + "," + accHolderName + "," + balance + "," + isAdmin;
    }
}
