package service;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import model.Shelve;
import org.junit.Test;
import service.processing.ShelveAggregator;

import java.util.List;

public class ShelveNameAggregatorTest {

    private static final ShelveAggregator SHELVE_AGGREGATOR = new ShelveAggregator();

    @Test
    public void clean() {
        final List<Shelve> shelvesToClean = newArrayList(
                new Shelve("currently-reading", 2000),
                new Shelve("science-fiction", 4500),
                new Shelve("scifi", 3200),
                new Shelve("sf", 300),
                new Shelve("sci-fi", 4600),
                new Shelve("fantasy", 2000),
                new Shelve("post-apocalyptic", 1000));

        final List<String> strings = SHELVE_AGGREGATOR.getTopShelves(shelvesToClean);

        System.out.println("Result: " + strings);

        assertThat(strings.size(), is(3));
        assertThat(strings.get(0), is("sci-fi"));
        assertThat(strings.get(1), is("fantasy"));
        assertThat(strings.get(2), is("post-apocalyptic"));
    }

}