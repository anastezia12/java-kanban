package main.data;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class DateConst {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");
    public static  final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
}
