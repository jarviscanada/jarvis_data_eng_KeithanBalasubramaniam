package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelperTest;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.junit.Assert.*;

public class TwitterServiceIntTest {

    Service service;
    Tweet posted;
    String text;

    @Before
    public void setUp() throws Exception {
        String cKey = System.getenv("consumerKey");
        String cSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        HttpHelper httpHelper = new TwitterHttpHelper(cKey, cSecret, accessToken, tokenSecret);

        CrdDao dao = new TwitterDao(httpHelper);
        service = new TwitterService(dao);

        String hashtag = "#abc";
        text = "some text " + hashtag + " " + System.currentTimeMillis();
        Tweet valid = CreateTweetUtil.createTweet(text, 33d, 33d);
        posted = service.postTweet(valid);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void postTweet() {
        Double lng = 33d;
        Double lat = 33d;
        // check valid
        assertNotNull(posted);
        assertEquals(lng, posted.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, posted.getCoordinates().getCoordinates().get(1));
        assertEquals(text, posted.getText());
    }

    @Test
    public void showTweet() {
        // valid id
        Tweet vT = service.showTweet(posted.getIdStr(), new String[]{});
        assertEquals(posted.getIdStr(), vT.getIdStr());
        Double lng = 33d;
        Double lat = 33d;
        // check valid
        assertNotNull(vT);
        assertEquals(lng, vT.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, vT.getCoordinates().getCoordinates().get(1));
        assertEquals(text, vT.getText());
    }

    @Test
    public void deleteTweets() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("The id of the tweet is invalid");
        String[] invalidIds = {"1408425639410126858", "1408425467728826376", "00a"};

        service.deleteTweets(invalidIds);

        String[] validIds = {"1408425639410126858", "1408425467728826376", "1408406943950151684"};
        List<Tweet> deletedTweets = service.deleteTweets(validIds);

        assertEquals(3, deletedTweets.size());
        assertNotNull(deletedTweets);
    }
}