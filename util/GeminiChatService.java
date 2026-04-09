package util;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiChatService {
    private static final String MODEL = "gemini-2.5-flash";
    private static final String SYSTEM_INSTRUCTION =
            "You are Campus Copilot for the EduPulse Nexus application, a student management and analytics project. "
                    + "Your job is to help users understand dashboard insights, explain student risk and opportunity signals, "
                    + "suggest academic improvement ideas, summarize student profiles in clear language, and answer questions about how to use the app. "
                    + "Ground answers in the provided project context. When giving advice, keep it practical, supportive, and concise. "
                    + "Do not invent hidden data, API keys, or database values. If information is missing, say so clearly. "
                    + "When the question is about a selected student, give useful interpretation based only on the supplied context. "
                    + "If the user asks a general academic or student-support question, answer in a way that fits a campus management dashboard.";
    private static final Pattern TEXT_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"((?:\\\\.|[^\\\\\"])*)\"");

    public String askQuestion(String question, String projectContext) throws Exception {
        String apiKey = ensureApiKey();
        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/"
                + MODEL
                + ":generateContent?key="
                + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        String prompt = "Project context:\n"
                + projectContext
                + "\nUser question:\n"
                + question
                + "\n\nRespond in clean plain text suitable for showing inside a desktop application.";

        String body = "{"
                + "\"system_instruction\":{"
                + "\"parts\":[{\"text\":\"" + escapeJson(SYSTEM_INSTRUCTION) + "\"}]"
                + "},"
                + "\"contents\":[{"
                + "\"parts\":[{\"text\":\"" + escapeJson(prompt) + "\"}]"
                + "}]"
                + "}";

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new IllegalStateException("Gemini request failed with status " + response.statusCode() + ". Response: " + response.body());
        }

        String answer = extractText(response.body());
        if (answer.isEmpty()) {
            return "Gemini returned an empty response. Try asking a more specific question.";
        }
        return answer;
    }

    private String ensureApiKey() {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("GEMINI_API_KEY is not set.");
        }
        return apiKey.trim();
    }

    private String extractText(String json) throws IOException {
        Matcher matcher = TEXT_PATTERN.matcher(json);
        while (matcher.find()) {
            String value = unescapeJson(matcher.group(1));
            if (!value.trim().isEmpty() && !SYSTEM_INSTRUCTION.equals(value)) {
                return value.trim();
            }
        }
        return "";
    }

    private String escapeJson(String value) {
        StringBuilder escaped = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                    if (ch < 32) {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
            }
        }
        return escaped.toString();
    }

    private String unescapeJson(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch == '\\' && i + 1 < value.length()) {
                char next = value.charAt(++i);
                switch (next) {
                    case '"':
                        result.append('"');
                        break;
                    case '\\':
                        result.append('\\');
                        break;
                    case '/':
                        result.append('/');
                        break;
                    case 'b':
                        result.append('\b');
                        break;
                    case 'f':
                        result.append('\f');
                        break;
                    case 'n':
                        result.append('\n');
                        break;
                    case 'r':
                        result.append('\r');
                        break;
                    case 't':
                        result.append('\t');
                        break;
                    case 'u':
                        if (i + 4 < value.length()) {
                            String hex = value.substring(i + 1, i + 5);
                            result.append((char) Integer.parseInt(hex, 16));
                            i += 4;
                        }
                        break;
                    default:
                        result.append(next);
                        break;
                }
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
