/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.com.bottega.ecommerce.sales.domain.invoicing;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

class RequestItem {

    private ProductData productData;

    private int quantity;

    private Money totalCost;

    public RequestItem(ProductData productData, int quantity, Money totalCost) {
        this.productData = productData;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }

    public Money getTotalCost() {
        return totalCost;
    }

    public ProductData getProductData() {
        return productData;
    }

    public int getQuantity() {
        return quantity;
    }


    static class RequestItemBuilder {

        private ProductData productData;
        private int quantity;
        private Money totalCost;

        public RequestItem.RequestItemBuilder withProductType(ProductData productData) {
            this.productData = productData;
            return this;
        }

        public RequestItem.RequestItemBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public RequestItem.RequestItemBuilder withTotalCost(Money totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public RequestItem build() {
            return new RequestItem(productData, quantity, totalCost);
        }

    }
}
