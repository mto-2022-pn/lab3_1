package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

	@Test
	void invoiceWithTwoFieldsRequestShouldInvokeCalculateTaxMethodTwice() {
		invoiceRequest.add(new RequestItemBuilder().build());
		invoiceRequest.add(new RequestItemBuilder().build());
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));

		bookKeeper.issuance(invoiceRequest, taxPolicy);
		verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
	}

	@Test
	void invoiceWithManyFieldsRequestShouldReturnInvoiceWithManyFields(){
		when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));

		invoiceRequest.add(new RequestItemBuilder().build());
		invoiceRequest.add(new RequestItemBuilder().build());
		invoiceRequest.add(new RequestItemBuilder().build());
		invoiceRequest.add(new RequestItemBuilder().build());
		invoiceRequest.add(new RequestItemBuilder().build());
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertEquals(invoice.getItems().size(), 5);
	}

	@Test
	void invoiceWithZeroFieldsRequestShouldReturnInvoiceWithZeroFields(){
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertTrue(invoice.getItems().isEmpty());
	}
}
