package org.resthub.web.tal.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class DateHelper {
    static final Map dateFormats = new TreeMap();
    
    public static final String format( String format, Date date ) {
        SimpleDateFormat dateFormat = (SimpleDateFormat)dateFormats.get( format );
        if ( dateFormat == null ) {
            dateFormat = new SimpleDateFormat( format );
            dateFormats.put( format, dateFormat );
        }
        return dateFormat.format( date );
    }
}

