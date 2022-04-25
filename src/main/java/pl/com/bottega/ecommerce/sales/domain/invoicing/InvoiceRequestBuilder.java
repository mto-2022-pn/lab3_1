package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.util.ArrayList;
import java.util.List;


public class InvoiceRequestBuilder {
    private ClientData client = new ClientData(Id.generate(), "Jan");
    private final ProductDataBuilder productDataBuilder = new ProductDataBuilder();
    private final RequestItem requestItem = new RequestItem(productDataBuilder.build(),
            1, Money.ZERO);
    private final List<RequestItem> requestItems = new ArrayList<>();

    public InvoiceRequest build() {
        InvoiceRequest invoiceRequest = new InvoiceRequest(client);
        for (RequestItem requestItem : requestItems)
            invoiceRequest.add(requestItem);
        return invoiceRequest;
    }

    public InvoiceRequestBuilder withClient(ClientData client) {
        this.client = client;
        return this;
    }
    public InvoiceRequestBuilder addSampleRequestItem() {
        requestItems.add(requestItem);
        return this;
    }
    public InvoiceRequestBuilder addRequestItem(RequestItem requestItem) {
        requestItems.add(requestItem);
        return this;
    }
}
