package org.resthub.web.log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: JRI <julien.ripault@atos.net>
 * Date: 07/11/12
 */
public class Logs extends ArrayList<LogDTO> {

    public Logs(int initialCapacity) {
        super(initialCapacity);
    }

    public Logs() {
    }

    public Logs(Collection<? extends LogDTO> c) {
        super(c);
    }
}
