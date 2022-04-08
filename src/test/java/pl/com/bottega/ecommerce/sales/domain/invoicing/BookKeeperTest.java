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
    void setUp() {
        bookKeeper = new BookKeeper(new InvoiceFactory());
        ClientData clientData = new ClientData(Id.generate(), "someClient");
        invoiceRequest = new InvoiceRequest(clientData);
        productDataBuilder = new ProductDataBuilder();
    }

    @Test
    void singleItemInvoiceRequest_ShouldReturnInvoiceWithOneItem() {
        Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "someTax"));

        ProductData productData = productDataBuilder.build();
        RequestItem requestItem = new RequestItem(productData, 1, Money.ZERO);
        invoiceRequest.add(requestItem);
        Invoice resultInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        assertEquals(1, resultInvoice.getItems().size());
    }

    @Test
    void twoItemsInvoiceRequest_ShouldCallCalculateTaxMethodTwice() {
        Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class)))
                .thenReturn(new Tax(Money.ZERO, "someTax"));

        for (int i = 0; i < 2; i++) {
            ProductData productData = productDataBuilder.build();
            RequestItem requestItem = new RequestItem(productData, 1, Money.ZERO);
            invoiceRequest.add(requestItem);
        }

        Invoice resultInvoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        Mockito.verify(taxPolicy, Mockito.times(2))
                .calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));
    }
}
