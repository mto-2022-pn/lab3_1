package pl.com.bottega.ecommerce.sharedkernel;

public class MoneyBuilder {
    
    private double denomination = 21.37;

    private String currencyCode = "EUR";

    public MoneyBuilder generateDefaultMoneyBuilder()
    {
        return this;
    }

    public Money build()
    {
        return new Money(denomination,currencyCode);
    }

}
