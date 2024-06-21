package org.tuebora.filediff.domain.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.tuebora.filediff.domain.model.vo.ChangeType;
import org.tuebora.filediff.domain.model.vo.Email;
import org.tuebora.filediff.domain.model.vo.EmployeeType;
import org.tuebora.filediff.domain.model.vo.ID;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

//Immutable InputUserRecord with id as record identifier and email, changeType as mandatory
@Builder
@Getter
public final class OutputUserRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = 2204465821087676969L;

    @NonNull
    private final ID id;
    private final String firstName;
    private final String lastName;
    private final EmployeeType employeeType;
    private final Email email;
    private final String location;
    private final String title;
    private final Map<String, String> otherAttributes;
    @NonNull
    private final ChangeType changeType;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OutputUserRecord other = (OutputUserRecord) obj;
        return id.equals(other.id);
    }
}
