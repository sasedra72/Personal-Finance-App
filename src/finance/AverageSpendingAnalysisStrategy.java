package finance;

import java.math.BigDecimal;

public class AverageSpendingAnalysisStrategy
        implements SpendingAnalysisStrategy {

    private BigDecimal multiplier;

    public AverageSpendingAnalysisStrategy() {
        this.multiplier = new BigDecimal("1.5");
    }

    public AverageSpendingAnalysisStrategy(BigDecimal multiplier) {

        if (multiplier == null ||
            multiplier.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid multiplier.");
        }

        this.multiplier = multiplier;
    }

    @Override
    public SpendingAlert analyze(
            BigDecimal currentSpending,
            BigDecimal averageSpending,
            String description) {

        if (averageSpending.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        BigDecimal limit = averageSpending.multiply(multiplier);

        if (currentSpending.compareTo(limit) > 0) {

            return new SpendingAlert(
                "UNUSUAL",
                description + ": Spending is unusually high. "
                + "Current spending is " + currentSpending
                + ", while the average is " + averageSpending + "."
            );
        }

        return null;
    }
}
