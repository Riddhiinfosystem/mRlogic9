package com.riddhi.r_logig_olpl.login;


import android.Manifest;
import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.riddhi.r_logig_olpl.MainActivity;
import com.riddhi.r_logig_olpl.R;
import com.riddhi.r_logig_olpl.databinding.ActivityLoginBinding;
import com.riddhi.r_logig_olpl.util.PreferenceUtil;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mCon = this;


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
            LoginModel loginModel = new LoginModel(userNameStr, passwordStr, cCodeStr, isRemember);
            if (isRemember) {
                PreferenceUtil.setUserLoggedIn(true);
                PreferenceUtil.setUser(loginModel);
            }
            Intent intent = new Intent(mCon, MainActivity.class);
            intent.putExtra("model", loginModel);
            intent.putExtra("version", version);
            startActivity(intent);
            finish();

            //loadApi(loginModel);
        }
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