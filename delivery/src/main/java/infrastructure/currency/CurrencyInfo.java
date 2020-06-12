package infrastructure.currency;

public class CurrencyInfo {
    private double ratioToDollar;
    private String currencySymbol;

    public double getRatioToDollar() {
        return ratioToDollar;
    }

    public void setRatioToDollar(double ratioToDollar) {
        this.ratioToDollar = ratioToDollar;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
