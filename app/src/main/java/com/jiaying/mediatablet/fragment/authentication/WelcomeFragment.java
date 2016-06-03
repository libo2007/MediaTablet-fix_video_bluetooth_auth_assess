package com.jiaying.mediatablet.fragment.authentication;

import android.app.Activity;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.jiaying.mediatablet.R;

import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.fragment.BaseFragment;
import com.jiaying.mediatablet.graphics.font.AbstractTypeface;
import com.jiaying.mediatablet.graphics.font.AbstractTypefaceCreator;
import com.jiaying.mediatablet.graphics.font.XKTypefaceCreator;
import com.jiaying.mediatablet.utils.MyLog;

/*
欢迎献浆员
 */


public class WelcomeFragment extends BaseFragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AbstractTypeface XKface;
    private AbstractTypefaceCreator xKtypefaceCreator;

    String title;

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
//            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
//            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
//            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),
//                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
//                showTip("播放完成");
            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
                MyLog.e("ERROR", "播放完成：" + error.getPlainDescription(true));

            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WelcomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeFragment newInstance(String param1, String param2) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WelcomeFragment() {
        // Required empty public constructor
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
        View viewRoot = inflater.inflate(R.layout.fragment_welcome_plasm, container, false);

        // Generate the typeface.
        xKtypefaceCreator = new XKTypefaceCreator();
        XKface = xKtypefaceCreator.createTypeface(getActivity());


        TextView tv_name = (TextView) viewRoot.findViewById(R.id.tv_name);

        if ("男".equals(DonorEntity.getInstance().getIdentityCard().getGender())) {
            title = "先生";
        } else {
            title = "女士";
        }

        tv_name.setText(mParam1 + "(" + title + ")" + ":");
        tv_name.setTypeface(XKface.getTypeface());

        // Set the slogan text view.
        TextView SloganTextView = (TextView) viewRoot.findViewById(R.id.slogan_text_view);
        SloganTextView.setTypeface(XKface.getTypeface());
        SloganTextView.setText(mParam2);

        // Set the welcome text view.
        TextView welcomeTextView = (TextView) viewRoot.findViewById(R.id.welcome_text_view);
        welcomeTextView.setTypeface(XKface.getTypeface());
        welcomeTextView.setText("\n欢迎您来献浆。\n");

        return viewRoot;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {

                play(mParam1 + title + "欢迎您来献浆。" + mParam2, mTtsListener);
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
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
        public void onFragmentInteraction(Uri uri);
    }


}
