package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientDataBuilder;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.MoneyBuilder;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    InvoiceFactory invoiceFactory;
    @Mock
    TaxPolicy taxPolicy;

    private BookKeeper bookKeeper;
    private InvoiceRequest invoiceRequest;
    private ClientData clientData;

    private ClientDataBuilder clientBuilder = new ClientDataBuilder();
    private RequestItemBuilder itemBuilder = new RequestItemBuilder();
    private MoneyBuilder moneyBuilder = new MoneyBuilder();

    @BeforeEach
    void setUp() throws Exception {
        clientData = clientBuilder.defaultClientData().build();
        bookKeeper = new BookKeeper(invoiceFactory);
        invoiceRequest = new InvoiceRequest(clientData);

        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        Mockito.when(taxPolicy.calculateTax(Mockito.any(), Mockito.any())).thenReturn(new Tax(moneyBuilder.defaultMoney().build(), "desc"));
    }

    @Test
    void testCase1() {
        invoiceRequest.add(itemBuilder.defaultItem().build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(1, invoice.getItems().size());
    }

    @Test
    void testCase2() {
        invoiceRequest.add(itemBuilder.defaultItem().build());
        invoiceRequest.add(itemBuilder.defaultItem().build());
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(), Mockito.any());
    }
}
