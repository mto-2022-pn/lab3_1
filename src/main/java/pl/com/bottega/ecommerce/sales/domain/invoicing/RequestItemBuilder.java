package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
    private ProductData productData = null;
    private int quantity = 1;
    private Money money = Money.ZERO;

    public RequestItem build() {
        if(productData == null) {
            productData = new ProductDataBuilder().build();
        }
        return new RequestItem(
                productData,
                quantity,
                money
        );
    }

    public RequestItemBuilder withProductData(ProductData productData) {
        this.productData = productData;
        return this;
    }

    public RequestItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public RequestItemBuilder withMoney(Money money) {
        this.money = money;
        return this;
    }
}
