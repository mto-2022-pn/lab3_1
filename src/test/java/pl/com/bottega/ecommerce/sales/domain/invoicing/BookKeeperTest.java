package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.fail;
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

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    
    @Mock
    InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    @Mock
    private TaxPolicy taxPolicy;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void test() {
        var client = new ClientData(new Id("id"), "Staszek");
        invoiceRequest = new InvoiceRequest(client);
        var builder = new RequestItemBuilder();
        invoiceRequest.add(builder.generateDefaultProductDataBuilder().build());

        BookKeeper bookKeeper = new BookKeeper(invoiceFactory);


        Mockito.when(bookKeeper.issuance(invoiceRequest, taxPolicy));


    }

}
