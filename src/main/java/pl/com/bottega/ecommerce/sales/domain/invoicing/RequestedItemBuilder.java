package pl.com.bottega.ecommerce.sales.domain.invoicing;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestedItemBuilder {
    private ProductData productData = new ProductBuilder().build().generateSnapshot();
    private Money totalCost = Money.ZERO;
    private int quantity = 1;

    public RequestItem build(){
        return new RequestItem(this.productData, this.quantity, this.totalCost);
    }

    public RequestedItemBuilder withProductData(Product product){
        this.productData = product.generateSnapshot();
        return this;
    }

    public RequestedItemBuilder withQuantity(int quantity){
        this.quantity = quantity;
        return this;
    }

    public RequestedItemBuilder withTotalCost(Money totalCost){
        this.totalCost = totalCost;
        return this;
    }

}
