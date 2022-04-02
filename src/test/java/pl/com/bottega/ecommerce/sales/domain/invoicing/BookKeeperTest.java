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
import static org.mockito.Mockito.when;
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

}
