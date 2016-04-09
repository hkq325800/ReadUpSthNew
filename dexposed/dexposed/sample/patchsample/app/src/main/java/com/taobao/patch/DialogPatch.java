package com.taobao.patch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodReplacement;

public class DialogPatch implements IPatch {

    @Override
    public void handlePatch(final PatchParam arg0) throws Throwable {
        Class<?> cls = null;
        try {
            cls = arg0.context.getClassLoader()
                    .loadClass("com.taobao.dexposed.MainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        DexposedBridge.findAndHookMethod(cls, "showDialog",
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        Activity mainActivity = (Activity) param.thisObject;
                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                        builder.setTitle("Dexposed sample")
                                .setMessage("The dialog is shown from patch apk!")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }).create().show();
                        return null;
                    }
                });
        final Class<?> finalCls = cls;
        //cls和mainActivity有什么区别?
        DexposedBridge.findAndHookMethod(cls, "testPatch", View.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                Activity mainActivity = (Activity) methodHookParam.thisObject;
                Log.e("testPatch", "yes");
                //mainActivity.finish();
                for (int i = 0; i < methodHookParam.args.length; i++) {
                    Log.d("args", String.valueOf(methodHookParam.args[i]));
                }
                //访问传入参数
                Button btn = (Button) methodHookParam.args[0];
                Toast.makeText(mainActivity.getBaseContext(), btn.getText(), Toast.LENGTH_SHORT).show();
                return null;
            }
        });
    }

}
