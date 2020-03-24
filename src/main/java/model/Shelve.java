package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Shelve {

    private String name;

    private Integer popularity;

    public Shelve(final String name, final Integer popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public Integer getPopularity() {
        return popularity;
    }

    @Override
    public boolean equals(final Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}