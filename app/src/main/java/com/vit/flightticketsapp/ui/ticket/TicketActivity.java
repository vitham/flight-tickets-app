package com.vit.flightticketsapp.ui.ticket;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.vit.flightticketsapp.R;
import com.vit.flightticketsapp.data.model.Ticket;
import com.vit.flightticketsapp.ui.base.BaseActivity;
import com.vit.flightticketsapp.ui.contact.ContactActivity;
import com.vit.flightticketsapp.ui.ticket.adapter.TicketAdapter;
import com.vit.flightticketsapp.ui.ticket.listener.OnClickTicketItemListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TicketActivity extends BaseActivity implements TicketContract.View, OnClickTicketItemListener {

    private static final String FROM = "DEL";
    private static final String TO = "HYD";

    @BindView(R.id.list_ticket)
    RecyclerView mRcvTicket;

    @BindView(R.id.layout_root)
    LinearLayout mLayoutRoot;

    @BindView(R.id.input_search)
    EditText mInputSearch;

    private TicketAdapter mAdapter;

    private TicketContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.ticket_activity;
    }

    @Override
    protected void initView() {
        initToolbar();
        initRcv();

        new TicketPresenter(this);
        mPresenter.loadTickets(FROM, TO);
        mPresenter.searchTickets(mInputSearch);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadTickets(FROM, TO);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull TicketContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTickets(List<Ticket> tickets) {
        mAdapter.setList(tickets);
    }

    @Override
    public void showTicketPrice(Ticket ticket) {
        int position = mAdapter.getList().indexOf(ticket);

        if (position != -1) {
            mAdapter.setItemPosition(position, ticket);
        }
    }

    @Override
    public void showTicketSearch(String text) {
        mAdapter.filter(text);
    }

    @Override
    public void showError(Throwable e) {
        Log.e(TAG, "showError: " + e.getMessage());

        showSnackbar(mLayoutRoot, e.getMessage());
    }

    @Override
    public void onClickTicketItem(Ticket ticket) {
        showToast(ticket.getAirline().getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticket_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_contact) {
            ContactActivity.start(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(FROM + " > " + TO);
    }

    private void initRcv() {
        mAdapter = new TicketAdapter(new ArrayList<>(0), this);
        mRcvTicket.setLayoutManager(new LinearLayoutManager(this));
        mRcvTicket.setHasFixedSize(true);
        mRcvTicket.setItemAnimator(new DefaultItemAnimator());
        mRcvTicket.setAdapter(mAdapter);
    }
}
