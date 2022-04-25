package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookKeeperTest {

    @Mock
    private TaxPolicy taxPolicy;

    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;

    @BeforeEach
    void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        ClientData clientData = new ClientData(Id.generate(), "Client");
        invoiceRequest = new InvoiceRequest(clientData);
        Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "Tax"));
    }

    @Test
    void testCase1() {
        invoiceRequest.add(new RequestItemBuilder().build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(1, invoice.getItems().size());
    }

    @Test
    void testCase2() {
        for(int i = 0; i < 2; i++) {
            invoiceRequest.add(new RequestItemBuilder().build());
        }
        bookKeeper.issuance(invoiceRequest, taxPolicy);

        Mockito.verify(taxPolicy, Mockito.times(2))
                .calculateTax(Mockito.any(ProductType.class),Mockito.any(Money.class));
    }

    @Test
    void testZeroInvoiceRequest() {

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(0, invoice.getItems().size());
    }

    @Test
    void testZeroInvoke() {

        bookKeeper.issuance(invoiceRequest, taxPolicy);

        Mockito.verify(taxPolicy, Mockito.never())
                .calculateTax(Mockito.any(ProductType.class),Mockito.any(Money.class));
    }

    @Test
    void testDiffrentProductInvoicesRequest() {
        ProductDataBuilder productDataBuilder = new ProductDataBuilder();
        invoiceRequest.add(new RequestItemBuilder().withProductDataBuilder(productDataBuilder.withProductType(ProductType.DRUG)).withQuantity(15).build());
        invoiceRequest.add(new RequestItemBuilder().withProductDataBuilder(productDataBuilder.withProductType(ProductType.FOOD).withName("Apple")).withQuantity(8).build());
        invoiceRequest.add(new RequestItemBuilder().withProductDataBuilder(productDataBuilder.withProductType(ProductType.STANDARD)).withQuantity(24).build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(3, invoice.getItems().size());
    }

    @Test
    void testDiffrentProductInvokes() {
        ProductDataBuilder productDataBuilder = new ProductDataBuilder();
        invoiceRequest.add(new RequestItemBuilder().withProductDataBuilder(productDataBuilder.withProductType(ProductType.DRUG)).withQuantity(12).build());
        invoiceRequest.add(new RequestItemBuilder().withProductDataBuilder(productDataBuilder.withProductType(ProductType.FOOD).withName("Banana")).withQuantity(10).build());
        invoiceRequest.add(new RequestItemBuilder().withProductDataBuilder(productDataBuilder.withProductType(ProductType.STANDARD)).withQuantity(30).build());
        bookKeeper.issuance(invoiceRequest, taxPolicy);

        Mockito.verify(taxPolicy, Mockito.times(3))
                .calculateTax(Mockito.any(ProductType.class),Mockito.any(Money.class));
    }
}
