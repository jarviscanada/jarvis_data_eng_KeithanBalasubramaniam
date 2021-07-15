package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "created_at",
        "id",
        "id_str",
        "text",
        "entities",
        "coordinates",
        "retweet_count",
        "favourite_count",
        "favorited",
        "retweeted"
})
public class Tweet {

    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("id")
    private long id;
    @JsonProperty("id_str")
    private String idStr;
    @JsonProperty("text")
    private String text;
    @JsonProperty("entities")
    private Entities entities;
    @JsonProperty("coordinates")
    private Coordinates coordinates;
    @JsonProperty("retweet_count")
    private int retweetCount;
    @JsonProperty("favorite_count")
    private int favoriteCount;
    @JsonProperty("favorited")
    private boolean favorited;
    @JsonProperty("retweeted")
    private boolean retweeted;

    @JsonProperty("created_at")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("created_at")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("id_str")
    public String getIdStr() {
        return idStr;
    }

    @JsonProperty("id_str")
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("entities")
    public Entities getEntities() {
        return entities;
    }

    @JsonProperty("entities")
    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    @JsonProperty("coordinates")
    public Coordinates getCoordinates() {
        return coordinates;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @JsonProperty("retweet_count")
    public int getRetweetCount() {
        return retweetCount;
    }

    @JsonProperty("retweet_count")
    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    @JsonProperty("favorite_count")
    public int getFavoriteCount() {
        return favoriteCount;
    }

    @JsonProperty("favorite_count")
    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    @JsonProperty("favorited")
    public boolean isFavorited() {
        return favorited;
    }

    @JsonProperty("favorited")
    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    @JsonProperty("retweeted")
    public boolean isRetweeted() {
        return retweeted;
    }

    @JsonProperty("retweeted")
    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tweet tweet = (Tweet) o;
        return id == tweet.id &&
                retweetCount == tweet.retweetCount &&
                favoriteCount == tweet.favoriteCount &&
                favorited == tweet.favorited &&
                retweeted == tweet.retweeted &&
                Objects.equals(createdAt, tweet.createdAt) &&
                Objects.equals(idStr, tweet.idStr) &&
                Objects.equals(text, tweet.text) &&
                Objects.equals(entities, tweet.entities) &&
                Objects.equals(coordinates, tweet.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(createdAt, id, idStr, text, entities, coordinates, retweetCount, favoriteCount,
                        favorited, retweeted);
    }
}
