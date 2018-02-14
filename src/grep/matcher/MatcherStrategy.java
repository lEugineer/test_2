package grep.matcher;

public interface MatcherStrategy {
    boolean matches(String line);
}
