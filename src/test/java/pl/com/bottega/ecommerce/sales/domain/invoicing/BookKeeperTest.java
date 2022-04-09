package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    private ProductDataBuilder productDataBuilder;

    @BeforeEach
    void setUp() throws Exception {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        ClientData clientData = new ClientData(Id.generate(), "Client");
        invoiceRequest = new InvoiceRequest(clientData);
        productDataBuilder = new ProductDataBuilder();
    }

    @Test
    void testCase1() {
        Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "Tax"));

        invoiceRequest.add(new RequestItemBuilder().build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(1, invoice.getItems().size());
    }

    @Test
    void testCase2() {
        Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "Tax"));

        for(int i = 0; i < 2; i++) {
            invoiceRequest.add(new RequestItemBuilder().build());
        }
        bookKeeper.issuance(invoiceRequest, taxPolicy);

        Mockito.verify(taxPolicy, Mockito.times(2))
                .calculateTax(Mockito.any(ProductType.class),Mockito.any(Money.class));
    }
}
