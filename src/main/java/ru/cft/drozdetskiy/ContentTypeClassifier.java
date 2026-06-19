package ru.cft.drozdetskiy;

import static ru.cft.drozdetskiy.ContentType.*;

/**
 * Функциональный класс. Специализируется на классификации {@linkplain  ContentType типа содержимого} строки.
 * Реализует статичный метод {@linkplain #classify(String)}
 */
final class ContentTypeClassifier {

    private ContentTypeClassifier() {
    }

    /**
     * Классифицирует строку - определяет {@linkplain  ContentType тип содержимого} строки. Пустая строка
     * классифицируется как строка. Если строка не пустая, то тип содержимого определяется методом исключения.
     * Сначала предполагается, что это целое число. У целого числа первый символ, кроме цифры, может быть знак,
     * а остальные символы должны быть цифрами. Если встречается символ десятичного разделителя и(или)
     * символ экспоненты, тогда предполагается, что это вещественное число в десятичной или экспоненциальной записи.
     * Если проверяемая строка не может быть числом, тогда она классифицируется как строка (текст).
     *
     * @param string проверяемая строка.
     * @return {@linkplain  ContentType Тип содержимого} строки.
     */
    public static ContentType classify(String string) {
        if (string.isEmpty()) {
            return STRING;
        }

        if (string.length() == 1) {
            return isDigit(string.charAt(0)) ? INTEGER : STRING;
        }

        final int firstIndex = isNumberSign(string.charAt(0)) ? 1 : 0;
        final int lastIndex = string.length() - 1;
        ContentType result = INTEGER;

        for (int i = firstIndex; result != STRING && i <= lastIndex; i++) {
            char symbol = string.charAt(i);

            if (isDecimalSeparator(symbol) && result == INTEGER && firstIndex != lastIndex) {
                result = FLOAT;
            } else if (isExponent(symbol) && firstIndex < i - (result == INTEGER ? 0 : 1)) {
                result = isSubstringConvertedToInteger(string, i + 1) ? FLOAT : STRING;
                break;
            } else if (!isDigit(symbol)) {
                result = STRING;
            }
        }

        return result;
    }

    /**
     * Проверяет что содержимое подстроки начиная с заданного индекса можно конвертировать в Integer
     * без минимального значения -2147483648, то есть это целое число в диапазоне от -2147483647 до +2147483647.
     * Если подстрока пустая (beginIndex больше последнего индекса string), то её нельзя конвертировать в число.
     * У целого числа первый символ, кроме цифры, может быть знак, а остальные символы должны быть цифрами.
     * Знак и начальные нули пропускаются. Если в оставшейся подстроке более 10 цифр, то такое целое число
     * точно выходит за диапазон, если меньше 10 цифр, то оно точно находится в диапазоне, если ровно 10 цифр,
     * тогда требуется произвести дополнительную проверку на попадание в диапазон.
     * Подстрока из 10 цифр имеет разницу крайних индексов 9 (последний - первый).
     *
     * @param string     полная строка из которой проверяется подстрока начиная с заданного индекса.
     * @param beginIndex индекс начала подстроки
     * @return true если содержимое подстроки можно конвертировать в Integer без Integer.MIN_VALUE.
     * @throws IndexOutOfBoundsException если beginIndex имеет отрицательное значение.
     */
    private static boolean isSubstringConvertedToInteger(String string, int beginIndex) {
        final int lastIndex = string.length() - 1;

        if (string.isEmpty() || lastIndex < beginIndex) {
            return false;
        }

        int firstIndex = isNumberSign(string.charAt(beginIndex)) ? beginIndex + 1 : beginIndex;

        while (firstIndex < lastIndex && string.charAt(firstIndex) == '0') {
            firstIndex++;
        }

        final int firstToLastIndexDiff = 9;
        boolean result = firstIndex <= lastIndex && lastIndex <= firstIndex + firstToLastIndexDiff;

        for (int i = firstIndex; result && i <= lastIndex; i++) {
            result = isDigit(string.charAt(i));
        }

        if (result && lastIndex == firstIndex + firstToLastIndexDiff) {
            String rangeBound = "2147483647";

            for (int i = 0; result && i <= firstToLastIndexDiff; i++) {
                result = string.charAt(firstIndex + i) <= rangeBound.charAt(i);
            }
        }

        return result;
    }

    /**
     * Проверяет, что символ является десятичным разделителем.
     * Для машиночитаемых данных в международном стандарте таким символом является точка.
     *
     * @param symbol проверяемый символ.
     * @return true если символ является десятичным разделителем.
     */
    private static boolean isDecimalSeparator(char symbol) {
        return symbol == '.';
    }

    /**
     * Проверяет, что символ является символом экспоненты.
     *
     * @param symbol проверяемый символ.
     * @return true если символ является символом экспоненты.
     */
    private static boolean isExponent(char symbol) {
        return symbol == 'e' || symbol == 'E';
    }

    /**
     * Проверяет, что символ является цифрой.
     *
     * @param symbol проверяемый символ.
     * @return true если символ является цифрой.
     */
    private static boolean isDigit(char symbol) {
        return '0' <= symbol && symbol <= '9';
    }

    /**
     * Проверяет, что символ является знаком числа.
     *
     * @param symbol проверяемый символ.
     * @return true если символ является знаком числа.
     */
    private static boolean isNumberSign(char symbol) {
        return symbol == '+' || symbol == '-';
    }
}
