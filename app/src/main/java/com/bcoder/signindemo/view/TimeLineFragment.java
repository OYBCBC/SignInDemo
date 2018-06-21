package com.bcoder.signindemo.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcoder.signindemo.R;
import com.bcoder.signindemo.view.TimeLine.OrderStatus;
import com.bcoder.signindemo.view.TimeLine.Orientation;
import com.bcoder.signindemo.view.TimeLine.TimeLineActivity;
import com.bcoder.signindemo.view.TimeLine.TimeLineAdapter;
import com.bcoder.signindemo.view.TimeLine.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/21.
 */

public class TimeLineFragment extends Fragment {



    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();

    private boolean mWithLinePadding;
    private Orientation mOrientation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_time_line, container, false);
        initTimeLineViews(view);

        return view;
    }

    public void initTimeLineViews(View view) {
        mOrientation = (Orientation) getActivity().getIntent().getSerializableExtra(TimeLineActivity.EXTRA_ORIENTATION);
        mWithLinePadding = getActivity().getIntent().getBooleanExtra(TimeLineActivity.EXTRA_WITH_LINE_PADDING, false);


        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);

        initView();
    }

    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    private void initView() {
        setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private void setDataListItems() {
        //从网络数据库中请求出数据

        mDataList.add(new TimeLineModel("Item successfully delivered", "", OrderStatus.INACTIVE));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", OrderStatus.ACTIVE));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", OrderStatus.COMPLETED));
        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", OrderStatus.COMPLETED));
    }
}
