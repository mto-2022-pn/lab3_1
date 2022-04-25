package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {
    private Id id = new Id("404");
    private Money money = new Money(1);
    private String name = "item";
    private ProductType type = ProductType.STANDARD;
    private Date date = new Date();

    public ProductDataBuilder withID(Id id) {
        this.id = id;
        return this;
    }

    public ProductDataBuilder withMoney(Money money) {
        this.money = money;
        return this;
    }

    public ProductDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductDataBuilder withType(ProductType type) {
        this.type = type;
        return this;
    }

    public ProductDataBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public ProductData build() {
        return new ProductData(id, money, name, type, date);
    }
}
