package com.taobao.dexposed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodHook;
import com.taobao.patch.PatchMain;
import com.taobao.patch.PatchResult;

import java.io.File;


public class MainActivity extends Activity {

    private boolean isSupport = false;

    private boolean isLDevice = false;

    private TextView mLogContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogContent = (TextView) (this.findViewById(R.id.log_content));
        //required // check device if support and auto load libs
        isSupport = DexposedBridge.canDexposed(this);//判断是否支持并loadLibrary
        //required
        isLDevice = android.os.Build.VERSION.SDK_INT == 21;//判断是否为5.0



        /*File cacheDir = getExternalCacheDir();
        if (cacheDir != null) {
            //File.separator 即 "/"
            String fullpath = cacheDir.getAbsolutePath() + File.separator + "patch.apk";
            Log.i("fullpath", fullpath);
            PatchResult result = PatchMain.load(this, fullpath, null);//important
            //原app给特定目录下特定名称的apk留好后门 且该apk只用于修改该activity下的函数
            //原app工程中一开始需要导入patch这个文件夹下的代码
            if (result.isSuccess()) {
                Log.e("Hotpatch", "patch success!");
            } else {
                Log.e("Hotpatch", "patch error is " + result.getErrorInfo());//
            }
        }*/
    }

    public void unhookAllMethods(View view) {
        Log.d("dexposed", "unhookAllMethods");
        DexposedBridge.unhookAllMethods();
    }

    private void showLog(String tag, String msg) {
        Log.d(tag, msg);
    }

    //Hook system log click
    public void hookSystemLog(View view) {
        //required
        //在Log.d之后执行的函数
        if (isSupport) {
            DexposedBridge.findAndHookMethod(isLDevice ? this.getClass() : Log.class
                    , isLDevice ? "showLog" : "d", String.class, String.class, new XC_MethodHook() {
                //Log.class,"d",String.class, String.class, new XC_MethodHook(){}
                @Override
                protected void afterHookedMethod(MethodHookParam arg0) throws Throwable {
                    String tag = (String) arg0.args[0];
                    String msg = (String) arg0.args[1];
                    mLogContent.setText(tag + "," + msg);
                }
            });
            if (isLDevice) {//版本21不支持
                showLog("dexposed", "It doesn't support AOP to system method on ART devices");
            } else {
                Log.d("dexposed", "Logs are redirected to display here");
            }
        } else {
            mLogContent.setText("This device doesn't support dexposed!");
        }
    }

    /*public void Mytest(View view) {
        if (isSupport) {
            DexposedBridge.findAndHookMethod(isLDevice ? this.getClass() : Log.class
                    , isLDevice ? "foo" : "e", String.class, String.class, new XC_MethodHook() {
                //Log.class,"d",String.class, String.class, new XC_MethodHook(){}
                @Override//运行前执行
                protected void beforeHookedMethod(MethodHookParam arg0) throws Throwable {
                    String tag = (String) arg0.args[0];
                    String msg = (String) arg0.args[1];
                    mLogContent.setText(tag + "," + msg);
                    Log.v("foo", "before");
                }

                @Override//运行后执行
                protected void afterHookedMethod(MethodHookParam arg0) throws Throwable {
                    Log.v("foo", "after");
                }
            });
            if (isLDevice) {//版本21不支持
                showLog("dexposed", "It doesn't support AOP to system method on ART devices");
            } else {
                Log.e("dexposed", "Mytest");
            }
        } else {
            mLogContent.setText("This device doesn't support dexposed!");
        }
    }*/

    // Hook choreographer click
    public void hookChoreographer(View view) {
        Log.d("dexposed", "hookChoreographer button clicked.");
        if (isSupport && !isLDevice) {
            if(!ChoreographerHook.instance().status()) {
                ChoreographerHook.instance().start();
            }else{
                ChoreographerHook.instance().stop();
            }
        } else {
            showLog("dexposed", "This device doesn't support this!");
        }
    }

    // Run patch apk
    public void runPatchApk(View view) {
        Log.d("dexposed", "runPatchApk button clicked.");
        if (isLDevice) {
            showLog("dexposed", "It doesn't support this function on L device.");
            return;
        }
        if (!isSupport) {
            Log.d("dexposed", "This device doesn't support dexposed!");
            return;
        }
        File cacheDir = getExternalCacheDir();
        if (cacheDir != null) {
            //File.separator 即 "/"
            String fullpath = cacheDir.getAbsolutePath() + File.separator + "patch.apk";
            Log.i("fullpath", fullpath);
            PatchResult result = PatchMain.load(this, fullpath, null);//important
            //原app给特定目录下特定名称的apk留好后门 且该apk只用于修改该activity下的函数
            //原app工程中一开始需要导入patch这个文件夹下的代码
            if (result.isSuccess()) {
                Log.e("Hotpatch", "patch success!");
            } else {
                Log.e("Hotpatch", "patch error is " + result.getErrorInfo());//
            }
        }
        showDialog();
    }

    //被替换的函数
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dexposed sample")
                .setMessage(
                        "Please clone patchsample project to generate apk, and copy it to \"/Android/data/com.taobao.dexposed/cache/patch.apk\"")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create().show();
    }

    public void testPatch(View view){
        Log.e("testPatch", "no");
    }

    private void test(){
        Log.e("test", "finish");
    }
    @Override
    public void finish(){
        //test();
        super.finish();
    }
}
