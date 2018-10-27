package com.feng.newmall;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.newtonproject.newpay.android.sdk.NewPayApi;
import org.newtonproject.newpay.android.sdk.bean.ProfileInfo;
import org.newtonproject.newpay.android.sdk.bean.SigMessage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout profileLinearLayout;
    private TextView nameTextView;
    private TextView cellphoneTextView;
    private TextView newidTextView;
    private ImageView imageView;
    private String TAG = "Activity";

    private static final int REQUEST_CODE_NEWPAY = 1000;
    private static final String privateKey = "0xbc6162af5677bc108fc227a1b1178aede933d05979cc5c6154078c2eae068dac";
    private static final String publicKey = "0xa16b27213b5279d1f43c67e0e43272dad5a19376af3182fc24199d06d8f169402b2a4bc1d609d8af3186b929f42ffee0fba00cb569547fffa3c2769abe55ae7a";
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        NewPayApi.init(getApplication(), privateKey, "1");
    }

    private void initView() {
        profileLinearLayout = findViewById(R.id.profileLayout);
        nameTextView = findViewById(R.id.nameTextView);
        cellphoneTextView = findViewById(R.id.cellphoneTextView);
        newidTextView = findViewById(R.id.newidTextView);
        imageView = findViewById(R.id.avatarImageView);
        profileLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileLayout:
                NewPayApi.requestProfileFromNewPay();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NewPayApi.REQUEST_CODE_NEWPAY && resultCode == RESULT_OK) {
            String profile = data.getStringExtra("profile");
            String sigMessage = data.getStringExtra("signature");
            Log.e(TAG, "onActivityResult: " + data.toString() );
            if(!TextUtils.isEmpty(profile)){
                ProfileInfo profileInfo = gson.fromJson(profile, ProfileInfo.class);
                cellphoneTextView.setText(profileInfo.cellphone);
                nameTextView.setText(profileInfo.name);
                newidTextView.setText(profileInfo.newid);
                Uri avatarUri = data.getData();
                imageView.setImageURI(avatarUri);
            }else{
                String error = data.getStringExtra("error");
                Log.e(TAG, "onActivityResult: " + error);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
            if(!TextUtils.isEmpty(sigMessage)) {
                SigMessage sig = gson.fromJson(sigMessage, SigMessage.class);
                Log.e(TAG, "onActivityResult: " + sig.toString());
            }

        }
        if(requestCode == NewPayApi.REQUEST_CODE_NEWPAY && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "user canceled", Toast.LENGTH_LONG).show();
        }
    }
}
