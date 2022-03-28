package pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductDataBuilder;

public class ClientDataBuilder {
    private Id id = new Id("clientId");
    private String name = "Name";

    public ClientDataBuilder defaultClientData() {
        return this;
    }

    public ClientData build() {
        return new ClientData(id, name);
    }
}
