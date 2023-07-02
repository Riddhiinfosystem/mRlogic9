package com.riddhi.r_logig_olpl.login;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.riddhi.r_logig_olpl.MainActivity;
import com.riddhi.r_logig_olpl.R;
import com.riddhi.r_logig_olpl.databinding.ActivityLoginBinding;
import com.riddhi.r_logig_olpl.network.ApiClient;
import com.riddhi.r_logig_olpl.network.NetworkService;
import com.riddhi.r_logig_olpl.util.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    Context mCon;
    boolean isRemember = false, isPasswordVisible;
    private String userNameStr = "", passwordStr = "", cCodeStr = "";
    String[] requests = {Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int REQUEST_PERMISSION_LOCATION = 10;
    public static final int REQUEST_READ_PHONE_STATE = 11;
    String version;

    ProgressDialog progress;
    NetworkService apiInterface;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mCon = this;
        apiInterface = ApiClient.getClient().create(NetworkService.class);

        try {
            PackageInfo pInfo = mCon.getPackageManager().getPackageInfo(mCon.getPackageName(), 0);
            version = pInfo.versionName;
            Log.d("version", "==" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String ver = "version " + version;
        binding.tvVersion.setText(ver);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            ActivityCompat.requestPermissions(this, requests, 1);
        if (PreferenceUtil.isUserLoggedIn()) {
            if (PreferenceUtil.getUser().isCheck()) {
                // LoginModel loginModel = new LoginModel(userNameStr, passwordStr, cCodeStr);
                Intent intent = new Intent(mCon, MainActivity.class);
                intent.putExtra("model", PreferenceUtil.getUser());
                intent.putExtra("version", version);
                startActivity(intent);
            }
        }

        binding.passwordTextInputEditText.setOnTouchListener((v, event) -> {
            final int RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.passwordTextInputEditText.getRight() - binding.passwordTextInputEditText.getCompoundDrawables()[RIGHT].getBounds().width())) {
                    int selection = binding.passwordTextInputEditText.getSelectionEnd();
                    if (isPasswordVisible) {
                        // set drawable image
                        binding.passwordTextInputEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_key, 0, R.drawable.ic_visibility_off, 0);
                        // hide Password
                        binding.passwordTextInputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        isPasswordVisible = false;
                    } else {
                        // set drawable image
                        binding.passwordTextInputEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_key, 0, R.drawable.ic_visible, 0);
                        // show Password
                        binding.passwordTextInputEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        isPasswordVisible = true;
                    }
                    binding.passwordTextInputEditText.setSelection(selection);
                    return true;
                }
            }
            return false;
        });

        binding.cbRemember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isRemember = isChecked;
        });

        binding.btnLogin.setOnClickListener(view -> {
            userNameStr = binding.usernameTextInputEditText.getText().toString().trim();
            passwordStr = binding.passwordTextInputEditText.getText().toString().trim();
            cCodeStr = binding.cCodeTextInputEditText.getText().toString().trim();
            validate();
        });

    }

    private void validate() {
        boolean isValidUser = false, isValidPassword = false, isValidCode = false;

        if (TextUtils.isEmpty(userNameStr)) {
            //    binding.usernameTextInputLayout.setError(getResources().getString(R.string.canot_be_empty));
            Snackbar snackbar = Snackbar
                    .make(binding.relativeLayout, "Please Enter UserName", Snackbar.LENGTH_LONG)
                    .setAction("OK", view -> {

                    });

            snackbar.show();
        } else {
            //  binding.usernameTextInputLayout.setError(null);
            isValidUser = true;
        }

        if (TextUtils.isEmpty(passwordStr)) {
            //  binding.passwordTextInputLayout.setError(getResources().getString(R.string.canot_be_empty));
            Snackbar snackbar = Snackbar
                    .make(binding.relativeLayout, "Please Enter Password", Snackbar.LENGTH_LONG)
                    .setAction("OK", view -> {

                    });

            snackbar.show();
        } else {
            // binding.passwordTextInputLayout.setError(null);
            isValidPassword = true;
        }

        if (TextUtils.isEmpty(cCodeStr)) {
            // binding.cCodeTextInputLayout.setError(getResources().getString(R.string.canot_be_empty));
            Snackbar snackbar = Snackbar
                    .make(binding.relativeLayout, "Please Enter Company Code", Snackbar.LENGTH_LONG)
                    .setAction("OK", view -> {

                    });

            snackbar.show();
        } else {
            //  binding.cCodeTextInputLayout.setError(null);
            isValidCode = true;
        }

        if (isValidUser && isValidPassword && isValidCode) {
            // FirebaseTopicSubscribe();
            LoginModel loginModel = new LoginModel("",userNameStr, passwordStr, cCodeStr, isRemember);

            GetCompanyIp(loginModel);
        }
    }

    public void GetCompanyIp(LoginModel loginModel)
    {
        progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("Loading. Please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
//http://183.82.104.253:82/mrlogicgeofast/AutoLoginApi/SetLogin?&UserName=Riddhi&Password=omsgn9&ccode=geofast&appVer=1.2
        Call<ResponseBody> call3 = apiInterface.GetCompanyIp(""+loginModel.getCcode());
        call3.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {

                    String responseRecieved = response.body().string();
                    Log.e("responseRecieved","===>"+responseRecieved);

                    JSONObject obj = new JSONObject(responseRecieved);
                    Log.e("MessageDescription","===>"+obj.getString("MessageDescription"));
                    if(!obj.getString("MessageDescription").equalsIgnoreCase(""))
                    {
                        loginModel.setBaseurl(""+obj.getString("MessageDescription"));
                    }else {
                        loginModel.setBaseurl("");
                    }


                    if (isRemember) {
                        PreferenceUtil.setUserLoggedIn(true);
                        PreferenceUtil.setUser(loginModel);
                    }

                    Intent intent = new Intent(mCon, MainActivity.class);
                       intent.putExtra("model", loginModel);
                        intent.putExtra("version", version);
                        startActivity(intent);
                        finish();

                    /*ln.clear();
                    JSONArray jArray = new JSONArray(responseRecieved);
                    //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                    for (int i = 0; i < jArray.length(); i++) {
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                        JSONObject json_data = jArray.getJSONObject(i);

                        //`CustId`, `SIPNo`, `CustName`, `ContactNo`, `Address`, `Amount`, `JoiningDate`, `IntroducerName`, `IntroducerContactNo`, `PhotoPath`, `UserName`, `UserId`
                        ln.add(new CustomerDetail(json_data.getInt("CustId"), json_data.getInt("LoanID"), json_data.getString("Name"), json_data.getString("ContactNo"), json_data.getString("Address"), json_data.getInt("Amount"), json_data.getInt("EMIAmount"), json_data.getString("LoanDate"), json_data.getString("PhotoPath"), json_data.getString("UserName"), json_data.getInt("UserId")));
                    }*/

                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }


                }catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;

            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;

            default:
                break;
        }
    }

}