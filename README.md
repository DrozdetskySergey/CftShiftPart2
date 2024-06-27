#  Утилита фильтрации содержимого файлов
### Задание
При запуске утилиты в командной строке подается несколько файлов, содержащих целые числа,
вещественные числа и строки. В качестве разделителя используется перевод строки.
Строки из файлов читаются по очереди в соответствии с их перечислением в командной строке.\
Задача утилиты записать разные типы данных в разные файлы. Целые числа в один
выходной файл, вещественные в другой, строки в третий. По умолчанию файлы с
результатами располагаются в текущей папке с именами integers.txt, floats.txt, strings.txt.
Файлы с результатами создаются по мере необходимости. Если какого-то типа данных во входящих
файлах нет, то и соответствующий файл не создается.\
Дополнительно с помощью опции -o можно задавать путь для результатов. Опция -p
задает префикс имен выходных файлов. Например -o /some/path -p result_ задают вывод в
файлы /some/path/result_integers.txt, /some/path/result_strings.txt и тд.
По умолчанию файлы результатов перезаписываются. С помощью опции -a можно задать
режим добавления в существующие файлы.\
В процессе фильтрации данных необходимо собирать статистику по каждому типу данных.
Статистика должна поддерживаться двух видов: краткая (по умолчанию) и полная.
Выбор статистики производится опциями -s и -f соответственно. Краткая статистика содержит
только количество элементов записанных в исходящие файлы. Полная статистика для чисел
дополнительно содержит сумму, среднее, минимальное и максимальное значение.
Полная статистика для строк, помимо их количества, содержит также размер самой
короткой строки и самой длинной.
Статистика по каждому типу отфильтрованных данных выводится в консоль.
### Особенности реализации
Числа распознаются только в десятичной системе счисления, в классическом виде
(никаких символов 'f' или 'L' в конце). Вещественные числа в виде записи через точку
либо в научной нотации.\
Программа реализована на Java версия 11.\
Используется библиотека Apache Commons Lang версия 3.14\
https://commons.apache.org/proper/commons-lang
### Запуск
Можно запустить с помощью команды:
java -jar FilesFilter.jar [options] [files...]
