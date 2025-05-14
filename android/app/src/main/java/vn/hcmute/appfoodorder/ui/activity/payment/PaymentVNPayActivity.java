package vn.hcmute.appfoodorder.ui.activity.payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import vn.hcmute.appfoodorder.R;
import vn.hcmute.appfoodorder.ui.activity.order.OrderDetailActivity;
import vn.hcmute.appfoodorder.viewmodel.OrderViewModel;

public class PaymentVNPayActivity extends AppCompatActivity {

    private WebView webView;
    private String paymentUrl;
    private OrderViewModel orderViewModel;
    private Long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_vnpay);

        // Lấy thông tin orderId và totalPrice từ Intent
        paymentUrl = getIntent().getStringExtra("paymentUrl");
        //Get data from back activity
        orderId = getIntent().getLongExtra("orderId", -1L);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        // Khởi tạo WebView
        webView = findViewById(R.id.webview_vnpay);
        setupWebView();

        webView.loadUrl(paymentUrl);
    }

    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        // Thiết lập WebViewClient để xử lý trang thanh toán trực tiếp trong WebView
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Tải lại trang khi có URL mới (trong trường hợp thanh toán qua các bước khác nhau)
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("/vn-pay-callback")) {
                    Uri uri = Uri.parse(url);
                    Log.d("Url vnpay", url);
                    String responseCode = uri.getQueryParameter("vnp_ResponseCode");

                    if ("00".equals(responseCode)) {
                        Toast.makeText(PaymentVNPayActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PaymentVNPayActivity.this, OrderDetailActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PaymentVNPayActivity.this, "Thanh toán thất bại! Mã: " + responseCode, Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else if (url.contains("fail")) {
                    Toast.makeText(PaymentVNPayActivity.this, "Thanh toán không thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });
    }
}
