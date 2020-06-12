package infrastructure.currency;

import java.util.Map;

public interface CurrencyInfoLoader {
    public Map<String, CurrencyInfo> getCurrencyInfo();
}
