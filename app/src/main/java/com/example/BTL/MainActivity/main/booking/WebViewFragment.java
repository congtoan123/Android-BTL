package com.example.BTL.MainActivity.main.booking;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.BTL.R;
import com.example.BTL.widget.fragmentnavigationcontroller.PresentStyle;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewFragment extends SupportFragment {
    private static final String TAG ="WebViewFragment";

    @BindView(R.id.web)
    WebView mWebView;
    private String mURL;

    public static WebViewFragment newInstance(String url) {
     WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.mURL = url;
        return webViewFragment;

    }

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.web_view,container,false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            mWebView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.web_size_portrait);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().findViewById(R.id.container).requestLayout();
        }
        else {
             View decorView = getActivity().getWindow().getDecorView();
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getActivity().findViewById(R.id.container).requestLayout();
            mWebView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        mWebView.requestLayout();


    }
    public static String getIDYoutube(Context context,String url) {

        String pattern = context.getString(R.string.youtube_pattern_id);

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            Log.d(TAG, "getIDYoutube: "+matcher.group());
            return matcher.group();
        }
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.reload();
        WebChromeClient webChromeClient = new WebChromeClient();
        mWebView.setWebChromeClient(webChromeClient);

        mIsPaused = true;
        mWebView.loadUrl("https://www.youtube.com/embed/" + getIDYoutube(getContext(),mURL)+getString(R.string.autoplay_youtube_pattern));
      //  mWebView.loadData(getHTML(getIDYoutube(mURL)),"text/html", "UTF-8");
}

    private boolean mIsPaused = false;

    @Override
    public void onResume() {
        super.onResume();
        if (mIsPaused)
        {
            // resume flash and javascript etc
            callHiddenWebViewMethod(mWebView, "onResume");
            mWebView.resumeTimers();
            mIsPaused = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!mIsPaused)
        {
            // pause flash and javascript etc
            callHiddenWebViewMethod(mWebView, "onPause");
            mWebView.pauseTimers();
            mIsPaused = true;
        }
    }
    private void callHiddenWebViewMethod(final WebView wv, final String name)
    {
        try
        {
            final Method method = WebView.class.getMethod(name);
            method.invoke(mWebView);
        } catch (final Exception e)
        {}
    }

    @Override
    public int getPresentTransition() {
        return PresentStyle.SCALEXY;
    }
}
