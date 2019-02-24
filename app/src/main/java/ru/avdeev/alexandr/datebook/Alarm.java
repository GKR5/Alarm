package ru.avdeev.alexandr.datebook;


/*

 класс Alarm для одного уведомления по расписанию

 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.UUID;


public class Alarm {

    // переменные
    private UUID mId;                    // уникальный идентификатор дела
    private AlarmManager alarmManager;   // менеджер сигналов
    private PendingIntent pendingIntent; //
    private Intent intent;


// конструктор

    public Alarm() {

        mId = UUID.randomUUID(); // при инициализации сразу уникальный идентификатор
    }


    // геттер

    public UUID getId() {

        return mId;
    }


    /**
     * ПОЛУЧАЕМ ТЕКУЩЕЕ ВРЕМЯ
     *
     * @param timePicker объект выбора времени
     */

    public void getCurrentTime(TimePicker timePicker, Calendar calendar) {

        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour()); // получим выбранное текущее время

        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());   // получим выбранное текущее время




    }


    /**
     * Выполнение задания по расписанию
     * @param context  текущий контекст
     * @param calendar объект календаря
     */

    public void scheduleAlarm(Context context, Calendar calendar) {
        Log.i("vip", "Планирование напоминания");
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // получим системный сервис

        intent = new Intent(context, MyAlarmService.class);

        pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

         // RTC - ориентируются на системное время
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

    }


}
