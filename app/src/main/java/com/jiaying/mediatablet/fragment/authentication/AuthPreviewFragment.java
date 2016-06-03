package com.jiaying.mediatablet.fragment.authentication;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cylinder.www.facedetect.FdAuthActivity;
import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.net.signal.RecSignal;
import com.jiaying.mediatablet.net.state.stateswitch.TabletStateContext;


public class AuthPreviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FdAuthActivity fdAuthActivity;

    private AuthenticationThread authenticationThread;

    private OnAuthFragmentInteractionListener mListener;

    public AuthPreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthPreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthPreviewFragment newInstance(String param1, String param2) {
        AuthPreviewFragment fragment = new AuthPreviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        authenticationThread = new AuthenticationThread();
        fdAuthActivity = new FdAuthActivity(this, 1);
        fdAuthActivity.onCreate(view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnEvaluationFragmentListener) {
//            mListener = (OnEvaluationFragmentListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnEvaluationFragmentListener");
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fdAuthActivity != null) {
            fdAuthActivity.onResume();
        }
        authenticationThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        authenticationThread.interrupt();
        if (fdAuthActivity != null) {
            fdAuthActivity.onPause();
        }

    }


    @Override
    public void onStop() {
        super.onStop();

        if (fdAuthActivity != null) {
            fdAuthActivity.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (fdAuthActivity != null) {
            fdAuthActivity.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (fdAuthActivity != null) {
            fdAuthActivity.onDestroy();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class AuthenticationThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                while (!isInterrupted()) {

                    if (fdAuthActivity.isFaceAuthentication()) {

                        Log.e("auth", "人脸通过");
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.AUTHPASS);

                        break;
                    } else {
                        Log.e("auth", "人脸未通过");
                    }
                    sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
<<<<<<< HEAD
     * <p>
=======
     * <p/>
>>>>>>> master
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAuthFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAuthFragmentInteraction(RecSignal recSignal);
    }


}
