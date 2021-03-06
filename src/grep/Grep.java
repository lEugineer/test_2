package grep;

import grep.matcher.Matcher;
import grep.matcher.MatcherStrategy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
//abcdf;
//abcd abc;
//bcd adbc abc;
//ghij abcd * acdf;
//ab ac ad;

//        args = new String[] {"abcd"};
//        args = new String[] {"abcd", "abc"};
//        args = new String[] {"^ab.+"};
//        args = new String[]{"*"};
//        args = new String[] {"pqrst"};
 */

public class Grep {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException( "At least one search pattern required" );
        }

        List<String> inputLines = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = reader.readLine()) != null) {
            if (input.isEmpty()) {
                break;
            }
            inputLines.add(input);
        }

        List<String> result = findAll(inputLines, Arrays.asList(args));
        result.forEach( System.out::println );
    }

    public static List<String> findAll (List<String> input, List<String> filters) {
        List<MatcherStrategy> patterns = buildMatchers(filters);

        List<String> result = new LinkedList<>();
        for (String line : input) {
            if (isMatchAny(line, patterns)) {
                result.add(line);
            }
        }

        return result;
    }

    private static boolean isMatchAny(String line, List<MatcherStrategy> patterns) {

        String normalizedLine = line;
        if (line.endsWith(";")) {
            normalizedLine = line.substring(0, line.length()-1);
        }

        for (String word : normalizedLine.split(" ")) {
            for (MatcherStrategy pattern : patterns) {
                if (pattern.matches(word)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static List<MatcherStrategy> buildMatchers(List<String> patterns) {
        List<MatcherStrategy> matchers = new LinkedList<>();
        for (String pattern : patterns) {
            matchers.add(Matcher.getMatcherStrategy(pattern));
        }
        return matchers;
    }
}
