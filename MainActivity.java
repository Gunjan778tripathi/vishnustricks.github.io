package com.appname.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String ver = "1.0";
    String[] versapp = ver.split("\\.");
    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("TECH MOBS");
        setSupportActionBar(toolbar);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.prb);
        wv = (WebView) this.findViewById(R.id.webview);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url)
            {
                if(url.contains("tipsandtricksonandroid.blogspot")) {
                    webView.loadUrl(url);
                } else startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.getAllowContentAccess();
        webSettings.getAllowFileAccess();
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        if(isInternetAvailable()) {
            wv.loadUrl("https://tipsandtricksonandroid.blogspot.com/");
        } else {
            progressBar.setVisibility(View.GONE);
            wv.setVisibility(View.GONE);
            ImageView tv = (ImageView) findViewById(R.id.nointv);
            tv.setVisibility(View.VISIBLE);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("message/rfc822");
                intent2.putExtra("android.intent.extra.EMAIL", new String[]{"techmobs.in@gmail.com"});
                intent2.putExtra("android.intent.extra.SUBJECT", "TECH MOBS v" + ver);
                intent2.putExtra("android.intent.extra.TEXT", "");
                try {
                    startActivity(Intent.createChooser(intent2, "Report..."));
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Can't find email client.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupNavigationHeader(navigationView);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Exit");
            alertDialogBuilder
                    .setMessage("Do you really want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    }).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_web) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://tipsandtricksonandroid.blogspot.in")));
        } else if (id == R.id.action_fb) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/tipsandtricksonandroid/")));
        } else if (id == R.id.action_gplus) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://plus.google.com/+VISHNUS%20TRICKS")));
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_youtube) {
            wv.loadUrl("https://www.youtube.com/c/vishnusivadas");
        } else if (id == R.id.nav_exit) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle((CharSequence) "Exit");
            alertDialogBuilder.setMessage((CharSequence) "Do you really want to exit?") .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    }).show();
        }
        else if (id == R.id.nav_home) {
            this.wv.loadUrl("https://tipsandtricksonandroid.blogspot.in/");
        }else if (id == R.id.nav_blog) {
            this.wv.loadUrl("https://tipsandtricksonandroid.blogspot.in/search?max-result=10");
        }else if (id == R.id.nav_about) {
            this.wv.loadUrl("http://tipsandtricksonandroid.blogspot.in/p/about.html");
        } else if (id == R.id.nav_contact) {

            this.wv.loadUrl("http://tipsandtricksonandroid.blogspot.in/p/contact.html");
        }
        else if (id == R.id.nav_whatsapp) {
            wv.loadUrl("https://chat.whatsapp.com/6NuAILVAIVjEjP6MaC0rCR");

        }
        else if (id == R.id.nav_share) {
            final String string = "TECH MOBS - Official Application v"+ ver + " :\n" +
                    "\n" +
                    " 1. Hacked Games\n" +
                    " 2. Hacked Apps\n" +
                    " 3. Hacking Tricks\n" +
                    " 4. Tutorials On Programming \n" +
                    " 5. Social Media Tricks\n" +
                    " 6. Wi-Fi Hacking Tutorial\n" +
                    " 7. Contact Us Faster\n" +
                    " 8. Email Me \n" +
                    " 9. Find Me On Facebook\n" +
                    "10. Easy Donate Option \n" +
                    "11. Best Browsing Experience \n" +
                    "12. No Advertisements\n "+
                    "13. Automatic Update Notifier\n" +
                    "Download Our Application At : https://tipsandtricksonandroid.blogspot.in/p/techmob-application.html\n"+
                    "\n"+
                    "Share It To All And Enjoy ....:)";
            final Intent intent3 = new Intent("android.intent.action.SEND");
            intent3.setType("text/plain");
            intent3.putExtra("android.intent.extra.SUBJECT", "TECH MOBS");
            intent3.putExtra("android.intent.extra.TEXT", string);
            startActivity(Intent.createChooser(intent3, "Share"));


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BACK)&& wv.canGoBack())
        {
            wv.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //other
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setupNavigationHeader(NavigationView mNavigationView) {
        View headerView = mNavigationView.getHeaderView(0);
        TextView navAppVersion = (TextView) headerView.findViewById(R.id.tv);
        try {
            navAppVersion.setText("Developer : Vishnu Sivadas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
