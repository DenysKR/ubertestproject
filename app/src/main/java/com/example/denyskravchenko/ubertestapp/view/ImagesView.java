package com.example.denyskravchenko.ubertestapp.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.denyskravchenko.ubertestapp.R;
import com.example.denyskravchenko.ubertestapp.UberApplication;
import com.example.denyskravchenko.ubertestapp.presenter.ImagesFetchingPresenter;
import com.example.denyskravchenko.ubertestapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesView extends IImagesView {

    @Inject
    ImagesFetchingPresenter mPresenter;

    @BindView(R.id.images_grid)
    RecyclerView mImagesGrid;

    private ImagesAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((UberApplication) getApplication()).getFetchingComponent().inject(this);

        mPresenter.bindView(this);

        int columnsNumber = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, columnsNumber);
        mImagesGrid.setLayoutManager(layoutManager);

        mPresenter.fetchImagesCollection("cat");

    }

    @Override
    public void showImagesByUrls(List<String> photos) {
        mImagesGrid.post(() -> {
            mAdapter = new ImagesAdapter(photos);
            mImagesGrid.setAdapter(mAdapter);
            mImagesGrid.setHasFixedSize(true);
        });
    }

    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
        private List<String> mUrls;
        private int mImageSize = Utils.dp2px(ImagesView.this, 200);

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mPhotoItem;

            public ViewHolder(ImageView v) {
                super(v);
                mPhotoItem = v;
            }
        }

        public ImagesAdapter(List<String> urls) {
            mUrls = urls;
        }

        @Override
        public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Glide.with(ImagesView.this)
                    .load(mUrls.get(position))
                    .override(mImageSize, mImageSize)
                    .into(holder.mPhotoItem);

        }

        @Override
        public int getItemCount() {
            return mUrls.size();
        }
    }


}
