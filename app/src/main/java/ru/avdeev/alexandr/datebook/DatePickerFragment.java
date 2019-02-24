package ru.avdeev.alexandr.datebook;

/*
DatePickerFragment для отображения даты во фрагменте

 */

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE ="date"; // константа для передачи даты
    private static final String ARG_DATE = "date"; // константа для получения даты во фрагмент
    private DatePicker mDatePicker;     // объект выбора даты
    private TimePicker mTimePicker;


    /**
     *  метод newInstance СОЗДАНИЕ АРГУМЕНТОВ ФРАГМЕНТА
     *  ЗАМЕНЯЕМ КОНСТРУКТОР КЛАССА НА newInstance
     *  при вызове инициализируется этот метод newInstance( с датой ) вместо конструктора
     *  по умолчанию
     *
     * @param date передаем дату
     * @return
     */
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date); // и присваиваение значение даты
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * метод onCreateDialog создает Alert Dialog
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // получим дату и приведем ее к типу объекта DatePicker
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        // заполняем представление
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);



        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker); // найдем объект DatePicker(выбор даты)
        mDatePicker.init(year, month, day, null); // инициализируем объект выбора даты


        return new AlertDialog.Builder(getActivity()) //передаем объект Context конструктору AlertDialog.Builder

                .setView(v)                         // устанавливаем это представление
                .setTitle(R.string.date_picker_title) // устанавливаем заголовок в окне даты выбора времени
                //    .setPositiveButton(android.R.string.ok,null) // кнопка при выборе даты (OK - закрывает дилоговое окно)

                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();   // получаем год
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).
                                        getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                .create();  //возвращает настроенный экземпляр AlertDialog
    }

    /**
     * метод, который создает интент, помещает в него дату как дополнение,
     * а затем вызывает NoteFragment.onActivityResult(…) ( ЦЕЛЕВОЕ). к кому адресовано !!!
     * @param resultCode
     * @param date
     */

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();      // создаем интент
        intent.putExtra(EXTRA_DATE, date); // помещаем в него дату как дополнение
        getTargetFragment()     // получаем целевой фрагмент
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
