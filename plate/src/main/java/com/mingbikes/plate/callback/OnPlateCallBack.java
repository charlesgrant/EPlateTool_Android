package com.mingbikes.plate.callback;

import com.mingbikes.plate.entity.BrandEntity;

import java.util.List;

/**
 * Created by cronus-tropix on 17/8/10.
 */

public interface OnPlateCallBack {

    void onBrandListLoad(List<BrandEntity> brandList);

    void onParkSpaceBrandListLoad(List<BrandEntity> brandList);

    void onParkExtraNotify(int type, String brandId, long delay);
}
