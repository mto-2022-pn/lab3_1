package pl.com.bottega.ecommerce.sales.domain.invoicing;

import java.util.ArrayList;
import java.util.List;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;

public class InvoiceRequestBuilder {
    
    private ClientData client;
    private List<RequestItem> items;

    InvoiceRequestBuilder()
    {
        client = new ClientData(new Id("00"),"Name");
        items = new ArrayList<RequestItem>();
        items.add((new RequestItemBuilder()).build());
    }

    public InvoiceRequestBuilder withInvoiceWithXDefaultItems(int x)
    {
        items = new ArrayList<RequestItem>();
        for(int i=0;i<x;i++)
        {
            items.add((new RequestItemBuilder()).build());
        }
        return this;
    }

    public InvoiceRequestBuilder withClient(ClientData client) {
        this.client = client;
        return this;
    }

    public InvoiceRequestBuilder withItems(List<RequestItem> items) {
        this.items = items;
        return this;
    }

    public InvoiceRequestBuilder addItem(RequestItem item) {
        items.add(item);
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
