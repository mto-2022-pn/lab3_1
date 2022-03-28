package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.MoneyBuilder;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    InvoiceFactory invoiceFactory;
    @Mock
    TaxPolicy taxPolicy;
    private InvoiceRequest invoiceRequest;
    private ProductDataBuilder productBuilder;
    private MoneyBuilder moneyBuilder;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void testCase1() {
        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);

        invoiceRequest = new InvoiceRequest(new ClientData(new Id("clientID"), "Name"));
        invoiceRequest.add(new RequestItem(productBuilder.build(), 1, moneyBuilder.build()));

        bookKeeper.issuance(invoiceRequest, taxPolicy);
    }

    @Test
    void testCase2() {
        fail("not implemented");
    }
}
