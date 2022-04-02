package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    private TaxPolicy taxPolicy;
    private BookKeeper bookKeeper;

    @BeforeEach
    void setUp() {
        this.bookKeeper = new BookKeeper(new InvoiceFactory());
    }

    @Test
    void testCase1() {
        when(taxPolicy.calculateTax(
                any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "unknown"));
        InvoiceRequest invoiceRequest = new InvoiceRequest(new ClientData(new Id("222440"), "Szymon"));
        invoiceRequest.add(new RequestItemBuilder().build());
        Invoice invoice = this.bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(invoice.getItems().size(), 1);
    }
}
