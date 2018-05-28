package com.mingbikes.plate;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mingbikes.plate.callback.OnPlateCallBack;
import com.mingbikes.plate.domain.Brand;
import com.mingbikes.plate.domain.Event;
import com.mingbikes.plate.entity.BrandEntity;
import com.mingbikes.plate.entity.BrandEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cronus-tropix on 17/8/10.
 */

public class PlateManager {

    private BluetoothAdapter mBluetoothAdapter;
    private ExecutorService mExecutor;
    private OnPlateCallBack mOnPlateCallBack;

    public PlateManager(OnPlateCallBack _onPlateCallBack) {
        // Create a private executor so we don't compete with threads used by AsyncTask
        // This uses fewer threads than the default executor so it won't hog CPU
        mExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        this.mOnPlateCallBack = _onPlateCallBack;
        initBrandList();
    }

    private void initBrandList() {
        mBrandList.clear();

        BrandEntity mingBikeBrand = new BrandEntity();
        mingBikeBrand.setName("小鸣");
        mingBikeBrand.setId(Brand.MINGBIKE_BIKE_BRAND);

        mBrandList.add(mingBikeBrand);

        BrandEntity ofoBrand = new BrandEntity();
        ofoBrand.setName("OFO");
        ofoBrand.setId(Brand.OFO_BIKE_BRAND);

        mBrandList.add(ofoBrand);

        BrandEntity mobikeBrand = new BrandEntity();
        mobikeBrand.setName("摩拜");
        mobikeBrand.setId(Brand.MOBIKE_BIKE_BRAND);

        mBrandList.add(mobikeBrand);

        BrandEntity youbikeBrand = new BrandEntity();
        youbikeBrand.setName("优拜");
        youbikeBrand.setId(Brand.UBIKE_BIKE_BRAND);

        mBrandList.add(youbikeBrand);

        BrandEntity KuqiBrand = new BrandEntity();
        KuqiBrand.setName("酷骑");
        KuqiBrand.setId(Brand.KUQI_BIKE_BRAND);

        mBrandList.add(KuqiBrand);

        mPlateSpaceMap.put(Brand.MINGBIKE_BIKE_BRAND, 0);
        mPlateSpaceMap.put(Brand.OFO_BIKE_BRAND, 0);
        mPlateSpaceMap.put(Brand.MOBIKE_BIKE_BRAND, 0);
        mPlateSpaceMap.put(Brand.UBIKE_BIKE_BRAND, 0);
        mPlateSpaceMap.put(Brand.KUQI_BIKE_BRAND, 0);

        if (mOnPlateCallBack != null) {
            mOnPlateCallBack.onBrandListLoad(mBrandList);
        }
    }

