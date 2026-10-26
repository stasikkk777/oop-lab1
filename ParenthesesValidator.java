import java.util.ArrayDeque;
import java.util.Deque;

public class ParenthesesValidator {
    private final Logger logger = new Logger("Parentheses");

    public boolean isValid(String input) {
        Deque<Integer> openPositions = new ArrayDeque<>(); // index of every unmatched "("
        boolean valid = true;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '(') {
                openPositions.push(i);
                logger.trace("Opening parenthesis found at index " + i);
            } else if (ch == ')') {
                if (openPositions.isEmpty()) {
                    valid = false;
                    logger.warn("Closing parenthesis at index " + i + " has no opening parenthesis");
                } else {
                    int start = openPositions.pop();
                    logger.info("Matching pair found: indexes " + start + " and " + i);
                }
            }
        }

        // anything left on the stack was never closed
        if (!openPositions.isEmpty()) {
            valid = false;
        }

        logger.info("Result for \"" + input + "\": " + valid);
        if (!valid) {
            logger.error("\"" + input + "\" is invalid");
        }
        return valid;
    }
}
