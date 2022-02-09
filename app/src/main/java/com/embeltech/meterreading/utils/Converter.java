package com.embeltech.meterreading.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.math.BigInteger;

public class Converter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static BigInteger getDecimalFromHexadecimal(String hex) {
        try {
            return new BigInteger(hex, 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return new BigInteger("0", 16);
    }

    public static String removeTrailingZeros(String num) {
        return num.replaceAll("0+$", "");
    }
}
