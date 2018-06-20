package com.bcoder.signindemo.view.TimeLine;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.bcoder.signindemo.R;
import com.github.vipulasri.timelineview.TimelineView;


/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {


    TextView mDate;
    TextView mMessage;
    TimelineView mTimelineView;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);

        mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);
        mDate = (TextView) itemView.findViewById(R.id.text_timeline_date);
        mMessage = (TextView) itemView.findViewById(R.id.text_timeline_title);


        mTimelineView.initLine(viewType);
    }
}
