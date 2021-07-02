package ca.jrvs.apps.twitter.dao.helper;

import java.io.IOException;
import java.net.URI;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URI;

public class TwitterHttpHelper implements HttpHelper{

    static final Logger logger = LoggerFactory.getLogger(TwitterHttpHelper.class);

    private OAuthConsumer consumer;
    private HttpClient httpClient;

    public TwitterHttpHelper(){
        //get all envs
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        //set up consumer and httpClient
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);
        httpClient = HttpClientBuilder.create().build();
    }

    /**
     * Constructor setting up environment variables from parameters
     *
     * @param consumerKey
     * @param consumerSecret
     * @param accessToken
     * @param tokenSecret
     */
    public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken, String tokenSecret){
        BasicConfigurator.configure();
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);

        httpClient = HttpClientBuilder.create().build();
    }

    /**
     * Execute a HTTP Post call
     *
     * @param uri
     * @return
     */
    @Override
    public HttpResponse httpPost(URI uri) {
        try {
            return completeHttpRequest(HttpMethod.POST, uri, null);
        } catch (OAuthException | IOException e){
            throw new RuntimeException("Failed to execute", e);
        }
    }

    /**
     * Execute a HTTP Get call
     *
     * @param uri
     * @return
     */
    @Override
    public HttpResponse httpGet(URI uri) {
        try {
            return completeHttpRequest(HttpMethod.GET, uri, null);
        } catch (OAuthException | IOException e){
            throw new RuntimeException("Failed to execute", e);
        }
    }

    private HttpResponse completeHttpRequest(HttpMethod method, URI uri, StringEntity stringEntity)throws OAuthException, IOException{
        if (method == HttpMethod.GET) {
            HttpGet request = new HttpGet(uri);
            consumer.sign(request);
            return httpClient.execute(request);
        } else if (method == HttpMethod.POST){
            HttpPost request = new HttpPost(uri);
            if (stringEntity != null) {
                request.setEntity(stringEntity);
            }
            consumer.sign(request);
            return httpClient.execute(request);
        } else {
            throw new IllegalArgumentException("Unknown HTTP method: " + method.name());
        }
    }
}
