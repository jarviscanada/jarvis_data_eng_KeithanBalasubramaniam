package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Service
public class TwitterService implements Service{

    private CrdDao dao;

    @Autowired
    public TwitterService(CrdDao dao)
    {
        this.dao = dao;
    }

    /**
     * Validate and post a user input Tweet
     *
     * @param tweet tweet to be created
     * @return created tweet
     * @throws IllegalArgumentException if text exceed max number of allowed characters or lat/long out of range
     */
    @Override
    public Tweet postTweet(Tweet tweet) {
        validatePostTweet(tweet);
        return (Tweet) dao.create(tweet);
    }

    /**
     * Search a tweet by ID
     *
     * @param id     tweet id
     * @param fields set fields not in the list to null
     * @return Tweet object which is returned by the Twitter API
     * @throws IllegalArgumentException if id or fields param is invalid
     */
    @Override
    public Tweet showTweet(String id, String[] fields) {
        validateTweetId(id);
        return (Tweet) dao.findById(id);
    }

    /**
     * Delete Tweet(s) by id(s).
     *
     * @param ids tweet IDs which will be deleted
     * @return A list of Tweets
     * @throws IllegalArgumentException if one of the IDs is invalid.
     */
    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        Arrays.stream(ids).forEach(this::validateTweetId);
        List<Tweet> tweets = new ArrayList<>();
        Arrays.stream(ids).forEach(id -> tweets.add((Tweet) dao.deleteById(id)));
        return tweets;
    }

    private void validatePostTweet(Tweet tweet){
        String tweet_text = tweet.getText();
        Double longitude = tweet.getCoordinates().getCoordinates().get(0);
        Double latitude = tweet.getCoordinates().getCoordinates().get(1);

        if (tweet_text.length() > 140){
            throw new IllegalArgumentException("Tweet Text must be 140 characters or less");
        } else if (longitude > 180 || longitude < -180){
            throw new IllegalArgumentException("The longitude is out of range, must be between [-180,180]");
        } else if (latitude > 90 || latitude < -90){
            throw new IllegalArgumentException("The longitude is out of range, must be between [-90,90]");
        }
    }

    private void validateTweetId(String id){
        try{
            Long.parseLong(id);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("The id of the tweet is invalid");
        }
    }

}
