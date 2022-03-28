package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductBuilder {
    private Id id = Id.generate();
    private Money price = Money.ZERO;
    private String name = "product";
    private ProductType type = ProductType.STANDARD;

    public ProductBuilder withId(Id id)
    {
        this.id = id;
        return this;
    }
    public ProductBuilder withPrice(Money price){
        this.price = price;
        return this;
    }
    public ProductBuilder withName(String name){
        this.name = name;
        return this;
    }
    public ProductBuilder withType(ProductType type){
        this.type = type;
        return  this;
    }
    public Product build(){
        return new Product(id, price, name, type);
    }
}
