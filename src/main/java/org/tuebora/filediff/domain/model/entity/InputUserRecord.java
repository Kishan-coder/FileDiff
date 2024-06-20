package org.tuebora.filediff.domain.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.tuebora.filediff.domain.model.vo.Email;
import org.tuebora.filediff.domain.model.vo.EmployeeType;
import org.tuebora.filediff.domain.model.vo.ID;
import org.tuebora.filediff.domain.exception.InvalidUserRecordException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

//Immutable InputUserRecord with id as record identifier and email as mandatory
@Getter
@Builder
public final class InputUserRecord implements Serializable{
    @Serial
    private static final long serialVersionUID = 407796278602936764L;
    @NonNull
    private final ID id;
    private final String firstName;
    private final String lastName;
    private final EmployeeType employeeType;
    private final Email email;
    private final String location;
    private final String title;
    private final Map<String, String> otherAttributes;
}
