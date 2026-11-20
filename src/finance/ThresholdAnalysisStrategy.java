package finance;

import java.math.BigDecimal;

public class ThresholdAnalysisStrategy
        implements SpendingAnalysisStrategy {

    private BigDecimal warningThreshold;

    public ThresholdAnalysisStrategy(BigDecimal warningThreshold) {

        if (warningThreshold == null ||
            warningThreshold.compareTo(BigDecimal.ZERO) <= 0 ||
            warningThreshold.compareTo(new BigDecimal("100")) > 0) {

            throw new IllegalArgumentException(
                "Threshold must be between 0 and 100."
            );
        }

        this.warningThreshold = warningThreshold;
    }

    @Override
    public SpendingAlert analyze(
            BigDecimal spending,
            BigDecimal budgetLimit,
            String description) {

        BigDecimal usage = spending.multiply(new BigDecimal("100"))
                .divide(budgetLimit, 2,
                        java.math.RoundingMode.HALF_UP);

        if (spending.compareTo(budgetLimit) >= 0) {

            return new SpendingAlert(
                "EXCEEDED",
                description + ": Budget exceeded. You spent "
                + spending + " out of " + budgetLimit + "."
            );

        } else if (usage.compareTo(warningThreshold) >= 0) {

            return new SpendingAlert(
                "WARNING",
                description + ": You have used "
                + usage + "% of your budget."
            );
        }

        return null;
    }
}
