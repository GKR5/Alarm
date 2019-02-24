package ru.avdeev.alexandr.datebook;

/*

 NoteListFragment фрагмент для списка заметок

 */


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NoteListFragment extends Fragment {

    private   int pos; // позиция перезагружаемого элемента в списке

    private NoteAdapter mNoteAdapter;    // адаптер

    private RecyclerView mNoteRecyclerView; // переменная RecyclerView (список представлений )

    private boolean mSubtitleVisible; // признак видимости скрыть / показать количество заметок


    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle"; // признак видимости при повороте


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // связываем фрагмент с макетом
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);


        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean
                    (SAVED_SUBTITLE_VISIBLE);
        }

        // найдем списковое представление
        mNoteRecyclerView = (RecyclerView) view.findViewById(R.id.note_recycler_view);

        //Объект LayoutManager управляет позиционированием элементов, а также определяет поведение прокрутки
        // mNoteRecyclerView назначаем объект LayoutManager.
        // если это не сделать будет ошибка
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI(); // обновляем UI

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }


    @Override
    public void onResume() {
        super.onResume();

        updateUI(); // обновляем когда выходит на передний план
    }

    /**
     * метод updateUI обновляет пользовательский интерфейс
     */
    private void updateUI() {
        Notes notes = Notes.get(getActivity());
        List<Note> notesList = notes.getNotes();

        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(notesList);
            mNoteRecyclerView.setAdapter(mNoteAdapter);
        } else {

            mNoteAdapter.setNotes(notesList); // обновляем список заметок
             //   mNoteAdapter.notifyDataSetChanged(); // перезагрузить все элементы

            //обнаружить, в какой позиции произошло изменение, и перезагрузить правильный элемент.

             mNoteAdapter.notifyItemChanged(pos);   // перезагрузить один элемент в списке


        }

        updateSubtitle();
    }

    /**
     * СОЗДАНИЕ МЕНЮ
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_list, menu);

        // инициируем повторное создание меню
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);


        if (mSubtitleVisible) {
            subtitleItem.setIcon(R.drawable.ic_close);
        } else {
            subtitleItem.setIcon(R.drawable.ic_open);
        }
    }

    /**
     *  Получение обратных вызовов так создаем во фрагменте меню
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * РЕАКЦИЯ НА ВЫБОР КОМАНДЫ В МЕНЮ
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_note:
                Note note = new Note();
                Notes.get(getActivity()).addNote(note);
                Intent intent = NotePagerActivity
                        .newIntent(getActivity(), note.getId());
                startActivity(intent);
                return true;

            case R.id.show_subtitle: // элемент действия показать количество заметок
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Методо задает подзаголовок
     */
    private void updateSubtitle() {
        Notes notes = Notes.get(getActivity());
        int noteCount = notes.getNotes().size();
        String subtitle = getString(R.string.subtitle_format, noteCount); // генерирует строку подзаголовка

        // показать скрыть подзаголовок
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        //активность, являющаяся хостом для NoteListFragment, преобразуется в AppCompatActivity
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }



    /**
     * класс NoteHolder приватный класс (ViewHolder) будет заполнять  макет данными для каждой заметки
     */
    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // переменные
        private TextView mTitleTextView;   // заголовок
        private TextView mDateTextView;   // кнопка даты

        private ImageView mSolvedImageView; // изображение

        private Note mOneNote;  // одна заметка

        // конструктор
        public NoteHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_note, parent, false));


            // найдем текстовые представления на фрагменте для одного элемента !!!
            mTitleTextView = (TextView) itemView.findViewById(R.id.note_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.note_date);
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.note_solved); // найдем изображение

            itemView.setOnClickListener(this); // добавим слушателя

        } // конец конструктора

        /**
         * метод bind будет вызываться каждый раз, ( когда RecyclerView потребует связать заданный объект NoteHolder)
         * когда в NoteHolder (заполняет макет данными) должен отображаться новый объект Note (то есть нового преступления)
         *
         * @param oneNote преступление
         */
        public void bind(Note oneNote) {
            mOneNote = oneNote;
            mTitleTextView.setText(mOneNote.getTitle());
            mDateTextView.setText(mOneNote.getDate().toString());

            mSolvedImageView.setVisibility(mOneNote.isSolved() ? View.VISIBLE : View.GONE);  //истина показываем картинку
        }


        /**
         * обработка касания по элементам
         *
         * @param v элемент представления
         */

        @Override
        public void onClick(View v) {

            //  с передачей идентификатора
            pos=getAdapterPosition();

            Intent intent = NotePagerActivity.newIntent(getActivity(), mOneNote.getId()); //вызываем NotePagerActivity

            startActivity(intent);

        }
    }


    // Класс RecyclerView взаимодействует с адаптером,
    // когда требуется создать новый объект ViewHolder или связать
    // существующий объект ViewHolder с объектом OneNote
    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> { // NoteAdapter знает все о OneNote (заметка)

        private List<Note> mNotes; // список заметок

        public NoteAdapter(List<Note> notes) {

            mNotes = notes;
        }

        /**
         * onCreateViewHolder вызывается виджетом RecyclerView,
         * когда ему требуется новое представление для отображения элемента
         * (сколько элементов на экране отображается столько и вызывается раз этот метод )
         *
         * @param viewGroup
         * @param i
         * @return
         */
        @Override
        public NoteHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new NoteHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(NoteHolder noteHolder, int position) {

            // вызываем метод для связывания

            Note oneNote = mNotes.get(position);

            noteHolder.bind(oneNote);


        }

        @Override
        public int getItemCount() {
            return mNotes.size(); // количество запрашивает
        }


        public void setNotes(List<Note> notes) {
            mNotes = notes;
        }

    }


}
