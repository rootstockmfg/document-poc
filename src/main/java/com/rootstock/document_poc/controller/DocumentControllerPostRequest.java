package com.rootstock.document_poc.controller;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

@Component
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@Builder
public class DocumentControllerPostRequest {
  private String tenantId;

  private String content;
}
