package ca.jrvs.apps.twitter.dao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TwitterJsonParser;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String> {

    // URI constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";

    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";

    private static final int HTTP_OK = 200;

    private HttpHelper httpHelper;

    @Autowired
    public TwitterDao(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    /**
     * Create an entity(Tweet) to the underlying storage
     *
     * @param tweet entity that to be created
     * @return created entity
     */
    @Override
    public Tweet create(Tweet tweet) {
        URI uri;
        uri = getPostUri(tweet);

        //Execute HTTP Request
        HttpResponse response = httpHelper.httpPost(uri);
        return parseData(response, HTTP_OK);
    }

    /**
     * Find an entity(Tweet) by its id
     *
     * @param s entity id
     * @return Tweet entity
     */
    @Override
    public Tweet findById(String s) {
        URI uri = URI.create(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s);

        HttpResponse response = httpHelper.httpGet(uri);
        try {
            Tweet t = parseData(response, HTTP_OK);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete an entity(Tweet) by its ID
     *
     * @param s of the entity to be deleted
     * @return deleted entity
     */
    @Override
    public Tweet deleteById(String s) {
        URI uri = URI.create(API_BASE_URI + DELETE_PATH + "/" + s + ".json");
        HttpResponse response = httpHelper.httpPost(uri);
        try {
            Tweet t = parseData(response, HTTP_OK);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private URI getPostUri(Tweet tweet) {
        URI uri;
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        try {
            uri = new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + percentEscaper
                    .escape(tweet.getText()) + AMPERSAND + "long" + EQUAL + tweet.getCoordinates()
                    .getCoordinates().get(0) + AMPERSAND + "lat" + EQUAL + tweet.getCoordinates()
                    .getCoordinates().get(1));
            return uri;
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Invalid input", ex);
        }
    }


    /**
     * Takes the response data of the request and creates the tweet object from the body
     *
     * @param response
     * @param code
     * @return
     */
    public Tweet parseData(HttpResponse response, int code) {
        Tweet tweet = null;

        int sCode = response.getStatusLine().getStatusCode();
        if (sCode != code) {
            throw new RuntimeException("Status code invalid: " + sCode);
        }

        String data;
        try {
            data = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert the response to String.");
        }

        try {
            tweet = TwitterJsonParser.toObjectFromJson(data, Tweet.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert data to a Tweet object -> " + e);
        }
        return tweet;
    }
}