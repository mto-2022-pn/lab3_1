package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ddd.support.domain.BaseAggregateRoot;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class Product extends BaseAggregateRoot {

    private Money price;

    private String name;

    private ProductType productType;

    private Product(final ProductBuilder builder) {
        this.id = builder.id;
        this.price = builder.price;
        this.name = builder.name;
        this.productType = builder.productType;
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
        return new ProductData(getId(), price, name, productType, new Date());
    }

    static class ProductBuilder {
        private Id id;
        private Money price;
        private String name;
        private ProductType productType;

        public ProductBuilder id(Id id) {
            this.id = id;
            return this;
        }

        public ProductBuilder price(Money price) {
            this.price = price;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder productType(ProductType productType) {
            this.productType = productType;
            return this;
        }

        public Product build() {
            return new Product(this);
        }

    }
}


