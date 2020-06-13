package infrastructure.currency;

import java.util.Map;

public interface CurrencyInfoLoader {
    Map<String, CurrencyInfo> getCurrencyInfo();
}
