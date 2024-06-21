package org.tuebora.filediff.domain.exception;

public class IncompatibleInputSourcesException extends AbstractFileDiffException {
    public IncompatibleInputSourcesException() {
        super(ErrorCode.INCOMPATIBLE_SOURCES.message, ErrorCode.INCOMPATIBLE_SOURCES.code);
    }
}
