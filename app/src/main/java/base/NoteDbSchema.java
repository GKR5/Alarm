package base;



/*
Схема базы данных
 */

public class NoteDbSchema {

    public static final class NoteTable {
        public static final String NAME = "nots";

        public static final class Cols {

            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String DESC="description";

        }
    }


}
