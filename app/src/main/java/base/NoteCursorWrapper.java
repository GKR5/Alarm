package base;


/*

 Класс создает тонкую «обертку» для Cursor.

 */

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import base.NoteDbSchema.NoteTable;
import ru.avdeev.alexandr.datebook.Note;

public class NoteCursorWrapper extends CursorWrapper {


    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(NoteTable.Cols.SOLVED));
        String desc = getString(getColumnIndex(NoteTable.Cols.DESC));


        Note note = new Note(UUID.fromString(uuidString));

        note.setTitle(title);
        note.setDate(new Date(date));
        note.setSolved(isSolved != 0);
        note.setDescription(desc);

        return note;
    }
}
