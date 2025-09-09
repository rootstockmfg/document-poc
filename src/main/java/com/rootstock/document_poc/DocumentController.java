package com.rootstock.document_poc;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/documents")
public class DocumentController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> createDocument(@RequestPart("file") Mono<FilePart> file) {
        return file.map(fp -> ResponseEntity.ok("Hello POST " + fp.filename()));
    }

    @GetMapping
    public ResponseEntity<String> getMethodName() {
        return ResponseEntity.ok("Hello World");
    }

}
