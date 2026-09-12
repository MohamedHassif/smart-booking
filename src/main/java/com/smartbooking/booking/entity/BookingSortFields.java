package com.smartbooking.booking.entity;

import java.util.Set;

public final class BookingSortFields {

    private BookingSortFields(){

    }

public static final Set<String> ALLOWED_FIELDS = Set.of(
        "id",
        "checkInDate",
        "checkOutDate",
        "status",
        "createdAt"
);
}
