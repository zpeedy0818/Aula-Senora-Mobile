package co.edu.aulasenora.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RandomUserResponse {
    
    @SerializedName("results")
    private List<User> results;

    public List<User> getResults() {
        return results;
    }

    public void setResults(List<User> results) {
        this.results = results;
    }
}