    public void startScan() {
        Log.e("", "start scan");
        if (mBluetoothAdapter == null || mBluetoothAdapter.isDiscovering()) {
            Log.e("", "scanning");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            stopScan();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    public void stopScan() {
        if (mLeScanCallback != null) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    public void checkPermission(Activity activity, int requestCode) {

        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, "不支持ble", Toast.LENGTH_LONG).show();
            activity.finish();
            return;
        }

        final BluetoothManager mBluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(activity, "不支持ble", Toast.LENGTH_LONG).show();
            activity.finish();
            return;
        }

        //打开蓝牙的套路
        if ((mBluetoothAdapter == null) || (!mBluetoothAdapter.isEnabled())) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, requestCode);
        }
    }

    private long mCurrentTime = 0;
    private long mLoopTime = 10 * 1000L;
    private List<ScanData> mScanDataList = new ArrayList<>();

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            if (device == null)
                return;
            if (TextUtils.isEmpty(device.getAddress())) {
                return;
            }
            if (Math.abs(rssi) <= 0) {
                return;
            }

            if (mCurrentTime == 0) {
                mCurrentTime = System.currentTimeMillis();
            }

            mScanDataList.add(new ScanData(device, rssi, scanRecord));

            if (System.currentTimeMillis() - mCurrentTime > mLoopTime) {
                Log.e("==", "==start process scan data");
                new ScanProcessor().executeOnExecutor(mExecutor, new ArrayList<>(mScanDataList));
                mCurrentTime = 0;
                mScanDataList.clear();
            }
        }
    };

    /**
     * handle scan data processor, ble scan callback do many work will cause ANR
     */
    private class ScanProcessor extends AsyncTask<ArrayList<ScanData>, Void, Integer> {

        public ScanProcessor() {
        }

        @Override
        protected Integer doInBackground(ArrayList<ScanData>... params) {

            if (params == null || params.length == 0) {
                return -1;
            }

            ArrayList<ScanData> scanDataListNotCompute = params[0];
            Map<String, String> rssiByMacAddressMap = new HashMap<>();
            List<ScanData> scanDataList = new ArrayList<>();

            for (ScanData scanData : scanDataListNotCompute) {

                if (rssiByMacAddressMap.containsKey(scanData.device.getAddress())) {

                    String rssiArray = rssiByMacAddressMap.get(scanData.device.getAddress());
                    rssiArray += "," + Math.abs(scanData.rssi);
                    rssiByMacAddressMap.put(scanData.device.getAddress(), rssiArray);
                } else {
                    rssiByMacAddressMap.put(scanData.device.getAddress(), Math.abs(scanData.rssi) + "");
                    scanDataList.add(scanData);
                }
            }

            int count = scanDataList.size();
            int max = 0;
            int min = 0;
            for (int index = 0; index < count; index++) {
                String macAddress = scanDataList.get(index).device.getAddress();

                if (rssiByMacAddressMap.containsKey(macAddress)) {

                    String rssiStr = rssiByMacAddressMap.get(macAddress);

                    if (TextUtils.isEmpty(rssiStr)) {
                        continue;
                    }

                    String[] rssiArray = rssiStr.split(",");

                    int rssiCount = 0;
                    int totalRssi = 0;
                    if (rssiArray != null && rssiArray.length > 2) {
                        for (String rssi : rssiArray) {
                            int rssiValue = Integer.valueOf(rssi);
                            if (max < rssiValue) max = rssiValue;
                            if (min > rssiValue) min = rssiValue;
                        }
                        for (String rssi : rssiArray) {
                            int rssiValue = Integer.valueOf(rssi);
                            if (max == rssiValue || min == rssiValue) {
                                continue;
                            }
                            totalRssi += rssiValue;
                            rssiCount += 1;
                        }
                        try {
                            totalRssi = totalRssi / rssiCount;
                        } catch (Exception e) {
                            FileUtils.writeTxtToFile("==throw exception length:" + rssiArray.length
                                            + ", max:" + max + ", min:" + min + ", totalRssi:" + totalRssi
                                            + ", rssiCount:" + rssiCount
                                    , null, null);

                            FileUtils.writeTxtToFile("===" + Arrays.toString(rssiArray) + ", rssiStr:" + rssiStr, null, null);
                            throw e;
                        }

                        scanDataList.get(index).rssi = totalRssi;
                    } else if (rssiArray != null && rssiArray.length == 2) {
                        for (String rssi : rssiArray) {
                            int rssiValue = Integer.valueOf(rssi);
                            totalRssi += rssiValue;
                        }
                        totalRssi = totalRssi / rssiArray.length;

                        scanDataList.get(index).rssi = totalRssi;
                    }
                }
            }

            String brandId;
            newMap.clear();
            mNewMacAddressList.clear();

            for (ScanData scanData : scanDataList) {

                if (BrandCheckUtil.checkOfo(scanData)) {
                    brandId = Brand.OFO_BIKE_BRAND;

                } else if (BrandCheckUtil.checkMoBike(scanData)) {
                    brandId = Brand.MOBIKE_BIKE_BRAND;

                } else if (BrandCheckUtil.checkMingBikes(scanData)) {
                    brandId = Brand.MINGBIKE_BIKE_BRAND;

                } else if (BrandCheckUtil.checkYouBike(scanData)) {
                    brandId = Brand.UBIKE_BIKE_BRAND;

                } else if (BrandCheckUtil.checkKuqi(scanData)) {
                    brandId = Brand.KUQI_BIKE_BRAND;

                } else {
                    brandId = null;
                }

                if (TextUtils.isEmpty(brandId)) {
                    continue;
                }

                String macAddress = scanData.device.getAddress();

                if (newMap.contains(macAddress)) {
                    continue;
                }

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("-");
                stringBuilder.append(brandId);
                stringBuilder.append("-");
                stringBuilder.append(macAddress);
                stringBuilder.append("\n");
                stringBuilder.append(Arrays.toString(scanData.scanRecord));
                stringBuilder.append("\n");
                stringBuilder.append(Arrays.toString(ByteUtils.toHexStringArray(scanData.scanRecord)));
                stringBuilder.append("\n");

                Log.e("", "==scan log:" + stringBuilder.toString());
                FileUtils.writeTxtToFile(stringBuilder.toString(), null, null);

                mNewMacAddressList.add(macAddress);
                newMap.add(macAddress);

                mBrandMacAddressMap.put(macAddress, brandId);
            }

            if (newMap.size() > 0) {
                checkDevices();
                return 1;
            } else {
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result < 0) {
                return;
            }
            processPlateSpaceData();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private Set<String> oldMap = new HashSet<>();
    private Set<String> newMap = new HashSet<>();
    private List<String> mOldMacAddressList = new ArrayList<>();
    private List<String> mNewMacAddressList = new ArrayList<>();
    private Map<String, String> mBrandMacAddressMap = new HashMap();
    private Map<String, Integer> mPlateSpaceMap = new HashMap();
    private List<BrandEvent> mPlateFoundList = new ArrayList<>();
    private List<BrandEntity> mBrandList = new ArrayList<>();

    private void checkDevices() {

        mPlateFoundList.clear();

        for (int index = 0; index < mNewMacAddressList.size(); index++) {
            String mac = mNewMacAddressList.get(index);
            if (TextUtils.isEmpty(mac)) {
                continue;
            }
            if (!oldMap.contains(mac)) {
                Log.e("新的-----", mac);
                mPlateFoundList.add(new BrandEvent(mac, Event.PLATE_IN_TYPE));
            }
        }

        for (int index = 0; index < mOldMacAddressList.size(); index++) {
            String mac = mOldMacAddressList.get(index);
            if (TextUtils.isEmpty(mac)) {
                continue;
            }
            if (!newMap.contains(mac)) {
                Log.e("旧的-----", mac);
                mPlateFoundList.add(new BrandEvent(mac, Event.PLATE_OUT_TYPE));
            }
        }

        mOldMacAddressList.clear();
        mOldMacAddressList.addAll(mNewMacAddressList);
        oldMap.clear();
        oldMap.addAll(newMap);
    }

    private void processPlateSpaceData() {

        int parkBikeCount = 0;

        for (int index = 0; index < mPlateFoundList.size(); index++) {

            BrandEvent event = mPlateFoundList.get(index);

            if (event == null || TextUtils.isEmpty(event.macAddress)) {
                continue;
            }

            String brandId = mBrandMacAddressMap.get(event.macAddress);

            if (!mPlateSpaceMap.containsKey(brandId)) {
                continue;
            }

            parkBikeCount = mPlateSpaceMap.get(brandId);

            if (event.type == Event.PLATE_IN_TYPE) {
                // in
                parkBikeCount += 1;
                Log.e("===", "===>PLATE_IN_TYPE:" + event.macAddress + "," + "brandId:" + brandId);
            } else {
                // out
                parkBikeCount -= 1;
                Log.e("===", "===>PLATE_OUT_TYPE:" + event.macAddress + "," + "brandId:" + brandId);
            }

            if (mOnPlateCallBack != null) {
                mOnPlateCallBack.onParkExtraNotify(event.type, brandId, index * 10);
            }

            mPlateSpaceMap.put(brandId, parkBikeCount);
        }

        if (mPlateSpaceMap.size() == 0) {
            return;
        }

        int count = mBrandList.size();
        for (int index = 0; index < count; index++) {
            String id = mBrandList.get(index).getId();
            if (mPlateSpaceMap.containsKey(id)) {
                mBrandList.get(index).setParkCount(mPlateSpaceMap.get(id));
                mBrandList.get(index).setUpdateDate(System.currentTimeMillis());
            }
        }

        if (mOnPlateCallBack != null) {
            mOnPlateCallBack.onParkSpaceBrandListLoad(mBrandList);
        }
    }
}
