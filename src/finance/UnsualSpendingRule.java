package finance;

import java.math.BigDecimal;

public class UnusualSpendingRule {

    private BigDecimal maximumAmount;

    public UnusualSpendingRule(BigDecimal maximumAmount) {

        if (maximumAmount == null ||
            maximumAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid maximum amount.");
        }

        this.maximumAmount = maximumAmount;
    }

    public boolean isUnusual(Transaction transaction) {

        return transaction.isExpense()
                && transaction.getAmount()
                .compareTo(maximumAmount) > 0;
    }

    public String getExplanation(Transaction transaction) {

        return "Unusual expense of "
                + transaction.getAmount()
                + " detected. It exceeds your configured limit of "
                + maximumAmount + ".";
    }

    public BigDecimal getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(BigDecimal maximumAmount) {

        if (maximumAmount == null ||
            maximumAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid maximum amount.");
        }

        this.maximumAmount = maximumAmount;
    }
}
