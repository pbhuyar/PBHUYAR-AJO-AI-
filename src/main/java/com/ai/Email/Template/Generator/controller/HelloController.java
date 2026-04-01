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

        // ✅ Prompt
        String prompt = """
        You are a professional email generator.

        Return ONLY valid JSON.
        No markdown.
        No explanation.

        Format:
        {
          "subject": "...",
          "body": "...",
          "cta": "..."
        }

        User Input:
        """ + request.getPrompt();

        // ✅ Call AI
        String response = groqClient.callAI(prompt);

        // ✅ Clean response
        String content = response
                .replace("```json", "")
                .replace("```", "")
                .trim();

        // ✅ Parse JSON
        JSONObject json = new JSONObject(content);

        String subject = json.getString("subject");
        String body = json.getString("body");
        String cta = json.getString("cta");

        // ✅ FIXED TYPE
        String html = buildHtml(subject, body, cta);

        // ✅ Escape for AJO
        html = html
                .replace("\"", "'")
                .replace("\n", "")
                .replace("\r", "");

        // ✅ FINAL RESPONSE (VERY IMPORTANT FOR AJO)
      return """
{
  "choices": [
    {
      "message": {
        "content": "%s"
      }
    }
  ]
}
""".formatted(html);
    }
    private String buildHtml(String subject, String body, String cta) {

        return """
        <table width="100%%" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center">

                    <table width="600" style="background:#ffffff;border-radius:10px;overflow:hidden">

                        <tr>
                            <td style="background:linear-gradient(90deg,#667eea,#764ba2);
                                padding:20px;color:#fff;text-align:center;
                                font-size:22px;font-weight:bold;">
                                %s
                            </td>
                        </tr>

                        <tr>
                            <td style="padding:30px;color:#333;font-size:16px;line-height:1.6;">
                                %s
                            </td>
                        </tr>

                        <tr>
                            <td align="center" style="padding:20px;">
                                <a href="#" style="background:#667eea;color:#fff;
                                    padding:12px 25px;text-decoration:none;
                                    border-radius:25px;font-weight:bold;">
                                    %s
                                </a>
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
        </table>
        """.formatted(subject, body, cta);
    }
}
