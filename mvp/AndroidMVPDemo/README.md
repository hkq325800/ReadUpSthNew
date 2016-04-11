# AndroidMVPDemo
a simple MVP Demo of Android

项目博客地址：
[简书博客地址](http://www.jianshu.com/p/9d40b298eca9)

demo代码流程：

1 Activity做了一些UI初始化的东西并需要实例化对应LoginPresenter的引用和实现 LoginView的接口，监听界面动作 

2 登陆按钮按下后即接收到登陆的事件，在onClick里接收到即通过LoginPresenter的引用把它交给LoginPresenter处理。LoginPresenter接收到了登陆的逻辑就知道要登陆了 

3 然后LoginPresenter显示进度条并且把逻辑交给我们的Model去处理，也就是这里面的LoginModel，（LoginModel的实现类LoginModelImpl），同时会把OnLoginFinishedListener也就是LoginPresenter自身传递给我们的Model（LoginModel）。 

4 LoginModel处理完逻辑之后，结果通过OnLoginFinishedListener回调通知LoginPresenter 

5 LoginPresenter再把结果返回给view层的Activity，最后activity显示结果
