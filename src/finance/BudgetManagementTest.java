
package finance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class BudgetManagementTest {

    public static void main(String[] args) {

        // Create sample transactions
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(
                new BigDecimal("200"),
                Category.DINING,
                LocalDate.of(2026, 11, 5),
                true
        ));

        transactions.add(new Transaction(
                new BigDecimal("50"),
                Category.DINING,
                LocalDate.of(2026, 11, 10),
                true
        ));

        transactions.add(new Transaction(
                new BigDecimal("100"),
                Category.DINING,
                LocalDate.of(2026, 11, 12),
                false
        ));

        transactions.add(new Transaction(
                new BigDecimal("30"),
                Category.SHOPPING,
                LocalDate.of(2026, 11, 15),
                true
        ));

        // Create budget
        Budget diningBudget = new Budget(
                Category.DINING,
                new BigDecimal("300"),
                YearMonth.of(2026, 11)
        );

        // Create analyzer
        SpendingAnalyzer analyzer = new SpendingAnalyzer(
                new ThresholdAnalysisStrategy(
                        new BigDecimal("80")
                ),
                new UnusualSpendingRule(
                        new BigDecimal("100")
                )
        );

        analyzer.addBudget(diningBudget);

        // TEST 1: Calculate spending
        BigDecimal total = analyzer.calculateSpending(
                diningBudget, transactions
        );

        check(total.compareTo(new BigDecimal("250")) == 0,
                "Total spending calculation");

        // TEST 2: Calculate budget usage
        BigDecimal usage = analyzer.calculateBudgetUsage(
                diningBudget, transactions
        );

        check(usage.compareTo(new BigDecimal("83.33")) == 0,
                "Budget usage calculation");

        // TEST 3: Generate warning
        SpendingAlert alert = analyzer.analyzeBudget(
                diningBudget, transactions
        );

        check(alert != null && alert.getLevel().equals("WARNING"),
                "Budget warning");

        // TEST 4: Detect unusual transaction
        transactions.add(new Transaction(
                new BigDecimal("150"),
                Category.SHOPPING,
                LocalDate.of(2026, 11, 18),
                true
        ));

        List<Transaction> unusual =
                analyzer.detectUnusualTransactions(transactions);

        check(unusual.size() == 1,
                "Unusual transaction detection");

        // TEST 5: Exclude transactions from another month
        transactions.add(new Transaction(
                new BigDecimal("500"),
                Category.DINING,
                LocalDate.of(2026, 10, 20),
                true
        ));

        total = analyzer.calculateSpending(
                diningBudget, transactions
        );

        check(total.compareTo(new BigDecimal("250")) == 0,
                "Monthly transaction filtering");

        // TEST 6: Test exceeded budget
        transactions.add(new Transaction(
                new BigDecimal("100"),
                Category.DINING,
                LocalDate.of(2026, 11, 20),
                true
        ));

        alert = analyzer.analyzeBudget(
                diningBudget, transactions
        );

        check(alert != null && alert.getLevel().equals("EXCEEDED"),
                "Budget exceeded alert");

        // TEST 7: Test alternative strategy
        AverageSpendingAnalysisStrategy averageStrategy =
                new AverageSpendingAnalysisStrategy();

        SpendingAlert averageAlert = analyzer.analyzeWithStrategy(
                averageStrategy,
                new BigDecimal("350"),
                new BigDecimal("200"),
                "Shopping"
        );

        check(averageAlert != null,
                "Average spending analysis");

        System.out.println("\nAll tests completed successfully!");
    }

    // Simple test helper
    private static void check(boolean condition, String testName) {

        if (!condition) {
            throw new AssertionError("FAILED: " + testName);
        }

        System.out.println("PASSED: " + testName);
    }
}
