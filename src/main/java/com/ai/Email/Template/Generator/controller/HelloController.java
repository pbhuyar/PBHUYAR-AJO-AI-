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

    @PostMapping("/generate")
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

            // ✅ Clean response
            String content = response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            // ✅ Parse JSON
            JSONObject json = new JSONObject(content);

            subject = json.optString("subject", "Special Offer Just for You!");
            body = json.optString("body", "<p>Check out our latest offers!</p>");

        } catch (Exception e) {
            // ✅ Fallback
            subject = "Exclusive Offer!";
            body = "<p>Unable to generate content right now.</p>";
        }

        // ✅ Remove unsafe content
        body = body.replaceAll("<script.*?>.*?</script>", "");

        // ✅ Wrap HTML
        String html = buildHtml(body);

        // ✅ Escape for JSON safety
        html = html.replace("\"", "'").replace("\n", "").replace("\r", "");
        subject = subject.replace("\"", "'");

        // ✅ FINAL RESPONSE (MATCHES AJO context mapping)
        JSONObject responseJson = new JSONObject();
        responseJson.put("content", html);   // 👈 REQUIRED for your {{context...content}}
        responseJson.put("subject", subject); // optional (if you want dynamic subject)

        return responseJson.toString();
    }

    // ✅ Email Template
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
