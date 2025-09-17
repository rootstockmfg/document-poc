package com.rootstock.document_poc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

  private final Tesseract ocr;
  private final VectorStore vectorStore;
  private final ChatClient chatClient;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Mono<ResponseEntity<String>> createDocument(@RequestPart("file") Mono<FilePart> file) {
    return file.flatMap(
            (FilePart fp) -> {
              try {
                Path tempFile = Files.createTempFile("temp", ".pdf");
                return fp.transferTo(tempFile)
                    .then(Mono.fromCallable(() -> ocr.doOCR(tempFile.toFile())));
              } catch (Exception e) {
                return Mono.error(e);
              }
            })
        .map(
            text -> {
              vectorStore.add(
                  List.of(
                      Document.builder()
                          .text(text)
                          .metadata(Map.of("tenantId", "some-tenant-id"))
                          .build()));
              return text;
            })
        .map(ResponseEntity::ok)
        .doOnError(
            e -> {
              log.error("Error processing file", e);
              ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
            });
  }

  @GetMapping
  public Mono<ResponseEntity<String>> askQuestion(@RequestParam String question) {
    return chatClient.prompt(question).stream()
        .content()
        .collectList()
        .map(s -> String.join("", s))
        .map(ResponseEntity::ok);
  }
}
