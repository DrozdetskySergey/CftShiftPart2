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
     * Классифицирует {@linkplain  ContentType тип содержимого} строки методом исключения.
     * Сначала проверяет, что это целое число. Если нет, то проверяет, что это вещественное число.
     * Если нет, тогда это обычная строка.
     * У целого числа первый символ, кроме цифры, может быть плюс или минус, а остальные символы должны быть цифрами.
     * У вещественного числа начало как у целого, а потом, при определённых условиях, должен встретиться
     * один из трёх символов: [точка, e, E]. Так же возможно сочетание символа [точка] и ещё
     * одного из двух символов: [e, E].
     *
     * @param string проверяемая строка.
     * @return {@linkplain  ContentType Тип содержимого} строки.
     */
    public static ContentType classify(String string) {
        if (string.isEmpty()) {
            return STRING;
        }

        final int firstIndex = string.charAt(0) == '+' || string.charAt(0) == '-' ? 1 : 0;
        final int lastIndex = string.length() - 1;
        ContentType result = firstIndex <= lastIndex ? INTEGER : STRING; // STRING если строка "+" или "-"

        for (int i = firstIndex; result != STRING && i <= lastIndex; i++) {
            char symbol = string.charAt(i);

            if (symbol == '.' && result == INTEGER && firstIndex != lastIndex) {
                result = FLOAT;
            } else if ((symbol == 'e' || symbol == 'E')
                       && ((result == INTEGER && firstIndex < i) || (result == FLOAT && firstIndex + 1 < i))) {
                result = isSubstringConvertedToInteger(string, i + 1) ? FLOAT : STRING;
                break;
            } else if (symbol < '0' || '9' < symbol) {
                result = STRING;
            }
        }

        return result;
    }

    /**
     * Проверяет что содержимое строки начиная с заданного индекса (подстрока) можно преобразовать в Integer
     * исключая Integer.MIN_VALUE, то есть это целое число в диапазоне от -2147483647 до +2147483647.
     * У целого числа первый символ, кроме цифры, может быть плюс или минус, а остальные символы должны быть цифрами.
     *
     * @param string     полная строка из которой берётся подстрока начиная с заданного индекса.
     * @param beginIndex индекс начала подстроки
     * @return true если содержимое строки можно конвертировать в Integer без Integer.MIN_VALUE.
     */
    private static boolean isSubstringConvertedToInteger(String string, int beginIndex) {
        final int lastIndex = string.length() - 1;

        if (lastIndex < beginIndex) {
            return false;
        }

        int firstIndex = beginIndex + (string.charAt(beginIndex) == '+' || string.charAt(beginIndex) == '-' ? 1 : 0);

        while (firstIndex < lastIndex && string.charAt(firstIndex) == '0') {
            firstIndex++;
        }

        final int firstToLastIndexDiff = 9; // строка из 10 цифр имеет разницу крайних индексов 9 (последний - первый)
        boolean result = firstIndex <= lastIndex && lastIndex <= firstIndex + firstToLastIndexDiff;

        for (int i = firstIndex; result && i <= lastIndex; i++) {
            char symbol = string.charAt(i);
            result = '0' <= symbol && symbol <= '9';
        }

        if (result && lastIndex == firstIndex + firstToLastIndexDiff) {
            String digits = string.substring(firstIndex);
            result = digits.compareTo("2147483647") <= 0;
        }

        return result;
    }
}
