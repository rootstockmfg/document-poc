package com.rootstock.document_poc.service.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;

import reactor.core.publisher.Mono;

public interface StorageService {
    void init();

    Mono<Void> store(FilePart file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
