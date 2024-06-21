package org.tuebora.filediff.domain.model.vo;

import org.tuebora.filediff.domain.exception.InvalidUserRecordException;

public enum EmployeeType {
    CUSTOMER,
    PARTNER,
    CONTRACTOR,
    EMPLOYEE;

    public static EmployeeType fromString(String type) {
        for (EmployeeType employeeType : EmployeeType.values()) {
            if (employeeType.toString().equalsIgnoreCase(type)) {
                return employeeType;
            }
        }
        throw new InvalidUserRecordException("Employee type '" + type + "' is not supported");
    }
}
