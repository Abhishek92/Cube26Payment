package com.android.cube26payment;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.cube26payment.fragment.PaymentDetailGatewayFragment;
import com.android.cube26payment.fragment.PaymentGatewayListFragment;
import com.android.cube26payment.model.PaymentGateway;

public class PaymentDetailActivity extends AppCompatActivity {

    private PaymentGateway data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data = getIntent().getParcelableExtra(PaymentGatewayListFragment.PAYMENT_GATEWAY_DATA);
        if (data != null)
            getSupportActionBar().setTitle(data.getName());
        attachPaymentDetailFragment();
    }

    private void attachPaymentDetailFragment() {
        PaymentDetailGatewayFragment fragment = new PaymentDetailGatewayFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PaymentGatewayListFragment.PAYMENT_GATEWAY_DATA, data);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, PaymentDetailGatewayFragment.TAG).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.action_download)
            downloadHowToDocument();
        return super.onOptionsItemSelected(item);
    }

    private void downloadHowToDocument() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(data.getHowToDocument()));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String fileName = data.getHowToDocument();
        fileName = fileName.substring(data.getHowToDocument().lastIndexOf("/") + 1, data.getHowToDocument().length()).replace("\n", "").replace("\r", "");;
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        downloadManager.enqueue(request);
        Toast.makeText(this, R.string.download_start_msg, Toast.LENGTH_SHORT).show();
    }
}
