package org.tuebora.filediff.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuebora.filediff.domain.model.entity.InputUserRecord;
import org.tuebora.filediff.domain.model.entity.OutputUserRecord;
import org.tuebora.filediff.domain.model.vo.ID;
import org.tuebora.filediff.domain.port.in.IReader;
import org.tuebora.filediff.domain.port.out.IWriter;
import org.tuebora.filediff.failsafe.ErrorHandler;
import org.tuebora.filediff.failsafe.port.out.IErrorWriter;
import org.tuebora.filediff.infrastructure.domain.adapter.in.CsvReader;
import org.tuebora.filediff.infrastructure.domain.adapter.out.CsvWriter;
import org.tuebora.filediff.infrastructure.failsafe.adapter.out.ErrorCsvWriter;

import java.io.IOException;

@Configuration
public class FileDiffConfig {
    private final ErrorHandler errorHandler;

    public FileDiffConfig(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Bean
    IReader<ID, InputUserRecord>  previousReader(@Value("${file.old}") String oldFileName) throws IOException {
        return new CsvReader(oldFileName, errorHandler);
    }
    @Bean
    IReader<ID, InputUserRecord> currentReader(@Value("${file.new}") String newFileName) throws IOException {
        return new CsvReader(newFileName, errorHandler);
    }
    @Bean
    IWriter<OutputUserRecord> outputWriter(@Value("${file.output}") String outputFileName) throws IOException {
        return new CsvWriter(outputFileName, errorHandler);
    }
}
