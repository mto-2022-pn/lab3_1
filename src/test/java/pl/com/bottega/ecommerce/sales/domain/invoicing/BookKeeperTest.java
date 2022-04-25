package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {

    @Mock
    private InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;

    @Mock
    private TaxPolicy taxPolicy;
    private ClientData clientData;
    private BookKeeper bookKeeper;

    @BeforeEach
    void setUp() throws Exception {
        bookKeeper = new BookKeeper(invoiceFactory);
        clientData = new ClientData(Id.generate(),"New client");
        invoiceRequest = new InvoiceRequest(clientData);
    }

    @Test
    void invoiceWithoutPositions(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));
        Invoice invoice = bookKeeper.issuance(invoiceRequest,taxPolicy);
        assertEquals(0, invoice.getItems().size());
        assertTrue(invoice.getItems().isEmpty());
    }

    @Test
    void invoiceWithOnePosition() {
        when(taxPolicy.calculateTax(ProductType.STANDARD,Money.ZERO)).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));

        RequestItem requestItem = new RequestedItemBuilder().build();
        invoiceRequest.add(requestItem);

        assertEquals(1, bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size());

    }


    @Test
    void invoiceWithManyPositions(){
        when(taxPolicy.calculateTax(any(ProductType.class),any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        int numberOfInvoice = 5;
        List<Product> productArray = new ArrayList<Product>();
        List<RequestItem> requestItemArray = new ArrayList<RequestItem>();
        for (int i = 0; i < numberOfInvoice; i++) {
            productArray.add(new ProductBuilder().build());
            requestItemArray.add(new RequestedItemBuilder().withProductData(productArray.get(i)).build());
            invoiceRequest.add(requestItemArray.get(i));
        }

        Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);

        for (int i = 0; i < numberOfInvoice; i++) {
            assertEquals(productArray.get(i).generateSnapshot(), invoice.getItems().get(i).getProduct());
        }

        assertEquals(numberOfInvoice, invoice.getItems().size());

    }

    @Test
    void invoiceWithoutPositionsShouldBeInvokedZeroTimes(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        Invoice invoice = bookKeeper.issuance(invoiceRequest,taxPolicy);
        assertTrue(invoice.getItems().isEmpty());
        verify(taxPolicy,times(0)).calculateTax(any(ProductType.class),any(Money.class));
    }

    @Test
    void invoiceWithOnePositionsShouldBeInvokedOnes(){
        when(taxPolicy.calculateTax(any(ProductType.class),any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        Product product = new ProductBuilder().build();
        RequestItem requestItem = new RequestedItemBuilder().withProductData(product).build();
        invoiceRequest.add(requestItem);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        verify(taxPolicy,times(1)).calculateTax(any(ProductType.class),any(Money.class));
    }



    @Test
    void invoiceWithoutManyShouldBeInvokedManyTimes(){
        when(taxPolicy.calculateTax(any(ProductType.class),any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        Product product = new ProductBuilder().withProductType(ProductType.FOOD).withPrice(new Money(2.50)).withName("Milk").build();
        RequestItem requestItem = new RequestedItemBuilder().withProductData(product).withQuantity(1).build();
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        verify(taxPolicy,times(2)).calculateTax(any(ProductType.class),any(Money.class));
    }

}
