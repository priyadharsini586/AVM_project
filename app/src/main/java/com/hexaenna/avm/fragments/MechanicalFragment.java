package com.hexaenna.avm.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.avm.R;
import com.hexaenna.avm.adapter.GridViewAdapter;
import com.hexaenna.avm.api.ApiClient;
import com.hexaenna.avm.api.ApiInterface;
import com.hexaenna.avm.model.Login;
import com.hexaenna.avm.model.ProductResponse;
import com.hexaenna.avm.utils.Constants;
import com.hexaenna.avm.utils.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MechanicalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MechanicalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MechanicalFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ApiInterface apiInterface;
    String isConnection = null;
    FrameLayout rldMainLayout;
    TSnackbar snackbar;
    View snackbarView;
    NetworkChangeReceiver networkChangeReceiver;
    View mainView;
    private OnFragmentInteractionListener mListener;
    GridView gridView;
    ArrayList imageUrl = new ArrayList();

    public MechanicalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MechanicalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MechanicalFragment newInstance(String param1, String param2) {
        MechanicalFragment fragment = new MechanicalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainView = inflater.inflate(R.layout.fragment_mechanical, container, false);
        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);


                if (isConnection == null) {
                    Bundle b = intent.getExtras();
                    isConnection = b.getString(Constants.MESSAGE);
                    Log.e("newmesage", "connection connection");
                    getNetworkState();
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        getActivity().registerReceiver(networkChangeReceiver,
                intentFilter);

        rldMainLayout = (FrameLayout) mainView.findViewById(R.id.rldMainLayout);
        gridView = (GridView) mainView.findViewById(R.id.gridView);
        getData();
        return mainView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public OnFragmentInteractionListener getmListener() {
        return mListener;
    }




    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Action Button", "onClick triggered");
                            }
                        });
                snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
                snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                textView.setTypeface(null, Typeface.BOLD);
                snackbar.show();


            }else if (isConnection.equals(Constants.NETWORK_CONNECTED))
            {
                if (snackbar != null)
                {
                    snackbar.dismiss();
                }

                getData();
            }
        }

    }

    private void getData() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("cat", "Mechanical");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<ProductResponse> call = apiInterface.productRequest(jsonObject);

                call.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if (response.isSuccessful()) {
                            ProductResponse login = response.body();

                            if (login.getStatus_code() != null) {

                                Log.e("output from splash", String.valueOf(login.getImage_array()));
                                ArrayList dumyArraylist = login.getImage_array();
                                for (int i=0 ; i < dumyArraylist.size() ; i ++)
                                {
                                    String url = (String) dumyArraylist.get(i);
                                    imageUrl.add(login.getImage_path()+url);
                                }

                                GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity(),imageUrl);

                                //Adding adapter to gridview
                                gridView.setAdapter(gridViewAdapter);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Log.e("output", t.getMessage());
                    }
                });


            } else {
                snackbar = TSnackbar
                        .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Action Button", "onClick triggered");
                                getData();
                            }
                        });
                snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
                snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                textView.setTypeface(null, Typeface.BOLD);
                snackbar.show();
            }
        }
    }
}
