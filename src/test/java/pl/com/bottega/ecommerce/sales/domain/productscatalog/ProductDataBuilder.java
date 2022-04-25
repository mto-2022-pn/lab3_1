package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {
    private Id id = Id.generate();
    private Money money = Money.ZERO;
    private String name = "product";
    private Date date = new Date();
    private ProductType productType = ProductType.STANDARD;

    public ProductDataBuilder withId(Id id){
        this.id=id;
        return this;
    }

    public ProductDataBuilder withMoney(Money money){
        this.money=money;
        return this;
    }
    public ProductDataBuilder withName(String name){
        this.name=name;
        return this;
    }
    public ProductDataBuilder withDate(Date date){
        this.date=date;
        return this;
    }
    public ProductDataBuilder withProductType(ProductType productType){
        this.productType=productType;
        return this;
    }

    public ProductData build(){
        return new ProductData(id, money, name, productType, date);
    }

}
