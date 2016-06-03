package com.jiaying.mediatablet.fragment.end;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.jiaying.mediatablet.R;

import com.jiaying.mediatablet.activity.MainActivity;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.fragment.BaseFragment;
import com.jiaying.mediatablet.graphics.font.AbstractTypeface;
import com.jiaying.mediatablet.graphics.font.AbstractTypefaceCreator;
import com.jiaying.mediatablet.graphics.font.XKTypefaceCreator;
import com.jiaying.mediatablet.net.signal.RecSignal;


/*
结束欢送页面
 */
public class EndFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View view;
    private TextView sloganTextView, thanksTextView;
    private String slogan;
    private String thanks;
    private MainActivity mainActivity;
    private AbstractTypeface xKface;
    private AbstractTypefaceCreator xKTypefaceCreator;
    String title;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EndFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EndFragment newInstance(String param1, String param2) {
        EndFragment fragment = new EndFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EndFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_over, container, false);

        xKTypefaceCreator = new XKTypefaceCreator();
        xKface = xKTypefaceCreator.createTypeface(getActivity());


        slogan = getActivity().getString(R.string.slogantwoabove);
        thanks = mParam1 + ", " + getActivity().getString(R.string.slogantwoabelow);

        // Generate the typeface
        AbstractTypefaceCreator abstractTypefaceCreator = new XKTypefaceCreator();
        AbstractTypeface abstractTypeface = abstractTypefaceCreator.createTypeface(getActivity());

        // Set these text views

        if ("男".equals(DonorEntity.getInstance().getIdentityCard().getGender())) {
            title = "先生";
        } else {
            title = "女士";
        }
        sloganTextView = (TextView) view.findViewById(R.id.end_slogan_text_view);
        sloganTextView.setText(DonorEntity.getInstance().getIdentityCard().getName() + "(" + title + ")" + ":");
        sloganTextView.setTypeface(xKface.getTypeface());

        thanksTextView = (TextView) view.findViewById(R.id.end_thanks_text_view);
//        thanksTextView.setText(thanks);
        thanksTextView.setTypeface(xKface.getTypeface());

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                play(DonorEntity.getInstance().getIdentityCard().getName() + title + "感谢您的爱心！祝您健康快乐！期待您的再次献浆！", mTtsListener);
            }
        }).start();

    }

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
                mainActivity.getTabletStateContext().handleMessge(mainActivity.getRecordState(), mainActivity.getObservableZXDCSignalListenerThread(), null, null, RecSignal.CHECKSTART);
            } else if (error != null) {
//                showTip(error.getPlainDescription(true));
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

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
