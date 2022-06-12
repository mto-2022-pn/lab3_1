package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ddd.support.domain.BaseAggregateRoot;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class Product extends BaseAggregateRoot {

    private Money price;

    private String name;

    private ProductType productType;

    public Product(Money price, String name, ProductType productType) {
        this.price = price;
        this.name = name;
        this.productType = productType;
    }

    public boolean isAvailable() {
        return !isRemoved();
    }

    public Money getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public ProductData generateSnapshot() {
        return new ProductDataBuilder().withProductId(getId()).withPrice(price).withName(name).withType(productType).withSnapshotDate(new Date()).build();
    }


}


