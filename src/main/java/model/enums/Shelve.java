package model.enums;

public enum Shelve {

    READ("read"),
    FAVORITES("favorites"),
    FAVOURITES("favourites"),
    ABANDONED("abandoned"),
    DNF("dnf");

    private String name;

    Shelve(final String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }
}