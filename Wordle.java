
public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
		In dictIn = new In(filename);
        return dictIn.readAllLines();
    }

    // Choose a random secret word from the dictionary. 
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {
		int wordIndex = (int) (Math.random() * dict.length);
        return dict[wordIndex];
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
		for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == c) {    return true;    }
        }
        return false;
    }

    // Counts how many times the char appears in the string
    public static int countChar(String secret, char c) {
        int count = 0;
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == c) {    count++;    }
        }

        return count;
    }

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
		for (int i = 0; i < secret.length(); i++) {
            // Checks if the string contains the char
            if (containsChar(secret, guess.charAt(i))) {
                resultRow[i] = 'Y';
            }
        }

        for (int i = 0; i < secret.length(); i++) {
            // If the string doesn't contain the chosen char
            if (!containsChar(secret, guess.charAt(i))) {
                resultRow[i] = '_';
            } 
            else {
                // If the letter matches exactly
                if (secret.charAt(i) == guess.charAt(i)) {
                    resultRow[i] = 'G';
                }
            }
        }

        /* 
        // Covers the cases where the word guessed has a char more times than the secret word
        String charsChecked = "";
        for (int i = 0; i < secret.length(); i++) {
            char c = guess.charAt(i);
            if (countChar(charsChecked, c) > 0) {   continue;   }
            // If the word has the char less times than the guess
            if (countChar(secret, c) < countChar(guess, c)) {
                int amountToRemove = countChar(guess, c) - countChar(secret, c);
                for (int j = resultRow.length - 1; j >= 0 && amountToRemove > 0; j--) {
                    if (guess.charAt(j) == c && resultRow[j] == 'Y') {
                        resultRow[j] = '_';
                        amountToRemove--;
                    }
                }
                charsChecked += c;
            }
        } */
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    // For example, of guess is HELLO, and row is 2, then after this function 
    // guesses should look like:
    // guesses[2][0] // 'H'
	// guesses[2][1] // 'E'
	// guesses[2][2] // 'L'
	// guesses[2][3] // 'L'
	// guesses[2][4] // 'O'
    public static void storeGuess(String guess, char[][] guesses, int row) {
		for (int i = 0; i < guess.length(); i++) {
            guesses[row][i] = guess.charAt(i);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
		for (char c : resultRow) {
            if (c != 'G') { return false;   }
        }
        return true;
    }

    public static void main(String[] args) {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[MAX_ATTEMPTS][WORD_LENGTH];
        char[][] results = new char[MAX_ATTEMPTS][WORD_LENGTH];

        // Prepare to read from the standart input 
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readLine().toUpperCase();
                
                if (guess.length() != WORD_LENGTH || !guess.matches("^[A-Z]*$")) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            // Stores the inputed guess
            storeGuess(guess, guesses, attempt);
            
            // Computes the results of the current guess and stores them in the results array (in the set row)
            computeFeedback(secret, guess, results[attempt]);

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            System.out.printf("Sorry, you did not guess the word.%nThe secret word was: %s%n", secret);
        }

        inp.close();
    }
}
