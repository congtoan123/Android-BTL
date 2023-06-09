package com.example.BTL.MainActivity.Auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.BTL.R;
import com.example.BTL.GetUser.MyPrefs;
import com.example.BTL.model.UserInfo;
import com.example.BTL.MainActivity.main.MainActivity;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LogIn extends SupportFragment implements View.OnClickListener {
    private static final String TAG="LogIn";
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private View root;

    MyPrefs myPrefs;
    FirebaseFirestore mDb;

    public LogIn() {

    }

    @BindView(R.id.btn_sign_in) Button btnSignIn;
    @BindView(R.id.sign_up) View mSignUp;
    @BindView(R.id.btn_google) Button btnGoogle;
    @BindView(R.id.btn_facebook) Button btnFacebook;
    @BindView(R.id.edi_email) TextInputLayout ediEmail;
    @BindView(R.id.txt_email) TextInputEditText txtEmail;
    @BindView(R.id.edi_password) TextInputLayout ediPassword;
    @BindView(R.id.txt_password) TextInputEditText txtPassword;
    @BindView(R.id.chb_remember) CheckBox chbRemember;
    @BindView(R.id.forgot_password) View mForgot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);

        myPrefs = new MyPrefs(getContext());

        root = view;
        mForgot.setOnClickListener(this);
        chbRemember.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        mSignUp.setOnClickListener(this);

        // Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDb = getMainActivity().mDb;

        if(myPrefs.getIsRememberMe()){
            String[] acc = myPrefs.getAccount();
            txtEmail.setText(acc[0]);
            txtPassword.setText(acc[1]);
            chbRemember.setChecked(true);
        }
    }

    public static LogIn newInstance() {
        return new LogIn();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.sign_up:
                ((MainActivity)getActivity()).dismiss();
                ((MainActivity)getActivity()).presentFragment(LogUp.newInstance());
                break;
            case R.id.forgot_password:
                ((MainActivity)getActivity()).presentFragment(ForgotPassword.newInstance());
                break;
        }
    }

    private void signIn() {
        String email = Objects.requireNonNull(ediEmail.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(ediPassword.getEditText()).getText().toString().trim();
        String[] acc = {email,password};
        myPrefs.setAccount(acc);

        if(chbRemember.isChecked()){
            myPrefs.setIsRememberMe(true);
        }
        else{
            myPrefs.setIsRememberMe(false);
        }

        if(validateAccount(email,password)){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(getContext(), R.string.signin_success, Toast.LENGTH_LONG).show();
                                myPrefs.setIsSignIn(true);
                                ((MainActivity)getActivity()).restartHomeScreen();
                            } else {
                                // sign in fails
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getContext(), R.string.signin_error,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean validateAccount(String email, String password){
        Boolean validate = true;

        if(email.isEmpty()){
            ediEmail.setError(getString(R.string.email_empty));
            validate = false;
        }
        else if(!isValidEmail(email)){
            ediEmail.setError(getString(R.string.email_invalid));
            validate = false;
        }
        else{   ediEmail.setError(null);        }

        if(password.isEmpty()){
            ediEmail.setError(null);
            ediPassword.setError(getString(R.string.password_empty));
            validate = false;
        }
        else if(password.length()<6){
            ediEmail.setError(null);
            ediPassword.setError(getString(R.string.password_length));
            validate = false;
        }
        else{   ediPassword.setError(null);    }

        return validate;
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

}
