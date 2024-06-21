package org.tuebora.filediff.domain.exception;

public class DuplicateUserRecordException extends AbstractFileDiffException {
    public DuplicateUserRecordException(String fileName) {
        super(String.format(ErrorCode.DUPLICATE_USER_RECORD.message, fileName), ErrorCode.DUPLICATE_USER_RECORD.code);
    }
}
