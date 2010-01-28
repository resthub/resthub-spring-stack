package org.resthub.web.tal.helper;

public class BoolHelper {
    public static boolean and( boolean a, boolean b ) {
        return a && b;
    }

    public static boolean or( boolean a, boolean b ) {
        return a || b;
    }

    public static Object cond( boolean b, Object x, Object y ) {
        return b ? x : y;
    }
}
