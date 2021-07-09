package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.CreateTweetUtil;
import java.util.List;

public class TwitterController implements Controller{

    private static final String COORD_SEP = ":";
    private static final String COMMA = ",";
    private Service service;

    public TwitterController(Service service){
        this.service = service;
    }


    /**
     * Parse user argument and post a tweet by calling service classes
     *
     * @param args
     * @return a posted tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet postTweet(String[] args) {
        if (args.length != 3){
            throw new IllegalArgumentException("Invalid Number of Arguments");
        }

        String text = args[1];
        String coords = args[2];
        String[] corr = coords.split(COORD_SEP);

        if (corr.length < 2){
            throw new IllegalArgumentException("Invalid Number of Coordinates for tweet");
        }

        Double lng;
        Double lat;

        try{
            lng = Double.parseDouble(corr[0]);
            lat = Double.parseDouble(corr[1]);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid Input: Error parsing coordinates");
        }

        Tweet tweet = CreateTweetUtil.createTweet(text, lng, lat);
        return service.postTweet(tweet);
    }

    /**
     * Parse user argument and search a tweet by calling service classes
     *
     * @param args
     * @return a tweet
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public Tweet showTweet(String[] args) {
        if (args.length < 2){
            throw new IllegalArgumentException("Invalid Number of Arguments");
        }

        String[] fields = args.length > 2 ? args[2].split(COMMA) : null;
        return service.showTweet(args[1], fields);
    }

    /**
     * Parse user argument and delete tweets by calling service classes
     *
     * @param args
     * @return a list of deleted tweets
     * @throws IllegalArgumentException if args are invalid
     */
    @Override
    public List<Tweet> deleteTweet(String[] args) {
        if (args.length < 2){
            throw new IllegalArgumentException("Invalid Number of Arguments");
        }

        String[] ids = args[1].split(COMMA);
        return service.deleteTweets(ids);
    }
}
