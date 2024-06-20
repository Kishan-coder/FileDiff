package org.tuebora.filediff.domain.port.in;

import java.util.List;
import java.util.Map;

//Interface to read complete data from input source
public interface IReader<K, V> {
    Map<K, V> readAll();
    //checks compatibility w.r.t schema between two input sources (old and new user store)
    boolean isCompatible(IReader<K, V> reader);
    //fetched input source schema
    List<String> getSchemaHeader();
}
