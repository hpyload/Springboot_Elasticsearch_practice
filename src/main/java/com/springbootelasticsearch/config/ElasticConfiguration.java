package com.springbootelasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfiguration {

    @Bean
    public RestClient getRestClient() {
        // Create and configure a REST client to connect to Elasticsearch on localhost:9200
        return RestClient.builder(
                new HttpHost("localhost", 9200)).build();
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport() {
        // Create an Elasticsearch transport component using the REST client
        // Configure it with a JacksonJsonpMapper for JSON serialization/deserialization
        return new RestClientTransport(
                getRestClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient getElasticsearchClient(){
        // Create an Elasticsearch client using the transport component
        return new ElasticsearchClient(getElasticsearchTransport());
    }
}
