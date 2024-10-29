package com.example.product;

import com.example.product.persistence.Reservation;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.*;


public class ReservationPojoTest {
    @Test
    public void create(){
        Reservation reservation = new Reservation("1", "Edwin");
        assertEquals("1", reservation.id());
        assertEquals("Edwin", reservation.name());
        assertTrue("the name is not valid.", isValidName(reservation.name()));
        assertThat(reservation.name(), Matchers.equalToIgnoringCase("edwin"));
        assertThat(reservation.name(), new BaseMatcher<String>() {

            @Override
            public boolean matches(Object o) {
                return o instanceof String && isValidName((String)o);

            }

            @Override
            public void describeTo(Description description) {
                description.appendText("the name should be started with upper case and longer than 4 chars.");
            }
            });
    }

    public boolean isValidName(String name){
        return name != null && name.length() > 4 && Character.isUpperCase(name.charAt(0));
    }
}
