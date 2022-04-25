package pl.com.bottega.ecommerce.sharedkernel;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;

import java.math.BigDecimal;

public class MoneyBuilder {
    private double denomination = 9.11;
    private String currencyCode = "PLN";

    public MoneyBuilder defaultMoney() {
        return this;
    }

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
