package org.tuebora.filediff.domain.exception;

public class InvalidUserRecordException extends AbstractFileDiffException{
    public InvalidUserRecordException() {
        super(ErrorCode.INVALID_USER_RECORD.message, ErrorCode.INVALID_USER_RECORD.code);
    }
    public InvalidUserRecordException(String message) {
        super(message, ErrorCode.INVALID_USER_RECORD.code);
    }
}
