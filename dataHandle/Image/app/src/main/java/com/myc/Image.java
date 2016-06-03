package com.myc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import imageUtil.ImageUtil;

public class Image extends Activity {
    EditText mTextXEdt, mTextYEdt;
    ImageView imageView1, imageView2;
    Drawable mDrawable;
    Bitmap mBitmap;
    Button mSignBtn, mSaveBtn;
    Context mContext;
    Bitmap tmpBitmap;
    int textColor = Color.RED;
    int textSize = 75;
    int pos;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_SIGN = 2;
    String signPath = "";
    Bitmap signBitmap;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        mContext = this;
        findView();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        pos = bundle.getInt("position", 0);
        setSpecial(mBitmap);
    }

    private void setSpecial(Bitmap mBitmap) {
        int maxX;
        int maxY;
        switch (pos) {
//		case 0:
//			tmpBitmap = ImageUtil.zoomBitmap(mBitmap, mBitmap.getWidth()/2, mBitmap.getHeight()/2);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//		case 1:
//			tmpBitmap = ImageUtil.getRoundedCornerBitmap(mBitmap, 10f);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//		case 2:
//			tmpBitmap = ImageUtil.createReflectionImageWithOrigin(mBitmap);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//		case 3:
//			tmpBitmap = ImageUtil.postRotateBitamp(mBitmap, 90);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//
//		case 4:
//			tmpBitmap = ImageUtil.reverseBitmap(mBitmap, 0);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
            case 1://6
                getSign();
                Bitmap b = signBitmap == null
                        ? ImageUtil.readBitMap(mContext, R.drawable.ic_launcher)
                        : signBitmap;
                maxX = mBitmap.getWidth() - b.getWidth();
                maxY = mBitmap.getHeight() - b.getHeight();
                mTextXEdt.setHint("X:0-" + maxX);
                mTextYEdt.setHint("Y:0-" + maxY);
                mTextXEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Math.log10(maxX) + 1)});
                mTextYEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Math.log10(maxY) + 1)});
                mTextXEdt.setText(maxX / 2 + "");
                mTextYEdt.setText(maxY / 2 + "");
                findViewById(R.id.mSignReL).setVisibility(View.VISIBLE);
                findViewById(R.id.mTextPositionReL).setVisibility(View.VISIBLE);
                mSignBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mTextXEdt.getText().length()==0
                                ||mTextYEdt.getText().length()==0){
                            Toast.makeText(Image.this, "请先定好XY坐标", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_SIGN);
                    }
                });
                tmpBitmap = ImageUtil.doodle(mBitmap, b,
                        maxX / 2, maxY / 2);
                imageView2.setImageBitmap(tmpBitmap);
                final Bitmap finalMBitmap1 = mBitmap;
                mTextXEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (mTextXEdt.getText().length() != 0
                                && mTextYEdt.getText().length() != 0) {
                            getSign();
                            tmpBitmap = ImageUtil.doodle(finalMBitmap1
                                    , signBitmap == null ? ImageUtil.readBitMap(mContext, R.drawable.ic_launcher) : signBitmap
                                    , Integer.parseInt(mTextXEdt.getText().toString())
                                    , Integer.parseInt(mTextYEdt.getText().toString()));
                            imageView2.setImageBitmap(tmpBitmap);
                            System.gc();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                mTextYEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (mTextXEdt.getText().length() != 0
                                && mTextYEdt.getText().length() != 0) {
                            getSign();
                            tmpBitmap = ImageUtil.doodle(finalMBitmap1
                                    , signBitmap == null ? ImageUtil.readBitMap(mContext, R.drawable.ic_launcher) : signBitmap
                                    , Integer.parseInt(mTextXEdt.getText().toString())
                                    , Integer.parseInt(mTextYEdt.getText().toString()));
                            imageView2.setImageBitmap(tmpBitmap);
                            System.gc();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                break;

            case 2://7
                maxX = mBitmap.getWidth();
                maxY = mBitmap.getHeight();
                mTextXEdt.setHint("X:0-" + maxX);
                mTextYEdt.setHint("Y:0-" + maxY);
                mTextXEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Math.log10(maxX) + 1)});
                mTextYEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Math.log10(maxY) + 1)});
                findViewById(R.id.mTextReL).setVisibility(View.VISIBLE);
                findViewById(R.id.mTextPositionReL).setVisibility(View.VISIBLE);
                mTextXEdt.setText("10");
                mTextYEdt.setText(maxY / 2 + "");
                final EditText mTextContentEdt = (EditText) findViewById(R.id.mTextContentEdt);
                final EditText mTextSizeEdt = (EditText) findViewById(R.id.mTextSizeEdt);
                tmpBitmap = ImageUtil.drawText(mBitmap, mTextContentEdt.getText().toString(), 10, mBitmap.getHeight() / 2
                        , textSize, textColor);
                imageView2.setImageBitmap(tmpBitmap);
                final Bitmap finalMBitmap = mBitmap;
                mTextSizeEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (mTextXEdt.getText().length() != 0
                                && mTextYEdt.getText().length() != 0
                                && mTextSizeEdt.getText().length() != 0
                                && mTextContentEdt.getText().length() != 0) {
                            tmpBitmap = ImageUtil.drawText(finalMBitmap, mTextContentEdt.getText().toString()
                                    , Integer.parseInt(mTextXEdt.getText().toString())
                                    , Integer.parseInt(mTextYEdt.getText().toString())
                                    , Integer.parseInt(mTextSizeEdt.getText().toString()), textColor);
                            imageView2.setImageBitmap(tmpBitmap);
                            System.gc();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                mTextContentEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (mTextXEdt.getText().length() != 0
                                && mTextYEdt.getText().length() != 0
                                && mTextSizeEdt.getText().length() != 0
                                && mTextContentEdt.getText().length() != 0) {
                            tmpBitmap = ImageUtil.drawText(finalMBitmap, mTextContentEdt.getText().toString()
                                    , Integer.parseInt(mTextXEdt.getText().toString())
                                    , Integer.parseInt(mTextYEdt.getText().toString())
                                    , Integer.parseInt(mTextSizeEdt.getText().toString()), textColor);
                            imageView2.setImageBitmap(tmpBitmap);
                            System.gc();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                mTextXEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (mTextXEdt.getText().length() != 0
                                && mTextYEdt.getText().length() != 0
                                && mTextSizeEdt.getText().length() != 0
                                && mTextContentEdt.getText().length() != 0) {
                            tmpBitmap = ImageUtil.drawText(finalMBitmap, mTextContentEdt.getText().toString()
                                    , Integer.parseInt(mTextXEdt.getText().toString())
                                    , Integer.parseInt(mTextYEdt.getText().toString())
                                    , Integer.parseInt(mTextSizeEdt.getText().toString()), textColor);
                            imageView2.setImageBitmap(tmpBitmap);
                            System.gc();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                mTextYEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (mTextXEdt.getText().length() != 0
                                && mTextYEdt.getText().length() != 0
                                && mTextSizeEdt.getText().length() != 0
                                && mTextContentEdt.getText().length() != 0) {
                            tmpBitmap = ImageUtil.drawText(finalMBitmap, mTextContentEdt.getText().toString()
                                    , Integer.parseInt(mTextXEdt.getText().toString())
                                    , Integer.parseInt(mTextYEdt.getText().toString())
                                    , Integer.parseInt(mTextSizeEdt.getText().toString()), textColor);
                            imageView2.setImageBitmap(tmpBitmap);
                            System.gc();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                break;

//		case 8:
//			tmpBitmap = ImageUtil.oldRemeber(mBitmap);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//
//		case 9:
//			tmpBitmap = ImageUtil.blurImage(mBitmap);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//
//		case 10:
//			tmpBitmap = ImageUtil.blurImageAmeliorate(mBitmap);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//
//		case 11:
//			tmpBitmap = ImageUtil.emboss(mBitmap);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//
//		case 12:
//			tmpBitmap = ImageUtil.sharpenImageAmeliorate(mBitmap);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
//
//		case 13:
//			tmpBitmap = ImageUtil.film(mBitmap);
//			imageView2.setImageBitmap(tmpBitmap);
//			ImageUtil.SaveBitmap(tmpBitmap, "test.jpg");
//			ImageUtil.saveToLocal(tmpBitmap);
//			break;
//
//		case 14:
//			tmpBitmap = ImageUtil.sunshine(mBitmap, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
//			imageView2.setImageBitmap(tmpBitmap);
//			break;
            default:
                mBitmap = ImageUtil.readBitMap("mnt/sdcard/Photos/5.jpg");
                if (mBitmap == null) {
                }
                imageView2.setImageBitmap(mBitmap);
                break;
        }
    }

    private void findView() {
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        mSignBtn = (Button) findViewById(R.id.mSignBtn);
        mSaveBtn = (Button) findViewById(R.id.mSaveBtn);
        mTextXEdt = (EditText) findViewById(R.id.mTextXEdt);
        mTextYEdt = (EditText) findViewById(R.id.mTextYEdt);
        mSaveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog(tmpBitmap);
            }
        });
        mDrawable = imageView1.getDrawable();
        mBitmap = ImageUtil.readBitMap(mContext, R.drawable.image);
        imageView2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //TODO 选择图片
