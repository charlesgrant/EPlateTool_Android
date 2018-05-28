package com.mingbikes.eplatetool;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingbikes.plate.PlateManager;
import com.mingbikes.plate.callback.OnPlateCallBack;
import com.mingbikes.plate.domain.Brand;
import com.mingbikes.plate.domain.Event;
import com.mingbikes.plate.entity.BrandEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int COMMON = 0;
    private final int SCANNING = 1;
    private int mDeviceStatus = COMMON;

    private BrandAdapter mBrandAdapter;
    // 贝塞尔曲线中间过程点坐标
    private float[] mCurrentPosition = new float[2];
    private ParkSpaceEntity mCurrentParkSpace;
    private PlateManager mPlateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
        setStatus("广东广州");

        mPlateManager = new PlateManager(new OnPlateCallBack() {
            @Override
            public void onBrandListLoad(List<BrandEntity> brandList) {
                mBrandAdapter.addList(brandList);
            }

            @Override
            public void onParkSpaceBrandListLoad(List<BrandEntity> brandList) {
                if (mCurrentParkSpace != null) {
                    mCurrentParkSpace.setAmount(0);
                    for (BrandEntity entity : brandList) {
                        mCurrentParkSpace.addAmount(entity.getParkCount());
                        showTitle(mCurrentParkSpace);
                    }
                }
                mBrandAdapter.addList(brandList);
            }

            @Override
            public void onParkExtraNotify(int type, String brandId, long delay) {
                onExtraNotify(type, brandId, delay);
            }
        });
        mPlateManager.checkPermission(this, 999);
    }

    public void onStartScanClick(View view) {
        if (mPlateManager == null) {
            return;
        }
        if (mDeviceStatus == COMMON) {
            mDeviceStatus = SCANNING;
            mPlateManager.startScan();
            tv_scan.setText("停止扫描");
        } else if (mDeviceStatus == SCANNING) {
            mDeviceStatus = COMMON;
            mPlateManager.stopScan();
            tv_scan.setText("开始扫描");
        }
    }

    public void onExtraNotify(final int park, final String brandId, long delay) {
        Log.e("==", "==onExtraNotify.brandId:" + brandId);

        if (park == Event.PLATE_IN_TYPE) {
            tv_park_space_name.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Brand.OFO_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_add, mBrandAdapter.tv_ofo_park_count);

                    } else if (Brand.MOBIKE_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_add, mBrandAdapter.tv_mobike_park_count);

                    } else if (Brand.MINGBIKE_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_add, mBrandAdapter.tv_xiaoming_park_count);

                    } else if (Brand.UBIKE_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_add, mBrandAdapter.tv_u_bike_park_count);

                    } else if (Brand.KUQI_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_add, mBrandAdapter.tv_ku_qi_park_count);

                    }
                }
            }, delay);
        } else {
            tv_park_space_name.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Brand.OFO_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_subtract, mBrandAdapter.tv_ofo_park_count);

                    } else if (Brand.MOBIKE_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_subtract, mBrandAdapter.tv_mobike_park_count);

                    } else if (Brand.MINGBIKE_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_subtract, mBrandAdapter.tv_xiaoming_park_count);

                    } else if (Brand.UBIKE_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_subtract, mBrandAdapter.tv_u_bike_park_count);

                    } else if (Brand.KUQI_BIKE_BRAND.equals(brandId)) {
                        showAnimation(R.drawable.icon_subtract, mBrandAdapter.tv_ku_qi_park_count);

                    }
                }
            }, delay);
        }
    }

    private void showAnimation(int res, View view) {
        if (view == null) {
            return;
        }

        int dp25 = ScreenUtil.dpToPx(25);

        final ImageView iv_park = new ImageView(this);
        iv_park.setImageResource(res);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp25, dp25);
        activity_main.addView(iv_park, params);

        int[] parentLocation = new int[2];
        activity_main.getLocationInWindow(parentLocation);

        int startLoc[] = new int[2];
        v_animation.getLocationInWindow(startLoc);

        int endLoc[] = new int[2];
        view.getLocationInWindow(endLoc);

        // 起始点：
        float startX = startLoc[0] - parentLocation[0] + dp25 / 2;
        float startY = startLoc[1] - parentLocation[1] + dp25 / 2;

        // 终点坐标
        int width = view.getWidth();
        int height = view.getHeight();

        float toX = endLoc[0] - parentLocation[0] - width / 2;
        float toY = endLoc[1] - parentLocation[1] - height / 4;

        // 开始绘制贝塞尔曲线
        Path path = new Path();
        // 移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        path.cubicTo((startX + toX) / 2, startY / 2, (startLoc[0] / 2), (startLoc[1] / 2), toX, toY);
        // mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，如果是true，path会形成一个闭环
        final PathMeasure pathMeasure = new PathMeasure(path, false);

        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(1000);

        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                pathMeasure.getPosTan(value, mCurrentPosition, null);
                iv_park.setTranslationX(mCurrentPosition[0]);
                iv_park.setTranslationY(mCurrentPosition[1]);
            }
        });

        // 开始执行动画
        valueAnimator.start();

        // 动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                activity_main.removeView(iv_park);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void showTitle(ParkSpaceEntity entity) {
        if (entity == null) {
            return;
        }
        tv_park_space_name.setText(entity.getName());
        tv_park_space_address.setText(entity.getLocation());
        tv_park_space_total_count.setText(entity.getAmount() + "/" + entity.getCapacity());

        int freeParkSpace = entity.getCapacity() - entity.getAmount();

        if (freeParkSpace > 0) {
            ll_park_space_total.setBackgroundResource(R.drawable.bg_kongxian);
            tv_park_space_tip.setTextColor(getResources().getColor(R.color.kongxian));

            tv_park_space_tip.setText(freeParkSpace + "个空闲车位");
        } else {
            ll_park_space_total.setBackgroundResource(R.drawable.bg_baoman);
            tv_park_space_tip.setTextColor(getResources().getColor(R.color.baoman));

            tv_park_space_tip.setText("超载" + Math.abs(freeParkSpace) + "辆");
        }
    }

    private RelativeLayout activity_main;

    private ListView ls_brand;

    private TextView tv_status;
    private TextView tv_park_space_name;
    private TextView tv_park_space_address;

    private LinearLayout ll_park_space_total;
    private TextView tv_park_space_total_count;
    private TextView tv_park_space_tip;
    private TextView tv_scan;

    private View v_animation;

    private void initViews() {

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_scan = (TextView) findViewById(R.id.tv_scan);
        tv_park_space_name = (TextView) findViewById(R.id.tv_park_space_name);
        tv_park_space_address = (TextView) findViewById(R.id.tv_park_space_address);
        tv_park_space_total_count = (TextView) findViewById(R.id.tv_park_space_total_count);
        tv_park_space_tip = (TextView) findViewById(R.id.tv_park_space_tip);
        ll_park_space_total = (LinearLayout) findViewById(R.id.ll_park_space_total);

        ls_brand = (ListView) findViewById(R.id.ls_brand);
        mBrandAdapter = new BrandAdapter(this);
        ls_brand.setAdapter(mBrandAdapter);

        v_animation = findViewById(R.id.v_animation);
    }

    private void initData() {
        mCurrentParkSpace = new ParkSpaceEntity();
        mCurrentParkSpace.setName("停车位001");
        mCurrentParkSpace.setCapacity(15);
        mCurrentParkSpace.setAmount(0);
        mCurrentParkSpace.setLocation("五山路尚德大厦141号");
        mCurrentParkSpace.setLongitude("113.346071");
        mCurrentParkSpace.setLatitude("23.140642");

        showTitle(mCurrentParkSpace);
    }

    private void setStatus(final String msg) {
        if (tv_status == null) {
            return;
        }

        tv_status.post(new Runnable() {
            @Override
            public void run() {
                tv_status.setText(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mPlateManager != null) {
            mPlateManager.stopScan();
        }
        super.onDestroy();
    }
}
