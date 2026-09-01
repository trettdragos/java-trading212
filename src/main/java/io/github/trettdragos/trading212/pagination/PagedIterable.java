package io.github.trettdragos.trading212.pagination;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Lazily walks a cursor-paginated Trading212 endpoint, fetching each subsequent page (50 items
 * at a time) only as the caller consumes items. A single instance can be iterated or streamed
 * once; each traversal starts a fresh sequence of requests.
 */
public final class PagedIterable<T> implements Iterable<T> {

    private final String firstPath;
    private final Function<String, Page<T>> fetchPage;

    public PagedIterable(String firstPath, Function<String, Page<T>> fetchPage) {
        this.firstPath = firstPath;
        this.fetchPage = fetchPage;
    }

    @Override
    public Iterator<T> iterator() {
        return new PageIterator();
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    private final class PageIterator implements Iterator<T> {
        private String nextPath = firstPath;
        private Iterator<T> current = Collections.emptyIterator();

        @Override
        public boolean hasNext() {
            while (!current.hasNext() && nextPath != null) {
                Page<T> page = fetchPage.apply(nextPath);
                current = page.items().iterator();
                // A handful of Trading212 endpoints echo back a nextPagePath containing the
                // literal string "null" for an exhausted cursor instead of returning null outright.
                nextPath = (page.nextPagePath() != null && !page.nextPagePath().contains("null"))
                        ? page.nextPagePath()
                        : null;
            }
            return current.hasNext();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return current.next();
        }
    }
}
