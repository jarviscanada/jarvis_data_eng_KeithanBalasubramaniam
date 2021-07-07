package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterDaoIntTest {


    String text;
    Double lng;
    Double lat;
    Tweet tweet;
    CrdDao<Tweet, String> dao;
    HttpHelper httpHelper;

    @Before
    public void setUp() throws Exception {
        String CONSUMER_KEY = System.getenv("consumerKey");
        String CONSUMER_SECRET = System.getenv("consumerSecret");
        String ACCESS_TOKEN = System.getenv("accessToken");
        String TOKEN_SECRET = System.getenv("tokenSecret");

        httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
                ACCESS_TOKEN, TOKEN_SECRET);

        dao = new TwitterDao(httpHelper);
        lng = 1d;
        lat = 1d;
        String hashtag = "#abc";
        text = "some text " + hashtag + " " + System.currentTimeMillis();
        tweet = CreateTweetUtil.createTweet(text, lng, lat);
    }

    @Test
    public void create() throws URISyntaxException{
        Tweet ret = dao.create(tweet);
        assertNotNull(ret);
        assertEquals(lng, ret.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, ret.getCoordinates().getCoordinates().get(1));
        assertEquals(text, ret.getText());

        try {
            dao.deleteById(ret.getId_str());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findById() throws URISyntaxException{
        Tweet ret = dao.create(tweet);

        Tweet fnd = dao.findById(ret.getId_str());
        assertNotNull(fnd);
        assertEquals(fnd.getId_str(), ret.getId_str());
        assertEquals(lng, fnd.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, fnd.getCoordinates().getCoordinates().get(1));
        assertEquals(text, fnd.getText());

        try {
            dao.deleteById(ret.getId_str());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteById() throws URISyntaxException{
        Tweet ret = dao.create(tweet);

        try {
            Tweet fnd = dao.deleteById(ret.getId_str());
            assertEquals(lng, fnd.getCoordinates().getCoordinates().get(0));
            assertEquals(lat, fnd.getCoordinates().getCoordinates().get(1));
            assertEquals(text, fnd.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}