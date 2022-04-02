package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.builders.ProductDataBuilder;
import pl.com.bottega.ecommerce.sales.domain.invoicing.builders.RequestItemBuilder;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
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
    void setUp() throws Exception {
        bookKeeper = new BookKeeper(invoiceFactory);
        clientData = new ClientData(Id.generate(), "klient");
        invoiceRequest = new InvoiceRequest(clientData);
    }

    @Test
    void RequestForAnInvoiceWithOneItemShouldReturnAnInvoiceWithOneItem(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "test tax"));

        ProductDataBuilder productdataBuilder = new  ProductDataBuilder();
        RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
        ProductData product = productdataBuilder.build();
        RequestItem requestItem = requestItemBuilder.withProductData(product).build();
        invoiceRequest.add(requestItem);
        int result = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();
        assertEquals(1,result);
    }

    @Test
    void RequestForAnInvoiceZeroItemsShouldReturnAnInvoiceWithZeroItem(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        int result = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();
        assertEquals(0,result);
    }

    @Test
    void RequestForAnInvoiceWithFewItemsShouldReturnAnInvoiceWithNumberOfItemsEqualToTheRequest(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "test tax"));

        ProductDataBuilder productdataBuilder = new  ProductDataBuilder();
        RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
        ProductData []product = {productdataBuilder.withPrice(new Money(10,Money.DEFAULT_CURRENCY)).withName("Corn flakes").withType(ProductType.FOOD).build(),
                productdataBuilder.withPrice(new Money(5,Money.DEFAULT_CURRENCY)).withName("Natural yoghurt").withType(ProductType.FOOD).build(),
                productdataBuilder.withPrice(new Money(50,Money.DEFAULT_CURRENCY)).withName("Aspirin").withType(ProductType.DRUG).build()};
        RequestItem requestItem;
        for (ProductData productData : product) {
            requestItem = requestItemBuilder.withProductData(productData).build();
            invoiceRequest.add(requestItem);
        }
        int result = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();
        assertEquals(3,result);
    }

    @Test
    void RequestForAnInvoiceWithTwoItemsShouldInvokeCalculateTaxTwice(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        when(taxPolicy.calculateTax(any(ProductType.class), any(Money.class))).thenReturn(new Tax(Money.ZERO, "test tax"));

        ProductDataBuilder productdataBuilder = new  ProductDataBuilder();
        RequestItemBuilder requestItemBuilder = new RequestItemBuilder();
        ProductData []product = {productdataBuilder.withPrice(new Money(20,Money.DEFAULT_CURRENCY)).withName("Oat flakes").withType(ProductType.FOOD).build(),
                productdataBuilder.withPrice(new Money(30,Money.DEFAULT_CURRENCY)).withName("Strawberry yoghurt").withType(ProductType.FOOD).build()};
        RequestItem requestItem;
        for (ProductData productData : product) {
            requestItem = requestItemBuilder.withProductData(productData).build();
            invoiceRequest.add(requestItem);
        }
        bookKeeper.issuance(invoiceRequest,taxPolicy);
        verify(taxPolicy, times(2)).calculateTax(any(ProductType.class),any(Money.class));
    }

    @Test
    void RequestForAnInvoiceWithZeroItemsShouldInvokeCalculateTaxZeroTimes(){
        when(invoiceFactory.create(clientData)).thenReturn(new Invoice(Id.generate(), clientData));
        bookKeeper.issuance(invoiceRequest,taxPolicy);
        verify(taxPolicy, times(0)).calculateTax(any(ProductType.class),any(Money.class));
    }

    @Test
    void RequestForAnInvoiceWithNullInvoiceRequestandNullTaxPolicyShouldThrowNullPointerException(){
        try{
            bookKeeper.issuance(null, null);
            fail("expected NullPointerException");
        }catch(NullPointerException ignored){
        }
    }


}
