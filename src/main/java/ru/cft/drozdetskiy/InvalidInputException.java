package ru.cft.drozdetskiy;

/**
 * Сигнал о том, что получен недопустимый ввод от пользователя.
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Генерирует сигнал (бросает исключение), что получен недопустимый ввод от пользователя.
     *
     * @param message сообщение.
     */
    public InvalidInputException(String message) {
        super(message);
    }

    /**
     * Генерирует сигнал (бросает исключение), что получен недопустимый ввод от пользователя.
     *
     * @param format параметризованное сообщение.
     * @param args   перечень или массив объектов для параметризованного сообщения.
     */
    public InvalidInputException(String format, Object... args) {
        super(String.format(format, args));
    }
}

