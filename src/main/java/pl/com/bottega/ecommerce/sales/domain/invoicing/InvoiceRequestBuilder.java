package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;


public class InvoiceRequestBuilder {
    private ClientData client = new ClientData(Id.generate(), "Jan");
    private ProductDataBuilder productDataBuilder;
    private final RequestItem requestItem = new RequestItem(productDataBuilder.build(),
            1, Money.ZERO);
    private InvoiceRequest sampleInvoiceRequest;

    public InvoiceRequest build() {
        sampleInvoiceRequest = new InvoiceRequest(client);
        return sampleInvoiceRequest;
    }

    public InvoiceRequestBuilder withClient(ClientData client) {
        this.client = client;
        return this;
    }
    public InvoiceRequest addSampleRequestItem() {
        sampleInvoiceRequest.add(requestItem);
        return sampleInvoiceRequest;
    }
    public InvoiceRequest addRequestItem(RequestItem requestItem) {
        sampleInvoiceRequest.add(requestItem);
        return sampleInvoiceRequest;
    }
}