//                imageView1.setVisibility(View.GONE);
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            mBitmap = BitmapFactory.decodeFile(picturePath);
            setSpecial(mBitmap);

        } else if (requestCode == RESULT_LOAD_SIGN && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            signPath = picturePath;
            getSign();
            tmpBitmap = ImageUtil.doodle(mBitmap, signBitmap
                    , Integer.parseInt(mTextXEdt.getText().toString())
                    , Integer.parseInt(mTextYEdt.getText().toString()));
            imageView2.setImageBitmap(tmpBitmap);
            int maxX, maxY;
            maxX = mBitmap.getWidth() - signBitmap.getWidth();
            maxY = mBitmap.getHeight() - signBitmap.getHeight();
            mTextXEdt.setHint("X:0-" + maxX);
            mTextYEdt.setHint("Y:0-" + maxY);
            mTextXEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Math.log10(maxX) + 1)});
            mTextYEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter((int) Math.log10(maxY) + 1)});
            mTextXEdt.setText(maxX / 2 + "");
            mTextYEdt.setText(maxY / 2 + "");
        }

    }


    private void inputDialog(final Bitmap bitmap) {
//        saveFileName = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(Image.this);
        LayoutInflater factory = LayoutInflater.from(Image.this);
        final View textEntryView = factory.inflate(R.layout.input_dialog, null);
        builder.setTitle("保存的文件名");
        builder.setView(textEntryView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText filename = (EditText) textEntryView.findViewById(R.id.filename);
                String saveFileName = filename.getText().toString();
                String path = ImageUtil.SaveBitmap(bitmap, saveFileName);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(Image.this, "保存到了"+path, Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.show();
    }

    private void getSign() {
        if (signPath.equals(""))
            return;
        Bitmap bitmap = BitmapFactory.decodeFile(signPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        signBitmap = ImageUtil.readBitMap(mContext, sbs);
        try {
            sbs.close();
            baos.close();
            bitmap.recycle();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
