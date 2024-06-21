package org.tuebora.filediff.domain.service;

import org.springframework.stereotype.Component;
import org.tuebora.filediff.domain.exception.IncompatibleInputSourcesException;
import org.tuebora.filediff.domain.exception.UnModifiedUserRecordException;
import org.tuebora.filediff.domain.model.entity.InputUserRecord;
import org.tuebora.filediff.domain.model.entity.OutputUserRecord;
import org.tuebora.filediff.domain.model.vo.ChangeType;
import org.tuebora.filediff.domain.model.vo.ID;
import org.tuebora.filediff.domain.port.in.IReader;
import org.tuebora.filediff.domain.port.out.IWriter;
import org.tuebora.filediff.failsafe.ErrorHandler;

import java.util.Map;

//This is the domain service class where all the business logic resides
@Component
public class FileDiffProcessingService {
    private final IReader<ID, InputUserRecord> previousReader;
    private final IReader<ID, InputUserRecord> currentReader;
    private final IWriter<OutputUserRecord> outputWriter;
    private final ErrorHandler errorHandler;

    public FileDiffProcessingService(IReader<ID, InputUserRecord> previousReader, IReader<ID, InputUserRecord> currentReader, IWriter<OutputUserRecord> outputWriter, ErrorHandler errorHandler) {
        this.previousReader = previousReader;
        this.currentReader = currentReader;
        this.outputWriter = outputWriter;
        this.errorHandler = errorHandler;
    }

    //business logic starts
    public void process() {
        if (!previousReader.isCompatible(currentReader)) {
            throw new IncompatibleInputSourcesException();
        } else {
            outputWriter.init(currentReader.getSchemaHeader());
        }
        Map<ID, InputUserRecord> previousUserMap = previousReader.readAll();
        Map<ID, InputUserRecord> currentUserMap = currentReader.readAll();
        previousUserMap.entrySet().parallelStream().forEach(oldUserRecordEntry -> handleOldUser(oldUserRecordEntry, currentUserMap));
        currentUserMap.entrySet().parallelStream().forEach(currentUserRecordEntry -> appendUserRecord(currentUserMap.get(currentUserRecordEntry.getKey()), ChangeType.ADDED));
    }

    private void appendUserRecord(InputUserRecord currentUserRecord, ChangeType changeType) {
        outputWriter.append(OutputUserRecord.builder()
                .id(currentUserRecord.getId()).email(currentUserRecord.getEmail()).
                title(currentUserRecord.getTitle()).employeeType(currentUserRecord.getEmployeeType())
                .location(currentUserRecord.getLocation()).changeType(changeType).
                firstName(currentUserRecord.getFirstName()).lastName(currentUserRecord.getLastName()).otherAttributes(currentUserRecord.getOtherAttributes()).build());
    }

    private void handleOldUser(Map.Entry<ID, InputUserRecord> oldUserRecordEntry, Map<ID, InputUserRecord> currentUserMap) {
        if (currentUserMap.containsKey(oldUserRecordEntry.getKey())) {
            InputUserRecord currentUserRecord = currentUserMap.get(oldUserRecordEntry.getKey());
            try {
                validateModifiedUserRecord(oldUserRecordEntry.getValue(), currentUserRecord);
                appendUserRecord(currentUserRecord, ChangeType.CHANGED);
            } catch (UnModifiedUserRecordException exception) {
                errorHandler.handle(currentUserRecord, exception);
            }
            currentUserMap.remove(oldUserRecordEntry.getKey());
        } else {
            appendUserRecord(oldUserRecordEntry.getValue(), ChangeType.DELETED);
        }
    }

    private void validateModifiedUserRecord(InputUserRecord previousUserRecord, InputUserRecord currentUserRecord) throws UnModifiedUserRecordException {
        if (previousUserRecord.getFirstName().equals(currentUserRecord.getFirstName()) && previousUserRecord.getLastName().equals(currentUserRecord.getLastName())
                && previousUserRecord.getEmail().equals(currentUserRecord.getEmail()) && previousUserRecord.getLocation().equals(currentUserRecord.getLocation())
                && previousUserRecord.getEmployeeType().equals(currentUserRecord.getEmployeeType()) && previousUserRecord.getTitle().equals(currentUserRecord.getTitle())) {
            for (Map.Entry<String, String> entry : previousUserRecord.getOtherAttributes().entrySet()) {
                if (entry.getValue().equals(currentUserRecord.getOtherAttributes().get(entry.getKey()))) ;
            }
            throw new UnModifiedUserRecordException();
        }
    }
}
