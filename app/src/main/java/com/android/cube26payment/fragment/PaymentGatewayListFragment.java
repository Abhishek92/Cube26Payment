package com.android.cube26payment.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cube26payment.PaymentDetailActivity;
import com.android.cube26payment.R;
import com.android.cube26payment.adapter.PaymentGatewayListAdapter;
import com.android.cube26payment.model.PaymentGateway;
import com.android.cube26payment.model.PaymentGatewayList;
import com.android.cube26payment.network.CubePaymentGatewayApiClient;
import com.android.cube26payment.utils.AppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentGatewayListFragment extends Fragment implements PaymentGatewayListAdapter.OnItemClickListener {


    public static final String TAG = "PaymentGatewayListFragment";
    public static final String PAYMENT_GATEWAY_DATA = "payment_gateway";
    private RecyclerView mPaymentGatewayRecyclerView;
    private TextView mEmptyText;
    private ProgressBar mProgressBar;
    private List<PaymentGateway> mPaymentGatewayList = new ArrayList<>();
    private PaymentGatewayListAdapter mAdapter;
    private SearchView mSearchView;

    public PaymentGatewayListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_payment_gateway_list, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPaymentGatewayRecyclerView = (RecyclerView) view.findViewById(R.id.payment_list_recycler_view);
        mEmptyText = (TextView) view.findViewById(R.id.emptyText);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPaymentGatewayRecyclerView.setLayoutManager(linearLayoutManager);
        if (AppUtils.isNetworkConnected(getActivity()))
            getPaymentGatewayList();
        else {
            mEmptyText.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void getPaymentGatewayList() {
        mProgressBar.setVisibility(View.VISIBLE);
        CubePaymentGatewayApiClient.getCubePaymentApiInterface().getPaymentPortalList(new Callback<PaymentGatewayList>() {
            @Override
            public void success(PaymentGatewayList paymentGatewayList, Response response) {
                mProgressBar.setVisibility(View.GONE);
                if (response != null && paymentGatewayList != null) {
                    mPaymentGatewayList = new ArrayList<>(paymentGatewayList.getPaymentGateways());
                    mAdapter = new PaymentGatewayListAdapter(getActivity(), mPaymentGatewayList);
                    mAdapter.setItemClickListener(PaymentGatewayListFragment.this);
                    mPaymentGatewayRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressBar.setVisibility(View.GONE);
                mEmptyText.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), R.string.server_error_msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        PaymentGateway data = (PaymentGateway) v.getTag();
        Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
        intent.putExtra(PAYMENT_GATEWAY_DATA, data);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint(getString(R.string.search_by_name_currency));
        searchItems();
    }

    private void searchItems(){
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(mAdapter != null && !TextUtils.isEmpty(newText))
                    mAdapter.setFilter(newText);
                else if(mAdapter != null && TextUtils.isEmpty(newText))
                    mAdapter.flushFilter();
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(mAdapter != null && !TextUtils.isEmpty(query))
                    mAdapter.setFilter(query);
                else if(mAdapter != null && TextUtils.isEmpty(query))
                    mAdapter.flushFilter();
                return true;
            }
        };
        mSearchView.setOnQueryTextListener(textChangeListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort_rating) {
            sortList(ratingComparator);
        } else if (item.getItemId() == R.id.action_sort_setup_fee) {
            sortList(setUpFeeComparator);
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortList(Comparator<PaymentGateway> comparator) {
        if (mPaymentGatewayList != null && !mPaymentGatewayList.isEmpty()) {
            Collections.sort(mPaymentGatewayList, comparator);
            mAdapter = new PaymentGatewayListAdapter(getActivity(), mPaymentGatewayList);
            mPaymentGatewayRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    private Comparator<PaymentGateway> ratingComparator = new Comparator<PaymentGateway>() {
        @Override
        public int compare(PaymentGateway lhs, PaymentGateway rhs) {
            return Float.valueOf(rhs.getRating()).compareTo(Float.valueOf(lhs.getRating()));
        }
    };

    private Comparator<PaymentGateway> setUpFeeComparator = new Comparator<PaymentGateway>() {
        @Override
        public int compare(PaymentGateway lhs, PaymentGateway rhs) {
            return Float.valueOf(rhs.getSetupFee()).compareTo(Float.valueOf(lhs.getSetupFee()));
        }
    };
}
