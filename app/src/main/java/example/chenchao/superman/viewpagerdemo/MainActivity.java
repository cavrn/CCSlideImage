package example.chenchao.superman.viewpagerdemo;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SimpleArrayMap<String, PictureSize> mPictureSizes = new SimpleArrayMap<>();
    private List<PictureModel> pictureModes = new ArrayList<>();
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pictureModes.add(new PictureModel(R.mipmap.pic_1, 600, 500));
        pictureModes.add(new PictureModel(R.mipmap.pic_2, 600, 900));
        pictureModes.add(new PictureModel(R.mipmap.pic_3, 600, 700));
        screenWidth = getScreenWidth(this);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pictureModes.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeAllViews();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
                container.addView(imageView);

                //图片尺寸规格
                PictureSize pictureSize = mPictureSizes.get(String.valueOf(position));
                if (pictureSize == null) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) container.getLayoutParams();
                    PictureModel pictureModel = pictureModes.get(position);
                    pictureSize = PictureSize.caculatePictureSize(pictureModel.getHeight(), pictureModel.getWidth(), (int) screenWidth);
                    mPictureSizes.put(String.valueOf(position), pictureSize);
                    params.height = pictureSize.getScaleHeight();
                    params.width = pictureSize.getScaleWidth();
                    container.setLayoutParams(params);
                    imageView.requestLayout();
                }

                Glide.with(container.getContext()).load(pictureModes.get(position).getPictureRes())
                        .override(pictureSize.getScaleWidth(), pictureSize.getScaleHeight())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
                return imageView;
            }
        });

        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position > 0 && position <= 1) {
                    int badgePosition = (int) (page.getX() / screenWidth) - 1;
                    PictureSize offsetModel = mPictureSizes.get(String.valueOf(badgePosition + 1));
                    if (offsetModel == null) return;
                    PictureSize nowModel = mPictureSizes.get(String.valueOf(badgePosition));
                    float disHeight = (offsetModel.getScaleHeight() - nowModel.getScaleHeight()) * (1 - position);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                    params.width = nowModel.getScaleWidth();
                    params.height = (int) (nowModel.getScaleHeight() + disHeight);
                    viewPager.setLayoutParams(params);
                    viewPager.requestLayout();
                }
            }
        });
    }

    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
