package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;


public class InvoiceRequestBuilder {
    private ClientData client = new ClientData(Id.generate(), "Jan");
    private ProductDataBuilder productDataBuilder;
    private RequestItem requestItem = new RequestItem(productDataBuilder.sampleProductData().build(),
            1, new Money(1));

    public InvoiceRequestBuilder sampleInvoice() {
        return this;
    }
    public InvoiceRequest build() {
        return new InvoiceRequest(client);
    }

    public InvoiceRequestBuilder withClient(ClientData client) {
        this.client = client;
        return this;
    }

    public InvoiceRequestBuilder withRequestItem(RequestItem requestItem) {
        this.requestItem = requestItem;
        return this;
    }
}
