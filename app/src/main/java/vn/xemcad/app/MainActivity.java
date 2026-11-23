package vn.xemcad.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private static final int REQ_FILE = 1001;

    private WebView web;
    private ValueCallback<Uri[]> pendingChooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        web = new WebView(this);
        web.setBackgroundColor(Color.BLACK);
        web.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        web.setLongClickable(false);
        web.setOnLongClickListener(v -> true);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);

        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient() {
            // Nút "Mở file" trong trang gọi vào đây để chọn file DXF từ bộ nhớ điện thoại.
            @Override
            public boolean onShowFileChooser(WebView view,
                                             ValueCallback<Uri[]> callback,
                                             FileChooserParams params) {
                if (pendingChooser != null) {
                    pendingChooser.onReceiveValue(null);
                }
                pendingChooser = callback;

                Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
                pick.addCategory(Intent.CATEGORY_OPENABLE);
                // DXF/DWG không có kiểu MIME chuẩn, để "*/*" thì trình chọn file nào cũng chọn được.
                pick.setType("*/*");
                pick.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                try {
                    startActivityForResult(Intent.createChooser(pick, "Chọn file DXF"), REQ_FILE);
                } catch (Exception e) {
                    pendingChooser = null;
                    callback.onReceiveValue(null);
                    return false;
                }
                return true;
            }
        });

        web.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_FILE && pendingChooser != null) {
            Uri[] result = null;
            if (resultCode == RESULT_OK && data != null) {
                result = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
            }
            pendingChooser.onReceiveValue(result);
            pendingChooser = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.destroy();
        }
        super.onDestroy();
    }
}
