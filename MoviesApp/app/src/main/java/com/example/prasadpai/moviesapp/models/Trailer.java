package com.example.prasadpai.moviesapp.models;

import java.io.Serializable;

/**
 * Created by prasadpai on 25/02/16.
 */

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "iso_639_1",
        "key",
        "name",
        "site",
        "size",
        "type"
})
public class Trailer {

    @JsonProperty("id")
    private String id;
    @JsonProperty("iso_639_1")
    private String iso6391;
    @JsonProperty("key")
    private String key;
    @JsonProperty("name")
    private String name;
    @JsonProperty("site")
    private String site;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The iso6391
     */
    @JsonProperty("iso_639_1")
    public String getIso6391() {
        return iso6391;
    }

    /**
     *
     * @param iso6391
     * The iso_639_1
     */
    @JsonProperty("iso_639_1")
    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    /**
     *
     * @return
     * The key
     */
    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     * The key
     */
    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The site
     */
    @JsonProperty("site")
    public String getSite() {
        return site;
    }

    /**
     *
     * @param site
     * The site
     */
    @JsonProperty("site")
    public void setSite(String site) {
        this.site = site;
    }

    /**
     *
     * @return
     * The size
     */
    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    /**
     *
     * @param size
     * The size
     */
    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     *
     * @return
     * The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}