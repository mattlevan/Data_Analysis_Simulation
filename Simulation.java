/*
 * Matt Levan
 * CSC 331, Dr. Amlan Chatterjee
 * Data Structures
 *
 * Project 2 -- Data Analysis Simulation
 *
 * Simulation.java
 * Main driver class for running the entire data analytics simulation program.
 *
 * A menu-driven program with these options:
 * 1 - Register/create user login (first name, last name, user id, password)
 * 2 - Login
 * 3 - Visit a website
 * 4 - Check browsing history of websites visited in chronological order
 * 5 - Check browsing history of websites visited in REVERSE chron. order
 * 6 - Check number of times each website is visited
 * 7 - Check most visited website
 * 8 - Logout
 * 
 * Must use at least one stack, one queue, one 2D array, methods to separate
 * work (main method should not have more than 20 lines of code).
 *
 * Due in full by 10/25/2015
 *
 */

import java.util.*;
import java.util.regex.*;
import java.io.*;

public class Simulation {
    // Global static variables

    // User variables
    private static String firstName;
    private static String lastName;
    private static String userID = null;
    private static String password;
    private static String tempPass;
    private static boolean userIDExists;
    private static boolean userLoggedIn = false;

    // Files
    private static String fileName = "userDatabase.txt"; // File name
    private static File file = new File(fileName); // File object

    private static String historyFileName = "webHistory.txt";
    private static File historyFile = new File(historyFileName);

    // Data structures
    private static QueueImplementation queue = new QueueImplementation();
    private static StackImplementation stack = new StackImplementation();
    private static String[][] array = new String[100][2];

    public static void main(String[] args) {
        // Analytics record array
        // Initialize objects
        for (int i = 0; i < array.length; i++) {
            array[i][0] = null;
            array[i][1] = "0";
        }
        mainMenu();
    }

