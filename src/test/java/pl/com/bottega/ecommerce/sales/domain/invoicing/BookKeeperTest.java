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

import java.util.Random;

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
    }

    @Test
    void requestInvoiceWithOneItem() {
        when(taxPolicy.calculateTax(
                any(ProductType.class), any(Money.class)
        )).thenReturn(new Tax(Money.ZERO, DEFAULT));

        invoiceRequest.add(new RequestItemBuilder().build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(invoice.getItems().size(), 1);
    }

    @Test
    void requestInvoiceWithTwoItem() {
        when(taxPolicy.calculateTax(
                any(ProductType.class), any(Money.class)
        )).thenReturn(new Tax(Money.ZERO, DEFAULT));

        invoiceRequest.add(new RequestItemBuilder().build());
        invoiceRequest.add(new RequestItemBuilder().build());
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(2))
                .calculateTax(
                        any(ProductType.class), any(Money.class)
                );
    }

    @Test
    void requestInvoiceWithNoItems() {
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(invoice.getItems().size(), 0);
    }

    @Test
    void requestInvoiceWithNoItemsBehaviourTest() {
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, never()).calculateTax(
                any(ProductType.class), any(Money.class)
        );
    }

    @Test
    void requestInvoiceWithManyItems() {
        when(taxPolicy.calculateTax(
                any(ProductType.class), any(Money.class)
        )).thenReturn(new Tax(Money.ZERO, DEFAULT));

        int t = new Random().nextInt(100) + 2;

        for(int i = 0; i < t; ++i)
            invoiceRequest.add(new RequestItemBuilder().build());

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(invoice.getItems().size(), t);
    }

    @Test
    void requestInvoiceWithManyItemsBehaviourTest() {
        when(taxPolicy.calculateTax(
                any(ProductType.class), any(Money.class)
        )).thenReturn(new Tax(Money.ZERO, DEFAULT));

        int t = new Random().nextInt(100) + 2;

        for(int i = 0; i < t; ++i)
            invoiceRequest.add(new RequestItemBuilder().build());

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(t))
                .calculateTax(
                        any(ProductType.class), any(Money.class)
                );
    }
}
