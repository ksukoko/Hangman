import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.io.IOException;

public class Hangman {
    private static char[] hiddenWord;
    private static String randomWord;
    private final static String START = "да";
    private final static String QUIT = "нет";
    private static final String[] HANGMAN_STAGES = {
            """
    +---+
    |   |
        |
        |
        |
        |
    =========
    """,
            """
    +---+
    |   |
    O   |
        |
        |
        |
    =========
    """,
            """
    +---+
    |   |
    O   |
    |   |
        |
        |
    =========
    """,
            """
    +---+
    |   |
    O   |
   /|   |
        |
        |
    =========
   """,
            """
    +---+
    |   |
    O   |
   /|\\  |
        |
        |
    =========
   """,
            """
    +---+
    |   |
    O   |
   /|\\  |
   /    |
        |
    =========
   """,
            """
    +---+
    |   |
    O   |
   /|\\  |
   / \\  |
        |
    =========
   """};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Если вы хотите начать игру, ведите %s, и %s, если не хотите %n", START, QUIT);
        while (true) {
            String command = scanner.nextLine().toLowerCase();
            if (command.equals(START)) {
                System.out.println("Давайте сыграем в виселицу");
                selectRandomWord();
                System.out.println("Начинаем игру!");
                startGame();
            } else if (command.equals(QUIT)) {
                System.out.println("Игра окончена");
                break;
            } else {
                System.out.println("Неправильный ввод, попробуйте ещё раз ");
            }
        }
    }

    private static void selectRandomWord() {
        try {
            List<String> words = Files.readAllLines(Paths.get("src/Words.txt"));
            randomWord = words.get(new Random().nextInt(words.size()));
            hiddenWord = new char[randomWord.length()];

            for (int i = 0; i < hiddenWord.length; i++) {
                hiddenWord[i] = '_';
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            System.exit(1);
        }
    }

    int remainingAttempts = 6;

    private static char inputLetter() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите букву ");
            String letter = scanner.nextLine();
            if (letter.length() == 1) {
                return letter.charAt(0);
            }
            System.out.println("Вы ничего не ввели или ввели не правильно, попробуйте ещё раз ввести букву");
        }

    }

    private static boolean isCorrectGuess(char userGuess) {
        boolean correct = false;
        for (int i = 0; i < randomWord.length(); i++) {
            if (randomWord.charAt(i) == userGuess) {
                hiddenWord[i] = userGuess;
                correct = true;
            }
        }
        return !correct;
    }


    private static boolean isWordComplete() {
        for (int i = 0; i < randomWord.length(); i++) {
            if (hiddenWord[i] == '_') {
                return false;
            }
        }
        return true;
    }


    private static void displayHangmanStage(int remainingAttempts) {
        System.out.println(HANGMAN_STAGES[6 - remainingAttempts]);
    }

    private static void startGame() {
        int remainingAttempts = 6;
        while (remainingAttempts > 0) {
            displayHangmanStage(remainingAttempts);
            System.out.println("Загаданное слово " + new String(hiddenWord));
            char userGuess = inputLetter();
            if (isCorrectGuess(userGuess)) {
                remainingAttempts--;
            } else if (isWordComplete()) {
                System.out.println("Поздравляем, вы победили!");
                break;
            }

            if (remainingAttempts == 0) {
                displayHangmanStage(remainingAttempts);
                System.out.println("Слово, которое было загадано: " + randomWord);
                System.out.println("Вы проиграли :( ");
            }
        }

    }

}