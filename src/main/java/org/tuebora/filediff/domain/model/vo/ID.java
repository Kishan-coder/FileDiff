package org.tuebora.filediff.domain.model.vo;

import lombok.Getter;
import org.tuebora.filediff.domain.exception.InvalidUserRecordException;

@Getter
public class ID {
    private String value;

    public ID(String value) {
        validateId(value);
        this.value = value;
    }

    private static void validateId(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidUserRecordException("ID must not be null or empty: " + id);
        }
        if (id.length() != 6) {
            throw new InvalidUserRecordException("ID must be 6 characters long: " + id);
        }
        for (int i = 0; i < 4; i++) {
            if (!Character.isUpperCase(id.charAt(i))) {
                throw new InvalidUserRecordException("Invalid InputUserRecord ID: " + id);
            }
        }
        for (int i = 4; i < 6; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                throw new InvalidUserRecordException("Invalid InputUserRecord ID: " + id);
            }
        }
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ID otherId && value.equals(otherId.value);
    }
}
