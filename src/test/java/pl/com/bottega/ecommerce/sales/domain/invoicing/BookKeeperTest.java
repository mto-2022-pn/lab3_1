package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

	@Mock
	private TaxPolicy taxPolicy;
	private BookKeeper bookKeeper;
	private InvoiceRequest invoiceRequest;

	@BeforeEach
	void setUp() {
		this.bookKeeper = new BookKeeper(new InvoiceFactory());
		this.invoiceRequest = new InvoiceRequest(new ClientData(new Id("228802"), "Mateusz"));
	}

	@Test
	void invoiceWithOneFieldRequestShouldReturnInvoiceWithOneField() {
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));

		invoiceRequest.add(new RequestItemBuilder().build());
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertEquals(invoice.getItems().size(), 1);
	}
}
