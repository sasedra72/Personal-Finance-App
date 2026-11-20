package finance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class SpendingAnalyzer {

    private List<Budget> budgets;
    private SpendingAnalysisStrategy strategy;
    private UnusualSpendingRule unusualRule;

    // Constructor
    public SpendingAnalyzer(
            SpendingAnalysisStrategy strategy,
            UnusualSpendingRule unusualRule) {

        this.budgets = new ArrayList<>();
        this.strategy = strategy;
        this.unusualRule = unusualRule;
    }

    // Add a new budget
    public void addBudget(Budget budget) {
        budgets.add(budget);
    }

    // Calculate spending for a specific budget
    public BigDecimal calculateSpending(
            Budget budget, List<Transaction> transactions) {

        BigDecimal total = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {

            boolean sameCategory =
                    transaction.getCategory().equals(budget.getCategory());

            boolean samePeriod =
                    YearMonth.from(transaction.getDate())
                    .equals(budget.getPeriod());

            if (transaction.isExpense()
                    && sameCategory && samePeriod) {

                total = total.add(transaction.getAmount());
            }
        }

        return total;
    }

    // Calculate budget usage percentage
    public BigDecimal calculateBudgetUsage(
            Budget budget, List<Transaction> transactions) {

        BigDecimal spent = calculateSpending(budget, transactions);

        return spent.multiply(new BigDecimal("100"))
                .divide(budget.getLimit(), 2, RoundingMode.HALF_UP);
    }

    // Analyze one budget and generate an alert if needed
    public SpendingAlert analyzeBudget(
            Budget budget, List<Transaction> transactions) {

        BigDecimal spent = calculateSpending(budget, transactions);

        return strategy.analyze(
                spent,
                budget.getLimit(),
                budget.getCategory().toString()
        );
    }

    // Analyze all budgets
    public List<SpendingAlert> analyzeAllBudgets(
            List<Transaction> transactions) {

        List<SpendingAlert> alerts = new ArrayList<>();

        for (Budget budget : budgets) {

            SpendingAlert alert =
                    analyzeBudget(budget, transactions);

            if (alert != null) {
                alerts.add(alert);
            }
        }

        return alerts;
    }

    // Detect unusual transactions
    public List<Transaction> detectUnusualTransactions(
            List<Transaction> transactions) {

        List<Transaction> unusualTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {

            if (unusualRule.isUnusual(transaction)) {
                unusualTransactions.add(transaction);
            }
        }

        return unusualTransactions;
    }

    // Generate explanations for unusual transactions
    public List<SpendingAlert> getUnusualAlerts(
            List<Transaction> transactions) {

        List<SpendingAlert> alerts = new ArrayList<>();

        for (Transaction transaction : transactions) {

            if (unusualRule.isUnusual(transaction)) {

                alerts.add(new SpendingAlert(
                        "UNUSUAL",
                        unusualRule.getExplanation(transaction)
                ));
            }
        }

        return alerts;
    }

    // Analyze spending using another strategy
    public SpendingAlert analyzeWithStrategy(
            SpendingAnalysisStrategy anotherStrategy,
            BigDecimal currentSpending,
            BigDecimal referenceAmount,
            String description) {

        return anotherStrategy.analyze(
                currentSpending,
                referenceAmount,
                description
        );
    }

    // Get all budgets
    public List<Budget> getBudgets() {
        return new ArrayList<>(budgets);
    }
}
