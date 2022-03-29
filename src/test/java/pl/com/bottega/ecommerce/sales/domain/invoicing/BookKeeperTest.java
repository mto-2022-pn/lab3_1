package pl.com.bottega.ecommerce.sales.domain.invoicing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    @Mock
    private InvoiceFactory invoiceFactory;
    @Mock
    private TaxPolicy taxPolicy;

    private InvoiceRequest invoiceRequest;
    private BookKeeper bookKeeper;
    private ClientData clientData;

    @BeforeEach
    void setUp() {
        bookKeeper = new BookKeeper(invoiceFactory);
        clientData = new ClientData(Id.generate(), "client");
        invoiceRequest = new InvoiceRequest(clientData);
    }

    // state test

    @Test
    public void singlePositionRequestReturnSinglePositionInvoice() {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        ProductBuilder productBuilder = new  ProductBuilder();
        RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
        Product product = productBuilder.build();
        RequestItem requestItem = requestItemBuilder.withProductData(product.generateSnapshot()).build();
        invoiceRequest.add(requestItem);
        int result = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();
        assertEquals(1,result);
    }
    @Test
    public void nonPositionRequestReturnNonePositionInvoice() {
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        assertEquals(0, bookKeeper.issuance(invoiceRequest, taxPolicy).getItems().size());
    }

    // behavior tests

    @Test
    public void doublePositionRequestInvokeCalculateTaxMethodTwoTimes(){
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        ProductBuilder productBuilder = new  ProductBuilder();
        RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
        Product product = productBuilder.withPrice(new Money(10,Money.DEFAULT_CURRENCY)).withName("chleb").build();
        RequestItem requestItem = requestItemBuilder.withProductData(product.generateSnapshot()).build();
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class),any(Money.class));
    }

    @Test
    public void nullRequestShouldNotInvokeCalculateTaxMethod()
    {
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        bookKeeper.issuance(invoiceRequest, taxPolicy);
        verify(taxPolicy,times(0)).calculateTax(any(ProductType.class),any(Money.class));
    }
}
