package org.tuebora.filediff.domain.exception;

public class DuplicateUserRecordException extends AbstractFileDiffException{
    public DuplicateUserRecordException() {
        super(ErrorCode.DUPLICATE_USER_RECORD.message, ErrorCode.DUPLICATE_USER_RECORD.code);
    }
}
