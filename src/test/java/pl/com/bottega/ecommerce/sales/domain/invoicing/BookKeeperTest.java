package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
@MockitoSettings(strictness = Strictness.LENIENT)
class BookKeeperTest {

    @Mock
    private TaxPolicy taxPolicy;
    private BookKeeper bookKeeper;
    private InvoiceRequest request;

    @BeforeEach
    void setUp() {
        this.bookKeeper = new BookKeeper(new InvoiceFactory());
        this.request = new InvoiceRequest(new ClientData(new Id("222440"), "Szymon"));
        when(taxPolicy.calculateTax(
                any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "unknown"));
    }

    @Test
    void testCase1() {
        request.add(new RequestItemBuilder().build());
        Invoice invoice = this.bookKeeper.issuance(this.request, taxPolicy);
        assertEquals(invoice.getItems().size(), 1);
    }

    @Test
    void testCase2() {
        request.add(new RequestItemBuilder().build());
        request.add(new RequestItemBuilder().build());
        this.bookKeeper.issuance(this.request, taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    void testReturnWithEmptyRequest() {
        Invoice invoice = this.bookKeeper.issuance(this.request, taxPolicy);
        assertEquals(invoice.getItems().size(), 0);
    }

    @Test
    void testBehaviorWithEmptyRequest() {
        this.bookKeeper.issuance(this.request, taxPolicy);
        verify(taxPolicy, never()).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    void testBehaviorWithDifferentProductTypes() {
        request.add(new RequestItemBuilder().withProductType(ProductType.DRUG).build());
        request.add(new RequestItemBuilder().withProductType(ProductType.FOOD).build());
        request.add(new RequestItemBuilder().withProductType(ProductType.STANDARD).build());
        this.bookKeeper.issuance(this.request, taxPolicy);
        verify(taxPolicy, times(3)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    void testReturnWithDifferentProductTypes() {
        request.add(new RequestItemBuilder().withProductType(ProductType.DRUG).build());
        request.add(new RequestItemBuilder().withProductType(ProductType.FOOD).build());
        request.add(new RequestItemBuilder().withProductType(ProductType.STANDARD).build());
        Invoice invoice = this.bookKeeper.issuance(this.request, taxPolicy);
        assertEquals(invoice.getItems().stream()
                .filter(elem -> elem.getProduct().getType().equals(ProductType.STANDARD)).count(),1);
        assertEquals(invoice.getItems().stream()
                .filter(elem -> elem.getProduct().getType().equals(ProductType.FOOD)).count(),1);
        assertEquals(invoice.getItems().stream()
                .filter(elem -> elem.getProduct().getType().equals(ProductType.DRUG)).count(),1);
    }
}
