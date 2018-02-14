package grep.matcher;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Matcher {
    public static MatcherStrategy getMatcherStrategy(String expression) {
        MatcherStrategy strategy;

        try {
            Pattern pattern = Pattern.compile(expression);
            strategy = word -> pattern.matcher(word).matches();
        } catch (PatternSyntaxException e) {
            strategy = expression::equals;
        }

        return strategy;
    }
}
