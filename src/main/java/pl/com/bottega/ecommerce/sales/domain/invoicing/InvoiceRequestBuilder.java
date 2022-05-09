package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.ArrayList;
import java.util.List;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class InvoiceRequestBuilder {
    
    private ClientData client;
    private List<RequestItem> items = new ArrayList<>();

    public InvoiceRequestBuilder generateDefaultInvoiceRequestBuilder()
    {
        client = new ClientData(new Id("00"),"Name");
        items = new ArrayList<RequestItem>();
        items.add((new RequestItemBuilder()).generateDefaultProductDataBuilder().build());
        return this;
    }

    public InvoiceRequestBuilder addClient(ClientData client) {
        this.client = client;
        return this;
    }

    public InvoiceRequestBuilder addItems(List<RequestItem> items) {
        this.items = items;
        return this;
    }

    public InvoiceRequest build() {
        InvoiceRequest buildResult = new InvoiceRequest(client);

        for (RequestItem requestItem : items) {
            buildResult.add(requestItem);
        }

        return buildResult;
    }
}
