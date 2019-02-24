package ru.avdeev.alexandr.datebook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import base.NoteBaseHelper;
import base.NoteCursorWrapper;
import base.NoteDbSchema.NoteTable;


/*

 класс Notes коллекция (список) заметок

 синглетный класс, можно создать только один экземпляр списка заметок
 */
public class Notes {

    private static Notes sNotes; // статичная переменная

    //   private List<Note> mNoteList;  // список заметок

    // переменные для управления БД
    private Context mContext;
    private SQLiteDatabase mDatabase;


    // закрытый конструктор
    //Другие классы не смогут создать экземпляр NoteLab в обход метода get()
    private Notes(Context context) {


        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext)  // открывает Бд если не создана то создает
                .getWritableDatabase();

        //   mNoteList = new ArrayList<>(); // создаем объект списка


        /*
        // заполним список 200 объектами

        for (int i = 0; i < 200; i++) {

            Note note = new Note();

            note.setTitle(" Заметка № " + i);
            note.setSolved(i % 3 == 0); // Для каждого третьего объекта (остаток от деления равен 0)
            note.setDescription("Описание заметки под номером" + i);
            mNoteList.add(note);

        }
     */
    }

    /**
     * добавление новой заметки
     *
     * @param c
     */
    public void addNote(Note c) {

        ContentValues values = getContentValues(c);
        mDatabase.insert(NoteTable.NAME, null, values);

        //mNoteList.add(c);
    }

    /**
     * удаление заметки
     * @param note
     */


    public void deleteNote(Note note){



       mDatabase.delete(NoteTable.NAME ,NoteTable.Cols.UUID + " = ?", new String[] { note.getId().toString()});



    }




    public static Notes get(Context context) {

        if (sNotes == null) {

            sNotes = new Notes(context);
        }

        return sNotes;
    }


    /**
     * Возвращает список заметок
     *
     * @return список заметок
     */
    public List<Note> getNotes() { // <> - тип элементов который будет хранить массив

        //return mNoteList;
      //  return new ArrayList<>();


        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper cursor = queryNotes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return notes;
    }


    /**
     * @param id идентификатор заметки
     * @return возвращает объект Note(заметка) с заданным идентификатором
     */

    public Note getNote(UUID id) {


        NoteCursorWrapper cursor = queryNotes(
                NoteTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNote();
        } finally {
            cursor.close();
        }
    }


    //NoteDbSchema.NoteTable

    /**
     * Преобразование Note (заметки)  в ContentValues
     *
     * @param note
     * @return
     */

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();

        values.put(NoteTable.Cols.UUID, note.getId().toString());
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.DATE, note.getDate().getTime());
        values.put(NoteTable.Cols.SOLVED, note.isSolved() ? 1 : 0);
        values.put(NoteTable.Cols.DESC, note.getDescription());


        return values;

    }

    /**
     * обновление строк в базе данных
     *
     * @param note
     */

    public void updateNote(Note note) {

        String uuidString = note.getId().toString();
        ContentValues contentValues = getContentValues(note);

        mDatabase.update(NoteTable.NAME, contentValues, NoteTable.Cols.UUID + " = ?", new String[]{uuidString});


    }


    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null, // columns - с null выбираются все столбцы
                whereClause,
                whereArgs,
                null, //
                null, //
                null //
        );
        return new NoteCursorWrapper(cursor);


    }
}




