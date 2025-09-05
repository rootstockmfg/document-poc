package com.rootstock.document_poc;

import org.springframework.boot.SpringApplication;

public class TestDocumentPocApplication {

	public static void main(String[] args) {
		SpringApplication.from(DocumentPocApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
