package com.jiaying.mediatablet.fragment.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.entity.DonorEntity;
import com.jiaying.mediatablet.entity.PersonInfo;
import com.jiaying.mediatablet.fragment.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AuthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ProgressDialog dispatchAuthPassDialog;

    public AuthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthFragment newInstance(String param1, String param2) {
        AuthFragment fragment = new AuthFragment();
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
        View viewRoot = inflater.inflate(R.layout.fragment_auth_pass, container, false);

        PersonInfo idcardPersonInfo = DonorEntity.getInstance().getIdentityCard();
        //
        PersonInfo documentPersonInfo = DonorEntity.getInstance().getDocument();


        //身份证信息显示
        TextView tv_name = (TextView) viewRoot.findViewById(R.id.tv_name);
        tv_name.setText(idcardPersonInfo.getName());

        TextView tv_sex = (TextView) viewRoot.findViewById(R.id.tv_sex);
        tv_sex.setText(idcardPersonInfo.getGender());

        TextView tv_nation = (TextView) viewRoot.findViewById(R.id.tv_nation);
        tv_nation.setText(idcardPersonInfo.getNation());

        TextView tv_birthday = (TextView) viewRoot.findViewById(R.id.tv_birthday);
        tv_birthday.setText(idcardPersonInfo.getBirth_year() + "年" + idcardPersonInfo.getBirth_month() + "月" + idcardPersonInfo.getBirth_day() + "日");

        TextView tv_address = (TextView) viewRoot.findViewById(R.id.tv_address);
        tv_address.setText(idcardPersonInfo.getAddress());

        TextView tv_idcard = (TextView) viewRoot.findViewById(R.id.tv_idcard);
        tv_idcard.setText(idcardPersonInfo.getId());
        tv_idcard.setText("******************");

        ImageView imageView = (ImageView) viewRoot.findViewById(R.id.iv_head);
        imageView.setImageBitmap(idcardPersonInfo.getFaceBitmap());

        //档案信息

        ImageView iv_document_pic = (ImageView) viewRoot.findViewById(R.id.iv_document_pic);
        iv_document_pic.setImageBitmap(documentPersonInfo.getFaceBitmap());

        TextView tv_document_address = (TextView) viewRoot.findViewById(R.id.tv_document_address);
        tv_document_address.setText(documentPersonInfo.getAddress());

        TextView tv_document_id = (TextView) viewRoot.findViewById(R.id.tv_document_id);
        tv_document_id.setText(documentPersonInfo.getId());

        TextView tv_document_gender = (TextView) viewRoot.findViewById(R.id.tv_document_gender);
        tv_document_gender.setText(documentPersonInfo.getGender());
        return viewRoot;
    }


    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                play(getString(R.string.auth), mTtsListener);
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
}
