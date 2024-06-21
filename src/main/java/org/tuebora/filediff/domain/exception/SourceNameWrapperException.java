package org.tuebora.filediff.domain.exception;

public class SourceNameWrapperException extends AbstractFileDiffException {
    public SourceNameWrapperException(AbstractFileDiffException e, String fileName) {
        super(e.getMessage() + " in source: " + fileName, 0);
    }
}
