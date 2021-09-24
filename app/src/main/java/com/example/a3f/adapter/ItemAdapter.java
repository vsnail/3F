package com.example.a3f.adapter;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3f.R;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;

    public ItemAdapter(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        setItemList(list);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

//        String text = mItemList.get(position).second;
//
//        holder.mText.setText(text);
        holder.itemView.setTag(mItemList.get(position));
        holder.myplan_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemList.remove(position);
                notifyDataSetChanged();
            }
        });

        String text2=holder.myplan_et.getText().toString().trim();
        holder.myplan_et.setText(text2);

    }



    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
//        TextView mText;
        ImageView myplan_delete;
        EditText myplan_et;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
//            mText = (TextView) itemView.findViewById(R.id.myplan_title);
            myplan_delete=itemView.findViewById(R.id.myplan_delete);
            myplan_et=itemView.findViewById(R.id.myplan_et);
        }

        @Override
        public void onItemClicked(View view) {

//            Toast.makeText(view.getContext(), "點擊", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
