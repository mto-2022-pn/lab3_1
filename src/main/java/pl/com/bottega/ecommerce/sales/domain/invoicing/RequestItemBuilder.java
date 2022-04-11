package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

public class RequestItemBuilder {

	private int quantity = 1;
	private Money totalCost = Money.ZERO;
	private ProductData productData = new ProductDataBuilder().build();

	public RequestItemBuilder addQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public RequestItemBuilder addTotalCost(Money totalCost) {
		this.totalCost = totalCost;
		return this;
	}

	public RequestItemBuilder addProductData(ProductData productData) {
		this.productData = productData;
		return this;
	}

	public RequestItem build() {
		return new RequestItem(productData, quantity, totalCost);
	}
}
