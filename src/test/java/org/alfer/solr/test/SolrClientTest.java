package org.alfer.solr.test;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by alfer on 7/20/17.
 */
public class SolrClientTest {

    SolrClient client = null;

    @Before
    public void init() {
        // Using a ZK Host String
        String zkHostString = "http://dev61:8983/solr";
        // solr = new CloudSolrClient.Builder().withZkHost(zkHostString).build();

        // Using already running Solr nodes
        String urlString = "http://dev61:8983/solr";
        client = new CloudSolrClient.Builder().withSolrUrl(urlString).build();
    }

    @Test
    public void addDocuement() throws IOException, SolrServerException {
        //SolrClient client = new HttpSolrClient.Builder("http://dev61:8983/solr/collection1").build();
        Collection<SolrInputDocument> list = new ArrayList<SolrInputDocument>();
        for (int i = 0; i < 1000; ++i) {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("cat", "book");
            doc.addField("id", "book-" + i);
            doc.addField("name", "The Legend of the Hobbit part " + i);
            list.add(doc);
//            client.add("gettingstarted", doc);
//            if (i % 100 == 0)
//                client.commit();  // periodically flush
        }
        client.optimize("gettingstarted");
        client.commit();
    }

    @Test
    public void query() throws IOException, SolrServerException {

        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.set("collection", "gettingstarted");
        query.setRequestHandler("/select");
        query.setFields("title");
        QueryResponse response = client.query(query);
        SolrDocumentList list = response.getResults();
        System.out.println("size=" + list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

}
