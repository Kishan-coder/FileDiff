package org.tuebora.filediff.infrastructure.domain.adapter.in;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.tuebora.filediff.domain.exception.AbstractFileDiffException;
import org.tuebora.filediff.domain.exception.DuplicateUserRecordException;
import org.tuebora.filediff.domain.exception.FileNameWrapperException;
import org.tuebora.filediff.domain.model.entity.InputUserRecord;
import org.tuebora.filediff.domain.model.vo.Email;
import org.tuebora.filediff.domain.model.vo.EmployeeType;
import org.tuebora.filediff.domain.model.vo.ID;
import org.tuebora.filediff.domain.port.in.IReader;
import org.tuebora.filediff.failsafe.ErrorHandler;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CsvReader implements IReader<ID, InputUserRecord> {
    //Headers: First Name,Last Name,Location,Title,EmployeeType,EmployeeID,Email Address, other attributes
    private final List<String> headers;
    private final Iterator<CSVRecord> csvIterator;
    private final ErrorHandler errorHandler;
    private final String fileName;

    public CsvReader(String fileName, ErrorHandler errorHandler) throws IOException {
        CSVParser parser = CSVParser.parse(new FileReader(fileName), CSVFormat.DEFAULT.builder().setHeader().build());
        csvIterator = parser.iterator();
        if (!csvIterator.hasNext()) {
            throw new IllegalArgumentException("CSV FILE EMPTY!!!");
        }
        headers = parser.getHeaderNames();
        this.errorHandler = errorHandler;
        this.fileName = fileName;
    }

    @Override
    public Map<ID, InputUserRecord> readAll() {
        Map<ID, InputUserRecord> userMap = new HashMap<>();
        while (csvIterator.hasNext()) {
            CSVRecord userRecord = csvIterator.next();
            InputUserRecord userRecordInput = null;
            try {
                userRecordInput = getUserRecord(userRecord);
                if (userRecordInput != null) {
                    if (userMap.containsKey(userRecordInput.getId())) {
                        throw new DuplicateUserRecordException(fileName);
                    }
                    userMap.put(userRecordInput.getId(), userRecordInput);
                }
            } catch (AbstractFileDiffException e) {
                if (userRecordInput == null){
                    e = new FileNameWrapperException(e, fileName);
                }
                errorHandler.handle(userRecordInput, e);
            }
        }
        return userMap;
    }

    @Override
    public boolean isCompatible(IReader<ID, InputUserRecord> reader) {
        if (reader instanceof CsvReader csvReader) {
            if (new HashSet<>(csvReader.getSchemaHeader()).containsAll(headers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getSchemaHeader() {
        return headers;
    }

    //CSV Record to POJO mapper method for UserRecords
    private InputUserRecord getUserRecord(CSVRecord userRecord) {
        String id = null;
        String emailAddress = null;
        String firstName = null;
        String lastName = null;
        String location = null;
        String title = null;
        String type = null;
        Map<String, String> otherAttributes = new HashMap<>();
        InputUserRecord inputUserRecord = null;
        for (String header : headers) {
            switch (header) {
                case "EmployeeID":
                    id = userRecord.get("EmployeeID");
                    break;
                case "First Name":
                    firstName = userRecord.get("First Name");
                    break;
                case "Last Name":
                    lastName = userRecord.get("Last Name");
                    break;
                case "Location":
                    location = userRecord.get("Location");
                    break;
                case "Title":
                    title = userRecord.get("Title");
                    break;
                case "EmployeeType":
                    type = userRecord.get("EmployeeType");
                    break;
                case "Email Address":
                    emailAddress = userRecord.get("Email Address");
                    break;
                default:
                    otherAttributes.put(header, userRecord.get(header));
            }
        }
        inputUserRecord = InputUserRecord.builder().id(new ID(id)).email(new Email(emailAddress))
                .firstName(firstName).lastName(lastName).location(location).employeeType(EmployeeType.fromString(type)).title(title).otherAttributes(otherAttributes).build();
        return inputUserRecord;
    }

}
