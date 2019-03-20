package ru.avdeev.alexandr.datebook;


/*


класс NoteFragment - детальный список одного дела

 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class NoteFragment  extends Fragment {


    private Note mOneNote;                   // одно задание/дело
    private EditText mTitleField;            //  заголовок дела
    private Button mDateButton;             // заполнение даты дела будет на кнопке
    private CheckBox mSolvedCheckBox;      //  состояние выполнения (сделано да/нет)
    private EditText mDescField;          //  подробное описание дела / задания
    private Alarm alarm;

    private static final String DIALOG_DATE = "DialogDate"; // константа для метки DatePickerFragment

    private static final String ARG_NOTE_ID = "note_id"; //

    private static final int REQUEST_DATE = 0; // константу для кода запроса для получения информации из DatePickerFragment



    public static NoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);
        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * ЭКЗЕМПЛЯР ФРАГМЕНТА НАСТРАИВАЕТСЯ onCreate()
     *
     * @param savedInstanceState
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); // получение обратных вызовов так как меню создаем во фрагменте

        UUID noteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID); // читаем дополнение

        mOneNote = Notes.get(getActivity()).getNote(noteId); // получаем заметку по которой щелкнули
        alarm=new Alarm();
    }


    /**
     * запись обновлений
     */
    @Override
    public void onPause() {
        super.onPause();

        Notes.get(getActivity()).updateNote(mOneNote);

    }


    /**
     * СОЗДАНИЕ МЕНЮ
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_note, menu);

    }



    /**
     * РЕАКЦИЯ НА ВЫБОР КОМАНДЫ В МЕНЮ
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.delete_note:  // при нажатии удалять заметку с таким id

                Notes.get(getActivity()).deleteNote(mOneNote);

//                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);

        }

    }



    /**
     * СОЗДАНИЕ И НАСТРОЙКА ПРЕДСТАВЛЕНИЯ ФРАГМЕНТА В onCreateView
     * заполение  и подключение элементов макета нужно делать в методе onCreateView()
     *
     * @param inflater           передача идентификатора ресурса макета представления
     * @param container          определяет родителя представления
     * @param savedInstanceState нужно ли включать заполненное представление в родителя
     *                           будет false так как представление будет добавлено в коде активности
     * @return заполненное представление фрагмента
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one_note, container, false);


        mTitleField = (EditText) view.findViewById(R.id.onejob_title); // найдем поле ввода заголовка

        mTitleField.setText(mOneNote.getTitle()); // устанавливаем в контроллере одного фрагмента данные полученные для этого объекта
        // создаем анонимный внутренний класс который реализует интерфейс слушателя TextWatcher
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            /**
             *
             * @param s      - ввод пользователя передается
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mOneNote.setTitle(s.toString()); // возвращает строку которая используется
                // для заполнения заголовка
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // найдем описание подробное для дела
        mDescField = (EditText) view.findViewById(R.id.onejob_multitext);
        mDescField.setText(mOneNote.getDescription()); // получим текст в переменную записанную
        // создаем анонимный внутренний класс который реализует интерфейс слушателя TextWatcher
        mDescField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mOneNote.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // заполнение даты составления дела / задания
        mDateButton = (Button) view.findViewById(R.id.onejob_date);

        updateDate(); // получаем дату

        mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // добавляем вызов newInstance(Date) прикрепленная дата!!!
                DatePickerFragment dialog=DatePickerFragment.newInstance(mOneNote.getDate());

                FragmentManager manager = getFragmentManager(); //DialogFragment находятся под
                // управлением экземпляра FragmentManager активности-хоста
                dialog.setTargetFragment(NoteFragment.this, REQUEST_DATE); // назначаем целевым фрагментом
                dialog.show(manager, DIALOG_DATE); // показать диалоговое окно

            }
        });


        // найдем чекбокс и установим слушателя для него
        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.onejob_solved);
        mSolvedCheckBox.setChecked(mOneNote.isSolved());// устанавливаем в контроллере одного фрагмента данные полученные для этого объекта
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mOneNote.setSolved(isChecked); // проверка нажатия статуса выполнения
            }
        });

        return view;
    }



    /**
     * ВОЗВРАЩАЕМ РЕЗУЛЬТАТ В ЭТОТ МЕТОД onActivityResult
     *  так как используються два фрагмента одного хоста то можем использовать этот метод
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) { // если получаем информацию даты из
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE); // получаем дату

            // Запланировать будильник
            mOneNote.setDate(date); // задаем дату
            updateDate(); // устанавливаем новую дату на кнопку
            Calendar calendar = Calendar.getInstance();

            Calendar plan = Calendar.getInstance();
            plan.setTime(date);
            alarm.getCurrentTime(plan , calendar);

            alarm.scheduleAlarm(getActivity() , calendar);
        }
    }

    /**
     *
     * метод updateDate() обновление даты на кнопке
     */

    private void updateDate()
    {
        mDateButton.setText(mOneNote.getDate().toString());
    }

}
