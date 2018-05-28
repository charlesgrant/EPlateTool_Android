package com.mingbikes.eplatetool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingbikes.plate.entity.BrandEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cronus-tropix on 17/7/29.
 */
public class BrandAdapter extends BaseAdapter {

    List<BrandEntity> mBrandList = new ArrayList<>();
    private LayoutInflater mInflater;

    public BrandAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
    }

    public void addList(List<BrandEntity> _brandList) {
        mBrandList.clear();
        mBrandList.addAll(_brandList);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mBrandList.size();
    }

    @Override
    public BrandEntity getItem(int position) {
        return mBrandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TextView tv_ofo_park_count;
    public TextView tv_mobike_park_count;
    public TextView tv_xiaoming_park_count;
    public TextView tv_u_bike_park_count;
    public TextView tv_ku_qi_park_count;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_brand, null, false);

            viewHolder = new ViewHolder();

            viewHolder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            viewHolder.tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            viewHolder.tv_brand_bike_count_in_park_space = (TextView) convertView.findViewById(R.id.tv_brand_bike_count_in_park_space);
            viewHolder.tv_brand_update_time = (TextView) convertView.findViewById(R.id.tv_brand_update_time);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BrandEntity brandEntity = mBrandList.get(position);

        if (brandEntity.getName().equals("OFO")) {
            viewHolder.iv_logo.setBackgroundResource(R.drawable.ofo);
            tv_ofo_park_count = viewHolder.tv_brand_bike_count_in_park_space;

        } else if (brandEntity.getName().equals("摩拜")) {
            viewHolder.iv_logo.setBackgroundResource(R.drawable.mobike);
            tv_mobike_park_count = viewHolder.tv_brand_bike_count_in_park_space;

        } else if (brandEntity.getName().equals("小鸣")) {
            viewHolder.iv_logo.setBackgroundResource(R.drawable.xiaoming);
            tv_xiaoming_park_count = viewHolder.tv_brand_bike_count_in_park_space;

        } else if (brandEntity.getName().equals("优拜")) {
            viewHolder.iv_logo.setBackgroundResource(R.drawable.u_bike);
            tv_u_bike_park_count = viewHolder.tv_brand_bike_count_in_park_space;

        } else if (brandEntity.getName().equals("酷骑")) {
            viewHolder.iv_logo.setBackgroundResource(R.drawable.ku_qi_bike);
            tv_ku_qi_park_count = viewHolder.tv_brand_bike_count_in_park_space;

        }

        viewHolder.tv_brand_name.setText(brandEntity.getName());
        viewHolder.tv_brand_bike_count_in_park_space.setText(brandEntity.getParkCount() + "辆");
        if (brandEntity.getUpdateDate() > 0) {
            viewHolder.tv_brand_update_time.setVisibility(View.VISIBLE);
            viewHolder.tv_brand_update_time.setText("最后更新时间: " + TimeUtil.getTime(brandEntity.getUpdateDate(), "-", true, true, true));
        } else {
            viewHolder.tv_brand_update_time.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_logo;
        TextView tv_brand_name;
        TextView tv_brand_bike_count_in_park_space;
        TextView tv_brand_update_time;
    }

}
