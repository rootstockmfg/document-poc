package com.rootstock.document_poc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.rootstock.document_poc.service.storage.StorageService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final StorageService storageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> createDocument(@RequestPart("file") Mono<FilePart> file) {
        return file.flatMap(fp ->
            storageService.store(fp)
                .then(Mono.just(ResponseEntity.ok("Hello POST " + fp.filename())))
        );
    }

    @GetMapping
    public ResponseEntity<String> getMethodName() {
        return ResponseEntity.ok("Hello World");
    }

}
