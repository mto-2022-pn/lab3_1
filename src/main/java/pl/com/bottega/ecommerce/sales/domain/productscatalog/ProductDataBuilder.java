package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sharedkernel.MoneyBuilder;

public class ProductDataBuilder {

    private Id productId = new Id("1");
    private Money price = null;
    private String name = "Rower";
    private Date snapshotDate = new Date();
    private ProductType type = ProductType.FOOD;

    public ProductDataBuilder generateDefaultProductDataBuilder()
    {
        MoneyBuilder builder = new MoneyBuilder();
        price = builder.generateDefaultMoneyBuilder().build();
        return this;
    }

    public ProductDataBuilder withProductID(Id productID) {
        this.productId = productID;
        return this;
    }

    public ProductData build() {
        return new ProductData(productId, price, name, type, snapshotDate);
    }

}
