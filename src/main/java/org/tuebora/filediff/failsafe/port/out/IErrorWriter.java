package org.tuebora.filediff.failsafe.port.out;

import org.tuebora.filediff.failsafe.vo.InputDiscardedRecord;
import org.tuebora.filediff.failsafe.vo.OutputDiscardedRecord;

import java.io.IOException;

//The aim of this interface is to write all erroneous records during processing into a file named discarded.csv
//Either Input can have bad records or there can be error during writing of Output Records
public interface IErrorWriter {
    void append(InputDiscardedRecord inputDiscardedRecord) throws IOException;

    void append(OutputDiscardedRecord outputDiscardedRecord) throws IOException;
}
