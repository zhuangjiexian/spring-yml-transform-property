package com.elndu.transform.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author elndu
 * @version 1.0, 2020/9/18 16:40
 * @since JDK 1.8
 */
public class PropertySource
{
    private String name;
    private Map<?, ?> source;

    @JsonCreator
    public PropertySource(@JsonProperty("name") String name, @JsonProperty("source") Map<?, ?> source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return this.name;
    }

    public Map<?, ?> getSource() {
        return this.source;
    }

    public String toString() {
        return "PropertySource [name=" + this.name + "]";
    }
}
