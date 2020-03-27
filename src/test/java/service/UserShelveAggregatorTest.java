package service;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import model.UserShelve;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import service.processing.ShelveAggregator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class UserShelveAggregatorTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        final List<String> strings = ShelveAggregator.getTopShelves(shelvesToClean);

        System.out.println("Result: " + strings);

        assertThat(strings.size(), is(3));
        assertThat(strings.get(0), is("sci-fi"));
        assertThat(strings.get(1), is("fantasy"));
        assertThat(strings.get(2), is("post-apocalyptic"));
    }

    @Test
    public void dateTest() {
        final String date = "Sat Feb 08 07:28:26 -0800 2020";
        final String[] split = date.split(StringUtils.SPACE);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd hh:mm:ss Z yyyy").withLocale(Locale.US);
        LocalDate.parse(date, formatter);
    }


    //"EEE MMM dd hh:mm:ss yyyy" -without year
//    G 	Era designator (before christ, after christ)
//    y 	Year (e.g. 12 or 2012). Use either yy or yyyy.
//    M 	Month in year. Number of M's determine length of format (e.g. MM, MMM or MMMMM)
//    d 	Day in month. Number of d's determine length of format (e.g. d or dd)
//    h 	Hour of day, 1-12 (AM / PM) (normally hh)
//    H 	Hour of day, 0-23 (normally HH)
//    m 	Minute in hour, 0-59 (normally mm)
//    s 	Second in minute, 0-59 (normally ss)
//    S 	Millisecond in second, 0-999 (normally SSS)
//    E 	Day in week (e.g Monday, Tuesday etc.)
//    D 	Day in year (1-366)
//    F 	Day of week in month (e.g. 1st Thursday of December)
//    w 	Week in year (1-53)
//    W 	Week in month (0-5)
//    a 	AM / PM marker
//    k 	Hour in day (1-24, unlike HH's 0-23)
//            K 	Hour in day, AM / PM (0-11)
//    z 	Time Zone
//' 	Escape for text delimiter
//        ' 	Single quote

}