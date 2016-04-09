package com.stupid.method.adapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

/**
 * @说明： 执行顺序 {@link XViewHolder#getLayoutId()}
 *      {@link XViewHolder#onCreate(XAdapter, android.content.Context)}
 *      {@link XViewHolder#onDestory(int)}
 *      {@link XViewHolder#onResetView(Object, int)}
 * 
 * @警告 请不要手动调用 {@link IXViewHolder #getView(Object, int)}
 * 
 * 
 * @author comcp@126.com <br>
 *         github: https://github.com/comcp/android-StupidMethod
 * @version v1.7
 * 
 * @创建时间：2014-12-3上午11:21:31
 * 
 * 
 *                           增加异常抛出,不再拦截异常
 * 
 */
abstract public class XViewHolder<T> implements IXViewHolder<T>,
		OnClickListener, OnLongClickListener {

	protected Context context;

	protected LayoutInflater inflater;

	private OnClickItemListener itemListener;

	private OnLongClickItemListener longClickItemListener;
	protected Class<T> entityClass;

	protected T mData;

	protected View mRoot;
	private boolean onScrolling;

	private int position;
	public String tag = this.getClass().getSimpleName();

	protected View findViewById(int id) {
		if (mRoot != null)
			return mRoot.findViewById(id);
		else
			return null;
	}

	@Override
	public abstract int getLayoutId();

	public int getPosition() {
		return position;
	}

	public Resources getResources() {
		return mRoot.getResources();
	}

	/**
	 * 如果用户要用代码创建View,重写getView();
	 * 
	 * <pre>
	 * &#064;Override
	 * public View getView() {
	 * 
	 * 	if (super.getView() == null) {
	 * 		mRoot = new View(context);
	 * 	}
	 * 
	 * 	return super.getView();
	 * }
	 * 
	 * </pre>
	 **/
	@Override
	public View getView() {
		return mRoot;
	}

	@Override
	public View getView(T data, int position, boolean onScrolling) {
		this.setOnScrolling(onScrolling);
		this.setPosition(position);
		mRoot.setTag(this);
		try {
			// 会出现强制类型转换问题
			onResetView(data, position);
		} catch (NullPointerException e) {
			Log.e(tag, String.format("在[%s]发生空指针异常,数据[%s],position[%d]", tag,
					data == null ? "null" : data.toString(), position));
			throw e;
		} catch (ClassCastException e) {
			Log.e(tag, String.format("在[%s] 发生类型转换异常->数据类型[%s] 无法转换成 泛型类型[%s]",
					tag, data == null ? "null" : data.getClass(),
					getEntityClass()));
			throw e;
		}
		return mRoot;
	}

	public boolean isOnScrolling() {
		return onScrolling;
	}

	@Override
	public void onClick(View v) {
		if (itemListener != null) {
			itemListener.onClickItem(v, getPosition());
		}

	}

	@Override
	public void onDestory(int nextPosition, int count) {

	}

	@Override
	public boolean onLongClick(View v) {
		if (longClickItemListener != null) {
			return longClickItemListener.onLongClickItem(v, getPosition());
		} else
			return false;
	}

	public final XViewHolder<T> setView(View v) {
		mRoot = v;
		return this;
	}

	public abstract void onResetView(T data, int position);

	public boolean post(Runnable action) {
		return mRoot.post(action);
	}

	public void setBackgroundColor(int color) {
		mRoot.setBackgroundColor(color);
	}

	public void setBackgroundDrawable(Drawable d) {
		mRoot.setBackgroundDrawable(d);
	}

	public void setBackgroundResource(int resid) {
		mRoot.setBackgroundResource(resid);
	}

	protected void setData(T data) {

		mData = data;

	}

	@Override
	public View setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
		this.context = inflater.getContext();

		if (mRoot == null)
			mRoot = getView();// 如果返回不为null,则使用该view
		if (mRoot == null) {

			mRoot = inflater.inflate(getLayoutId(), null);
			if (mRoot == null) {
				Log.e(tag,
						String.format(
								"[%s]类未实现getLayoutId(),或者传递进来的layout id 是错误的,当前获得到的layoutId为[%d]",
								getClass(), getLayoutId()));
				throw new NullPointerException(
						"通过 getLayoutId() 无法 inflate layout");
			}
		}
		return mRoot;
	}

	public void setLongClickable(boolean longClickable) {
		mRoot.setLongClickable(longClickable);
	}

	@Override
	public void setOnClickItemListener(OnClickItemListener itemListener) {
		this.itemListener = itemListener;
	}

	public void setOnClickListener(OnClickListener l) {
		if (mRoot != null)
			mRoot.setOnClickListener(l);
	}

	public void setOnKeyListener(OnKeyListener l) {
		mRoot.setOnKeyListener(l);

	}

	@Override
	public void setOnLongClickItemListener(
			OnLongClickItemListener longClickItemListener) {
		this.longClickItemListener = longClickItemListener;

	}

	public void setOnLongClickListener(OnLongClickListener l) {
		if (mRoot != null)
			mRoot.setOnLongClickListener(l);
	}

	public void setOnScrolling(boolean onScrolling) {
		this.onScrolling = onScrolling;
	}

	public void setOnTouchListener(OnTouchListener l) {
		mRoot.setOnTouchListener(l);
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setTag(int key, Object tag) {
		mRoot.setTag(key, tag);
	}

	public void setTag(Object tag) {
		mRoot.setTag(tag);
	}

	ViewHolder v7viewHolder;

	public ViewHolder getViewHolder() {
		setTag(this);
		if (v7viewHolder == null)
			v7viewHolder = new ViewHolder(getView()) {
			};
		return v7viewHolder;
	}

	protected Class<T> getEntityClass() {
		if (entityClass == null) {
			ParameterizedType type = (ParameterizedType) getClass()
					.getGenericSuperclass();

			Type[] t = type.getActualTypeArguments();
			if (t.length > 0)
				entityClass = (Class<T>) t[0];
			else
				Log.w(tag, "无法获得泛型类型");
		}
		return entityClass;
	}
}
