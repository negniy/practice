import java.time.LocalDateTime;

public class BankAccount {
    private String ownerName;
    private int balance;
    private LocalDateTime openingDate;
    private boolean isLocked;


    public BankAccount(String ownerName) {
        this.ownerName = ownerName;
        this.balance = 0;
        this.openingDate = LocalDateTime.now();
        this.isLocked = false;
    }

    public boolean deposit(int amount) {
        if (isLocked || amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(int amount) {
        if (isLocked || amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public boolean transfer(BankAccount otherAccount, int amount) {
        if (isLocked || otherAccount.isLocked || amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        otherAccount.balance += amount;
        return true;
    }

    public int getBalance() {
        return balance;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}