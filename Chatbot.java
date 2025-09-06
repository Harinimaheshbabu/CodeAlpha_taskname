import java.util.*;

public class Chatbot {

    /**
     * An inner class to represent a rule for the chatbot.
     * Each rule has a set of keywords, a corresponding response, and a priority.
     * Rules with higher priority and more matching keywords are chosen first.
     */
    private static class Rule {
        final String[] keywords;
        final String response;
        final int priority;

        Rule(String[] keywords, String response, int priority) {
            this.keywords = keywords;
            this.response = response;
            this.priority = priority;
        }
    }

    private final List<Rule> rules;
    private String[] unknownResponses;
    private final Random random;
    private String currentTopic = ""; // Holds the context of the conversation

    public Chatbot() {
        rules = new ArrayList<>();
        random = new Random();
        initializeResponses();
    }

    private void initializeResponses() {
        // Priority 10: Specific, high-importance phrases
        rules.add(new Rule(new String[]{"hello"}, "Hello! I'm AlphaBot, a prototype model for the Code Alpha project. How can I assist you?", 10));
        rules.add(new Rule(new String[]{"hi"}, "Hi there! Welcome. My name is AlphaBot. What can I do for you today?", 10));
        rules.add(new Rule(new String[]{"how", "are", "you"}, "As a model, I'm always operating at peak performance. Thanks for asking! How may I help?", 10));
        rules.add(new Rule(new String[]{"bye"}, "Goodbye! It was a pleasure assisting you. Feel free to return with more questions.", 10));
        rules.add(new Rule(new String[]{"thanks"}, "You're very welcome! I'm here to help if you need anything else.", 10));
        rules.add(new Rule(new String[]{"thank", "you"}, "Anytime! Is there another topic you'd like to explore?", 10));

        // Priority 5: Specific questions that involve multiple keywords.
        rules.add(new Rule(new String[]{"what", "is", "your", "name"}, "You can call me AlphaBot. I'm a demonstration project for a Code Alpha internship.", 5));
        rules.add(new Rule(new String[]{"what", "can", "you", "do"}, "I can answer questions on topics like Java, AI, Machine Learning, and NLP. I can also remember our conversation topic to answer follow-up questions.", 5));
        rules.add(new Rule(new String[]{"who", "created", "java"}, "Java was originally developed by James Gosling at Sun Microsystems. It's a cornerstone of modern software development.", 5));
        rules.add(new Rule(new String[]{"tell", "me", "a", "joke"}, "Why did the Java developer quit his job? Because he didn't get arrays!", 5));


        // Priority 1: General topics that can be triggered by a single keyword.
        rules.add(new Rule(new String[]{"help"}, "I can provide information on topics like Java, AI, Machine Learning, and NLP. You can also ask me my name or ask for a joke.", 1));
        rules.add(new Rule(new String[]{"java"}, "Java is a powerful, object-oriented programming language. We can discuss its history, features, or applications. What interests you most?", 1));
        rules.add(new Rule(new String[]{"nlp"}, "Natural Language Processing is a field of AI that enables computers to understand human language. It's how I'm able to chat with you now!", 1));
        rules.add(new Rule(new String[]{"ai"}, "Artificial Intelligence is a broad field of computer science focused on creating smart machines. My own logic is a very basic form of AI.", 1));
        rules.add(new Rule(new String[]{"machine", "learning"}, "Machine Learning is a subset of AI that allows systems to learn from data. It's the technology behind things like recommendation engines and spam filters.", 1));
        rules.add(new Rule(new String[]{"weather"}, "As a language model, I don't have access to real-time weather data, but I'd be happy to discuss technology topics with you!", 1));

        // Responses for when the bot doesn't understand
        unknownResponses = new String[]{
                "I'm sorry, that seems to be outside of my current knowledge base. Could we talk about Java, AI, or another tech topic?",
                "That's an interesting question, but I'm not equipped to answer it. My expertise is in technology topics.",
                "My apologies, but my model hasn't been trained on that information. Please try another question.",
                "I didn't quite understand that. Could you rephrase or ask me something like 'What is NLP?'"
        };
    }

    public String getResponse(String userInput) {
        String processedInput = userInput.toLowerCase().trim().replaceAll("[^a-z\\s]", "");
        Set<String> inputWords = new HashSet<>(Arrays.asList(processedInput.split("\\s+")));

        // --- Context-Specific Responses ---
        // Check for follow-up questions based on the current topic.
        if (inputWords.contains("it") || inputWords.contains("its")) {
            if ((inputWords.contains("creator") || inputWords.contains("created") || inputWords.contains("developer")) && "java".equals(currentTopic)) {
                return "Java was created by James Gosling at Sun Microsystems. A true legend in programming!";
            }
             if ((inputWords.contains("applications") || inputWords.contains("uses")) && "java".equals(currentTopic)) {
                return "Java is incredibly versatile! It's used for web backends, Android apps, big data processing, and scientific applications.";
            }
        }

        // --- Rule-Based Responses ---
        Rule bestMatch = null;
        int bestScore = -1;

        // Iterate through all rules to find the best match.
        for (Rule rule : rules) {
            int currentScore = 0;
            for (String keyword : rule.keywords) {
                if (inputWords.contains(keyword)) {
                    currentScore++;
                }
            }

            if (currentScore == rule.keywords.length) {
                int score = currentScore * rule.priority;
                if (score > bestScore) {
                    bestScore = score;
                    bestMatch = rule;
                }
            }
        }

        if (bestMatch != null) {
            // --- Update Conversation Topic ---
            // Based on the matched rule, set the context for the next turn.
            Set<String> matchedKeywords = new HashSet<>(Arrays.asList(bestMatch.keywords));
            if (matchedKeywords.contains("java")) {
                this.currentTopic = "java";
            } else if (matchedKeywords.contains("ai") || matchedKeywords.contains("machine")) {
                this.currentTopic = "ai";
            } else if (matchedKeywords.contains("nlp")) {
                this.currentTopic = "nlp";
            } else if (matchedKeywords.contains("hello") || matchedKeywords.contains("bye") || matchedKeywords.contains("thanks")) {
                 this.currentTopic = ""; // Reset topic on conversational filler
            }
            return bestMatch.response;
        }

        return unknownResponses[random.nextInt(unknownResponses.length)];
    }
}


