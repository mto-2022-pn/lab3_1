package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
    private ProductData productData;

    private int quantity;

    private Money totalCost;

    public RequestItemBuilder productData(Product product){
        this.productData = product.generateSnapshot();
        return this;
    }
    public RequestItemBuilder quantity(int quantity){
        this.quantity = quantity;
        return this;
    }
    public RequestItem build(){
        this.totalCost =productData.getPrice().multiplyBy(quantity);
        return new RequestItem(productData,quantity,totalCost);
    }
}
