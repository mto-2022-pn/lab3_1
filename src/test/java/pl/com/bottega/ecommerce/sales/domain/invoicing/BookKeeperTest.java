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

import java.util.Random;

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
    void setUp() {
        clientData = clientBuilder.defaultClientData().build();
        bookKeeper = new BookKeeper(invoiceFactory);
        invoiceRequest = new InvoiceRequest(clientData);
    }

    @Test
    void testCase1() {
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        Mockito.when(taxPolicy.calculateTax(Mockito.any(), Mockito.any())).thenReturn(new Tax(moneyBuilder.defaultMoney().build(), "desc"));

        invoiceRequest.add(itemBuilder.defaultItem().build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(1, invoice.getItems().size());
    }

    @Test
    void testCase2() {
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        Mockito.when(taxPolicy.calculateTax(Mockito.any(), Mockito.any())).thenReturn(new Tax(moneyBuilder.defaultMoney().build(), "desc"));

        invoiceRequest.add(itemBuilder.defaultItem().build());
        invoiceRequest.add(itemBuilder.defaultItem().build());
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(), Mockito.any());
    }

    @Test
    void testResultInvoiceEmpty() {
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(0, invoice.getItems().size());
    }

    @Test
    void testResultInvoiceWithSeveralItems() {
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        Mockito.when(taxPolicy.calculateTax(Mockito.any(), Mockito.any())).thenReturn(new Tax(moneyBuilder.defaultMoney().build(), "desc"));

        Random rand = new Random();
        int count = rand.nextInt(10)+1;
        for (int i=0; i<count; ++i)
            invoiceRequest.add(itemBuilder.defaultItem().build());
        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
        assertEquals(count, invoice.getItems().size());
    }

    @Test
    void testCalculateTaxInvokedZeroTimes() {
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        bookKeeper.issuance(invoiceRequest, taxPolicy);
        Mockito.verify(taxPolicy, Mockito.times(0)).calculateTax(Mockito.any(), Mockito.any());
    }

    @Test
    void testCalculateTaxInvokedSeveralTimes() {
        Mockito.when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        Mockito.when(taxPolicy.calculateTax(Mockito.any(), Mockito.any())).thenReturn(new Tax(moneyBuilder.defaultMoney().build(), "desc"));

        Random rand = new Random();
        int count = rand.nextInt(10)+1;
        for (int i=0; i<count; ++i)
            invoiceRequest.add(itemBuilder.defaultItem().build());
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        Mockito.verify(taxPolicy, Mockito.times(count)).calculateTax(Mockito.any(), Mockito.any());
    }
}
