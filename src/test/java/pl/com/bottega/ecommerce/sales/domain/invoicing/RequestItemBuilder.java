package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {
    private ProductData productData = new ProductBuilder().build().generateSnapshot();

    private int quantity = 1;

    private Money totalCost = Money.ZERO;

    public RequestItemBuilder withQuantity(int quantity){
        this.quantity = quantity;
        return this;
    }
    public RequestItemBuilder withProductData(ProductData productData){
        this.productData = productData;
        return this;
    }
    public  RequestItemBuilder withTotalPrice(Money totalCost){
        this.totalCost = totalCost;
        return this;
    }
    public RequestItem build(){
        return new RequestItem(productData,quantity,totalCost);
    }
}
