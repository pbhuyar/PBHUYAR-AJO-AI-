
package com.ai.Email.Template.Generator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import com.ai.Email.Template.Generator.client.GroqClient;
import com.ai.Email.Template.Generator.model.EmailRequest;

@RestController
@RequestMapping("/api/v1/email")
public class HelloController {

    @Autowired
    private GroqClient groqClient;

    @PostMapping(value = "/generate", produces = "application/json")
    public String generateEmail(@RequestBody EmailRequest request) {

        // ✅ Prompt for Groq (LLM)
        String prompt = """
        You are a professional email generator.

        Return ONLY valid JSON.
        No markdown.
        No explanation.

        Format:
        {
          "subject": "short catchy subject",
          "body": "clean HTML email content"
        }

        Rules:
        - Subject must be short and engaging
        - Body must contain only valid HTML (table, tr, td, p, a)
        - No backticks
        - No extra text outside JSON

        User Input:
        """ + request.getPrompt();

        String subject;
        String body;

        try {
            // ✅ Call Groq
            String response = groqClient.callAI(prompt);

            // ✅ Debug log (important in real project)
            System.out.println("AI RAW RESPONSE: " + response);

            // ✅ Clean response
            String content = response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            // ✅ Parse JSON safely
            JSONObject json = new JSONObject(content);

            subject = json.optString("subject", "Special Offer Just for You!");
            body = json.optString("body", "<p>Check out our latest offers!</p>");

        } catch (Exception e) {
            // ✅ Fallback (very important)
            System.out.println("AI ERROR: " + e.getMessage());

            subject = "Exclusive Offer!";
            body = "<p>Unable to generate content right now.</p>";
        }

        // ✅ Sanitize (avoid AJO rendering issues)
        body = body.replaceAll("<script.*?>.*?</script>", "");

        // ✅ Wrap HTML
        String html = buildHtml(body);

        // ✅ Escape JSON-breaking characters
        html = html
                .replace("\"", "'")
                .replace("\n", "")
                .replace("\r", "");

        subject = subject.replace("\"", "'");

        // ✅ FINAL RESPONSE (MATCHES YOUR AJO CONTEXT MAPPING)
        JSONObject responseJson = new JSONObject();
        responseJson.put("content", html);   // REQUIRED
        responseJson.put("subject", subject); // OPTIONAL but useful

        return responseJson.toString();
    }

    // ✅ Email HTML Template
    private String buildHtml(String body) {

        return """
        <table width="100%%" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center">

                    <table width="600" style="background:#ffffff;border-radius:10px;overflow:hidden">

                        <tr>
                            <td style="padding:30px;color:#333;font-size:16px;line-height:1.6;">
                                %s
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>
        """.formatted(body);
    }
}
    public String generateEmail(@RequestBody EmailRequest request) {

        // ✅ Prompt for Groq (LLM)
        String prompt = """
        You are a professional email generator.

        Return ONLY valid JSON.
        No markdown.
        No explanation.

        Format:
        {
          "subject": "short catchy subject",
          "body": "clean HTML email content"
        }

        Rules:
        - Subject must be short and engaging
        - Body must contain only valid HTML (table, tr, td, p, a)
        - No backticks
        - No extra text outside JSON

        User Input:
        """ + request.getPrompt();

        String subject;
        String body;

        try {
            // ✅ Call Groq
            String response = groqClient.callAI(prompt);

            // ✅ Debug log (important in real project)
            System.out.println("AI RAW RESPONSE: " + response);

            // ✅ Clean response
            String content = response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            // ✅ Parse JSON safely
            JSONObject json = new JSONObject(content);

            subject = json.optString("subject", "Special Offer Just for You!");
            body = json.optString("body", "<p>Check out our latest offers!</p>");

        } catch (Exception e) {
            // ✅ Fallback (very important)
            System.out.println("AI ERROR: " + e.getMessage());

            subject = "Exclusive Offer!";
            body = "<p>Unable to generate content right now.</p>";
        }

        // ✅ Sanitize (avoid AJO rendering issues)
        body = body.replaceAll("<script.*?>.*?</script>", "");

        // ✅ Wrap HTML
        String html = buildHtml(body);

        // ✅ Escape JSON-breaking characters
        html = html
                .replace("\"", "'")
                .replace("\n", "")
                .replace("\r", "");

        subject = subject.replace("\"", "'");

        // ✅ FINAL RESPONSE (MATCHES YOUR AJO CONTEXT MAPPING)
        JSONObject responseJson = new JSONObject();
        responseJson.put("content", html);   // REQUIRED
        responseJson.put("subject", subject); // OPTIONAL but useful

        return responseJson.toString();
    }

    // ✅ Email HTML Template
    private String buildHtml(String body) {

        return """
        <table width="100%%" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center">

                    <table width="600" style="background:#ffffff;border-radius:10px;overflow:hidden">

                        <tr>
                            <td style="padding:30px;color:#333;font-size:16px;line-height:1.6;">
                                %s
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>
        """.formatted(body);
    }
}
