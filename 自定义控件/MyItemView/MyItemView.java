package com.example.asdfsd;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by deng on 16/12/13.
 */

public class MyItemView extends RelativeLayout {

	private Context mContext;

	private int initTitleColor = Color.parseColor("#3f3f3f");
	private int initContentColor = Color.parseColor("#ababab");
	private int initBgColor = Color.parseColor("#ffffff");

	public TextView titleTv;
	public TextView contentTv;
	public ImageView iconIv;
	public ImageView arrowIv;
	public View bottomLine;

	public MyItemView(Context context) {
		this(context, null);
	}

	public MyItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		this.mContext = context;

		initView();

		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.MyItemView, defStyleAttr, 0);
		int arrayCount = typedArray.getIndexCount();
		for (int i = 0; i < arrayCount; i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.MyItemView_iconLeft:
				iconIv.setVisibility(View.VISIBLE);
				iconIv.setImageDrawable(typedArray.getDrawable(attr));
				break;
			case R.styleable.MyItemView_bgColor:
				setBackgroundColor(typedArray.getColor(attr, initBgColor));
				break;
			case R.styleable.MyItemView_titleSize:
				titleTv.setTextSize(typedArray.getDimension(attr, 14));
				break;
			case R.styleable.MyItemView_titleColor:
				titleTv.setTextColor(typedArray.getColor(attr, initTitleColor));
				break;
			case R.styleable.MyItemView_contentColor:
				contentTv.setTextColor(typedArray.getColor(attr, initContentColor));
				break;
			case R.styleable.MyItemView_contentSize:
				contentTv.setTextSize(typedArray.getDimension(attr, 14));
				break;
			case R.styleable.MyItemView_height:
				setMinimumHeight((int) typedArray.getDimension(attr, dp2px(48)));
				break;
			case R.styleable.MyItemView_hideBottomLine:
				
				boolean isHideLine = typedArray.getBoolean(attr, false);
				if (isHideLine)
					bottomLine.setVisibility(View.GONE);
				else
					bottomLine.setVisibility(View.VISIBLE);
				
				break;
			case R.styleable.MyItemView_hideArrow:
				
				boolean isHideArrow = typedArray.getBoolean(attr, false);
				if (isHideArrow)
					arrowIv.setVisibility(View.GONE);
				else
					arrowIv.setVisibility(View.VISIBLE);
				
				break;
			}
		}
		typedArray.recycle();
	}

	private void initView() {
		setBackgroundColor(initBgColor);
		
		setMinimumWidth(LayoutParams.MATCH_PARENT);
		setMinimumHeight(dp2px(56));
		
//		ViewGroup.LayoutParams params = getLayoutParams();
//		params.height =dp2px(56); 
//		setLayoutParams(params);
//		setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(56)));
		
		
		// iconIv
		iconIv = new ImageView(mContext);
		iconIv.setId(10080); // 不设id下面 RIGHT_OF 无效
		iconIv.setVisibility(View.GONE);
		RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iconParams.addRule(RelativeLayout.CENTER_VERTICAL);
		iconParams.leftMargin = dp2px(16);
		addView(iconIv, iconParams);

		// arrowIv
		arrowIv = new ImageView(mContext);
		arrowIv.setId(10081);
		arrowIv.setImageResource(R.drawable.arrow_right);
		RelativeLayout.LayoutParams arrowParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		arrowParams.addRule(RelativeLayout.CENTER_VERTICAL);
		arrowParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		arrowParams.rightMargin = dp2px(16);
		addView(arrowIv, arrowParams);

		// titleTv
		titleTv = new TextView(mContext);
		titleTv.setId(10082);
		titleTv.setTextColor(initTitleColor);
		titleTv.setTextSize(16);
		titleTv.setSingleLine(true);
		titleTv.setEllipsize(TruncateAt.END);
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		titleParams.addRule(RelativeLayout.RIGHT_OF, iconIv.getId());
		titleParams.leftMargin = dp2px(16);
		addView(titleTv, titleParams);
		
		// contentTv
		contentTv = new TextView(mContext);
		contentTv.setTextColor(initContentColor);
		contentTv.setSingleLine(true);
		contentTv.setGravity(Gravity.RIGHT);
		contentTv.setEllipsize(TruncateAt.END);
		RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		contentParams.addRule(RelativeLayout.CENTER_VERTICAL);
		contentParams.addRule(RelativeLayout.RIGHT_OF, titleTv.getId());
		contentParams.addRule(RelativeLayout.LEFT_OF, arrowIv.getId());
		contentParams.leftMargin = dp2px(16);
		contentParams.rightMargin = dp2px(16);
		addView(contentTv, contentParams);
		
		//bottomLine
		bottomLine = new View(mContext);
		bottomLine.setBackgroundColor(Color.parseColor("#cccccccc"));
		RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, dp2px(0.5f));
		lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lineParams.leftMargin = dp2px(16);
		addView(bottomLine, lineParams);
		
	}

	private int dp2px(float dp) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		if (enabled) {
			titleTv.setTextColor(initTitleColor);
		} else {
			titleTv.setTextColor(initContentColor);
		}
	}

}
