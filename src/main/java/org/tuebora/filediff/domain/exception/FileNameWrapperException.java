package org.tuebora.filediff.domain.exception;

public class FileNameWrapperException extends AbstractFileDiffException{
    public FileNameWrapperException(AbstractFileDiffException e, String fileName) {
        super(e.getMessage()+ " in file: "+fileName, 0);
    }
}
