package org.tuebora.filediff.domain.exception;

//Problem Statement Didn't Specify about unmodified user records common to new and old files
//So Assuming that such records to be discarded
public class UnModifiedUserRecordException extends AbstractFileDiffException {
    public UnModifiedUserRecordException() {
        super(ErrorCode.UNMODIFIED_USER_RECORD.message, ErrorCode.UNMODIFIED_USER_RECORD.code);
    }
}
