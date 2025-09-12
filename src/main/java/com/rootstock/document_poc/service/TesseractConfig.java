package com.rootstock.document_poc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sourceforge.tess4j.Tesseract;

@Configuration
public class TesseractConfig {

    @Value("${tesseract.datapath}")
    String dataPath;
    
    @Bean Tesseract tesseract() {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(dataPath);
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);

        return tesseract;
    }
}
