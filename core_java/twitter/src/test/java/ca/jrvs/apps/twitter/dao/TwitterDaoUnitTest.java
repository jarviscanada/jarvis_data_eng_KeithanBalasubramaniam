package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import ca.jrvs.apps.twitter.util.TwitterJsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

    @Mock
    HttpHelper mockHelper;

    @InjectMocks
    TwitterDao dao;

    Tweet expectedTweet;
    String tweetJsonStr;

    @Before
    public void setUp() throws Exception {
        tweetJsonStr = "{\n"
                + "   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n"
                + "   \"id\":1097607853932564480,\n"
                + "   \"id_str\":\"1097607853932564480\",\n"
                + "   \"text\":\"test with loc223\",\n"
                + "   \"entities\":{\n"
                + "      \"hashtags\":[],"
                + "      \"user_mentions\":[]"
                + "   },\n"
                + "   \"coordinates\":null,"
                + "   \"retweet_count\":0,\n"
                + "   \"favorite_count\":0,\n"
                + "   \"favorited\":false,\n"
                + "   \"retweeted\":false\n"
                + "}";

        expectedTweet = TwitterJsonParser.toObjectFromJson(tweetJsonStr, Tweet.class);
    }

    @Test
    public void create() {
        //test failed request
        String hashTag = "#abc";
        //adding timestamp as twitter doesn't allow duplicate tweets
        String text = "your tweet" + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        //exception is expected here
        when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            dao.create(CreateTweetUtil.createTweet(text, lon, lat));
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }
        //Test happy path
        //however, we don't want to call parseResponseBody.
        //we will make a spyDao which can fake parseResponseBody return value

        when(mockHelper.httpPost(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        //mock parseResponseBody
        doReturn(expectedTweet).when(spyDao).parseData(any(), anyInt());

        Tweet tweet = spyDao.create(CreateTweetUtil.createTweet(text, lon, lat));
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
    }

    @Test
    public void findById() {
        when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock"));
        try {
            dao.findById("");
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        verify(mockHelper).httpGet(isNotNull());

        when(mockHelper.httpGet(isNotNull())).thenReturn(null);
        TwitterDao spyDao = Mockito.spy(dao);
        //mock parseResponseBody
        doReturn(expectedTweet).when(spyDao).parseData(any(), anyInt());

        Tweet tweet = spyDao.findById(expectedTweet.getId_str());
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
    }

    @Test
    public void deleteById() {

        TwitterDao spyDao = Mockito.spy(dao);
        //mock parseResponseBody
        doReturn(expectedTweet).when(spyDao).parseData(any(), anyInt());

        Tweet tweet = spyDao.deleteById(expectedTweet.getId_str());
        assertNotNull(tweet);
        assertNotNull(tweet.getText());
    }
}