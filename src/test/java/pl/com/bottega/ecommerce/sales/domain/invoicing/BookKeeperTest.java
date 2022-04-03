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
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    void testZeroInvoiceRequestShouldReturnEmptyInvoice() {
        InvoiceRequest invoiceRequest = invoiceRequestBuilder.build();
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(0, invoice.getItems().size());
    }
    @Test
    void testManyInvoiceRequestShouldReturnInvoiceWithManyItemsForDifferentProducts() {
        InvoiceRequest invoiceRequest = invoiceRequestBuilder
                .addSampleRequestItem()
                .addRequestItem(new RequestItem(new ProductDataBuilder().withName("Milk").build(), 1, Money.ZERO))
                .addRequestItem(new RequestItem(new ProductDataBuilder().withType(ProductType.FOOD).build(), 3, Money.ZERO))
                .addRequestItem(new RequestItem(new ProductDataBuilder().withType(ProductType.DRUG).withName("Vicodin").build(), 10, Money.ZERO))
                .build();
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(4, invoice.getItems().size());
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
    @Test
    void testZeroInvoiceRequestsShouldNeverInvokeMethodCalculateTax() {
        InvoiceRequest invoiceRequest = invoiceRequestBuilder
                .build();
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, never()).calculateTax(any(ProductType.class), any(Money.class));
    }
    @Test
    void testManyInvoiceRequestsShouldInvokeMethodCalculateTaxManyTimesForDifferentProducts() {
        InvoiceRequest invoiceRequest = invoiceRequestBuilder
                .addRequestItem(new RequestItem(new ProductDataBuilder().withName("Bread").build(), 1, new Money(1)))
                .addRequestItem(new RequestItem(new ProductDataBuilder().withType(ProductType.DRUG).build(), 100, Money.ZERO))
                .addRequestItem(new RequestItem(new ProductDataBuilder().withType(ProductType.FOOD).build(), 3, Money.ZERO))
                .build();
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy, times(3)).calculateTax(any(ProductType.class), any(Money.class));
    }

}
