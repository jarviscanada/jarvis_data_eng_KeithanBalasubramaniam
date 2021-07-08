package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelperTest;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import java.net.URISyntaxException;

import ca.jrvs.apps.twitter.util.TwitterJsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.After;
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
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);

        dao = new TwitterDao(httpHelper);
        lng = 1d;
        lat = 1d;
        String hashtag = "#abc";
        text = "some text " + hashtag + " " + System.currentTimeMillis();
        tweet = CreateTweetUtil.createTweet(text, lng, lat);
    }

    @Test
    public void create() throws Exception {
        Tweet ret = dao.create(tweet);
        assertNotNull(ret);
        assertEquals(lng, ret.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, ret.getCoordinates().getCoordinates().get(1));
        assertEquals(text, ret.getText());

        try {
            dao.deleteById(ret.getIdStr());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void findById() throws Exception {
        Tweet ret = dao.create(tweet);

        Tweet fnd = dao.findById(ret.getIdStr());
        assertNotNull(fnd);
        assertEquals(fnd.getIdStr(), ret.getIdStr());
        assertEquals(lng, fnd.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, fnd.getCoordinates().getCoordinates().get(1));
        assertEquals(text, fnd.getText());

        try {
            dao.deleteById(ret.getIdStr());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteById() {
        Tweet ret = dao.create(tweet);

        try {
            Tweet fnd = dao.deleteById(ret.getIdStr());
            assertEquals(lng, fnd.getCoordinates().getCoordinates().get(0));
            assertEquals(lat, fnd.getCoordinates().getCoordinates().get(1));
            assertEquals(text, fnd.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}