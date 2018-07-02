package com.leebaba.android.blockedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lichen on 2018/6/28.
 */

public class BlockEdittext extends LinearLayout {
    private static final String TAG = BlockEdittext.class.getSimpleName();

    private OnInputListener onInputListener;

    //edittext数量，默认6
    private int edittextNum;
    //edittext大小,默认50pix
    private float blockSize;
    //字体大小,默认25pix
    private float textSize;
    //字体颜色
    private int textColor;
    //边框样式
    @DrawableRes
    private int blockStyle;
    //edittext边框间距
    private float blockMargin;

    private List<EditText> editTextList;
    private List<Integer> idList;

    private StringBuffer stringBuffer = new StringBuffer();

    public BlockEdittext(Context context) {
        this(context, null);
    }

    public BlockEdittext(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockEdittext(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);

        blockInit(context, defStyleAttr);
    }

    public void setBlockSize(float blockSize) {
        this.blockSize = blockSize;
        for (int i = 0; i < editTextList.size(); i++) {
            EditText editText = editTextList.get(i);
            LinearLayout.LayoutParams layoutParams = (LayoutParams) editText.getLayoutParams();
            layoutParams.width = (int) blockSize;
            layoutParams.height = (int) blockSize;
            editText.setLayoutParams(layoutParams);
        }
        requestLayout();
    }


    public void setTextSize(float textSize) {
        this.textSize = textSize;
        for (int i = 0; i < editTextList.size(); i++) {
            EditText editText = editTextList.get(i);
            editText.setTextSize(textSize);
        }
        requestLayout();
    }

    public void setTextSize(int unit,float textSize) {
        this.textSize = textSize;
        for (int i = 0; i < editTextList.size(); i++) {
            EditText editText = editTextList.get(i);
            editText.setTextSize(unit,textSize);
        }
        requestLayout();
    }


    public void setBlockMargin(float blockMargin) {
        this.blockMargin = blockMargin;
        for (int i = 0; i < editTextList.size(); i++) {
            if(i != 0){
                EditText editText = editTextList.get(i);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
                layoutParams.setMargins((int) blockMargin,0,0,0);
                editText.setLayoutParams(layoutParams);
            }

        }
        requestLayout();
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        //默认水平
        setOrientation(LinearLayout.HORIZONTAL);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlockEdittext);
        edittextNum = typedArray.getInt(R.styleable.BlockEdittext_edittextNum, 6);
        blockSize = typedArray.getDimension(R.styleable.BlockEdittext_blockSize, 100);
        textSize = typedArray.getDimension(R.styleable.BlockEdittext_textSize, 50);
        textColor = typedArray.getColor(R.styleable.BlockEdittext_textColor, context.getResources().getColor(R.color.default_textcolor));
        blockStyle = typedArray.getResourceId(R.styleable.BlockEdittext_blockStyle, 0);
        if (blockStyle == 0) {
            blockStyle = R.drawable.shape_default_bg;
        }

        blockMargin = typedArray.getDimension(R.styleable.BlockEdittext_blockMargin, 40);

    }



    private void blockInit(Context context, int defStyleAttr) {
        editTextList = new ArrayList<EditText>();
        idList = new ArrayList<Integer>();
        EditText editText;
        for (int i = 0; i < edittextNum; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(((int) blockSize), ((int) blockSize), defStyleAttr);

            editText = new EditText(context);
            editText.setId(View.generateViewId());
            if (i != 0) {
                layoutParams.setMargins(((int) blockMargin), 0, 0, 0);
            }
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setTextColor(textColor);
            editText.setBackgroundResource(blockStyle);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            editText.setMaxLines(1);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setPadding(0, 0, 0, 0);


            editTextList.add(editText);

            addView(editText);
        }

        for (int i = 0; i < editTextList.size(); i++) {
            editText = editTextList.get(i);
            idList.add(editText.getId());
        }


        for (int i = 0; i < editTextList.size(); i++) {
            editText = editTextList.get(i);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int id = getFocusedChild().getId();
                    int index = getIndex(id);

                    if (s == null || TextUtils.isEmpty(s)) {
                        //减少
//                        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                        int length = stringBuffer.length();
                        if (index > 0) {
                            editTextList.get(index - 1).requestFocus();

                        }
                    } else {
                        //添加
                        /*stringBuffer.append(s);
                        int length = stringBuffer.length();*/
                        if (index < edittextNum-1) {
                            editTextList.get(index+1).requestFocus();
                        }


                    }

                    if(null != onInputListener){
                        EditText editTextTemp;
                        StringBuffer sb = new StringBuffer();
                        for (int j = 0; j < editTextList.size(); j++) {
                            editTextTemp = editTextList.get(j);
                            if(editTextTemp.getText()!=null && !TextUtils.isEmpty(editTextTemp.getText())){
                                sb.append(editTextTemp.getText());
                            }
                        }
                        onInputListener.OnInput(sb.toString());
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (int) (blockSize * edittextNum + blockMargin * (edittextNum - 1));
        int heigh = (int) blockSize;
        setMeasuredDimension(width, heigh);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private int getIndex(int id){
        for (int i = 0; i < idList.size(); i++) {
            if(id == idList.get(i)){
                return i;
            }
        }

        return -1;
    }

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }
}
