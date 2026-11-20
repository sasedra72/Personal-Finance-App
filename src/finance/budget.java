package finance;

import java.math.BigDecimal;
import java.time.YearMonth;

public class Budget {

    private Category category;
    private BigDecimal limit;
    private YearMonth period;

    public Budget(Category category, BigDecimal limit,
                  YearMonth period) {

        if (category == null || limit == null || period == null) {
            throw new IllegalArgumentException("Budget information cannot be null.");
        }

        if (limit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Budget limit must be positive.");
        }

        this.category = category;
        this.limit = limit;
        this.period = period;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public void setLimit(BigDecimal limit) {
        if (limit == null || limit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid budget limit.");
        }

        this.limit = limit;
    }
}

