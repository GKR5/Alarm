package ru.avdeev.alexandr.datebook;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/*

 класс MainActivity хостинг для фрагментов

 */

public class MainActivity extends AppCompatActivity  {


    private DrawerLayout mDrawerLayout; // выдвижная панель



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mDrawerLayout = findViewById(R.id.drawer_layout);

        //с выходом Android 5.0 (Lollipop). До Lollipop для навигации и размещения действий
        // в приложении рекомендовалось использовать панель действий (action bar).
        // Панель инструментов расширяет возможности панели действий:

        // добавляем кнопку навигацинной выдвижной панели (панель действий (action bar))
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);  // вызов кнопки домой
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // установить выбранный элемент, чтобы сохранить выделение
                        menuItem.setChecked(true);
                        // закрыть выдвижную панель когда элемент выбран
                        mDrawerLayout.closeDrawers();


                        //   смена фрагментов пользовательского интерфейса

                        Fragment fragment;

                        switch (menuItem.getItemId()) {
                            case R.id.list:
                                fragment = new NoteListFragment();
                                break;

                           case R.id.reminder:
                                fragment = new  ReminderFragment();
                                break;

                            default:
                                fragment = new NoteListFragment();

                        }

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .addToBackStack(null)
                                .commit();

                        return true;
                    }
                });

        // Задание фрагмента по умолчанию (список новостей)
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new NoteListFragment())
                .addToBackStack(null)
                .commit();



        // слушатель открытия и закрытия выдвижной панели
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // ответ когда меняется позиция выдвжижной панели

                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                         // ответ когда выдвжижная панель открыта
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // ответ когда выдвжижная панель закрыта
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // ответ когда изменение состояние выдвижной панели
                    }
                }
        );

    }

    /**
     * что бы открыть навигационную панель
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START); // передается как открытая
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
