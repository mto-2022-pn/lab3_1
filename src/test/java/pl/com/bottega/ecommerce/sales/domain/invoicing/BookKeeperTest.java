package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookKeeperTest {

    private final BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
    private InvoiceRequest invoiceRequest;
    private final RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
    private final ProductDataBuilder productDataBuilder = new ProductDataBuilder();

    @Mock
    private TaxPolicy taxPolicy;

    @BeforeEach
    void setUp() {
//        bookKeeper = new BookKeeper(new InvoiceFactory());
        invoiceRequest = new InvoiceRequest(new ClientData(new Id("228779"), "Hubert Krauza"));
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "default tax"));
    }

    @Test
    void testInvoiceRequestWithOneItemShouldReturnOneItem() {
        invoiceRequest.add(requestItemBuilder.build());

        int result = bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size();
        assertEquals(1, result);
    }

    @Test
    void testInvoiceRequestWithZeroItemShouldReturnZeroItem() {
        int result = bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size();
        assertEquals(0, result);
    }

    @Test
    void testInvoiceRequestWithDifferentTypeItemsShouldInvokesCalculateTax() {
        invoiceRequest.add(requestItemBuilder.withProductData(productDataBuilder.withType(ProductType.DRUG).build()).build());
        invoiceRequest.add(requestItemBuilder.withQuantity(5).build());
        invoiceRequest.add(requestItemBuilder.withMoney(new Money(32)).build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        verify(taxPolicy, Mockito.times(3)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    void testInvoiceRequestWithDifferentTypeItemsShouldReturnThoseItems() {
        invoiceRequest.add(requestItemBuilder.withProductData(productDataBuilder.withType(ProductType.DRUG).build()).build());
        invoiceRequest.add(requestItemBuilder.withQuantity(5).build());
        invoiceRequest.add(requestItemBuilder.withMoney(new Money(32)).build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        List<InvoiceLine> returnedItems = invoice.getItems();
        assertEquals(returnedItems.get(0).getProduct().getType(), ProductType.DRUG);
        assertEquals(returnedItems.get(1).getQuantity(), 5);
        assertEquals(returnedItems.get(2).getNet(), new Money(32));
        int result = invoice.getItems().size();
        assertEquals(3, result);
    }

    @Test
    void testInvoiceRequestWithTwoItemsShouldInvokeCalculateTaxTwoTimes() {
        invoiceRequest.add(requestItemBuilder.build());
        invoiceRequest.add(requestItemBuilder.build());

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, Mockito.times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    void testInvoiceRequestWithNoItemsShouldNotInvokeCalculateTax() {

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, Mockito.never()).calculateTax(any(ProductType.class), any(Money.class));
    }


}
