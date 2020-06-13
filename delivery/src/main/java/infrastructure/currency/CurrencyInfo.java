package infrastructure.currency;

public class CurrencyInfo {
    private long ratioToDollar;
    private String currencySymbol;

    public long getRatioToDollar() {
        return ratioToDollar;
    }

    public void setRatioToDollar(long ratioToDollar) {
        this.ratioToDollar = ratioToDollar;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
