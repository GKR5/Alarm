package ru.avdeev.alexandr.datebook;

/*

 класс Note модель одной заметки или какого либо дела

 */

import java.util.Date;
import java.util.UUID;

public class Note {

    // переменные
    private UUID mId;               // уникальный идентификатор
    private String mTitle;         // заголовок
    private Date mDate;           // дата
    private String description;  // описание что нужно сделать (одного дела)
    private boolean mSolved;    // состояние выполнения



    // конструктор
    public Note(){

        this(UUID.randomUUID());
      //  mId = UUID.randomUUID();    //UUID генератор уникальных идентификаторов одного дела
     //   mDate = new Date();        // инициализирует текущую дату !!!
    }

    public Note(UUID id){

        mId=id;

        mDate = new Date();
    }


    // геттеры и сеттеры

    public UUID getId() {
        return mId;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) { // установить заголовок
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
