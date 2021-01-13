package com.miguel.imagegallery;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PictureBrowser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureBrowser extends Fragment implements onIndicatorClickedListener{

    private static final String PICTURE_ARRAY = "pictureArray";
    private static final String PICTURE_POSITION = "picturePosition";

    private Context mContext;
    private ArrayList<Picture> mPictureArray = new ArrayList<>();
    private int mPosition;
    private ImageView mImage;
    private ViewPager mImagePager;
    private int viewVisibilityController;
    private int viewVisibilityLooper;
    private ImagesPagerAdapter mPagerAdapter;
    private int previousSelected = -1;
    private RecyclerView mIndicatorRecycler;


    public PictureBrowser(){}
    public PictureBrowser(Context context ,ArrayList<Picture> pictures, int position) {
        // Required empty public constructor
        this.mContext = context;
        this.mPictureArray = pictures;
        this.mPosition = position;
    }

    public static PictureBrowser newInstance(Context context ,ArrayList<Picture> pictures, int position) {
        PictureBrowser fragment = new PictureBrowser(context, pictures, position);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture_browser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * initialisation of the recyclerView visibility control integers
         */
        viewVisibilityController = 0;
        viewVisibilityLooper = 0;

        mImagePager = view.findViewById(R.id.imagePager);
        mPagerAdapter = new ImagesPagerAdapter();
        mImagePager.setAdapter(mPagerAdapter);
        /** Only 3 images will be kept in memory for each side of the current image.
         * This allows performance improvement */
        mImagePager.setOffscreenPageLimit(3);
        mImagePager.setCurrentItem(mPosition);


        /** Create the indicator recyclerview */
        mIndicatorRecycler = view.findViewById(R.id.indicatorRecycler);
        mIndicatorRecycler.setHasFixedSize(true);
        mIndicatorRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.HORIZONTAL, false));

        IndicatorRecyclerAdapter adapter = new IndicatorRecyclerAdapter(getContext(), mPictureArray, this);
        mIndicatorRecycler.setAdapter(adapter);

        /** Adjusting the position of the recyclerview to the current position of the view holder */

        mPictureArray.get(mPosition).setSelected(true);
        previousSelected = mPosition;
        adapter.notifyDataSetChanged();
        mIndicatorRecycler.scrollToPosition(mPosition);

        /** Control recyclerview by adding listeners to the viewpager */

        mImagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(previousSelected!=-1){
                    mPictureArray.get(previousSelected).setSelected(false);
                }
                previousSelected = position;
                mPictureArray.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
                mIndicatorRecycler.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private class ImagesPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPictureArray.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            /** returns each view that will ocupy the page in the view pager */

            LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.picture_browser_pager,null);

            ImageView image = view.findViewById(R.id.image_browser);

            setTransitionName(image, String.valueOf(position)+"picture");

            Picture picture = mPictureArray.get(position);
            Glide.with(mContext)
                    .load(picture.getPicturePath())
                    .apply(new RequestOptions().fitCenter())
                    .into(image);

            image.setOnClickListener(
                    v -> mIndicatorRecycler.setVisibility(mIndicatorRecycler.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE)
            );

            /*
            if(view.getParent()!=null)
                ((ViewGroup) view.getParent()).removeView(view);
            */
            ((ViewPager) container).addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
    @Override
    public void onIndicatorClicked(int position) {
        if(previousSelected!=-1){
            mPictureArray.get(previousSelected).setSelected(false);
        }
        previousSelected = position;
        mPictureArray.get(position).setSelected(true);
        mIndicatorRecycler.getAdapter().notifyDataSetChanged();

        mImagePager.setCurrentItem(position);
    }
}