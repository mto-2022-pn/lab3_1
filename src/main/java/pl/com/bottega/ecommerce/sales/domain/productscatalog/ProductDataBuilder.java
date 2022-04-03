package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Calendar;
import java.util.Date;

public class ProductDataBuilder {
    private Id productId = Id.generate();

    private Money price = Money.ZERO;
    private String name = "ProductName";

    private Date snapshotDate = new Date();

    private ProductType type = ProductType.STANDARD;

    public ProductDataBuilder sampleProductData() {
        return this;
    }

    public ProductData build() {
        return new ProductData(productId, price, name, type, snapshotDate);
    }

    public ProductDataBuilder withProductId(Id productId) {
        this.productId = productId;
        return this;
    }

    public ProductDataBuilder withPrice(Money price) {
        this.price = price;
        return this;
    }

    public ProductDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductDataBuilder withSnapshotDate(Date snapshotDate) {
        this.snapshotDate = snapshotDate;
        return this;
    }

    public ProductDataBuilder withType(ProductType type) {
        this.type = type;
        return this;
    }
}
