#  Утилита фильтрации содержимого файлов

### Задание
При запуске утилиты в командной строке подается несколько файлов, содержащих в
перемешку целые числа, строки и натуральные числа. В качестве разделителя
используется перевод строки. Строки из файлов читаются по очереди в соответствии с их
перечислением в командной строке.
Задача утилиты записать разные типы данных в разные файлы. Целые числа в один
выходной файл, натуральные в другой, строки в третий. По умолчанию файлы с
результатами располагаются в текущей папке с именами integers.txt, floats.txt, strings.txt.
Дополнительно с помощью опции -o нужно уметь задавать путь для результатов. Опция -p
задает префикс имен выходных файлов. Например -o /some/path -p result_ задают вывод в
файлы /some/path/result_integers.txt, /some/path/result_strings.txt и тд.
По умолчанию файлы результатов перезаписываются. С помощью опции -a можно задать
режим добавления в существующие файлы.
Файлы с результатами должны создаваться по мере необходимости. Если какого-то типа
данных во входящих файлах нет, то и создавать исходящий файл, который будет заведомо
пустым, не нужно.
В процессе фильтрации данных необходимо собирать статистику по каждому типу данных.
Статистика должна поддерживаться двух видов: краткая и полная. Выбор статистики
производится опциями -s и -f соответственно. Краткая статистика содержит только
количество элементов записанных в исходящие файлы. Полная статистика для чисел
дополнительно содержит минимальное и максимальное значения, сумма и среднее.
Полная статистика для строк, помимо их количества, содержит также размер самой
короткой строки и самой длинной.
Статистику по каждому типу отфильтрованных данных утилита должна вывести в консоль.

### Особенности реализации
Программа реализована на чистой Java версия 11. 
Система сборки отсутствует.
Сторонние библиотеки отсутствуют.

### Запуск
Можно запустить с помощью команды:
java -jar FilesFilter.jar -(ключи) (файл1) (файл2) (...)
