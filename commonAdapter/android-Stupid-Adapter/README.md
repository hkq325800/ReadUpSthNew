# android-Stupid-Adapter
一个比较蠢的adapter,一切只是为了少写两行代码,
支持listview,gridView, recyclerview, 的adapter,  
开发者只需要关注 item View  和 数据的关系,业务逻辑.剩下的一切交给我

将viewholder,与 adapter分开,每个absListview 不需要创建一个对应的adapter 类,
只需要创建一个xAdapter 对象, 和一个viewHolder类 继承 xViewHolder<T> 然后实现里面的方法

XAdapter 继承自baseadapter.还有一些功能还没实现.平时开发也没用上,就没深究


wiki 已经写好.....

欢迎大家挑bug......如果有人用的话.

未来打算多创建几个XViewHolder 用来实现像,item侧滑动删除拉,什么的.....

#wiki


1.XAdapter2

	1.1.实例化 
	       1.1.1 解释 XAdapter2<Java Bean> adapter //存放在adapter里的数据格式,也可以指定object
		   例子 XAdapter2<Joke> adapter;

	       1.1.2 解释 adapter = new XAdapter2<Java bean>(Content对象, 初始化数据,可以传null , 每条item的View Holder);
		   例子 adapter = new XAdapter2<Joke>(this, null, JokeViewHolder.class);
	       1.1.3 为listview设置adapter
		   listView.setAdapter(adapter);
	1.2.事件
	       1.2.1 点击事件
		   adapter.setClickItemListener(this);// 设置item的点击事件;
		   回调
		   public void onClickItem(View v, int p); //v 当前点击的控件,p 当前点击的元素位置,
	       1.2.2 长按事件
		   adapter.setLongClickItemListener(this);// 设置item的长按事件;
		   回调
		   public boolean onLongClickItem(View v, int p) //v 当前点击的控件,p 当前点击的元素位置,

2. IXViewHolder 

		viewholder 主要实现 IXViewHolder 方法,并按照 IXViewHolder 的要求实现,
		只要继承了IXViewHolder的类就可以传递给adapter构造

	2.1 IXViewHolder 执行顺序
	 
		1)void onCreat(XAdapter adapter, Context context);

		2)void setOnClickItemListener(OnClickItemListener itemListener);

		3)void setOnLongClickItemListener(OnLongClickItemListener longClickItemListener);

		4)View getView(Object data, int position);

		5)View getView();

		6)View setInflater(LayoutInflater inflater);

		7)void onDestory(int nextPosition, int count);
	2.2 执行顺序

		当adapter开始 getView 时
		判断IXViewHolder是否创建,
		
		if 未创建
		
			开始创建IXViewHolder,
			1.实例化IXViewHolder
			2.(只执行一次)调用 (6) setInflater(inflater);
			3.(只执行一次)调用 (1) onCreat(adapter, context);
			4.(只执行一次)调用 (2) setOnClickItemListener
			5.(只执行一次)调用 (3) setOnLongClickItemListener
		
		else 已创建(循环执行)
			
			1.(7) onDestory(position, getCount());
			2.(5) getView(object, position);



---------分割线----大清早起来写代码--2015-08-26 7:54-------没写完,明天再说吧!-------------


--------2016-03-25------
这个"明天再写"一拖就是半年, 也没继续写.额,今天终于办理离职了,虽说是离职了,但是还得给公司做兼职.
未来要去哪里,我也没太想好,大连这个小地方,也不知道哪里适合我.虽然手里有个offer,但总觉得不是我的菜.


