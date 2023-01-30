package com.example.ca1_assignment;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import static android.content.ContentValues.TAG;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private ArrayList<Upload> mUploads;
    private String cutString;
    private OnItemClickListener mListener;

    public ImageAdapter(Context context, ArrayList<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        Log.d(TAG, "string length: " + uploadCurrent.getDescription().length());
        if (uploadCurrent.getDescription().length() > 50) {
            cutString = uploadCurrent.getDescription().substring(0,50);
            cutString += "...";
            holder.textDescription.setText(cutString);
        }
        else {
            holder.textDescription.setText(uploadCurrent.getDescription());
        }
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName, textDescription;
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.favName);
            textDescription = itemView.findViewById(R.id.favDescription);
            imageView = itemView.findViewById(R.id.favImage);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}


//public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
//    private Context mContext;
//    private ArrayList<String> mUploads;
//
//    public ImageAdapter(Context context, ArrayList<String> uploads) {
//        mContext = context;
//        mUploads = uploads;
//    }
//
//    @Override
//    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
//        return new ImageViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(ImageViewHolder holder, int position) {
//        String uploadCurrent = mUploads.get(position);
////        holder.textViewName.setText(uploadCurrent.getName());
//        Picasso.get()
//                .load(uploadCurrent)
//                .fit()
//                .centerCrop()
//                .into(holder.imageView);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mUploads.size();
//    }
//
//    public class ImageViewHolder extends RecyclerView.ViewHolder {
//        public TextView textViewName;
//        public ImageView imageView;
//
//        public ImageViewHolder(View itemView) {
//            super(itemView);
//
//            textViewName = itemView.findViewById(R.id.favName);
//            imageView = itemView.findViewById(R.id.favImage);
//        }
//    }
//}