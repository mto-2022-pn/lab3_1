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
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    private final String DEFAULT = "default";
    private final String ID = "12345678";
    private final String NAME = "JOHN";

    @Mock
    private TaxPolicy taxPolicy;
    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;

    @BeforeEach
    void setUp() throws Exception {
        bookKeeper = new BookKeeper(new InvoiceFactory());

        invoiceRequest = new InvoiceRequest(
                new ClientData(new Id(ID), NAME)
        );

        when(taxPolicy.calculateTax(
                any(ProductType.class), any(Money.class)
        )).thenReturn(new Tax(Money.ZERO, DEFAULT));
    }

    @Test
    void requestInvoiceWithOneItem() {
        invoiceRequest.add(new RequestItemBuilder().build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(invoice.getItems().size(), 1);
    }

    @Test
    void requestInvoiceWithTwoItem() {
        invoiceRequest.add(new RequestItemBuilder().build());
        invoiceRequest.add(new RequestItemBuilder().build());
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2))
                .calculateTax(
                        any(ProductType.class), any(Money.class)
                );
    }
}
