package edu.niit.testbmob.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.niit.testbmob.R;
import edu.niit.testbmob.Utils.Event;



public class EventAdapter extends RecyclerView.Adapter implements View.OnClickListener{

    private List<Event> alist;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener =
            null;
    private OnItemLongClickListener mOnItemLongClickListener;


    public EventAdapter(List<Event> list) {
        if (list != null) {
            this.alist = list;

        } else {
            alist = new ArrayList<Event>();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .event_item, parent, false);
        view.setOnClickListener(this);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        holder.itemView.setTag(position + "");
        if(onRecyclerViewItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    onRecyclerViewItemClickListener.onItemClick(holder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
        EventViewHolder eventViewHolder = (EventViewHolder) holder;
        eventViewHolder.position = position;
        Event info = alist.get(position);
        eventViewHolder.tv_event_address.setText(info.getAddress());
        eventViewHolder.tv_event_title.setText(info.getTitle());
        eventViewHolder.tv_event_time.setText(info.getTime());

    }


    @Override
    public int getItemCount() {
        return alist.size();
    }

    @Override
    public void onClick(View view) {
//        if (onRecyclerViewItemClickListener != null) {
//            onRecyclerViewItemClickListener.onItemClick(view, Integer.parseInt(view.getTag
//                    ().toString()));
//        }

    }

    public void setOnRecyclerViewItemClickListener
            (OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;

    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    private class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tv_event_title;
        TextView tv_event_address;
        TextView tv_event_time;
        int position;

        EventViewHolder(View itemView) {
            super(itemView);
            tv_event_title = (TextView) itemView.findViewById(R.id.tv_event_tittle);
            tv_event_address = (TextView) itemView.findViewById(R.id.tv_event_address);
            tv_event_time = (TextView) itemView.findViewById(R.id.tv_event_time);

        }
    }
}
