package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.MoneyBuilder;

public class RequestItemBuilder {

    private ProductData productData;

	private int quantity = 1;

	private Money totalCost;

    public RequestItemBuilder generateDefaultProductDataBuilder()
    {
        ProductDataBuilder bDataBuilder = new ProductDataBuilder();
        productData = bDataBuilder.generateDefaultProductDataBuilder().build();

        MoneyBuilder moneyBuilder = new MoneyBuilder();
        totalCost = moneyBuilder.generateDefaultMoneyBuilder().build();
        return this;
    }

    public RequestItem build() {
        return new RequestItem(productData, quantity, totalCost);
    }
    
}
