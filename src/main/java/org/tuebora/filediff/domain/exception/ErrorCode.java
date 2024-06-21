package org.tuebora.filediff.domain.exception;

public enum ErrorCode {
    INVALID_USER_RECORD("Invalid User Record Encountered", 600),
    DUPLICATE_USER_RECORD("Duplicate User Record Encountered as Employee Id Matches in File: %s", 601),
    UNMODIFIED_USER_RECORD("User Record remains same in both Sources", 602),
    INCOMPATIBLE_SOURCES("Found Incompatible Input Source(s)", 603);
    public final String message;
    public final int code;

    ErrorCode(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
