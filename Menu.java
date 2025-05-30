import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private BankService bankService;
    private BankAccount currentAcc;

    public Menu(BankService bankService) {
        this.scanner = new Scanner(System.in);
        this.bankService = bankService;
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\nBanking System");
            System.out.println("1. Customer Login");
            System.out.println("2. Administrator Login");
            System.out.println("3. Exit");
            System.out.print("Select option: ");

            int choice = getIntInput(1, 3);

            switch (choice) {
                case 1:
                case 2:
                    Login(choice == 2);
                    break;
                case 3:
                    FileManager.saveAccounts(bankService.getAccounts());
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void Login(boolean isAdmin) {
        System.out.print("\nEnter account number: ");
        String accNumber = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        currentAcc = bankService.verify(accNumber, pin);
        if (currentAcc != null && currentAcc.isAdmin() == isAdmin) {
            if (isAdmin) {
                showAdminMenu();
            } else {
                showCustomerMenu();
            }
        } else {
            System.out.println("Invalid login");
        }
    }

    private void showCustomerMenu() {
        while (true) {
            System.out.println("\nCustomer Menu");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Logout");
            System.out.print("Select option: ");

            int choice = getIntInput(1, 4);

            switch (choice) {
                case 1:
                    System.out.printf("\nCurrent balance: %d %n", currentAcc.getBalance());
                    break;
                case 2:
                    Deposit();
                    break;
                case 3:
                    Withdraw();
                    break;
                case 4:
                    return;
            }
        }
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("\nAdministrator Menu");
            System.out.println("1. Create New Account");
            System.out.println("2. Close Account");
            System.out.println("3. Modify Account");
            System.out.println("4. Logout");
            System.out.print("Select option: ");

            int choice = getIntInput(1, 4);

            switch (choice) {
                case 1:
                    createNewAcc();
                    break;
                case 2:
                    closeAcc();
                    break;
                case 3:
                    modifyAcc();
                    break;
                case 4:
                    return;
            }
        }
    }

    private void Deposit() {
        System.out.print("\nEnter deposit amount: ");
        int amount = getIntInput(1, Integer.MAX_VALUE);
        currentAcc.deposit(amount);
        System.out.printf("%d amount deposited.%n", amount);
    }

    private void Withdraw() {
        System.out.print("\nEnter withdrawal amount: ");
        int amount = getIntInput(1, currentAcc.getBalance());
        if (currentAcc.withdraw(amount)) {
            System.out.printf("%d amount withdrawn.%n", amount);
        } else {
            System.out.println("Withdrawal failed.");
        }
    }

    private void createNewAcc() {
        System.out.println("\nCreate New Account");
        String accNumber;
        do {
            System.out.print("Enter new account number: ");
            accNumber = scanner.nextLine();
            if (bankService.accExists(accNumber)) {
                System.out.println("Account number exists");
            }
        } while (bankService.accExists(accNumber));

        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        System.out.print("Enter account holder name: ");
        String name = scanner.nextLine();
        System.out.print("Enter initial balance (cents): ");
        int balance = getIntInput(0, Integer.MAX_VALUE);
        System.out.print("Is this an admin account? (y/n): ");
        boolean isAdmin = scanner.nextLine().equalsIgnoreCase("y");

        bankService.addAccount(new BankAccount(accNumber, pin, name, balance, isAdmin));
        System.out.println("Account created.");
    }

    private void closeAcc() {
        System.out.print("\nEnter account number to close: ");
        String accNumber = scanner.nextLine();
        if (bankService.closeAcc(accNumber)) {
            System.out.println("Account closed.");
        } else {
            System.out.println("Account not found.");
        }
    }

    private void modifyAcc() {
        System.out.print("\nEnter account number to modify: ");
        String accNumber = scanner.nextLine();
        BankAccount account = null;
        for (BankAccount acc : bankService.getAccounts()) {
            if (acc.getAccNumber().equals(accNumber)) {
                account = acc;
                break;
            }
        }

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.println("\nCurrent account details:");
        System.out.println("1. Account Number: " + account.getAccNumber());
        System.out.println("2. PIN: " + account.getPin());
        System.out.println("3. Account Holder: " + account.getAccHolderName());
        System.out.println("4. Balance: " + account.getBalance());
        System.out.println("5. Admin Status: " + account.isAdmin());

        System.out.print("\nSelect detail to modify (1-5): ");
        int choice = getIntInput(0, 5);

        if (choice == 0) return;

        switch (choice) {
            case 1:
                System.out.println("Account number cannot be changed.");
                break;
            case 2:
                System.out.print("Enter new PIN: ");
                account.setPin(scanner.nextLine());
                break;
            case 3:
                System.out.print("Enter new name: ");
                account.setAccHolderName(scanner.nextLine());
                break;
            case 4:
                System.out.print("Enter new balance: ");
                account.setBalance(getIntInput(0, Integer.MAX_VALUE));
                break;
            case 5:
                System.out.print("Set as admin? (y/n): ");
                account.setAdmin(scanner.nextLine().equalsIgnoreCase("y"));
                break;
        }
        System.out.println("Account updated.");
    }

    private int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }
}
