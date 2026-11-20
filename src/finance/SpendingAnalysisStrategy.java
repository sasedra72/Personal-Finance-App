package finance;

import java.math.BigDecimal;

public interface SpendingAnalysisStrategy {

    SpendingAlert analyze(
        BigDecimal spending,
        BigDecimal referenceAmount,
        String description
    );
}
