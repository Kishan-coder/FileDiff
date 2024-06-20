package org.tuebora.filediff.domain.exception;

public class AbstractFileDiffException extends RuntimeException{
    final String message;
    final int errorCode;

    public AbstractFileDiffException(String message, int errorCode) {
        super(String.format("%s [%d]", message, errorCode));
        this.message = message;
        this.errorCode = errorCode;
    }
}
