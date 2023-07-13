package com.mio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import net.kdt.pojavlaunch.R;

public class MioSelectKeyDialog extends AlertDialog.Builder implements View.OnClickListener {
    private LinearLayout mainLayout;
    private LinearLayout keyboardLayout;
    private LinearLayout specialLayout;
    private AlertDialog dialog;
    private Context context;
    private Button ok;
    private Button cancle;
    private String currentKey=null;
    public interface CallBack{
        void onSelected(String key);
    }

    private TextView selectedKey;
    public MioSelectKeyDialog(@NonNull Context context) {
        super(context, R.style.FullDialog);
        this.context=context;
        init();
    }
    private void init(){
        mainLayout=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_selectkey,null);
        keyboardLayout=mainLayout.findViewById(R.id.selecter_keyboard);
        for(int i=0;i<keyboardLayout.getChildCount();i++){
            if(keyboardLayout.getChildAt(i) instanceof LinearLayout){
                LinearLayout t=(LinearLayout)keyboardLayout.getChildAt(i);
                for(int j=0;j<t.getChildCount();j++){
                    if(t.getChildAt(j) instanceof MioSelectorButton){
                        t.getChildAt(j).setOnClickListener(this);
                    }
                }
            }
        }
        specialLayout=mainLayout.findViewById(R.id.view_special);
        for(int i=0;i<specialLayout.getChildCount();i++){
            if(specialLayout.getChildAt(i) instanceof MioSelectorButton){
                specialLayout.getChildAt(i).setOnClickListener(this);
            }
        }
        setView(mainLayout);

        dialog= super.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ok=mainLayout.findViewById(R.id.dialog_selectkey_btn_ok);
        cancle=mainLayout.findViewById(R.id.dialog_selectkey_btn_cancel);

        cancle.setOnClickListener(v->{
            dialog.dismiss();
        });
        selectedKey=mainLayout.findViewById(R.id.dialog_selectkey_text_key);
    }

    @Override
    public void onClick(View view) {
        currentKey=((MioSelectorButton) view).getKey();
        selectedKey.setText(currentKey);
    }

    public void show(CallBack callBack){
        ok.setOnClickListener(v->{
            callBack.onSelected(currentKey);
            dialog.dismiss();
        });
        dialog.show();
    }
}
