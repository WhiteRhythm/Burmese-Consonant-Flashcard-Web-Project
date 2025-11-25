package org.global.academy;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
    
    // A static list to hold all our Burmese flashcards
    private static List<Flashcard> flashcards;

    public static void main(String[] args) {
        // 1. Initialize the data (Load all 33 Consonants)
        flashcards = createBurmeseFlashcards();
        
        // 2. Configure Spark
        port(8080);
        staticFiles.location("/public");
        
        // 3. Enable CORS (Allows your HTML to talk to this Java Server)
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
        });

        Gson gson = new Gson();
        Random random = new Random();

        // --- ROUTE: Get a random Burmese Flashcard ---
        get("/flashcard", (req, res) -> {
            // Logic: Pick a random index from 0 to Size-1
            int randomIndex = random.nextInt(flashcards.size());
            Flashcard randomCard = flashcards.get(randomIndex);
            
            res.type("application/json");
            return gson.toJson(randomCard);
        });

        // --- ROUTE: Login ---
        post("/login", (req, res) -> {
            LoginRequest lr = gson.fromJson(req.body(), LoginRequest.class);
            
            // Simple check: username "alice", password "secret"
            if ("alice".equals(lr.username) && "secret".equals(lr.password)) {
                res.type("application/json");
                return gson.toJson(new LoginResponse("fake-jwt-token", lr.username));
            } else {
                res.status(401);
                res.type("application/json");
                return gson.toJson(new ErrorResponse("Invalid credentials"));
            }
        });
        
        System.out.println("Server is running on http://localhost:8080");
    }

    // --- HELPER: Create the list of all 33 Burmese Consonants ---
    private static List<Flashcard> createBurmeseFlashcards() {
        List<Flashcard> cards = new ArrayList<>();
        
        // Group 1 (Ka Wag) - Gutturals
        cards.add(new BurmeseConsonantFlashcard("က", "Ka - (Chicken)"));
        cards.add(new BurmeseConsonantFlashcard("ခ", "Kha - (Spiral)"));
        cards.add(new BurmeseConsonantFlashcard("ဂ", "Ga - (Snail)"));
        cards.add(new BurmeseConsonantFlashcard("ဃ", "Gha - (Great)"));
        cards.add(new BurmeseConsonantFlashcard("င", "Nga - (Fish)"));
        
        // Group 2 (Sa Wag) - Palatals
        cards.add(new BurmeseConsonantFlashcard("စ", "Sa - (Sparrow)"));
        cards.add(new BurmeseConsonantFlashcard("ဆ", "Hsa - (Twisted)"));
        cards.add(new BurmeseConsonantFlashcard("ဇ", "Za - (Egret)"));
        cards.add(new BurmeseConsonantFlashcard("ဈ", "Zha - (Big Eye)"));
        cards.add(new BurmeseConsonantFlashcard("ည", "Nya - (Palm Tree)"));

        // Group 3 (Ta Wag) - Retroflex
        cards.add(new BurmeseConsonantFlashcard("ဋ", "Tta - (Post)"));
        cards.add(new BurmeseConsonantFlashcard("ဌ", "Ttha - (Circle)"));
        cards.add(new BurmeseConsonantFlashcard("ဍ", "Dda - (Duck)"));
        cards.add(new BurmeseConsonantFlashcard("ဎ", "Ddha - (Water Filter)"));
        cards.add(new BurmeseConsonantFlashcard("ဏ", "Nna - (Money)"));

        // Group 4 (Ta Wag) - Dentals
        cards.add(new BurmeseConsonantFlashcard("တ", "Ta - (Soldier)"));
        cards.add(new BurmeseConsonantFlashcard("ထ", "Tha - (Bag)"));
        cards.add(new BurmeseConsonantFlashcard("ဒ", "Da - (Peacock)"));
        cards.add(new BurmeseConsonantFlashcard("ဓ", "Dha - (Flag)"));
        cards.add(new BurmeseConsonantFlashcard("န", "Na - (Ear)"));

        // Group 5 (Pa Wag) - Labials
        cards.add(new BurmeseConsonantFlashcard("ပ", "Pa - (Box)"));
        cards.add(new BurmeseConsonantFlashcard("ဖ", "Pha - (Pagoda)"));
        cards.add(new BurmeseConsonantFlashcard("ဗ", "Ba - (Drum)"));
        cards.add(new BurmeseConsonantFlashcard("ဘ", "Bha - (Hermit)"));
        cards.add(new BurmeseConsonantFlashcard("မ", "Ma - (Mushroom)"));

        // Group 6 (Miscellaneous)
        cards.add(new BurmeseConsonantFlashcard("ယ", "Ya - (Pet)"));
        cards.add(new BurmeseConsonantFlashcard("ရ", "Ra - (Sun/Carriage)"));
        cards.add(new BurmeseConsonantFlashcard("လ", "La - (Moon)"));
        cards.add(new BurmeseConsonantFlashcard("ဝ", "Wa - (Cotton/Circle)"));
        cards.add(new BurmeseConsonantFlashcard("သ", "Tha - (Fruit)"));
        
        // Group 7
        cards.add(new BurmeseConsonantFlashcard("ဟ", "Ha - (Laugh)"));
        cards.add(new BurmeseConsonantFlashcard("ဠ", "La - (Great)"));
        cards.add(new BurmeseConsonantFlashcard("အ", "A - (Official/Bowl)"));

        return cards;
    }

    // --- DTO Classes (Data Transfer Objects) ---
    static class LoginRequest {
        String username;
        String password;
    }

    static class LoginResponse {
        String token;
        String username;
        LoginResponse(String t, String u) {
            token = t;
            username = u;
        }
    }

    static class ErrorResponse {
        String error;
        ErrorResponse(String e) {
            error = e;
        }
    }
}