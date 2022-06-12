package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.sharedkernel.Money;

public class ProductBuilder {
	private Money price = Money.ZERO;
	private String name = "Default";
	private ProductType productType = ProductType.STANDARD;

	public ProductBuilder withPrice(Money price) {
		this.price = price;
		return this;
	}

	public ProductBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ProductBuilder withProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}

	public Product build() {
		return new Product(price, name, productType);
	}

}
