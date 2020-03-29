package model.enums;

public enum ShelveName {

    READ("read"),
    FAVORITES("favorites"),
    FAVOURITES("favourites"),
    ABANDONED("abandoned"),
    DNF("dnf");

    private String name;

    ShelveName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }
}