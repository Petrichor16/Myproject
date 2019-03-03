package com.ombre.woodhouse.Helper;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.lang.reflect.Field;



/**
 * Created by OMBRE on 2018/5/22.
 */

public class BottomNavigationViewHelper {
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try{
            Field shiftingMode=menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView,false);
            shiftingMode.setAccessible(false);
            for(int i=0;i<menuView.getChildCount();i++){
                BottomNavigationItemView item=(BottomNavigationItemView)menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        }catch (NoSuchFieldException e){
            Log.e("BNVHelper","Unable to Change value shift mode",e);
        }catch (IllegalAccessException e){
            Log.e("BNVHelper","Unable to Change value shift mode",e);
        }

    }
}
