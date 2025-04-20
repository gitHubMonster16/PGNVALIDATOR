package pgn_parser;

import models.Board;

import java.util.HashSet;
import java.util.Set;

public class PgnParser {
    private Board board;
    private Set<Character> files = new HashSet<>(Set.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'));
    private Set<Character> rank=new HashSet<>(Set.of('1','2','3','4','5','6','7','8'));
    private Set<Character> pieces=new HashSet<>(Set.of('N','B'));

    public boolean isMoveSyntaxRight(String move){
        if(move.length()==2){
            return files.contains(move.charAt(0))&&rank.contains(move.charAt(1));
        }
        if(move.length()==3){
            return (pieces.contains(move.charAt(0))&&files.contains(move.charAt(1))&&rank.contains(move.charAt(2)))||move.equals("O-O");
        }
        if(move.length()==5){
            return move.equals("O-O-O");
        }
         return false;
    }
    public static boolean eventLine(String line) {
        if (!line.startsWith("[Event \"")) {
            System.out.println("Error: Must start with '[Event \"'");
            System.out.println("Your input: " + (line.length() > 50 ? line.substring(0, 50) + "..." : line));
            return false;
        }

        if (!line.endsWith("\"]")) {
            System.out.println("Error: Must end with '\"]'");
            System.out.println("Your input: " + (line.length() > 50 ? line.substring(0, 50) + "..." : line));
            return false;
        }

        // 2. Extract event content
        String eventContent = line.substring(8, line.length() - 2);

        // 3. Validate event content
        if (eventContent.isEmpty()) {
            System.out.println("Error: Event name cannot be empty");
            return false;
        }

        if (eventContent.length() > 150) {
            System.out.println("Error: Event name exceeds maximum length (150 characters)");
            System.out.println("Length: " + eventContent.length());
            return false;
        }

        // 4. Check for invalid characters
        if (eventContent.matches(".*[<>{}|\\\\^].*")) {
            System.out.println("Error: Contains invalid characters (<>{}|\\^)");
            System.out.println("Problematic content: " +
                    eventContent.replaceAll("[<>{}|\\\\^]", ">>>$0<<<"));
            return false;
        }

        // 5. Check for suspicious patterns (optional)
        if (eventContent.trim().length() != eventContent.length()) {
            System.out.println("Warning: Event name has leading/trailing whitespace");
        }

        // 6. Validate against common event patterns
        if (!eventContent.matches("^[\\w\\s\\-',.:;()]+$")) {
            System.out.println("Warning: Event name contains unusual characters");
            System.out.println("Allowed: letters, numbers, spaces, and basic punctuation");
        }

        return true;
    }
    public static boolean siteLine(String line) {
        // 1. Basic null and structure validation
        if (!line.startsWith("[Site \"")) {
            System.out.println("Error: Must start with '[Site \"'");
            System.out.println("Your input: " + (line.length() > 50 ? line.substring(0, 50) + "..." : line));
            return false;
        }

        if (!line.endsWith("\"]")) {
            System.out.println("Error: Must end with '\"]'");
            System.out.println("Your input: " + (line.length() > 50 ? line.substring(0, 50) + "..." : line));
            return false;
        }

        // 2. Extract site content
        String siteContent = line.substring(7, line.length() - 2);

        // 3. Validate site content
        if (siteContent.isEmpty()) {
            System.out.println("Error: Site cannot be empty");
            return false;
        }

        if (siteContent.length() > 200) {
            System.out.println("Error: Site exceeds maximum length (200 characters)");
            System.out.println("Length: " + siteContent.length());
            return false;
        }

        // 4. Validate URL format if it appears to be a URL
        if (siteContent.matches(".*https?://.*")) {
            if (!isValidUrl(siteContent)) {
                System.out.println("Error: Invalid URL format");
                System.out.println("Your URL: " + siteContent);
                return false;
            }
        }

        // 5. Check for invalid characters
        if (siteContent.matches(".*[<>{}|\\\\^].*")) {
            System.out.println("Error: Contains invalid characters (<>{}|\\^)");
            System.out.println("Problematic content: " +
                    siteContent.replaceAll("[<>{}|\\\\^]", ">>>$0<<<"));
            return false;
        }

        // 6. Check for suspicious patterns (optional)
        if (siteContent.matches(".*(password|secret|admin).*")) {
            System.out.println("Warning: Site contains potentially sensitive words");
        }
        return true;
    }

    private static boolean isValidUrl(String url) {
        try {
            new java.net.URL(url);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }
    public static boolean dateLine(String line) {
        // 1. Basic null and structure validation
        if (line == null) {
            System.out.println("Error: Date line is null");
            return false;
        }

        if (!line.startsWith("[Date \"")) {
            System.out.println("Error: Must start with '[Date \"'");
            System.out.println("Your input: " + (line.length() > 50 ? line.substring(0, 50) + "..." : line));
            return false;
        }

        if (!line.endsWith("\"]")) {
            System.out.println("Error: Must end with '\"]'");
            System.out.println("Your input: " + (line.length() > 50 ? line.substring(0, 50) + "..." : line));
            return false;
        }

        // 2. Extract date content
        String dateContent = line.substring(7, line.length() - 2);

        // 3. Validate date format
        if (!dateContent.matches("\\d{4}\\.(0[1-9]|1[0-2])\\.(0[1-9]|[12][0-9]|3[01])")) {
            System.out.println("Error: Invalid date format");
            System.out.println("Expected format: YYYY.MM.DD (e.g., 2023.12.31)");
            System.out.println("Your input: " + dateContent);


            if (!dateContent.matches("\\d{4}\\..*")) {
                System.out.println("- Year must be 4 digits followed by a dot");
            } else if (!dateContent.matches("\\d{4}\\.\\d{2}\\..*")) {
                System.out.println("- Month must be 2 digits (01-12) followed by a dot");
            } else if (!dateContent.matches(".*\\d{2}$")) {
                System.out.println("- Day must be 2 digits at the end");
            } else {
                String[] parts = dateContent.split("\\.");
                if (parts.length != 3) {
                    System.out.println("- Must contain exactly two dots separating YYYY, MM, DD");
                } else {
                    try {
                        int month = Integer.parseInt(parts[1]);
                        int day = Integer.parseInt(parts[2]);

                        if (month < 1 || month > 12) {
                            System.out.println("- Month must be between 01 and 12");
                        }
                        if (day < 1 || day > 31) {
                            System.out.println("- Day must be between 01 and 31");
                        }
                        // Additional checks for month-day combinations
                        if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                            System.out.println("- This month only has 30 days");
                        }
                        if (month == 2 && day > 29) {
                            System.out.println("- February has maximum 29 days");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("- Month and day must be numbers");
                    }
                }
            }
            return false;
        }

        // 4. Advanced date validation (leap years, etc.)
        String[] dateParts = dateContent.split("\\.");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        // Validate February days
        if (month == 2) {
            boolean isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
            if (day > 29 || (day == 29 && !isLeapYear)) {
                System.out.println("Error: Invalid February date");
                System.out.println(year + (isLeapYear ? " is a leap year" : " is not a leap year"));
                System.out.println("February " + year + " only has " + (isLeapYear ? "29" : "28") + " days");
                return false;
            }
        }

        return true;
    }
    public static boolean whiteLine(String line) {
        // 1. Check basic structure format

        if (!line.startsWith("[White \"")) {
            System.out.println("Error: Must start with '[White \"'");
            System.out.println("Your input: " + (line.length() > 50 ? line.substring(0, 50) + "..." : line));
            return false;
        }

        if (!line.endsWith("\"]")) {
            System.out.println("Error: Must end with '\"]'");
            System.out.println("Your input: " + (line.length() > 50 ? line.substring(0, 50) + "..." : line));
            return false;
        }

        // 2. Extract and validate content
        String content = line.substring(8, line.length() - 2);

        if (content.isEmpty()) {
            System.out.println("Error: White player name cannot be empty");
            return false;
        }

        if (content.length() > 100) {
            System.out.println("Error: White player name exceeds maximum length (100 characters)");
            System.out.println("Length: " + content.length());
            return false;
        }

        if (content.matches(".*[<>{}|\\\\^].*")) {
            System.out.println("Error: Contains invalid characters (<>{}|\\^)");
            System.out.println("Problematic content: " + content.replaceAll("[<>{}|\\\\^]", ">>>$0<<<"));
            return false;
        }

        // 3. Additional business rules (if needed)
        if (content.trim().length() != content.length()) {
            System.out.println("Warning: White player name has leading/trailing whitespace");

        }

        return true;
    }
    public static boolean blackLine(String line) {
        // Check basic structure first
        if (!line.startsWith("[Black \"") || !line.endsWith("\"]")) {
            System.out.println("Error: Black line must start with '[Black \"' and end with '\"]'");
            System.out.println("Your input: " + line);
            return false;
        }

        // Extract the content between quotes
        String content = line.substring(8, line.length() - 2);

        // Validate content rules (add your specific requirements here)
        if (content.isEmpty()) {
            System.out.println("Error: Black player name cannot be empty");
            return false;
        }


        // Example: Check for maximum length
        if (content.length() > 100) {
            System.out.println("Error: Black player name exceeds maximum length (100 characters)");
            return false;
        }

        // Example: Check for invalid characters
        if (content.matches(".*[<>].*")) {
            System.out.println("Error: Black player name contains invalid characters (< or >)");
            return false;
        }

        return true;
    }
    public static boolean resultLine(String st) {

        if (!st.startsWith("[Result \"") || !st.endsWith("\"]")) {
            System.out.println("Error: Result line must start with '[Result \"' and end with '\"]'");
            return false;
        }


        String content = st.substring(9, st.length() - 2);


        if (!content.matches("1-0|0-1|1/2-1/2")) {
            System.out.println("Error: Invalid result format. Allowed values are: \"1-0\", \"0-1\", \"1/2-1/2\"");
            System.out.println("Your input was: \"" + content + "\"");
            return false;
        }

        return true;
    }

  public boolean ParsePGN(String game){
      return false;
  }

  
}
