package ca.jrvs.apps.twitter.dao.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gdata.util.common.base.PercentEscaper;
import junit.framework.TestCase;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import static org.junit.Assert.*;

public class TwitterHttpHelperTest extends TestCase {
    private static final String CONSUMER_KEY = System.getenv("consumerKey");
    private static final String CONSUMER_SECRET = System.getenv("consumerSecret");
    private static final String ACCESS_TOKEN = System.getenv("accessToken");
    private static final String TOKEN_SECRET = System.getenv("tokenSecret");
    private TwitterHttpHelper httpHelper;

    @Before
    public void setUp() {
        httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
                ACCESS_TOKEN, TOKEN_SECRET);
    }

    @Test
    public void testHttpPost() throws URISyntaxException {
        String status = "Test post";
        PercentEscaper percentEscaper = new PercentEscaper("", false);

        URI uri = new URI("https://api.twitter.com/1.1/statuses/update.json?status="
                + percentEscaper.escape(status));

        httpHelper.httpPost(uri);
    }

    @Test
    public void testHttpGet() throws URISyntaxException, IOException {
        URI uri = new URI("https://api.twitter.com/1.1/statuses/show.json?id=1404474031647215621");
        HttpResponse response = httpHelper.httpGet(uri);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}