package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

class BookKeeperTest {

    private BookKeeper bookKeeper;
    // private InvoiceFactory invoiceFactory;
    private InvoiceRequest invoiceRequest;
    private ClientData clientData;
    private InvoiceFactory invoiceFactory;
    private TaxPolicy taxPolicy;

    @BeforeEach
    void setUp() throws Exception {

        invoiceFactory = new InvoiceFactory();
        bookKeeper = new BookKeeper(invoiceFactory);
        clientData = new ClientData(Id.generate(), "Client");
        invoiceRequest = new InvoiceRequest(clientData);
        // taxPolicy = new Tax(new Money(10.00), "Opis");
        taxPolicy = new TaxPolicy() {

            @Override
            public Tax calculateTax(ProductType productType, Money net) {
                return new Tax(new Money(10.00), "Opis");
            }
            
        };
    }

    @Test
    void test() {
        fail("not implemented");
    }

    @Test
    void testCase1() {
        Product product = new Product(Id.generate(),
                                        new Money(60.00),
                                        "Produkt",
                                        ProductType.FOOD);
        RequestItem requestItem = new RequestItem(product.generateSnapshot(), 1, product.getPrice());
        invoiceRequest.add(requestItem);
        int count = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();


        assertEquals(1,count);
    }
    @Test
    void testCase2()
    {

        Product product = new Product(Id.generate(),
                                        new Money(60.00),
                                        "Produkt",
                                        ProductType.FOOD);
        RequestItem requestItem = new RequestItem(product.generateSnapshot(), 1, product.getPrice());
        invoiceRequest.add(requestItem);
        invoiceRequest.add(requestItem);
        bookKeeper.issuance(invoiceRequest,taxPolicy);

        int count = bookKeeper.issuance(invoiceRequest,taxPolicy).getItems().size();
        assertEquals(2,count);
    }
}
