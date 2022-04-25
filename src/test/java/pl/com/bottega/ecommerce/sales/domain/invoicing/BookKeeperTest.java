package pl.com.bottega.ecommerce.sales.domain.invoicing;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

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
    void init(){
        bookKeeper = new BookKeeper(invoiceFactory);
        clientData = new ClientData(Id.generate(),"Test Client");
        invoiceRequest = new InvoiceRequest(clientData);
    }

    @Test
    void invoiceWithOnePositionOnRequest() {
        when(taxPolicy.calculateTax(any(ProductType.class),any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        Product product = new ProductBuilder().withProductType(ProductType.FOOD).withPrice(new Money(2.50)).withName("Milk").build();
        RequestItem requestItem = new RequestItemBuilder().withProductData(product).withQuantity(1).build();
        invoiceRequest.add(requestItem);
        int res = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();
        assertEquals(1,res);
    }

    @Test
    void invoiceWithCouplePositionsOnRequest(){
        when(taxPolicy.calculateTax(any(ProductType.class),any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        Product product1 = new ProductBuilder().withProductType(ProductType.FOOD).withPrice(new Money(2.50)).withName("Milk").build();
        Product product2 = new ProductBuilder().withProductType(ProductType.DRUG).withPrice(new Money(14.99)).withName("Aspirin").build();
        Product product3 = new ProductBuilder().withProductType(ProductType.STANDARD).withPrice(new Money(9.99)).withName("Calgon").build();

        RequestItem requestItem1 = new RequestItemBuilder().withProductData(product1).withQuantity(12).build();
        RequestItem requestItem2 = new RequestItemBuilder().withProductData(product2).withQuantity(1).build();
        RequestItem requestItem3 = new RequestItemBuilder().withProductData(product3).withQuantity(2).build();

        invoiceRequest.add(requestItem1);
        invoiceRequest.add(requestItem2);
        invoiceRequest.add(requestItem3);

        Invoice invoice = bookKeeper.issuance(invoiceRequest,taxPolicy);

        assertEquals(product1.generateSnapshot(),invoice.getItems().get(0).getProduct());
        assertEquals(product2.generateSnapshot(),invoice.getItems().get(1).getProduct());
        assertEquals(product3.generateSnapshot(),invoice.getItems().get(2).getProduct());

        assertEquals(3,invoice.getItems().size());
    }

    @Test
    void invoiceWithNoPositions(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        Invoice invoice = bookKeeper.issuance(invoiceRequest,taxPolicy);

        assertEquals(0,invoice.getItems().size());
        assertTrue(invoice.getItems().isEmpty());
    }

    @Test
    void invoiceWithTwoPositionsTwoTimesInvokes(){
        when(taxPolicy.calculateTax(any(ProductType.class),any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        Product product = new ProductBuilder().withProductType(ProductType.FOOD).withPrice(new Money(2.50)).withName("Milk").build();
        RequestItem requestItem = new RequestItemBuilder().withProductData(product).withQuantity(1).build();
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);

        bookKeeper.issuance(invoiceRequest,taxPolicy);
        verify(taxPolicy,times(2)).calculateTax(any(ProductType.class),any(Money.class));
    }


    @Test
    void invoiceWithoutAnyPositionsShouldBeInvokedZeroTimes(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));

        Invoice invoice = bookKeeper.issuance(invoiceRequest,taxPolicy);
        assertTrue(invoice.getItems().isEmpty());
        verify(taxPolicy,times(0)).calculateTax(any(ProductType.class),any(Money.class));
    }

    @Test
    void invoiceWithXNumberOfPositionsShouldBeInvokedXNumberTimes(){
        when(taxPolicy.calculateTax(any(ProductType.class),any(Money.class))).thenReturn(new Tax(Money.ZERO, "tax"));
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(),clientData));
        int x = 10;

        Random random = new Random();
        for (int i = 0; i < x; i++) {
            ProductType pType = ProductType.values()[random.nextInt(ProductType.values().length)];
            double price = random.nextDouble();
            String name = "TestName"+(i+1);
            int amount = random.nextInt(10);
            Product product = new ProductBuilder().withProductType(pType).withPrice(new Money(price)).withName(name).build();
            RequestItem requestItem = new RequestItemBuilder().withProductData(product).withQuantity(amount).build();
            invoiceRequest.add(requestItem);
        }
        bookKeeper.issuance(invoiceRequest,taxPolicy);
        verify(taxPolicy,times(x)).calculateTax(any(ProductType.class),any(Money.class));

    }


}
