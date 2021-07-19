package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {
    @Mock
    TwitterService service;

    @InjectMocks
    TwitterController controller;

    @Test
    public void postTweet() {
        Tweet tweet;
        String text = "some text";
        Double lat = 1.0d;
        Double lon = 1.0d;
        when(service.postTweet(any())).thenReturn(CreateTweetUtil.createTweet(text, lat, lon));
        String[] args = {"post", text, lat + ":" + lon};
        tweet = controller.postTweet(args);
        assertNotNull(tweet);

        try {
            String[] invalidArgs = {"post", text};
            tweet = controller.postTweet(invalidArgs);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        try {
            String[] invalidArgs = {"post", text, "1.0"};
            tweet = controller.postTweet(invalidArgs);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

        try {
            String[] invalidArgs = {"post", text, "hey:hey"};
            tweet = controller.postTweet(invalidArgs);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }

    }

    @Test
    public void showTweet() {
        Tweet tweet;
        when(service.showTweet(any(), any())).thenReturn(new Tweet());
        String[] args = {"show", "123456789"};
        tweet = controller.showTweet(args);
        assertNotNull(tweet);

        try {
            String[] invalidArgs = {"show"};
            tweet = controller.showTweet(invalidArgs);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteTweet() {
        when(service.deleteTweets(any())).thenReturn(Arrays.asList(new Tweet()));
        String[] args = {"delete", "123456789"};
        List<Tweet> deletedTweets =  controller.deleteTweet(args);
        assertNotNull(deletedTweets);

        try {
            String[] invalidArgs = {"delete"};
            deletedTweets = controller.deleteTweet(invalidArgs);
            fail();
        } catch (RuntimeException ex) {
            assertTrue(true);
        }
    }
}