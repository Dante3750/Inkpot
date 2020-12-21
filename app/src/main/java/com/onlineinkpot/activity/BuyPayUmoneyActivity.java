package com.onlineinkpot.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onlineinkpot.R;
import com.onlineinkpot.app.Cons;
import com.onlineinkpot.app.PrefManager;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class BuyPayUmoneyActivity extends AppCompatActivity {
    private static final String TAG = "BuyPayUmoneyActivity";
    private String phone = "";
    public PrefManager pref;
    public String c_date = "";
    public String key = "Zuks2W9F";
    public String salt = "LnU5BQSoBB";
    public String txnid = "";
    public String price = "";
    public String firstname = "";
    public String email = "test@gmail.com";
    public String productInfo = "Product1";
    private ProgressDialog mProgressDialog;
    public HashMap<String, String> userdetail;
    public WebView webviewPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_payu_money_activity);
        txnid = getIntent().getStringExtra("taxtnid");
        price = getIntent().getStringExtra("price");
        pref = new PrefManager(this);
        userdetail = pref.getUpdatedData();
        mProgressDialog = new ProgressDialog(BuyPayUmoneyActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        initView();
        webviewPayment = (WebView) findViewById(R.id.webView1);
        webviewPayment.getSettings().setJavaScriptEnabled(true);
        webviewPayment.getSettings().setDomStorageEnabled(true);
        webviewPayment.getSettings().setLoadWithOverviewMode(true);
        webviewPayment.getSettings().setUseWideViewPort(true);
        webviewPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webviewPayment.getSettings().setSupportMultipleWindows(true);
        webviewPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StringBuilder url_s = new StringBuilder();
        url_s.append("https://secure.payu.in/_payment");
        Log.e(TAG, "call url " + url_s);
        webviewPayment.postUrl(url_s.toString(), getPostString().getBytes(Charset.forName("UTF-8")));
        webviewPayment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressDialog.dismiss();
            }

            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view, SslErrorHandler handler) {
                Log.e("Error", "Exception caught!");
                handler.cancel();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initView() {
        pref = new PrefManager(getApplication());
        c_date = new SimpleDateFormat("MMM dd-yyyy").format(Calendar.getInstance().getTime());
        phone = userdetail.get(Cons.KEY_VIEW_USER_MOBILENO);
        email = userdetail.get(Cons.KEY_VIEW_USER_EMAIL);
        firstname = userdetail.get(Cons.KEY_VIEW_USER_FNAME);

    }

    private final class PayUJavaScriptInterface {
        PayUJavaScriptInterface() {
        }

        @JavascriptInterface
        public void success(long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Intent intent = new Intent(getApplication(), BuyOrderSuccessActivity.class);
                    intent.putExtra("taxtnid", txnid);
                    intent.putExtra("check", "1");
                    startActivity(intent);

                }
            });
        }

        @JavascriptInterface
        public void failure(long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    //Failed
                    Intent intent = new Intent(getApplication(), BuyOrderFailureActivity.class);
                    startActivity(intent);


                }
            });
        }

    }

    private String getPostString() {
        StringBuilder post = new StringBuilder();
        post.append("key=");
        post.append(key);
        post.append("&");
        post.append("txnid=");
        post.append(txnid);
        post.append("&");
        post.append("amount=");
        post.append(price);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append(phone);
        post.append("&");
        post.append("surl=");
        post.append("https://www.payumoney.com/mobileapp/payumoney/success.php");
        post.append("&");
        post.append("furl=");
        post.append("https://www.payumoney.com/mobileapp/payumoney/failure.php");
        post.append("&");
        StringBuilder checkSumStr = new StringBuilder();
        MessageDigest digest = null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");
            checkSumStr.append(key);
            checkSumStr.append("|");
            checkSumStr.append(txnid);
            checkSumStr.append("|");
            checkSumStr.append(price);
            checkSumStr.append("|");
            checkSumStr.append(productInfo);
            checkSumStr.append("|");
            checkSumStr.append(firstname);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(salt);
            digest.update(checkSumStr.toString().getBytes());
            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");
            Log.i(TAG, "SHA result is " + hash);
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        post.append("service_provider=");
        post.append("payu_paisa");
        return post.toString();
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
