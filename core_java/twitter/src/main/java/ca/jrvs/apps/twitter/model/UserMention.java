package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Arrays;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "name",
        "indices",
        "screen_name",
        "id",
        "id_str"
})
public class UserMention {

    @JsonProperty("name")
    private String name;
    @JsonProperty("indices")
    private int[] indices;
    @JsonProperty("screen_name")
    private String screenName;
    @JsonProperty("id")
    private long id;
    @JsonProperty("id_str")
    private String idStr;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("indices")
    public int[] getIndices() {
        return indices;
    }

    @JsonProperty("indices")
    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    @JsonProperty("screen_name")
    public String getScreenName() {
        return screenName;
    }

    @JsonProperty("screen_name")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserMention that = (UserMention) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Arrays.equals(indices, that.indices) &&
                Objects.equals(screenName, that.screenName) &&
                Objects.equals(idStr, that.idStr);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, screenName, id, idStr);
        result = 31 * result + Arrays.hashCode(indices);
        return result;
    }
}
