package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Date;

public class ProductDataBuilder {

	private Id id = Id.generate();
	private Money price = Money.ZERO;
	private String name = "Standard";
	private ProductType productType = ProductType.STANDARD;
	private Date date = new Date();

	public ProductDataBuilder addId(Id id) {
		this.id = id;
		return this;
	}

	public ProductDataBuilder addPrice(Money price) {
		this.price = price;
		return this;
	}

	private ProductDataBuilder addName(String name) {
		this.name = name;
		return this;
	}

	private ProductDataBuilder addProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}

	private ProductDataBuilder addDate(Date date) {
		this.date = date;
		return this;
	}

	public ProductData build() {
		return new ProductData(id, price, name, productType, date);
	}
}
