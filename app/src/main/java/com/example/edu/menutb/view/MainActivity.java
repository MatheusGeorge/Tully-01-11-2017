package com.example.edu.menutb.view;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ProfileController;
import com.example.edu.menutb.data.TullyOpenHelper;
import com.example.edu.menutb.view.configure.ConfigureActivity;
import com.example.edu.menutb.view.login.LoginActivity;
import com.example.edu.menutb.view.map.ChallengeActivity;
import com.example.edu.menutb.view.notification.NotificationAcitivity;
import com.example.edu.menutb.view.profile.ProfileActivity;
import com.example.edu.menutb.view.ranking.RankingActivity;
import com.example.edu.menutb.view.search.SearchActivity;
import com.example.edu.menutb.view.timeline.TimelineActivity;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TullyOpenHelper mDbOpenHelper;
    private TextView textViewNomeNav;
    boolean isLastFragmentSupportType = false;
    boolean lastFragmentShowed = false;
    String lastFragmentTag;

    ImageView imageViewProfileMenu;

    Context context = this;

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_menu);
        mDbOpenHelper = new TullyOpenHelper(this);
        SharedPreferences idBusca = getSharedPreferences("id", Context.MODE_PRIVATE);
        String resultId = idBusca.getString("id", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTully);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Menu lateral
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //customizando o título
        Menu menu = navigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.menuTitle);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.MenuTitle), 0, s.length(), 0);
        tools.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        textViewNomeNav = (TextView) headerLayout.findViewById(R.id.textViewNomeNav);
        imageViewProfileMenu = (ImageView) headerLayout.findViewById(R.id.imageViewProfileMenu);
        loadProfileData(resultId);

        //adiciona o ícone na action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //TROCAR A FONTE
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/amaticscbold.ttf");
        TextView textToolBarTitle = (TextView) findViewById(R.id.toolbarTitle);
        textToolBarTitle.setTypeface(type);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new TimelineActivity()).commit();
    }

    private void loadProfileData(final String resultId) {
        AsyncTask task = new AsyncTask() {
            @Override
            protected String[] doInBackground(Object[] params) {
                final String[] val= new ProfileController().selectProfileToMenu(resultId, getBaseContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setaOsDados(val[0], val[1]);
                    }
                });
                return null;
            }
        };
        task.execute();
    }

    private void setaOsDados(String name, String photo) {
        textViewNomeNav.setText(String.valueOf(name));
        new LoadImageTask(imageViewProfileMenu).execute(String.valueOf(photo));
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return new ProfileController().loadProfileImage(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;
        android.support.v4.app.FragmentTransaction fragmentTransactionv4;

        //aaaaaaaaa
        if (id == R.id.navTimeline) {
            if(isLastFragmentSupportType && lastFragmentShowed){
                android.support.v4.app.Fragment fr_v4 = getSupportFragmentManager().findFragmentByTag(lastFragmentTag);
                getSupportFragmentManager().beginTransaction().remove(fr_v4).commit();
            }
            fragmentTransaction =  fragmentManager.beginTransaction().replace(R.id.content_frame, new TimelineActivity(), "TAG1");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            lastFragmentTag = "TAG1";
            lastFragmentShowed = true;
            isLastFragmentSupportType = false;
        } else if (id == R.id.navChallenges) {
            if(isLastFragmentSupportType && lastFragmentShowed){
                android.support.v4.app.Fragment fr_v4 = getSupportFragmentManager().findFragmentByTag(lastFragmentTag);
                getSupportFragmentManager().beginTransaction().remove(fr_v4).commit();
            }
            fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.content_frame, new ChallengeActivity(), "TAG2");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            lastFragmentTag = "TAG2";
            lastFragmentShowed = true;
            isLastFragmentSupportType = false;
        } else if (id == R.id.navProfile) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            /*fragmentTransactionv4 = getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ProfileActivity(), "TAG5");
            fragmentTransactionv4.addToBackStack(null);
            fragmentTransactionv4.commit();
            lastFragmentTag = "TAG5";
            lastFragmentShowed = true;
            isLastFragmentSupportType = true;*/
        } else if (id == R.id.navNotifications) {
            if(isLastFragmentSupportType && lastFragmentShowed){
                android.support.v4.app.Fragment fr_v4 = getSupportFragmentManager().findFragmentByTag(lastFragmentTag);
                getSupportFragmentManager().beginTransaction().remove(fr_v4).commit();
            }
            fragmentTransaction =  fragmentManager.beginTransaction().replace(R.id.content_frame, new NotificationAcitivity(), "TAG3");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            lastFragmentTag = "TAG3";
            lastFragmentShowed = true;
            isLastFragmentSupportType = false;
        } else if (id == R.id.navConfigure) {
            if(isLastFragmentSupportType && lastFragmentShowed){
                android.support.v4.app.Fragment fr_v4 = getSupportFragmentManager().findFragmentByTag(lastFragmentTag);
                getSupportFragmentManager().beginTransaction().remove(fr_v4).commit();
            }
            fragmentTransaction =  fragmentManager.beginTransaction().replace(R.id.content_frame, new ConfigureActivity(), "TAG5");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            lastFragmentTag = "TAG5";
            lastFragmentShowed = true;
            isLastFragmentSupportType = false;
        } else if (id == R.id.navRanking) {
            if(isLastFragmentSupportType && lastFragmentShowed){
                android.support.v4.app.Fragment fr_v4 = getSupportFragmentManager().findFragmentByTag(lastFragmentTag);
                getSupportFragmentManager().beginTransaction().remove(fr_v4).commit();
            }
            fragmentTransaction =  fragmentManager.beginTransaction().replace(R.id.content_frame, new SearchActivity(), "TAG4");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            lastFragmentTag = "TAG4";
            lastFragmentShowed = true;
            isLastFragmentSupportType = false;
        } else if (id == R.id.navRankingg) {
            if(isLastFragmentSupportType && lastFragmentShowed){
                android.support.v4.app.Fragment fr_v4 = getSupportFragmentManager().findFragmentByTag(lastFragmentTag);
                getSupportFragmentManager().beginTransaction().remove(fr_v4).commit();
            }
            fragmentTransaction =  fragmentManager.beginTransaction().replace(R.id.content_frame, new RankingActivity(), "TAG6");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            lastFragmentTag = "TAG6";
            lastFragmentShowed = true;
            isLastFragmentSupportType = false;
        } else if (id == R.id.navSignOut) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
            final View alterarView = layoutInflaterAndroid.inflate(R.layout.side_menu_dialog_signout, null);
            TextView textView = (TextView) alterarView.findViewById(R.id.dialogTitle);
            textView.setTextColor(getResources().getColor(R.color.colorBranco));
            textView.setText(MainActivity.this.getString(R.string.youSureSignOut));
            AlertDialog.Builder dialogName = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
            dialogName.setView(alterarView);
            dialogName.setCancelable(false).setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    SharedPreferences idGrava = getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorId = idGrava.edit();
                    editorId.clear();
                    editorId.commit();
                    Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(myIntent);
                }
            }).setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogBox, int id) {
                    dialogBox.cancel();
                }
            });
            AlertDialog dialogFinalName = dialogName.create();
            dialogFinalName.show();
            dialogFinalName.getButton(dialogFinalName.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBranco));
            dialogFinalName.getButton(dialogFinalName.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBranco));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}