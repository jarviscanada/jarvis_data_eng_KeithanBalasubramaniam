package ca.jrvs.apps.twitter.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelperTest;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

    static final Logger logger = LoggerFactory.getLogger(TwitterHttpHelperTest.class);

    @Mock
    CrdDao dao;

    @InjectMocks
    TwitterService service;

    Double lng = 50d;
    Double lat = 0d;

    @Test
    public void postTweet() {

        when(dao.create(any(Tweet.class)))
                .thenReturn(CreateTweetUtil.createTweet("This is a test", lng, lat));

        // latitude out of range
        Tweet tweetLat = CreateTweetUtil.createTweet("This is a test", 45.0, -91.0);
        try {
            service.postTweet(tweetLat);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
        // longitude out of range
        Tweet tweetLong = CreateTweetUtil.createTweet("This is a test", 181.0, 32.0);
        try {
            service.postTweet(tweetLong);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
        // text characters out of range
        String text = "this tweet is too long for twitter to handle, this test our ability to hand tweets over the character limit that we set. " +
                "testtesttesttesttesttesttesttesttesttesttesttesttestesttesttesttesttesttesttesttestttesttesttesttesttesttestttesttesttesttestst" +
                "etesttesttestteststststststststs";
        Tweet tweetText = CreateTweetUtil.createTweet(text, 33.0, 33.0);
        try {
            service.postTweet(tweetText);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }

        Tweet newTweet = service.postTweet(CreateTweetUtil.createTweet("test", lng, lat));
        assertEquals(lng, newTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, newTweet.getCoordinates().getCoordinates().get(1));
        assertEquals("This is a test", newTweet.getText());
    }

    @Test
    public void showTweet() {
        Tweet testTweet = CreateTweetUtil.createTweet("test", lng, lat);
        when(dao.findById(anyString())).thenReturn(testTweet);

        // wrong id
        try {
            service.showTweet("29293229383747920g", new String[]{});
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
        Tweet tweet = service.showTweet("1375498033895444489", new String[]{});
        assertEquals(lng, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
        assertEquals("test", tweet.getText());
    }

    @Test
    public void deleteTweets() {
        when(dao.deleteById(any())).thenReturn(CreateTweetUtil.createTweet("test", lng, lat));

        String[] invalidIds = {"29293229383747920g"};
        String[] ids = {"1375169319819218951", "1375168793782128641",
                "1375166867111878660"};
        try {
            service.deleteTweets(invalidIds);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
        List<Tweet> tweets = service.deleteTweets(ids);
        tweets.stream().forEach(t -> {
            assertEquals(lng, t.getCoordinates().getCoordinates().get(0));
            assertEquals(lat, t.getCoordinates().getCoordinates().get(1));
            assertEquals("test", t.getText());
        });
    }
}