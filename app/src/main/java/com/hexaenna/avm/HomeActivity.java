package com.hexaenna.avm;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hexaenna.avm.download.FileDownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    TextView txtView,txtDownload,txtPercentage;
    private WebView webHome;
    LinearLayout ldtCertification,ldtViewDownload,ldtProgressBar;
    String fromWhere = "";
    ScrollView scrMainView;
    TextView txtToolbarText;
    ProgressBar progressBar;
    final String TAG = "DPG";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    private int progressStatus = 0;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);


        scrMainView = (ScrollView) findViewById(R.id.scrMain);

        txtToolbarText = (TextView) findViewById(R.id.txtToolbarText);
        ldtCertification = (LinearLayout) findViewById(R.id.ldtCertification);
        webHome = (WebView) findViewById(R.id.webHome);

        txtView = (TextView) findViewById(R.id.txtView);
        txtView.setOnClickListener(this);

        txtDownload = (TextView) findViewById(R.id.txtDownload);
        txtDownload.setOnClickListener(this);

        txtPercentage = (TextView) findViewById(R.id.txtPercentage);

        ldtProgressBar = (LinearLayout) findViewById(R.id.ldtProgressBar);
        ldtProgressBar.setVisibility(View.GONE);
        ldtViewDownload =(LinearLayout) findViewById(R.id.ldtViewDownload);
        ldtViewDownload.setOnClickListener(this);

        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null)
        {
            Log.e("from",extrasBundle.getString("where"));
            fromWhere = extrasBundle.getString("where");
        }

        if (fromWhere.contains("home"))
        {
            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/home.html");

            ldtCertification.setVisibility(View.GONE);
            webHome.setBackgroundColor(Color.TRANSPARENT);
            txtToolbarText.setText("Home");
            ldtProgressBar.setVisibility(View.GONE);
            ldtViewDownload.setVisibility(View.GONE);
        }else if (fromWhere.contains("certificate"))
        {
            txtToolbarText.setText("Certificate");
            ldtCertification.setVisibility(View.VISIBLE);
            webHome.setVisibility(View.GONE);
            ldtProgressBar.setVisibility(View.GONE);
            ldtViewDownload.setVisibility(View.VISIBLE);
        }else if (fromWhere.contains("otherServices"))
        {
            txtToolbarText.setText("Other Services");
            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/otherservices.html");
            webHome.setBackgroundColor(Color.TRANSPARENT);
            ldtCertification.setVisibility(View.GONE);
            ldtProgressBar.setVisibility(View.GONE);
            ldtViewDownload.setVisibility(View.GONE);
        }else if (fromWhere.contains("sales")){
            txtToolbarText.setText("Sales");
            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/sales.html");
            webHome.setBackgroundColor(Color.TRANSPARENT);
            ldtCertification.setVisibility(View.GONE);
            ldtProgressBar.setVisibility(View.GONE);
            ldtViewDownload.setVisibility(View.GONE);

        }else if (fromWhere.contains("repair")){


            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/repair.html");
            txtToolbarText.setText("Repair");
            webHome.setBackgroundColor(Color.TRANSPARENT);
            ldtCertification.setVisibility(View.GONE);
            ldtProgressBar.setVisibility(View.GONE);
            ldtViewDownload.setVisibility(View.GONE);

           /* ldtOtherServices.setVisibility(View.GONE);
            ldtCalibration.setVisibility(View.GONE);*/
        }else if (fromWhere.contains("calibration")){
            txtToolbarText.setText("Calibration");
            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/calibration.html");
            webHome.setBackgroundColor(Color.TRANSPARENT);
            ldtCertification.setVisibility(View.GONE);
            ldtProgressBar.setVisibility(View.GONE);
            ldtViewDownload.setVisibility(View.GONE);
           /* ldtOtherServices.setVisibility(View.GONE);
            ldtCalibration.setVisibility(View.GONE);*/

        }else if (fromWhere.contains("contact"))
        {
            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/contact.html");
            txtToolbarText.setText("Contact Us");
            webHome.setBackgroundColor(Color.TRANSPARENT);
            ldtCertification.setVisibility(View.GONE);
            ldtProgressBar.setVisibility(View.GONE);
            ldtViewDownload.setVisibility(View.GONE);
        }


        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= 23) {

            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);
            } else {
//            Toast.makeText(getApplicationContext(),"Permissions already granted.", Toast.LENGTH_LONG).show();
            }
        }
            progressBar = (ProgressBar) findViewById(R.id.progressBarPercen);
            progressBar.setIndeterminate(false);
            progressBar.setProgress(0);
            progressBar.setMax(100);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ldtViewDownload:
                download(v);
                break;

        }
    }

    public void download(View v)
    {
        boolean fileExists =  new File(Environment.getExternalStorageDirectory() + "/avm/" + "NABL_CERT_AND_SCOPE.pdf").isFile();
        if (fileExists)
            view(v);
        else {
            ldtProgressBar.setVisibility(View.VISIBLE);
            new DownloadFile().execute("http://avmlabs.in/images/download/NABL_CERT_AND_SCOPE.pdf", "NABL_CERT_AND_SCOPE.pdf");
        }

//        new DownloadFile().execute("http://avmlabs.in/images/download/NABL_CERT_AND_SCOPE.pdf", "NABL_CERT_AND_SCOPE.pdf");
    }

    public void view(View v)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/avm/" + "NABL_CERT_AND_SCOPE.pdf");
        Uri path=null;
        if (Build.VERSION.SDK_INT >= 24) {
        path = FileProvider.getUriForFile(getApplicationContext(), "com.hexaenna.avm", pdfFile);
        } else {
        path = Uri.fromFile(pdfFile);
        }

          // -> filename = maven.pdf
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(HomeActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Integer, Void> {

        private static final int  MEGABYTE = 1024 * 1024;
        public  float per = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "avm");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                int downloadedSize = 0;


                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    per = ((float) downloadedSize / totalSize) * 100;
                    Integer percentage = (int) per;

                    publishProgress(percentage);
                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            values[0] = Math.round(values[0]);
            Log.e("percentage", String.valueOf(values[0]));

            progressBar.setProgress(values[0]);
            txtPercentage.setText(String.valueOf(values[0]) + "%");

            if (values[0] == 100)
            {
                ldtProgressBar.setVisibility(View.GONE);
            }
        }

        /* @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            Log.e("percentage", String.valueOf(percentage));
            progressBar.setProgress(percentage);
        }*/
    }


    //permission for read and write storage

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            String msg = "These permissions are mandatory for the application. Please allow access.";
                            showMessageOKCancel(msg,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Permissions garanted.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


}
