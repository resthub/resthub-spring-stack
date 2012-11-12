package org.resthub.web.log;

import java.util.ArrayList;
import java.util.Collection;

public class Logs extends ArrayList<Log> {

    public Logs(int initialCapacity) {
        super(initialCapacity);
    }

    public Logs() {
    }

    public Logs(Collection<? extends Log> c) {
        super(c);
    }
}
