package org.tuebora.filediff.failsafe;

import org.springframework.stereotype.Component;
import org.tuebora.filediff.domain.model.entity.OutputUserRecord;
import org.tuebora.filediff.failsafe.port.out.IErrorWriter;
import org.tuebora.filediff.failsafe.vo.InputDiscardedRecord;
import org.tuebora.filediff.domain.model.entity.InputUserRecord;
import org.tuebora.filediff.failsafe.vo.OutputDiscardedRecord;

import java.io.IOException;

@Component
public class ErrorHandler {
    IErrorWriter discardedRecordWriter;
    public ErrorHandler(IErrorWriter discardedRecordWriter) {
        this.discardedRecordWriter = discardedRecordWriter;
    }
    public void handle(InputUserRecord inputUserRecord, Throwable throwable) {
        InputDiscardedRecord inputDiscardedRecord = new InputDiscardedRecord(inputUserRecord, throwable.getMessage());
        try {
            discardedRecordWriter.append(inputDiscardedRecord);
        } catch (IOException e){
            System.err.println("FATAL ERROR DURING INPUT-DISCARD-RECORD APPEND: " + e.getMessage());
        }
    }
    public void handle(OutputUserRecord outputUserRecord, Throwable throwable) {
        OutputDiscardedRecord outputDiscardedRecord = new OutputDiscardedRecord(outputUserRecord, throwable.getMessage());
        try {
            discardedRecordWriter.append(outputDiscardedRecord);
        } catch (IOException e){
            System.err.println("FATAL ERROR DURING OUTPUT-DISCARD-RECORD APPEND: " + e.getMessage());
        }
    }
}
