package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
    private int quantity = 1;
    private Money money = Money.ZERO;
    ProductDataBuilder productDataBuilder = new ProductDataBuilder();

    RequestItemBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    RequestItemBuilder withCost(Money money) {
        this.money = money;
        return this;
    }

    RequestItemBuilder withProductDataBuilder(ProductDataBuilder productDataBuilder) {
        this.productDataBuilder = productDataBuilder;
        return this;
    }

    RequestItem build() {
        return new RequestItem(this.productDataBuilder.build(), this.quantity, this.money);
    }
}
