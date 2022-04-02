package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class RequestItemBuilder {
    private int quantity = 1;
    private Money cost = Money.ZERO;
    ProductDataBuilder productDataBuilder = new ProductDataBuilder();

    RequestItemBuilder withQuantity(int n) {
        this.quantity = n;
        return this;
    }

    RequestItemBuilder withCost(Money m) {
        this.cost = m;
        return this;
    }

    RequestItem build() {
        return new RequestItem(this.productDataBuilder.build(), this.quantity, this.cost);
    }

    public RequestItemBuilder withProductDate(Date date) {
        this.productDataBuilder = productDataBuilder.withDate(date);
        return this;
    }

    public RequestItemBuilder withProductID(Id id) {
        this.productDataBuilder = productDataBuilder.withID(id);
        return this;
    }

    public RequestItemBuilder withProductMoney(Money money) {
        this.productDataBuilder = productDataBuilder.withMoney(money);
        return this;
    }

    public RequestItemBuilder withProductName(String name) {
        this.productDataBuilder = productDataBuilder.withName(name);
        return this;
    }

    public RequestItemBuilder withProductType(ProductType type) {
        this.productDataBuilder = productDataBuilder.withType(type);
        return this;
    }
}
