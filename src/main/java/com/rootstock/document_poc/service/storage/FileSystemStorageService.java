package com.rootstock.document_poc.service.storage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    private final Path rootPath;

    public FileSystemStorageService(StorageProperties properties) {
        String location = properties.getLocation();
        if (location.trim().isEmpty()) {
            throw new IllegalArgumentException("Storage location must be set");
        }

        this.rootPath = Path.of(location);
        // Ensure directory exists
        init();
    }

    @Override
    public void init() {
        try {
            java.nio.file.Files.createDirectories(rootPath);
            log.debug("Created storage directory {}", rootPath);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public Mono<Void> store(FilePart file) {
        Path destination = rootPath.resolve(file.filename());
        log.debug("Storing file to {}", destination);
        return file.transferTo(destination).then();
    }

    @Override
    public Stream<Path> loadAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadAll'");
    }

    @Override
    public Path load(String filename) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'load'");
    }

    @Override
    public Resource loadAsResource(String filename) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadAsResource'");
    }

    @Override
    public void deleteAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAll'");
    }

}
