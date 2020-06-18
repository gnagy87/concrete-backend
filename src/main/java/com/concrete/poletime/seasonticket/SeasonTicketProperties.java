package com.concrete.poletime.seasonticket;

import java.util.HashMap;

public class SeasonTicketProperties {
    public static final HashMap<Integer, Integer> AMOUNT_AND_WEEK = new HashMap<>()
    {{
        put(5, 5);
        put(10, 8);
        put(15, 6);
        put(20, 4);
    }};
}
