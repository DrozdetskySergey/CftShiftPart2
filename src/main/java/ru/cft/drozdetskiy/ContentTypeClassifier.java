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
     * Классифицирует строку - определяет {@linkplain  ContentType тип содержимого} строки методом исключения.
     * Сначала проверяет, что это целое число. У целого числа первый символ, кроме цифры, может быть знак,
     * а остальные символы должны быть цифрами. Если встречается символ десятичного разделителя и(или)
     * символ экспоненты, тогда проверяет, что это вещественное число в десятичной или экспоненциальной записи.
     * Если нет, то это обычная строка.
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
            } else if (isDecimalExponent(symbol) &&
                       ((result == INTEGER && firstIndex < i) || (result == FLOAT && firstIndex + 1 < i))) {
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
     * без минимального значения MIN_VALUE, то есть это целое число в диапазоне от -2147483647 до +2147483647.
     * У целого числа первый символ, кроме цифры, может быть знак, а остальные символы должны быть цифрами.
     * Знак и начальные нули пропускаются. Если в оставшейся подстроке более 10 цифр, то такое целое число
     * точно выходит за диапазон, если меньше 10 цифр, то оно точно входит в диапазон, если ровно 10 цифр,
     * тогда требуется произвести дополнительную проверку на попадание в диапазон.
     * Подстрока из 10 цифр имеет разницу крайних индексов 9 (последний - первый).
     *
     * @param string     полная строка из которой проверяется подстрока начиная с заданного индекса.
     * @param beginIndex индекс начала подстроки
     * @return true если содержимое подстроки можно конвертировать в Integer без Integer.MIN_VALUE.
     */
    private static boolean isSubstringConvertedToInteger(String string, int beginIndex) {
        final int lastIndex = string.length() - 1;

        if (lastIndex < beginIndex) {
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

        if (result && firstIndex + firstToLastIndexDiff == lastIndex) {
            String digits = string.substring(firstIndex);
            result = digits.compareTo("2147483647") <= 0;
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
     * Проверяет, что символ является десятичным показателем степени.
     *
     * @param symbol проверяемый символ.
     * @return true если символ является десятичным показателем степени.
     */
    private static boolean isDecimalExponent(char symbol) {
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
