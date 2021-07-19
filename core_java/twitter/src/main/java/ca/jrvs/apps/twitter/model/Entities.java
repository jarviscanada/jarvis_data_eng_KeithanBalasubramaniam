package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "hashtags",
        "user_mentions"
})
public class Entities {

    @JsonProperty("hashtags")
    private List<Hashtag> hashtags = null;
    @JsonProperty("user_mentions")
    private List<UserMention> userMentions = null;

    @JsonProperty("hashtags")
    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    @JsonProperty("hashtags")
    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    @JsonProperty("user_mentions")
    public List<UserMention> getUserMentions() {
        return userMentions;
    }

    @JsonProperty("user_mentions")
    public void setUserMentions(List<UserMention> userMentions) {
        this.userMentions = userMentions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entities entities = (Entities) o;
        return Objects.equals(hashtags, entities.hashtags) &&
                Objects.equals(userMentions, entities.userMentions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashtags, userMentions);
    }
}
