package cn.trinea.android.demo.eventbus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.Serializable;

/**
 * BroadcastReceiver Demo, include general broadcast, local broadcast, ordered broadcast,sticky broadcast
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-9-20
 */
public class MainActivity extends BaseActivity {

    private Button sendSimplestEventBtn;
    private Button sendEventSelfDefinedBtn;
    private Button diffThreadModeBtn;
    private Button sendOrderedEventBtn;
    private Button sendStickyEventBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        initView();
    }

    private void initView() {
        //学习完毕 可接收object 但需要附带一个public void onEvent函数进行数据的返回处理
        //在onStart中EventBus.getDefault().register(this);onStop()中EventBus.getDefault().unregister(this);
        //用public void onEvent(SubscriberExceptionEvent event) 接收eventBus返回的错误
        sendSimplestEventBtn = (Button)findViewById(R.id.send_simplest_event);
        sendSimplestEventBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SendSimplestEventActivity.class));
            }
        });

        //与我在第一个中传输的TestBean类似不需要继承Serializable之类
        //可以尝试在startActivity后向前一个register过的activity发送EventBus看返回时接不接收得到
        sendEventSelfDefinedBtn = (Button)findViewById(R.id.send_event_self_defined);
        sendEventSelfDefinedBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SendSelfDefinedEventActivity.class));
            }
        });

        //除onEvent之外还可以使用onEventBackgroundThread、onEventAsync、onEventMainThread作函数名
        //其中使用的线程信息onEvent与onEventMainThread等效
        //响应速度onEvent > onEventAsync > onEventBackgroundThread > onEventMainThread
        //onEventBackgroundThread有时会触发不能后台进程setText的错误 较不稳定
        //onEventAsync也有小概率因为同样问题无法进行
        //CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views
        //对\r\n学习了一个：回车换行
        diffThreadModeBtn = (Button)findViewById(R.id.diff_thread_mode);
        diffThreadModeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, DiffThreadModeActivity.class));
            }
        });

        //在接收几次之后使用cancelEventDelivery可以取消event的进一步传播
        //fragment在onCreateView中register(this, index)设定优先级，在onDestroyView中unregister
        sendOrderedEventBtn = (Button)findViewById(R.id.send_ordered_event);
        sendOrderedEventBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ViewPagerDemo.class));
            }
        });

        //使用sticky形式的EventBus可以使数据被缓存在内存中而不必立即使用
        //发送的方法EventBus.getDefault().postSticky(obj)
        //注册的方法有所不同，需要在使用时EventBus.getDefault().registerSticky(this);
        //使用完毕后立即EventBus.getDefault().unregister(this);
        //接收的方法也写在同一个runnable中
        //register必须与至少一个onEvent一同出现 否则crash
        //而且要先postSticky再registerSticky
        sendStickyEventBtn = (Button)findViewById(R.id.send_sticky_event);
        sendStickyEventBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SendStickyEventActivity.class));
            }
        });
    }
}
