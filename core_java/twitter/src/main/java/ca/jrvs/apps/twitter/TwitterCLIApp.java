package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.TwitterJsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TwitterCLIApp {

    private Controller controller;

    @Autowired
    public TwitterCLIApp(Controller controller){
        this.controller = controller;
    }

    public static void main(String[] args) {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");

        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
                tokenSecret);
        CrdDao dao = new TwitterDao(httpHelper);
        Service service = new TwitterService(dao);
        Controller controller = new TwitterController(service);
        TwitterCLIApp app = new TwitterCLIApp(controller);

        app.run(args);
    }

    public void run(String[] args){
        if (args.length < 2) {
            throw new IllegalArgumentException("USAGE: TwitterCLIApp post|show|delete [options]");
        }
        switch (args[0]) {
            case "post":
                Tweet postedTweet = controller.postTweet(args);
                printTweet(postedTweet);
                break;
            case "show":
                Tweet fetchedTweet = controller.showTweet(args);
                printTweet(fetchedTweet);
                break;
            case "delete":
                List<Tweet> deletedTweets = controller.deleteTweet(args);
                deletedTweets.forEach(t -> printTweet(t));
                break;
            default:
                throw new IllegalArgumentException("USAGE: TwitterCLIApp post|show|delete [options]");
        }
    }

    public void printTweet(Tweet tweet){
        try{
            System.out.println(TwitterJsonParser.toJson(tweet, true, false));
        }catch(JsonProcessingException ex){
            throw new RuntimeException("Error: Cannot convert tweet to Json", ex);
        }
    }
}
