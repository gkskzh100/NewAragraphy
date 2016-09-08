package jm.dodam.newaragraphy.utils;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import jm.dodam.newaragraphy.R;

/**
 * Created by kim on 2016-09-08.
 */
public class CustomLoading {
    private static Dialog m_loadingDialog = null;

    public static void showLoading(Context context) {

        if (m_loadingDialog == null) {
            //다이얼로그가 없으면 만들고 보이게 하라
            m_loadingDialog = new Dialog(context, R.style.custom_loading);
            //프로그레스를 생성하자
            ProgressBar pb = new ProgressBar(context);
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            //프로그래스를 다이얼로그에 포함하자
            m_loadingDialog.setContentView(pb, params);
            m_loadingDialog.setCancelable(false);
            m_loadingDialog.show();
        } else if(!m_loadingDialog.isShowing()){
            //다이얼로그가 있는데 HIDE 상태면 보이게 하라
            m_loadingDialog = null;
            m_loadingDialog = new Dialog(context, R.style.custom_loading);
            ProgressBar pb = new ProgressBar(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            m_loadingDialog.setContentView(pb, params);
            m_loadingDialog.setCancelable(false);
            m_loadingDialog.show();
        }

    }
    public static void hideLoading() {
        if (m_loadingDialog != null) {
            if(m_loadingDialog.isShowing()){
                //다이얼로그가 있고 보이는 상태면 안보이게 하라
                m_loadingDialog.dismiss();
                m_loadingDialog = null;
            }
        }
    }
}
