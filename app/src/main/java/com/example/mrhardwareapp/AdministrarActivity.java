package com.example.mrhardwareapp;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mrhardwareapp.adapters.DialogSalir;
import com.example.mrhardwareapp.fragments.AdminCommentFragment;
import com.example.mrhardwareapp.fragments.AdminReportesFragment;
import com.example.mrhardwareapp.fragments.AdminUsuariosFragment;
import com.example.mrhardwareapp.models.Usuario;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class AdministrarActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    FragmentStateAdapter pageAdapter;
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar);

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        setSupportActionBar(toolbar);


        fragments.add(new AdminUsuariosFragment());
        fragments.add(new AdminCommentFragment());
        fragments.add(new AdminReportesFragment());
        pageAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        viewPager.setAdapter(pageAdapter);


        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.administrar_usuarios);
                            break;
                        case 1:
                            tab.setText(R.string.administrar_comentarios);
                            break;
                        case 2:
                            tab.setText(R.string.reportes);
                            break;
                    }
                }
        ).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.salir) {
            DialogSalir dialogo = new DialogSalir();
            dialogo.show(this.getSupportFragmentManager(), "salir");
            return true;
        } else if (item.getItemId() == R.id.actualizar) {
            int position = viewPager.getCurrentItem();
            fragments = new ArrayList<>();
            fragments.add(new AdminUsuariosFragment());
            fragments.add(new AdminCommentFragment());
            fragments.add(new AdminReportesFragment());
            pageAdapter = new ScreenSlidePagerAdapter(this);
            viewPager.setPageTransformer(new ZoomOutPageTransformer());
            viewPager.setAdapter(pageAdapter);
            viewPager.setCurrentItem(position);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DialogSalir dialogo = new DialogSalir();
        dialogo.show(this.getSupportFragmentManager(), "salir");
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

    public static class ZoomOutPageTransformer implements ViewPager2.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(@NonNull View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1) {
                view.setAlpha(0f);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else {
                view.setAlpha(0f);

            }
        }
    }

    public ArrayList<Fragment> obtenerFragments() {
        return fragments;
    }
}