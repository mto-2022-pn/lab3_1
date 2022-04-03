package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    @Mock
    private TaxPolicy taxPolicy;

    private InvoiceRequestBuilder invoiceRequestBuilder;
    private BookKeeper bookKeeper;
    @BeforeEach
    void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        invoiceRequestBuilder = new InvoiceRequestBuilder();
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "Tax"));
    }

    @Test
    void testOneInvoiceRequestShouldReturnInvoiceWithOneItem() {
        InvoiceRequest invoiceRequest = invoiceRequestBuilder.addSampleRequestItem().build();
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(1, invoice.getItems().size());
    }
    @Test
    void testTwoInvoiceRequestsShouldInvokeMethodCalculateTaxTwoTimes() {
        InvoiceRequest invoiceRequest = invoiceRequestBuilder
                .addSampleRequestItem()
                .addSampleRequestItem()
                .build();
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }
}
