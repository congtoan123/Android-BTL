package com.example.BTL.MainActivity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.BTL.R;
import com.example.BTL.GetUser.MyPrefs;
import com.example.BTL.model.UserInfo;
import com.example.BTL.MainActivity.main.home.BottomPagerAdapter;
import com.example.BTL.MainActivity.main.home.FireBaseActivity;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;
import com.example.BTL.widget.fragmentnavigationcontroller.FragmentNavigationController;
import com.example.BTL.widget.fragmentnavigationcontroller.PresentStyle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FireBaseActivity  {
    private static final String TAG ="MainActivity";
    FirebaseAuth mAuth;
    public FirebaseFirestore mDb;
    public FirebaseUser user;
    MyPrefs myPrefs;

    @BindView(R.id.message) TextView mTextMessage;

    BottomPagerAdapter mBottomAdapter;

    @BindView(R.id.bottom_view_pager)
    ViewPager mBottomPager;

   FragmentNavigationController mNavigationController;

    @Override
    public void onBackPressed() {
        if(mNavigationController.getTopFragment().isReadyToDismiss())
        if(!(isNavigationControllerInit() && mNavigationController.dismissFragment(true)))
            super.onBackPressed();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_trending:
                    mBottomPager.setCurrentItem(0);
                    mTextMessage.setText(R.string.trending);
                    return true;
                case R.id.navigation_cinema:
                    mBottomPager.setCurrentItem(1);
                    mTextMessage.setText(R.string.cinema);
                    return true;
                case R.id.navigation_profile:
                    mBottomPager.setCurrentItem(2);
                    mTextMessage.setText(R.string.my_profile);
                    return true;
            }
            return false;
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dismiss();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        myPrefs = new MyPrefs(this);
        getUserType();

        mBottomAdapter = new BottomPagerAdapter(this,getSupportFragmentManager());
        mBottomPager.setAdapter(mBottomAdapter);

        BottomNavigationView navigation =  findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initBackStack(savedInstanceState);

    }
    public static int PRESENT_STYLE_DEFAULT = PresentStyle.ACCORDION_LEFT;


    private void initBackStack(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        mNavigationController = FragmentNavigationController.navigationController(fm, R.id.container);
        mNavigationController.setPresentStyle(PRESENT_STYLE_DEFAULT);
        mNavigationController.setDuration(250);
        mNavigationController.setInterpolator(new AccelerateDecelerateInterpolator());


          mNavigationController.presentFragment(new MainFragment());


    }
    private boolean isNavigationControllerInit() {
        return null!= mNavigationController;
    }
    public void presentFragment(SupportFragment fragment) {
        if(isNavigationControllerInit()) {
            mNavigationController.setPresentStyle(fragment.getPresentTransition());
              mNavigationController.presentFragment(fragment, true);

        }
    }
    public void dismiss() {
        if(isNavigationControllerInit()) {
            mNavigationController.dismissFragment();
        }
    }

    public void presentFragment(SupportFragment fragment, boolean animated) {
        if(isNavigationControllerInit()) {
            mNavigationController.presentFragment(fragment,animated);
        }
    }
    public void dismiss(boolean animated) {
        if(isNavigationControllerInit()) {
            mNavigationController.dismissFragment(animated);
        }
    }
    private void replaceFragment(Fragment newFragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.bottom_view_pager, newFragment, tag)
                .commit();
    }

    public void restartHomeScreen() {
        Intent intent = new Intent(this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void getUserType(){
        if(myPrefs.getIsSignIn()){
            String id = user.getUid();

            DocumentReference userGet = mDb.collection("user_info").document(id);
            userGet.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UserInfo info = documentSnapshot.toObject(UserInfo.class);
                            if(!info.getUserType().matches("")){
                                myPrefs.setIsAdmin(true);
                            }
                            else{
                                myPrefs.setIsAdmin(false);
                                Toast.makeText(MainActivity.this,"Tài khoản admin \"[admin@gmail.com],[123456]\"",Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
        else{
            myPrefs.setIsAdmin(false);
            Toast.makeText(MainActivity.this,"Tài khoản admin \"[admin@gmail.com],[123456]\"",Toast.LENGTH_LONG).show();

        }
    }
}
