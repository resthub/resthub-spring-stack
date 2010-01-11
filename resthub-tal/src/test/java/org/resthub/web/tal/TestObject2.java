package org.resthub.web.tal;

import java.util.HashMap;
import java.util.Map;

public class TestObject2 {
    Map map = new HashMap();

    TestObject2() {
        map.put( "friend", "kevin" );
        map.put( "enemy", "mc2" );
        map.put( "hello", new Long(99l) );
    }

    public int getNumber() {
        return 9;
    }

    public long getZero() {
        return 0l;
    }

    public Map getMap() {
        return map;
    }

    public String toString() {
        return "Albert";
    }
}


