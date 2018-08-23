package com.glide.demo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn)
    public Button startBtn;

    @BindView(R.id.iv)
    public ImageView iv;

    private String jpgPicUrl = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";
    private String gifPicUrl = "http://p1.pstatp.com/large/166200019850062839d3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn, R.id.iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher_round)        //添加点位图
                        .skipMemoryCache(true)                          //取消内存缓存
                        .diskCacheStrategy(DiskCacheStrategy.NONE)     //取消本地缓存
                        .error(R.mipmap.ic_launcher)                    //添加下载失败后显示的图片
                        .override(200, 100)             //指定图片大小 原始大小可以用Target.SIZE_ORIGINAL
                        .transform(new GlideRoundTransform(this));  //添加圆角

                Glide.with(this)
                        .load(jpgPicUrl)
                        .apply(options)
                        .into(iv);
                break;
            case R.id.iv:
                break;
        }
    }

    //圆角图片
    public static class GlideRoundTransform extends BitmapTransformation {

        private static float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 200);
        }

        public GlideRoundTransform(Context context, int dp) {
//            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        public String getId() {
            return getClass().getName() + Math.round(radius);
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
}
