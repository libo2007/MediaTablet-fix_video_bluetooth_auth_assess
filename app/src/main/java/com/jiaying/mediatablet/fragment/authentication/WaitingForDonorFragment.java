package com.jiaying.mediatablet.fragment.authentication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.graphics.font.AbstractTypeface;
import com.jiaying.mediatablet.graphics.font.AbstractTypefaceCreator;
import com.jiaying.mediatablet.graphics.font.XKTypefaceCreator;

/*
等待献浆员
 */
public class WaitingForDonorFragment extends Fragment {
    private static final String ARG_PARAM1 = "slogan";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AbstractTypeface XKface;
    private AbstractTypefaceCreator typefaceCreator;

    public static WaitingForDonorFragment newInstance(String param1, String param2) {
        WaitingForDonorFragment fragment = new WaitingForDonorFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Given the typeface, we should construct a factory pattern for these type face.
        typefaceCreator = new XKTypefaceCreator();
        XKface = typefaceCreator.createTypeface(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiting_plasm, container, false);
        TextView textViewSlogan = (TextView) view.findViewById(R.id.tv_slogan);

//        textViewSlogan.setText(mParam1);

        textViewSlogan.setTypeface(XKface.getTypeface());
        return view;
    }
}
