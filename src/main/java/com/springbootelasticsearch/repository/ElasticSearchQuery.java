package com.springbootelasticsearch.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.springbootelasticsearch.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchQuery {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "products";

    // This class is a Spring Data Elasticsearch repository for performing operations on Elasticsearch documents.

    // Method to create or update a document in Elasticsearch.
    public String createOrUpdateDocument(Product product) throws IOException {
        // Index the product using ElasticsearchClient.
        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(product.getId())
                .document(product)
        );

        // Check the response and return a message accordingly.
        if (response.result().name().equals("Created")) {
            return "Document has been successfully created.";
        } else if (response.result().name().equals("Updated")) {
            return "Document has been successfully updated.";
        }
        return "Error while performing the operation.";
    }

    // Method to get a document by its ID.
    public Product getDocumentById(String productId) throws IOException {
        Product product = null;
        // Retrieve the document by ID using ElasticsearchClient.
        GetResponse<Product> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(productId),
                Product.class
        );

        // Check if the document is found and return it.
        if (response.found()) {
            product = response.source();
            System.out.println("Product name " + product.getName());
        } else {
            System.out.println("Product not found");
        }

        return product;
    }

    // Method to delete a document by its ID.
    public String deleteDocumentById(String productId) throws IOException {
        // Create a delete request for the specified document ID.
        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(productId));

        // Delete the document using ElasticsearchClient.
        DeleteResponse deleteResponse = elasticsearchClient.delete(request);

        // Check the response and return a message accordingly.
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return "Product with id " + deleteResponse.id() + " has been deleted.";
        }
        System.out.println("Product not found");
        return "Product with id " + deleteResponse.id() + " does not exist.";
    }

    // Method to search for all documents in the Elasticsearch index.
    public List<Product> searchAllDocuments() throws IOException {
        // Create a search request for the index.
        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));

        // Perform the search using ElasticsearchClient.
        SearchResponse<Product> searchResponse = elasticsearchClient.search(searchRequest, Product.class);

        // Process the search results and return a list of products.
        List<Hit<Product>> hits = searchResponse.hits().hits();
        List<Product> products = new ArrayList<>();
        for (Hit object : hits) {
            System.out.print(((Product) object.source()));
            products.add((Product) object.source());
        }
        return products;
    }
}
