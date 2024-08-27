package com.example.doctracker;

import org.springframework.ai.autoconfigure.vertexai.gemini.VertexAiGeminiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocTrackerApplication.class, args);
	}

}
