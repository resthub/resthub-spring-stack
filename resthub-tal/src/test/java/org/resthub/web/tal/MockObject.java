package org.resthub.web.tal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MockObject extends MockObjectSuperClass {
    List people;
    String[] animals = {
        "horse", 
        "dog",
        "cat",
        "pig",
        "crocodile"
    };
    
    int[][] table = {
        { 1, 2, 3, 4 },
        { 5, 6, 7, 8 },
        { 9, 10 }
    };
    
    MockObject() {
        people = new ArrayList(5);
        people.add( "Chris" );
        people.add( "Karen" );
        people.add( "Mike" );
        people.add( "Marsha" );
        people.add( "Christiane" );
    }

    public List getPeople() {
        return people;
    }

    public String[] getAnimals() {
        return animals;
    }

    public int[][] getTable() {
        return table;
    }
    
    public String getFavoriteColor() {
        return "red";
    }

    public boolean isGoodLooking() {
        return true;
    }

    public boolean isDumb() {
        return false;
    }

    public int add( int x, int y ) {
        return x + y;
    }

    public int multiply( int x, int y ) {
        return x * y;
    }

    private final MockObject2 friend = new MockObject2();
    
    public MockObject2 getFriend() {
        return friend;
    }

    public MockObject2 getEnemy() {
        return null;
    }

    public String getDiatribe() {
        return "<b>The cabinet</b> has <i>usurped</i> the authority of the <h3>president</h3><p>";
    }

    public String getDiatribe2() {
        return "<b>The cabinet</b> has <i>usurped</i> the authority of the <h3>president</h3><p/>";
    }
    
    public HTMLFragment getDiatribe3() throws Exception {
        return new HTMLFragment( 
            "<b>The cabinet</b> has <i>usurped</i> the authority of the <h3>president</h3><p>" 
        );
    }
    
    public List getNumbers() {
        List numbers = new ArrayList(100);
        for ( int i = 100; i > 0; i-- ) {
            numbers.add( new Integer( i ) );
        }
        return numbers;
    }
    
    public Date getBirthday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set( 1975, 6, 7, 7, 57, 23 );
        return calendar.getTime();
    }
}
