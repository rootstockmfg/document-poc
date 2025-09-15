package com.rootstock.document_poc.service;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesseractConfig {

  @Bean
  Tesseract tesseract() {
    Tesseract tesseract = new Tesseract();
    tesseract.setPageSegMode(1);
    tesseract.setOcrEngineMode(1);

    return tesseract;
  }
}
