package com.hexaenna.avm;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ldtNABL,ldtLABCat,ldtAVMSinglePage,ldtCalibrationWheel,ldtWRD;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    TextView txtPercentage;
    ProgressBar progressBar;
    LinearLayout ldtProgressBar;
    private String TAG = "display";
    boolean isCompleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ldtNABL = (LinearLayout) findViewById(R.id.ldtNABL);
        ldtNABL.setOnClickListener(this);

        ldtWRD = (LinearLayout) findViewById(R.id.ldtWRD);
        ldtWRD.setOnClickListener(this);

        ldtLABCat = (LinearLayout) findViewById(R.id.ldtLABCat);
        ldtLABCat.setOnClickListener(this);

        ldtAVMSinglePage = (LinearLayout) findViewById(R.id.ldtAVMSinglePage);
        ldtAVMSinglePage.setOnClickListener(this);

        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) findViewById(R.id.progressDownload);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }else
        {
            progressBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }

        ldtProgressBar = (LinearLayout) findViewById(R.id.ldtProgressDownload);
        ldtProgressBar.setVisibility(View.GONE);

        ldtCalibrationWheel = (LinearLayout) findViewById(R.id.ldtCalibrationWheel);
        ldtCalibrationWheel.setOnClickListener(this);

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

    }


    @Override
    public void onClick(View v) {
        ldtProgressBar.setVisibility(View.GONE);
        switch (v.getId())
        {
            case R.id.ldtNABL:
                download("http://avmlabs.in/images/download/NABL_CERT_AND_SCOPE.pdf","NABL_CERT_AND_SCOPE.pdf");
                break;
            case R.id.ldtLABCat:
                download("http://avmlabs.in/images/download/AVM_CATALOGUE_2017.pdf","AVM_CATALOGUE_2017.pdf");
                break;
            case R.id.ldtAVMSinglePage:
                downloadImage("http://avmlabs.in/images/download/Catalogue.jpg","catalogue.jpg");
                break;

            case R.id.ldtCalibrationWheel:
                downloadImage("http://avmlabs.in/images/download/CalibrationonWheelsBrochure.jpg","calibrationonWheels.jpg");
                break;

            case R.id.ldtWRD:
                download("http://avmlabs.in/images/download/WRG_CERT.pdf","WRG_CERT.pdf");
                break;
        }
    }


    public void download(String url,String pdfName)
    {
        boolean fileExists =  new File(Environment.getExternalStorageDirectory() + "/avm/" +  pdfName).isFile();
        if (fileExists)
            view(pdfName);
        else {
            ldtProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            new DownloadFile().execute(url,pdfName);
        }

//        new DownloadFile().execute("http://avmlabs.in/images/download/NABL_CERT_AND_SCOPE.pdf", "NABL_CERT_AND_SCOPE.pdf");
    }

    public void downloadImage(String url,String pdfName)
    {
        boolean fileExists =  new File(Environment.getExternalStorageDirectory() + "/avm/" + pdfName).isFile();
        if (fileExists)
            viewImage(pdfName);
        else {
            ldtProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            new DownloadFile().execute(url,pdfName );

        }

//        new DownloadFile().execute("http://avmlabs.in/images/download/NABL_CERT_AND_SCOPE.pdf", "NABL_CERT_AND_SCOPE.pdf");
    }


    public  void  viewImage(String fileName)
    {
        /*Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + "/avm/catalogue.jpg"), "image*//*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/avm/" + fileName);
        Uri path=null;
        if (Build.VERSION.SDK_INT >= 24) {
            path = FileProvider.getUriForFile(getApplicationContext(), "com.hexaenna.avm", pdfFile);
        } else {
            path = Uri.fromFile(pdfFile);
        }

        // -> filename = maven.pdf
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "image/*");
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(DownloadActivity.this, "No image avilable", Toast.LENGTH_SHORT).show();
        }

    }

    public void view(String pdfName)
    {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/avm/" + pdfName);
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
            Toast.makeText(DownloadActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Integer, Void> {

        private static final int  MEGABYTE = 1024;
        public  float per = 0;
        String fileName = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
             fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File pdfFile = null;

                File folder = new File(extStorageDirectory, "avm");
                folder.mkdir();

                pdfFile = new File(folder, fileName);

                try {
                    pdfFile.createNewFile();
                } catch (IOException e) {
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
                Toast.makeText(getApplicationContext(),"Download Completed",Toast.LENGTH_LONG).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isCompleted = true;

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
    public void onBackPressed() {
//        super.onBackPressed();

        if (ldtProgressBar.getVisibility() == View.VISIBLE)
        {
            Toast.makeText(getApplicationContext(),"File Downloading",Toast.LENGTH_LONG).show();
        }else if (ldtProgressBar.getVisibility() == View.GONE)
        {
            finish();

        }

    }

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
        new AlertDialog.Builder(DownloadActivity.this)
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
