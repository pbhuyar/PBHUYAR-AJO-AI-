package com.ai.Email.Template.Generator.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Component
public class GroqClient {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.url}")
    private String url;

    public String callAI(String prompt) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // ✅ Build JSON safely using Map (NO string concatenation)
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.1-8b-instant");
        requestBody.put("messages", List.of(message));
        requestBody.put("temperature", 0.5);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

   
        System.out.println("API KEY VALUE: " + apiKey);
        
        try {
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, entity, Map.class);

            // ✅ Extract ONLY content (important)
            Map body = response.getBody();
            List choices = (List) body.get("choices");
            Map choice = (Map) choices.get(0);
            Map msg = (Map) choice.get("message");

            return msg.get("content").toString();

        } catch (Exception e) {
            throw new RuntimeException("Groq API Error: " + e.getMessage());
        }
    }
}
