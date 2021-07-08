package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TwitterServiceIntTest {

    Service service;
    Tweet posted;
    String text;

    @Before
    public void setUp() throws Exception {
        String CONSUMER_KEY = System.getenv("consumerKey");
        String CONSUMER_SECRET = System.getenv("consumerSecret");
        String ACCESS_TOKEN = System.getenv("accessToken");
        String TOKEN_SECRET = System.getenv("tokenSecret");

        HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET,
                ACCESS_TOKEN, TOKEN_SECRET);

        CrdDao<Tweet, String> dao = new TwitterDao(httpHelper);
        service = new TwitterService(dao);

        String hashtag = "#abc";
        text = "some text " + hashtag + " " + System.currentTimeMillis();
        Tweet valid = CreateTweetUtil.createTweet(text, 33.0, 33.0);
        posted = service.postTweet(valid);


    }

    @Test
    public void postTweet() {
        Double lng = 33.0;
        Double lat = 33.0;
        // check valid
        assertNotNull(posted);
        assertEquals(lng, posted.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, posted.getCoordinates().getCoordinates().get(1));
        assertEquals(text, posted.getText());
    }

    @Test
    public void showTweet() {
        // valid id
        Tweet vT = service.showTweet(posted.getId_str(), new String[]{});
        assertEquals(posted.getId_str(), vT.getId_str());
        Double lng = 33.0;
        Double lat = 33.0;
        // check valid
        assertNotNull(vT);
        assertEquals(lng, vT.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, vT.getCoordinates().getCoordinates().get(1));
        assertEquals(text, vT.getText());
    }

    @Test
    public void deleteTweets() {
        String[] ids = {posted.getId_str()};

        List<Tweet> tweets = service.deleteTweets(ids);
        assertTrue(tweets.size() == 4);
        assertEquals(posted.getId_str(), tweets.get(0).getId_str());
    }
}