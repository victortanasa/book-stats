package service;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

import java.util.List;

class ShelveCleaner {

    private static final List<String> SHELVES_TO_REMOVE = newArrayList("to-read", "currently-reading", "owned", "default", "favorites",
            "books-i-own", "ebook", "kindle", "library", "audiobook", "owned-books", "audiobooks", "my-books", "ebooks", "to-buy",
            "english", "calibre", "books", "british", "audio", "my-library", "favourites", "re-read", "general", "e-books", "fiction");

    static List<String> cleanShelves(final List<String> shelvesToClean) {
        return shelvesToClean.stream()
                .filter(shelve -> !SHELVES_TO_REMOVE.contains(shelve))
                .collect(toList());
    }

    //    <shelf name="science-fiction" count="623"/>
//    <shelf name="sci-fi" count="461"/>
//    <shelf name="scifi" count="151"/>
//    <shelf name="sf" count="80"/>
//    <shelf name="sci-fi-fantasy" count="19"/>
//    <shelf name="scifi-fantasy" count="19"/>


}