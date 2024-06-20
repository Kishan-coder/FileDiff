package org.tuebora.filediff.infrastructure.failsafe.adapter.out;

import jakarta.annotation.PreDestroy;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tuebora.filediff.domain.model.entity.InputUserRecord;
import org.tuebora.filediff.domain.model.entity.OutputUserRecord;
import org.tuebora.filediff.failsafe.port.out.IErrorWriter;
import org.tuebora.filediff.failsafe.vo.InputDiscardedRecord;
import org.tuebora.filediff.failsafe.vo.OutputDiscardedRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ErrorCsvWriter implements IErrorWriter {
    private static final int BATCH_SIZE = 100;
    private final AtomicInteger currentBatchSize = new AtomicInteger(0);
    private final CSVPrinter csvPrinter;

    public ErrorCsvWriter(@Value("${file.discarded}") String fileName) throws IOException {
        this.csvPrinter = new CSVPrinter(new FileWriter(fileName, false), CSVFormat.DEFAULT.builder().build());
    }

    @Override
    public void append(InputDiscardedRecord inputDiscardedRecord) throws IOException {
        InputUserRecord inputUserRecord = inputDiscardedRecord.inputUserRecord();
        Collection<String> otherAttributeValues= inputUserRecord.getOtherAttributes().values();
        List<String> values = getInputUserRecordAttributesAsStrings(inputUserRecord);
        List<String> allValues = new ArrayList<>();
        allValues.add("InputUserRecord");
        allValues.addAll(values);
        allValues.addAll(otherAttributeValues);
        allValues.add(inputDiscardedRecord.reason());
        csvPrinter.printRecord(allValues.toArray());
        flush();
    }

    @Override
    public void append(OutputDiscardedRecord outputDiscardedRecord) throws IOException {
        OutputUserRecord outputUserRecord = outputDiscardedRecord.outputUserRecord();
        Collection<String> otherAttributeValues= outputUserRecord.getOtherAttributes().values();
        List<String> values = getOutputUserRecordAttributesAsStrings(outputUserRecord);
        List<String> allValues = new ArrayList<>();
        allValues.add("OutputUserRecord");
        allValues.addAll(values);
        allValues.addAll(otherAttributeValues);
        allValues.add(outputDiscardedRecord.reason());
        flush();
    }

    private void flush() throws IOException {
        int i = currentBatchSize.incrementAndGet();
        if (i == BATCH_SIZE) {
            csvPrinter.flush();
            currentBatchSize.set(0);
        }
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        csvPrinter.close(true);
    }

    private List<String> getInputUserRecordAttributesAsStrings(InputUserRecord inputUserRecord) {
        return List.of(inputUserRecord.getFirstName(), inputUserRecord.getLastName(), inputUserRecord.getTitle(), inputUserRecord.getEmployeeType().name(), inputUserRecord.getLocation(), inputUserRecord.getId().getValue(), inputUserRecord.getEmail().address());
    }

    private List<String>  getOutputUserRecordAttributesAsStrings(OutputUserRecord outputUserRecord) {
        return List.of(outputUserRecord.getFirstName(), outputUserRecord.getLastName(), outputUserRecord.getTitle(), outputUserRecord.getEmployeeType().name(), outputUserRecord.getLocation(), outputUserRecord.getId().getValue(), outputUserRecord.getEmail().address());
    }
}
