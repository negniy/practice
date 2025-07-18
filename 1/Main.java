public class Main {
    public static void main(String[] args) {
        BankAccount account1 = new BankAccount("Alice");
        BankAccount account2 = new BankAccount("Bob");

   
        System.out.println("Deposit 1000 to Alice: " + account1.deposit(1000));
        System.out.println("Alice's balance: " + account1.getBalance());

        System.out.println("Withdraw 500 from Alice: " + account1.withdraw(500));
        System.out.println("Alice's balance: " + account1.getBalance());

        System.out.println("Withdraw 1000 from Alice: " + account1.withdraw(1000));
        System.out.println("Alice's balance: " + account1.getBalance());

        System.out.println("Transfer 300 from Alice to Bob: " + account1.transfer(account2, 300));
        System.out.println("Alice's balance: " + account1.getBalance());
        System.out.println("Bob's balance: " + account2.getBalance());

        account1.setLocked(true);
        System.out.println("Attempt to deposit to locked account: " + account1.deposit(200));
    }
}