package org.tuebora.filediff.infrastructure.domain.adapter.out;

import jakarta.annotation.PreDestroy;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.tuebora.filediff.domain.model.entity.OutputUserRecord;
import org.tuebora.filediff.domain.port.out.IWriter;
import org.tuebora.filediff.failsafe.ErrorHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvWriter implements IWriter<OutputUserRecord> {
    private static final int BATCH_SIZE = 500;
    private static final int KNOWN_FIELDS_COUNT = 7;

    private final AtomicInteger currentBatchSize = new AtomicInteger(0);
    private final CSVPrinter csvPrinter;
    private final ErrorHandler errorHandler;
    private List<String> headers;

    public CsvWriter(String fileName, ErrorHandler errorHandler) throws IOException {
        //the file will be overwritten by the new content
        this.csvPrinter = new CSVPrinter(new FileWriter(fileName, false), CSVFormat.DEFAULT.builder().build());
        this.errorHandler = errorHandler;
    }

    @Override
    public void append(OutputUserRecord outputUserRecord) {
        String[] values = new String[headers.size()];
        for (int i=0;i<headers.size();i++) {
            switch (headers.get(i)) {
                case "EmployeeID":
                    values[i] = outputUserRecord.getId().getValue();
                    break;
                case "First Name":
                    values[i] = outputUserRecord.getFirstName();
                    break;
                case "Last Name":
                    values[i] = outputUserRecord.getLastName();
                    break;
                case "Location":
                    values[i] = outputUserRecord.getLocation();
                    break;
                case "Title":
                    values[i] = outputUserRecord.getTitle();
                    break;
                case "EmployeeType":
                    values[i] = outputUserRecord.getEmployeeType().name();
                    break;
                case "Email Address":
                    values[i] = outputUserRecord.getEmail().address();
                    break;
                default:
                    break;
            }
        }
        for (int j = KNOWN_FIELDS_COUNT; j<values.length-1; j++) {
            values[j] = outputUserRecord.getOtherAttributes().get(headers.get(j));
        }
        values[values.length-1] = outputUserRecord.getChangeType().name();
        try {
            csvPrinter.printRecord(values);
            int i = currentBatchSize.incrementAndGet();
            if (i == BATCH_SIZE) {
                currentBatchSize.set(0);
                csvPrinter.flush();
            }
        } catch (IOException e) {
            errorHandler.handle(outputUserRecord, e);
        }
    }

    @Override
    public void init(List<String> schemaHeader) {
        List<String> clonedHeaders = new ArrayList<>(schemaHeader);
        clonedHeaders.add("Change Type");
        try {
            csvPrinter.printRecord(clonedHeaders.toArray());
            headers = clonedHeaders;
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Cannot Write Into Output File");
        }
    }

    @PreDestroy
    public void destroy() throws IOException {
        csvPrinter.close(true);
    }
}
