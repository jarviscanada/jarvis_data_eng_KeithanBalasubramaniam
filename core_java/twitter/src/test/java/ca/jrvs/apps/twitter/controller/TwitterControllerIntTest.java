package ca.jrvs.apps.twitter.controller;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelperTest;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterControllerIntTest {

    static final Logger logger = LoggerFactory.getLogger(TwitterHttpHelperTest.class);
    TwitterDao twitterDao;
    Tweet newTweet;
    TwitterService twitterService;
    TwitterController controller;
    Tweet tweet;

    @Before
    public void setup() {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        logger.debug(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
                tokenSecret);
        CrdDao dao = new TwitterDao(httpHelper);
        twitterService = new TwitterService(dao);
        controller = new TwitterController(twitterService);

        String hashtag = "#canada";
        String text = "Helloworld! " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        tweet = CreateTweetUtil.createTweet(text, lat, lon);
        tweet = twitterService.postTweet(tweet);
    }

    @After
    public void tearDown() {
        try {
            String[] tweets = new String[] {tweet.getIdStr()};
            twitterService.deleteTweets(tweets);
        } catch (RuntimeException ex) {
            logger.debug("Tweet was already deleted");
        }
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void postTweet() {
        String hashtag = "#canada";
        String text = "Helloworld! " + hashtag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        Tweet tweet = CreateTweetUtil.createTweet(text, lat, lon);

        String[] args = new String[]{"post",text,lat + ":" + lon};
        newTweet = controller.postTweet(args);

        assertNotNull(newTweet.getCoordinates());
        assertEquals(tweet.getText(), newTweet.getText());
        assertEquals(tweet.getCoordinates().getCoordinates().get(0), newTweet.getCoordinates().getCoordinates().get(0));
        assertEquals(tweet.getCoordinates().getCoordinates().get(1), newTweet.getCoordinates().getCoordinates().get(1));
    }

    @Test
    public void showTweet() {
        String[] args = new String[]{"show",tweet.getIdStr(), "id,text,retweet_count"};
        newTweet = controller.showTweet(args);

        assertEquals(tweet.getIdStr(), newTweet.getIdStr());
        assertEquals(tweet.getText(), newTweet.getText());
        assertEquals(tweet.getRetweetCount(), newTweet.getRetweetCount());
    }

    @Test
    public void deleteTweet() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("The id of the tweet is invalid");

        String[] invalidIds = {"1408425639410126858", "1408425467728826376", "00a"};

        twitterService.deleteTweets(invalidIds);

        String[] validIds = {"1408425639410126858", "1408425467728826376", "1408406943950151684"};
        List<Tweet> deletedTweets = twitterService.deleteTweets(validIds);

        assertEquals(3, deletedTweets.size());
        assertNotNull(deletedTweets);
    }
}