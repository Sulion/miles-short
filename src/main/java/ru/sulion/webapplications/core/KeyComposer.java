package ru.sulion.webapplications.core;

import com.google.inject.Inject;
import org.mapdb.Atomic;
import org.mapdb.DB;
import ru.sulion.webapplications.api.Redirect;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by sulion on 27.08.16.
 */
public class KeyComposer {

    public static final String HASH_SEQ = "HASH_SEQ";
    private static final String ALPHABET;
    private final DB db;

    static {
        StringBuilder builder = new StringBuilder(62);
        builder.append(collectCharRange('0', '9'));
        builder.append(collectCharRange('a', 'z'));
        builder.append(collectCharRange('A', 'Z'));
        ALPHABET = builder.toString();
    }

    @Inject
    public KeyComposer(DB db) {
        this.db = db;
        if(this.db != null ) {
            Atomic.Long start = this.db.atomicLong(HASH_SEQ).createOrOpen();
            start.compareAndSet(0, System.currentTimeMillis() % 1000000000L / 100L * 62 * 62);
            //Theoretically, we could've started from the number
            // one, but that would have given us dull and boring urls
        }
    }

    private static String collectCharRange(char start, char end) {
        return IntStream.rangeClosed(start, end)
                .mapToObj(c -> "" + (char) c).collect(Collectors.joining());
    }

    public static final int SHORT_URL_SIZE = 6;

    public String toShortURL(String fullUrl) {
        long current = db.atomicLong(HASH_SEQ).createOrOpen().incrementAndGet();
        return toBase62(current);
    }


    public String toBase62(long current) {
        long tmp = current;
        StringBuilder builder = new StringBuilder();

        while (tmp > 0) {
            builder.append(ALPHABET.charAt((int) (tmp % ALPHABET.length())));
            tmp /= ALPHABET.length();
        }
        return builder.reverse().toString();
    }

    protected long fromBase62(String encoded) {
        char[] chars = encoded.toCharArray();
        long sum = 0;
        long mult = 1;
        for (int i = 1; i <= chars.length; i++) {
            char c = chars[chars.length - i];
            int value = ALPHABET.indexOf(c);
            sum += value * mult;
            mult *= ALPHABET.length();
        }
        return sum;
    }

    public String toStatsKey(Redirect redirect) {
        return redirect.getShortUrl() + redirect.getLocation().toString();
    }
    public String removePrefix(String statsKey) {
        return statsKey.substring(SHORT_URL_SIZE);
    }


}
