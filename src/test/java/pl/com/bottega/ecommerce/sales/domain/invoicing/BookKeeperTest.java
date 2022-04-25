package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

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
    void init() {
        bookKeeper = new BookKeeper(invoiceFactory);
        clientData = new ClientData(Id.generate(), "test");
        invoiceRequest = new InvoiceRequest(clientData);
    }


    @Test
    public void singlePositionRequestReturnSinglePositionInvoice() {
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));


        Product product = Product.builder().productType(ProductType.STANDARD).build();
        RequestItem requestItem = RequestItem.builder().productData(product.generateSnapshot()).totalCost(Money.ZERO).build();
        invoiceRequest.add(requestItem);
        int count = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();


        assertEquals(1,count);
    }

    @Test
    public void requestWithTwoPositionsShouldMakeMethodInvokeCalculateTaxTwice(){
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));


        Product product = Product.builder().productType(ProductType.STANDARD).build();
        RequestItem requestItem = RequestItem.builder().productData(product.generateSnapshot()).totalCost(Money.ZERO).build();
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest,taxPolicy);


        verify(taxPolicy,times(2)).calculateTax(any(ProductType.class), any(Money.class));
    }

    @Test
    public void emptyListRequestShouldMakeMethodReturnEmptyListRespond(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        int count = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();

        assertEquals(0,count);
    }

    @Test
    public void methodInvokedWithNullParamsReturnException() {
        assertThrows(NullPointerException.class, () -> bookKeeper.issuance(null, null));
    }

    @Test
    public void methodInvokedWithEmptyListRequestShouldNotInvokeCalculateTax(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        bookKeeper.issuance(invoiceRequest,taxPolicy);

        verify(taxPolicy,times(0)).calculateTax(any(ProductType.class),any(Money.class));
    }

    @Test
    public void sizeOfListReturnedByMethodShouldMatchTimesCalculateTaxWasInvoked(){
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));


        Product product = Product.builder().productType(ProductType.STANDARD).build();
        RequestItem requestItem = RequestItem.builder().productData(product.generateSnapshot()).totalCost(Money.ZERO).build();
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        int count = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();


        verify(taxPolicy,times(count)).calculateTax(any(ProductType.class), any(Money.class));
    }

}
