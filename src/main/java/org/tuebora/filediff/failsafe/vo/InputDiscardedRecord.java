package org.tuebora.filediff.failsafe.vo;

import lombok.Getter;
import org.tuebora.filediff.domain.model.entity.InputUserRecord;

import java.io.Serial;
import java.io.Serializable;

public record InputDiscardedRecord(InputUserRecord inputUserRecord, String reason) implements Serializable {
    @Serial
    private static final long serialVersionUID = -4037049670590698718L;

}
