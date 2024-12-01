import java.util.Scanner;

public class LoanChartGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input details
        System.out.print("Enter loan amount (Principal): ");
        double loanAmount = scanner.nextDouble();

        System.out.print("Enter annual interest rate (in %): ");
        double annualInterestRate = scanner.nextDouble();

        System.out.print("Enter loan tenure (in months): ");
        int tenureMonths = scanner.nextInt();

        System.out.print("Enter the number of months for the chart: ");
        int chartPeriod = scanner.nextInt();

        // Calculate EMI
        double monthlyInterestRate = annualInterestRate / 12 / 100;
        double emi = (loanAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureMonths))
                / (Math.pow(1 + monthlyInterestRate, tenureMonths) - 1);

        System.out.printf("\nEMI for the loan is: %.2f%n", emi);

        // Print loan chart header
        System.out.println("\nLoan Repayment Chart:");
        System.out.printf("%-10s%-20s%-20s%-20s%-20s%n", "Month", "Outstanding Balance", "Principal Paid", "Interest Paid", "EMI");
        System.out.println("===========================================================================================");

        // Generate chart for the specified period
        double balance = loanAmount;

        for (int month = 1; month <= chartPeriod && month <= tenureMonths; month++) {
            double interest = balance * monthlyInterestRate;
            double principal = emi - interest;
            balance -= principal;

            // Print monthly details
            System.out.printf("%-10d%-20.2f%-20.2f%-20.2f%-20.2f%n", month, balance + principal, principal, interest, emi);
        }

        scanner.close();
    }
}
