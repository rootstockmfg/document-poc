package com.rootstock.document_poc.controller;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class DocumentControllerPostRequest {
  private String tenantId;

  private String content;
}
