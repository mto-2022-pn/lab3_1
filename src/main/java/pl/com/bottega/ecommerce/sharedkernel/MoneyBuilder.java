package pl.com.bottega.ecommerce.sharedkernel;

public class MoneyBuilder {

    private double denomination = 21.37;

    private String currencyCode = "EUR";

    public MoneyBuilder withDenomination(double denomination) {
        this.denomination = denomination;
        return this;
    }

    public MoneyBuilder withCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public Money build() {
        return new Money(denomination, currencyCode);
    }

}
