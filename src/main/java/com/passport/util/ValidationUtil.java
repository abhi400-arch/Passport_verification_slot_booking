package com.passport.util;

import com.passport.exception.ValidationException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PASSPORT =
            Pattern.compile("^[A-Za-z][0-9A-Za-z]{7,19}$");

    private ValidationUtil() {}

    public static void validateRegistration(String name, String email, String phone, String password)
            throws ValidationException {
        if (name == null || name.trim().length() < 3) throw new ValidationException("Name must contain at least 3 characters.");
        if (email == null || !EMAIL.matcher(email.trim()).matches()) throw new ValidationException("Enter a valid email address.");
        if (phone == null || !phone.matches("\\d{10,15}")) throw new ValidationException("Phone must contain 10 to 15 digits.");
        if (password == null || password.length() < 6) throw new ValidationException("Password must contain at least 6 characters.");
    }

    public static void validatePassport(String number, LocalDate dob, String nationality, String address, String type)
            throws ValidationException {
        if (number == null || !PASSPORT.matcher(number.trim().toUpperCase()).matches())
            throw new ValidationException("Passport number must start with a letter and contain 8-20 alphanumeric characters.");
        if (dob == null || !dob.isBefore(LocalDate.now().minusYears(18))) 
            throw new ValidationException("Applicant must be at least 18 years old for this demo.");
        if (nationality == null || nationality.isBlank()) throw new ValidationException("Nationality is required.");
        if (address == null || address.trim().length() < 10) throw new ValidationException("Please enter a complete address.");
        if (!"NEW".equals(type) && !"RENEWAL".equals(type)) throw new ValidationException("Invalid application type.");
    }

    public static void validateSlot(LocalDate date, java.time.LocalTime start, java.time.LocalTime end, int capacity)
            throws ValidationException {
        if (date == null || !date.isAfter(LocalDate.now())) throw new ValidationException("Slot date must be a future date.");
        if (start == null || end == null || !end.isAfter(start)) throw new ValidationException("End time must be after start time.");
        if (capacity < 1 || capacity > 100) throw new ValidationException("Capacity must be between 1 and 100.");
    }
}
