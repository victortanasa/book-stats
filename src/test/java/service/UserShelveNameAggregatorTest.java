package service;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import model.UserShelve;
import org.junit.Test;
import service.processing.ShelveAggregator;

import java.util.List;

public class UserShelveNameAggregatorTest {

    private static final ShelveAggregator SHELVE_AGGREGATOR = new ShelveAggregator();

    @Test
    public void clean() {
        final List<UserShelve> shelvesToClean = newArrayList(
                new UserShelve("currently-reading", 2000),
                new UserShelve("science-fiction", 4500),
                new UserShelve("scifi", 3200),
                new UserShelve("sf", 300),
                new UserShelve("sci-fi", 4600),
                new UserShelve("fantasy", 2000),
                new UserShelve("post-apocalyptic", 1000));

        final List<String> strings = SHELVE_AGGREGATOR.getTopShelves(shelvesToClean);

        System.out.println("Result: " + strings);

        assertThat(strings.size(), is(3));
        assertThat(strings.get(0), is("sci-fi"));
        assertThat(strings.get(1), is("fantasy"));
        assertThat(strings.get(2), is("post-apocalyptic"));
    }

}