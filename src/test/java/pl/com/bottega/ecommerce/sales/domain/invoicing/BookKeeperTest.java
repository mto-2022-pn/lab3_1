package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    @Mock
    private TaxPolicy taxPolicy;
    BookKeeper bookKeeper;

    @BeforeEach
    void setUp() throws Exception {
        invoiceFactory = new InvoiceFactory();
        bookKeeper = new BookKeeper(invoiceFactory);
    }

    @Test
    void requestOneItem() {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "Test Tax"));

        
        invoiceRequest = new InvoiceRequestBuilder().generateDefaultInvoiceRequestBuilder().build();
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertTrue(invoice.getItems().size() == 1);

    }

}
