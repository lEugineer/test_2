package calc;

/*
* 2.Необходимо реализовать консольное приложение-калькулятор с поддержкой функционала поиска суммы/ разности/
* произведения/ частного/ возведения в целую степень. Выводить корректное сообщения об ошибке в случае невалидных
* параметров (например, деление на 0). Реализовать поддержку нескольких операций одновременно и приоритета операций
* с учётом скобок ( () >> ^ >> * / >> ±)
* */

//    2 + 2 * 2
//    (4 + 3) * 2 ^ -2
//    5 + 1/0
//    (17 ^ 4 + 5 * 974 ^ 33 + 2.24 * 4,75)^0
//    4 2 * 3
//    4a * 5

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class CalcMain {
    public static void main ( String[] args ) throws IOException {

        BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
        String input;
        while ((input = reader.readLine()) != null) {
            if (input.isEmpty()) {
                break;
            }

            try {
                BigDecimal result = Calculator.evaluateExpression( input );
                System.out.println( input + " = " + result );
            } catch (Exception e) {
                System.out.println( input + " : " + e.getMessage() );
            }
        }
    }
}
