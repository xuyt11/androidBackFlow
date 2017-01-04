# androidBackFlow
a tool to control the view(activity and fragment) rollback flow

一个控制Android视图（即：activity与fragment）回退的工具。


## 快速使用
0. 使用前：将App中所有的activity与fragment都继承于两个基础类（BaseActivity与BaseFragment）；或在自己的基础类中@override startActivity与onActivityResult两个方法；
1. 结束App功能：结束App中所有的activity（准确的说是：finish该task中所有的activity）
    ```java
    BackFlow.finishApp(activity | fragment);
    ```
2. 返回到指定的activity（回退到指定的activity）
    ```java
    BackFlow.request(activity | fragment, @NonNull Class<? extends Activity> atyClass);
    ```
3. 返回到指定的fragment（回退到包含了指定fragment的activity）
    ```java
    BackFlow.requestF(activity | fragment, @NonNull Class<? extends ragment> fragmentClass);
    ```
4. 返回到activity和fragment都一致的activity；
    ```java
    BackFlow.request(activity | fragment, @NonNull Class<? extends Activity> atyClass, @NonNull Class<? extends ragment> fragmentClass);
    ```


## 内部实现
1. 利用startActivityForResult、onActivityResult、setResult与finish(activity)4四个方法，进行实现的；
2. 需要有两个基础类：BaseActivity与BaseFragment，所有的activity与fragment都需要继承于他们；
3. 需要@Override App中BaseActivity与BaseFragment两个类的startActivity(intent)方法，并且在内部实现中调用startActivityForResult(intent, requestCode)方法，使得在BackFlow操作时，能串行的回退；
    ```java
       @Override
       public void startActivity(Intent intent) {
           startActivityForResult(intent, BackFlow.REQUEST_CODE);
       }
    ```
4. 需要@Override App中BaseActivity与BaseFragment两个类的onActivityResult(requestCode, resultCode, data)方法，并在内部调用BackFlow.handle(this, resultCode, data)来进行回退操作的管理，并在目标位置结束继续调用onActivityResult方法；
    ```java
       @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           if (BackFlow.handle(this, resultCode, data)) {
               return;
           }
           super.onActivityResult(requestCode, resultCode, data);
       }
    ```
5. 调用BackFlow方法执行回退操作；
    * BackFlow操作内部会调用setResult与finish(activity)方法，用于链式回退；
    * 例如：退出当前的App（finish该task中所有的activity） **tip：有些情况会有影响，下面会有讲解**
    ```java
       BackFlow.finishApp(activity)
    ```


## 代码简介
1. 主功能：BackFlowType.java
    * 共五个分类：error，finish_app，back_to_activity，back_to_fragment，back_to_activity_fragment
       * finish_app: 结束App功能：结束App中所有的activity（准确的说是：finish该task中所有的activity）
       * back_to_activity: 返回到指定的activity（回退到指定的activity）
       * back_to_fragment: 返回到指定的fragment（回退到包含了指定fragment的activity）；
       * back_to_activity_fragment: 返回到activity和fragment都一致的activity；
       * error：异常情况，onActivityResult方法参数data中data.getIntExtra(BACK_FLOW_TYPE, ERROR_BACK_FLOW_TYPE)，异常时都将返回该类型，且都不会处理；
2. 调用类：BackFlow.java
    * BackFlowType类的请求执行与处理的包装器，方便使用；
    * 设置了默认RESULT_CODE值（Integer.MAX_VALUE）；
        * 这是回退功能的核心结构，所以其他操作的resultCode不能与其一样，否则会有错误；
    * 设置了默认的REQUEST_CODE值（0x0000ffff）；
        * override startActivity方法时调用的，防止不能触发onActivityResult方法
        * 其他的requestCode，不能与其一样，否则App内部业务逻辑可能有异常情况
        * tip: Can only use lower 16 bits for requestCode
3. 基础类：BaseActivity与BaseFragment
    * 所有的activity与fragment都需要继承于他们；
    * 或者实现两个类的功能：
        * @Override 两个类的startActivity(intent)方法，并且在内部实现中调用startActivityForResult(intent, requestCode)方法，使得在BackFlow操作时，能串行的回退；
        * @Override 两个类的onActivityResult(requestCode, resultCode, data)方法，并在内部调用BackFlow.handle(this, resultCode, data)来进行回退操作的管理，并在目标位置结束继续调用onActivityResult方法；


## BackFlow不能使用的情况或使用不能回退到目标位置
1. 若在回退链中间有任何一个TempActivity消耗过onActivityResult方法，则会停留在该TempActivity，不能继续回退
    * 因为整个回退功能都是依赖于setResult方法将回退数据，链式的传递给前一个activity的onActivityResult方法，而在activity消耗了onActivityResult方法之后，是不会再调用该方法的。
2. 现在发现的消耗onActivityResult方法的情况有：
    * 切换task；
    * 切换process；
    * 在startActivity时，调用了intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);





