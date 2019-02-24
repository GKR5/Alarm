package ru.avdeev.alexandr.datebook;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


/*

 класс ReminderFragment напоминалка о каком либо событии будет устанавливаться в нем будильник
 */
public class ReminderFragment extends Fragment {


    // переменные

    Calendar calendar;          // календарь

    private String mHour;      // часы

    private String mMin;      // минуты

    private Button buttonOff;  // отключить будильник

    private Button buttonOn;  // включить будильник

    private TextView textView;  // текстовое поле

   Context context;

    private TimePicker mtimePicker;



    private Alarm alarm;  // будильник


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       alarm=new Alarm();
       context=getContext();

    }

    /**
     * СОЗДАНИЕ И НАСТРОЙКА ПРЕДСТАВЛЕНИЯ ФРАГМЕНТА
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.reminder_fragment, container, false);

        // найдем время
        mtimePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);


        calendar = Calendar.getInstance(); // инициализируем календарь

        // устанавливаем текущее время в picker
        mtimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mtimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

        // найдем текстовое поле

        textView = (TextView) view.findViewById(R.id.textView);

        // найдем кнопку включить

        buttonOn = (Button) view.findViewById(R.id.buttonOn);

        // найдем кнопку выключить

        buttonOff = (Button) view.findViewById(R.id.buttonOff);





        mtimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                mHour = String.valueOf(hourOfDay);
                mMin = String.valueOf(minute);

/*
                if (hourOfDay >12){

                    mHour=String.valueOf(hourOfDay-12);
                }
*/
                if (minute < 10) {

                    mMin = "0" + String.valueOf(mMin);
                }
            }
        });


        buttonOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // по щелчку устанавливаем новое время !!!

                setTxt("Будильник включен " + mHour + ":" + mMin);

                alarm.getCurrentTime(mtimePicker , calendar );

                alarm.scheduleAlarm(context , calendar);

                

            }
        });


        buttonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTxt("Будильник выключен");
            }
        });

        return view;
    }

    /**
     * устанавливаем текст в поле
     * @param s строка
     */
    private void setTxt(String s) {

        textView.setText(s);

    }


}