    public static void mainMenu() {
        // Scanner for accepting user input in main menu
        Scanner input = new Scanner(System.in); 

        // Temp variable for storing user's choice
        int choice = 0;

        // Let user choose a menu item
        do {
            System.out.println("Welcome to the data analysis simulation.");

            try {                
                if (!file.exists()) {
                    choice = 1;
                }
                else {
                    System.out.println("Please choose an item from the menu: ");
                    System.out.println("");
                    System.out.println("1 - Register");
                    System.out.println("2 - Login");
                    System.out.println("3 - Visit a website");
                    System.out.println("4 - Browsing history");
                    System.out.println("5 - Reverse browsing history");
                    System.out.println("6 - Website statistics");
                    System.out.println("7 - Most visited webite");
                    System.out.println("8 - Logout");
                    System.out.println("");
                    System.out.print("> ");
                    
                    choice = input.nextInt();
                }

                switch (choice) {
                    case 1:
                        register();
                        break;
                    case 2:
                        login();
                        break;
                    case 3:
                        visitWebsite();
                        break;
                    case 4:
                        history();
                        break;
                    case 5:
                        reverseHistory();
                        break;
                    case 6:
                        statistics();
                        break;
                    case 7:
                        mostVisited();
                        break;
                    default:
                        break;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("");
                System.out.println("Choice must be 1-8.");
                System.out.println("");

                input.next();
            }   
        }
        while (choice != 8);

        historyFile.delete();

        System.out.println("");
        System.out.println("Goodbye!");
        System.out.println("");
    }

    public static void register() {
        // Scanner for accepting user input
        Scanner registerInput = new Scanner(System.in);

        // Registration menu
        System.out.println("");
        System.out.println("REGISTRATION");
        System.out.println("");
        System.out.println("Please enter your information.");
        System.out.println("");
        
        // Regular expression patterns
        Pattern alpha = Pattern.compile("[a-zA-Z]+"); // Alpha chars only
        Pattern eightChars = Pattern.compile(".{8,}"); // At least eight chars
        Pattern userIDRegEx = Pattern.compile("[a-zA-Z0-9]+");
        
        // Get first name and check that it is alphabetical
        System.out.print("First name: ");
        while (firstName == null) {
            try {
                firstName = registerInput.next(alpha); // Ensure alphabetical
            }
            catch (InputMismatchException e) {
                System.out.println("");
                System.out.println("Name must use only alphabet characters.");
                System.out.println("");
                System.out.print("First name: ");

                registerInput.next();
            }
        }

        // Get last name and check that it is alphabetical
        System.out.print("Last name: ");
        while (lastName == null) {
            try {
                lastName = registerInput.next(alpha); // Ensure alphabetical
            }
            catch (InputMismatchException e) {
                System.out.println("");
                System.out.println("Name must use only alphabet characters.");
                System.out.println("");
                System.out.print("Last name: ");

                registerInput.next();
            }
        }
        
        // Get userID
        int userIDCounter = 0;
        boolean written = false;

        System.out.print("Desired user ID: ");
        while (!written) {
            while (userID == null) {
                try {
                    userID = registerInput.next(userIDRegEx);
                }
                catch (InputMismatchException e) {
                    System.out.println("");
                    System.out.println("User ID must use only alphabetical, " +
                        "or numerical characters.");
                    System.out.println("");
                    System.out.print("Desired user ID: ");

                    registerInput.next();
                }
            }

            if (userIDExists(userID) && userIDCounter < 2) {
                userIDCounter++;

                System.out.println("");
                System.out.println("User ID already exists!");

                if (userIDCounter < 2) {
                    System.out.print("Desired user ID: ");

                    registerInput.next();
                }
            }
            else if (!userIDExists(userID) && userIDCounter < 2) {
                if (writeUserID(userID)) {
                    written = true;
                }
            }
            else {
                userID = generateUserID(firstName, lastName);

                if (writeUserID(userID)) {
                    System.out.println("Your user ID is " + userID + ".");
                    System.out.println("");
                    written = true;
                }
            }
        }


        // Get password and check that it is at least 8 characters long
        System.out.print("Password (must be at least 8 characters): ");
        while (password == null) {
            try {
                password = registerInput.next(eightChars); // Ensure 8+ chars
            }
            catch (InputMismatchException e) {
                System.out.println("");
                System.out.println("Password must be at least 8 characters.");
                System.out.println("");
                System.out.print("Password: ");

                registerInput.next();
            }
        }
        
        // Re-enter password and check that it matches first
        while (!password.equals(tempPass)) { // While NO match
            try {
                System.out.print("Re-enter password: ");
                tempPass = registerInput.next();

                if (!password.equals(tempPass)) {
                    InputMismatchException e = new InputMismatchException();
                    throw e;
                }
                else {
                    writePassword(password);

                    System.out.println("");
                    System.out.println("Registration successful. Please login.");
                    System.out.println("");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("");
                System.out.println("Passwords do not match!");
                System.out.println("");
            }
        }
    }

    public static void login() {
        // Scanner for accepting user input
        Scanner loginInput = new Scanner(System.in);

        if (userLoggedIn) {
            System.out.println("");
            System.out.println("You're already logged in, " + userID + "!");
            System.out.println("");
        }

        while (!userLoggedIn) { // While NOT logged in
            try {
                System.out.println("");
                System.out.print("User ID: ");

                userID = loginInput.next();

                if (userIDExists(userID)) {
                    System.out.print("Password: ");

                    password = loginInput.next();

                    if (passwordMatch(userID, password)) {
                        userLoggedIn = true;

                        System.out.println("");
                        System.out.println("Welcome " + userID + "!");
                        System.out.println("");
                    }
                }
                else {
                    System.out.println("");
                    System.out.println("User ID doesn't exist!");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Try again!");
            }
        }
    }

    public static void visitWebsite() {
        if (userLoggedIn) {
            String website = null;
            Scanner websiteInput = new Scanner(System.in);
            Scanner continueInput = new Scanner(System.in);

            // Regular expression to validate URL found here:
            // http://stackoverflow.com/questions/24924072/website-url-validation-
            // regex-in-java
            Pattern websiteRegEx = Pattern.compile(
                "(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");

            System.out.println("");
            System.out.print("Enter a website URL: ");

            while (website == null) {
                try {
                    website = websiteInput.next(websiteRegEx); // Valid URL
                    website = website.replace("https://", ""); // Remove https
                    website = website.replace("http://", ""); // Remove http
                    website = website.replace("www.", ""); // Remove www
                }
                catch (InputMismatchException e) {
                    System.out.println("");
                    System.out.println("Please enter a valid URL.");
                    System.out.println("");
                    System.out.print("Enter a website URL: ");

                    websiteInput.next();
                }
            }

            System.out.println("");
            System.out.println("That's an interesting website...");

            // Add the website to the records
            try {
                // Stack record
                // If not a duplicate, add to stack
                if (!websiteExists(website)) {
                    stack.push(website);
                }

                // Add website to array 
                if (!websiteExists(website)) {
                    for (int i = 0; i < array.length; i++) {
                        if (array[i][0] == null) {
                            array[i][0] = website;
                            break;
                        }
                    }        
                }

                // Iterate counter
                for (int i = 0; i < array.length; i++) {
                    if ((array[i][0] != null) && array[i][0].equals(website)) {
                        int tempInt = Integer.parseInt(array[i][1]);
                        tempInt++;
                        array[i][1] = Integer.toString(tempInt);
                    }
                }

                // Text file record
                FileWriter fileWriter = new FileWriter(historyFile, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.append(website + "\n"); 
                bufferedWriter.close();

                // Queue record
                queue.enqueue(website);
            }
            catch (IOException e) {
                System.out.println("");
                System.out.println("IO Error!");
                System.out.println("");
            }

            System.out.print("Visit another website? Y/N: ");
            if (continueInput.next().toLowerCase().equals("y")) {
                visitWebsite();
            }
            else {
                System.out.println("");
            }
        }
        else {
            System.out.println("");
            System.out.println("Please register and/or login first!");
            System.out.println("");
        }
    }

    public static void history() {
        System.out.println("");
        System.out.println("History (chronological from top to bottom): ");
        System.out.println("");

        queue.display();
    }

    public static void reverseHistory() {
        System.out.println("");
        System.out.println("Reverse history (no duplicates): ");
        System.out.println("");

        // Display reverse history
        StackImplementation tempStack = new StackImplementation();
        String tempWebsite = null;

        while (stack.count > 0) {
            tempWebsite =  stack.pop().toString();
            System.out.println(tempWebsite);
            tempStack.push(tempWebsite);
        }
        while (tempStack.count > 0) {
            stack.push(tempStack.pop());
        }

        System.out.println("");
    }

    public static void statistics() {
        System.out.println("");
        System.out.println("Website statistics (name and times visisted):");
        System.out.println("");

        for (int i = 0; i < array.length; i++) {
            if (array[i][0] != null) {
                System.out.println(array[i][0] + ": " + array[i][1]);
            }
        }

        System.out.println("");
    }

    public static void mostVisited() {
        int counter = 0;
        int index = 0;

        for (int i = 0; i < array.length; i++) {
            if (Integer.parseInt(array[i][1]) > counter) {
                counter = Integer.parseInt(array[i][1]);
                index = i;
            }
        }

        System.out.println("");
        System.out.println("Most visited website(s):");
        System.out.println("");

        for (int i = 0; i < array.length; i++) {
            if (Integer.parseInt(array[i][1]) == counter) {
                System.out.println(array[i][0] + ": " + counter);
            }
        }

        System.out.println("");
    }

    // Non main menu methods

    public static boolean websiteExists(String website) {
        boolean websiteExists = false;
        String word = null; // Temp variable for iterating through file

        // Open database text file for reading
        try {
            // If file doesn't exist, create it
            if (!historyFile.exists()) {
                historyFile.createNewFile();
            }

            Scanner scanner = new Scanner(historyFile);

            // Check that the user doesn't already exist
            while (scanner.hasNext()) {
                word = scanner.next();

                if (word.equals(website)) {
                    websiteExists = true;
                }
            }

            scanner.close();

            return websiteExists;
        }
        catch (FileNotFoundException e) {
            System.out.println("");
            System.out.println("Unable to open " + fileName + ".");
            System.out.println("");

            return websiteExists;
        }
        catch (IOException e) {
            System.out.println("Write error in " + fileName + ".");

            return websiteExists;
        }
    }

   public static boolean writeUserID(String userID) {
        // Create or open database text file for writing
        try {            
            // If file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(userID + " ");
            bufferedWriter.close();

            return true;
        }
        catch (IOException e) {
            System.out.println("Write error in " + fileName + ".");

            return false;
        }
    }

    public static boolean userIDExists(String userID) {
        String word = null; // Temp variable for iterating through file

        // Open database text file for reading
        try {
            // If file doesn't exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            Scanner scanner = new Scanner(file);

            // Check that the user doesn't already exist
            while (scanner.hasNext()) {
                word = scanner.next();

                if (word.equals(userID)) {
                    userIDExists = true;
                }
            }

            scanner.close();

            return userIDExists;
        }
        catch (FileNotFoundException e) {
            System.out.println("");
            System.out.println("Unable to open " + fileName + ".");
            System.out.println("");

            return userIDExists;
        }
        catch (IOException e) {
            System.out.println("Write error in " + fileName + ".");

            return userIDExists;
        }
    }

    public static void writePassword(String password) {
        // Open database text file for writing
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(password + "\n");
            bufferedWriter.close();
        }
        catch (IOException e) {
            System.out.println("Write error in " + fileName + ".");
        }
    }

    public static boolean passwordMatch(String userID, String password) {
        boolean passwordMatch = false;
        String word = null; // Temp variable for iterating through file
        File file = new File(fileName);

        // Open database text file for reading
        try {
            Scanner scanner = new Scanner(file);

            // Find the userID in the file
            while (scanner.hasNext()) {
                word = scanner.next();

                if (word.equals(userID)) {
                    // Proceed to the next word and see if it matches password
                    word = scanner.next();

                    if (word.equals(password)) {
                        passwordMatch = true;
                    }
                }
            }

            return passwordMatch;
        }
        catch (FileNotFoundException e) {
            System.out.println("");
            System.out.println("Unable to open " + fileName + ".");
            System.out.println("");

            return passwordMatch;
        }
    }

    public static String generateUserID(String firstName, String lastName) {
        Random random = new Random();
        String randomNumber = Integer.toString(random.nextInt(1000));
        String generation = firstName.substring(0, 1).toLowerCase() + 
            lastName.toLowerCase() + randomNumber;

        return generation;
    }
}