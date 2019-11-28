package com.shiva.ayapadeeksha.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiva.ayapadeeksha.R;
import com.shiva.ayapadeeksha.Utils.AppConstants;

public class ImportanceofDeekshaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public ImportanceofDeekshaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_importanceof_deeksha, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onResume() {
        super.onResume();
        AppConstants.FRAGMENT_NAME="Importance of Deeksha";
        getActivity().setTitle("Importance of Deeksha");
    }
}
