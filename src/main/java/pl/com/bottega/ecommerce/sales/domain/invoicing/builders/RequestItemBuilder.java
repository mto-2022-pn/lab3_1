package pl.com.bottega.ecommerce.sales.domain.invoicing.builders;

import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.concurrent.locks.ReadWriteLock;

public class RequestItemBuilder {
    private ProductData productData;

    private int quantity=1;

    private Money totalCost=Money.ZERO;
    public RequestItemBuilder(){}
    public RequestItemBuilder withProductData(ProductData productData){
        this.productData=productData;
        return this;
    }
    public RequestItemBuilder withQuantity(int quantity){
        this.quantity=quantity;
        return this;
    }
    public RequestItemBuilder withTotalCost(Money totalCost){
        this.totalCost=totalCost;
        return this;
    }
    public RequestItem build(){
        return new RequestItem(productData,quantity,totalCost);
    }
}
