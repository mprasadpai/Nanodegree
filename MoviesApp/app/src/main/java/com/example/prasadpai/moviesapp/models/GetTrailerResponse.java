package com.example.prasadpai.moviesapp.models;

/**
 * Created by prasadpai on 26/02/16.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "results",
})
public class GetTrailerResponse {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("results")
    private List<Trailer> results = new ArrayList<>();
    @JsonProperty("total_results")
    private Integer totalResults;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The results
     */
    @JsonProperty("results")
    public List<Trailer> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    @JsonProperty("results")
    public void setResults(List<Trailer> results) {
        this.results = results;
    }

    /**
     * @return The totalResults
     */
    @JsonProperty("total_results")
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The total_results
     */
    @JsonProperty("total_results")
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }



}
