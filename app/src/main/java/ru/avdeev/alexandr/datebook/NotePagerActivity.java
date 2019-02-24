package ru.avdeev.alexandr.datebook;

/*
 класс NotePagerActivity будет активностью хостом для для одного фрагмента заметки (NoteFragment)

 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class NotePagerActivity extends AppCompatActivity {


    private static final String EXTRA_NOTE_ID = "note_id"; // идентификатор доплнения для интента

    // переменные
    private ViewPager mViewPager;  // view pager
    private List<Note> mNotes; // список заметок


    /**
     *
     * @param packageContext
     * @param noteId передаем в метод уникальный идентификатор заметки
     * @return
     */
    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NotePagerActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId); // положим дополнительные данные для передачи UUID уникальный идентификатор
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pager); // назначим макет для активности

        //получаем дополнение из интента
        UUID noteId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_NOTE_ID);


        // найдем  ViewPager в представлении активности
        mViewPager = (ViewPager) findViewById(R.id.note_view_pager);

        mNotes=Notes.get(this).getNotes(); // получаем от Списка заметок набор данных - контейнер List объектов Note


        FragmentManager fragmentManager = getSupportFragmentManager(); //получаем экземпляр FragmentManager для активности NotePagerActivity
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) { //адаптером назначается безымянный(анонимный) экземпляр FragmentStatePagerAdapter
            //FragmentStatePagerAdapter — ваш агент, управляющий взаимодействием с ViewPager
            //Чтобы агент мог выполнить свою работу с фрагментами,
            // возвращаемыми в getItem(int), он должен быть способен добавить их в активность.
            //Что именно делает агент? Вкратце, он добавляет возвращаемые фрагменты в активность
            // и помогает ViewPager идентифицировать представления фрагментов для их правильного размещения

            /**
             * получает экземпляр заметки для заданной позиции в наборе данных,
             * после чего использует его идентификатор для создания и возвращения
             * правильно настроенного экземпляра NoteFragment
             * @param position
             * @return
             */
            @Override
            public Fragment getItem(int position) {
                Note note = mNotes.get(position);
                return NoteFragment.newInstance(note.getId());
            }


            /**
             * Метод getCount() возвращает текущее количество элементов в списке
             *
             * @return
             */
            @Override
            public int getCount() {
                return mNotes.size();
            }
        });

        // дял правильного отображения элемента списка делаем перебор
        for (int i = 0; i < mNotes.size(); i++) {
            if (mNotes.get(i).getId().equals(noteId)) {
                mViewPager.setCurrentItem(i);
                //  break;
            }
        }


    }

}
