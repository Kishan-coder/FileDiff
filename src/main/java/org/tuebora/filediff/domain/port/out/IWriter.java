package org.tuebora.filediff.domain.port.out;

import java.util.List;

public interface IWriter<T> {
    void init(List<String> schemaHeader);

    void append(T outputUserRecord);
}
