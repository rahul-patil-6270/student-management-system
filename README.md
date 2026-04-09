# EduPulse Nexus :-

EduPulse Nexus is a Java Swing student management project with three pages:

- `Dashboard`
- `Manage Students`
- `Chatbot`

## Gemini chatbot setup

The chatbot is designed to use the Gemini Java SDK with the model `gemini-2.5-flash`.

Your API key is **not stored anywhere in the code**. The app reads it only from the environment variable:

```powershell
$env:GEMINI_API_KEY="your_api_key_here"
```

## Gemini note

The chatbot now uses a direct HTTPS request to Gemini, so you do not need to add the Gemini Java SDK jar just to use chat.

## Chatbot behavior

The chatbot is configured as a project-aware campus assistant for this app. It can:

- explain dashboard insights
- summarize selected student context
- suggest academic improvement ideas
- answer questions about how to use EduPulse Nexus
