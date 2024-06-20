package org.tuebora.filediff.domain.model.vo;

import org.tuebora.filediff.domain.exception.InvalidUserRecordException;

import java.util.regex.Pattern;

public record Email(String address) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+\\..+$");

    public Email {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new InvalidUserRecordException("Invalid email address: " + address);
        }
    }
}
