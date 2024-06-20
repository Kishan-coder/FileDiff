package org.tuebora.filediff.failsafe.vo;

import org.tuebora.filediff.domain.model.entity.OutputUserRecord;

import java.io.Serial;
import java.io.Serializable;

public record OutputDiscardedRecord(OutputUserRecord outputUserRecord, String reason) implements Serializable {
    @Serial
    private static final long serialVersionUID = 2037049699590698718L;

}
