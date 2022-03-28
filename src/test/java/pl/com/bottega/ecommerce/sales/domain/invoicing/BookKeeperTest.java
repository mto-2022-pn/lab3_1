package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookKeeperTest {
    @Mock
    private InvoiceRequest invoiceRequest;

    @Mock
    private TaxPolicy taxPolicy;

    private InvoiceRequestBuilder invoiceRequestBuilder;
    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void testOneInvoiceRequestShouldReturnInvoiceWithOneItem() {

    }

}
