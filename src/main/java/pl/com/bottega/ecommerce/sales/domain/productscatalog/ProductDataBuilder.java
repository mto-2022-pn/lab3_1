package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {
    private Id productId = new Id("productID");
    private Money price;
    private String name = "Name";
    private Date snapshotDate = new Date();
    private ProductType type = ProductType.DRUG;

    public ProductDataBuilder defaultProductData() {
        return this;
    }

    public ProductData build() {
        return new ProductData(productId, price, name, type, snapshotDate);
    }
}
