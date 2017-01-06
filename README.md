# androidBackFlow
a tool to control the view(activity and fragment) rollback flow

一个控制Android视图（即：activity与fragment）回退的工具。


## 快速使用
0. 使用前：
    * 将App中所有的activity与fragment都继承于两个基础类（BaseActivity与BaseFragment）；
    * 或在自己的基础类中@override startActivity与onActivityResult两个方法；
1. 结束该activity所属的task：
    * 若该App是单task的，则有结束App中所有的activity效果（finish该task中所有的activity，即退出了App）
    * 若在整个回退流程流程中，没有匹配到目标，也相当于finish_task的功能。
    * 若中间有onActivityResult方法被消耗，则会停留在最后一个被消耗的activity（因为setResult已无效）。
```java
BackFlow.finishTask(activity | fragment);
```
2. 返回到指定的activity（回退到指定的activity），若有多个activity实例，则只会回退到第一个匹配；
```java
BackFlow.request(activity | fragment, @NonNull Class<? extends Activity> atyClass);
```
3. 返回到指定的fragment列（回退到第一个匹配该fragment顺序列的activity）
```java
BackFlow.request(activity | fragment, @NonNull Class<? extends Fragment> fragmentClazzs);
```
4. 返回到activity和fragment列都一致的activity（回退到包含了该fragment顺序列的activity）
```java
BackFlow.request(activity | fragment, @NonNull Class<? extends Activity> atyClass, @NonNull Class<? extends Fragment> fragmentClazzs);
```
5. 若有额外参数，可以使用带Bundle参数的request方法
    * 传入额外参数
    ```java
    BackFlow.request(activity | fragment, @NonNull Bundle extra, @NonNull Class<? extends Activity> atyClass)
    ```
    * 判断是否有额外参数
    ```java
    BackFlow.hasExtra(Intent data)
    ```
    * 获取额外参数
    ```java
    BackFlow.getExtra(Intent data)
    ```
6. 也可以自己去使用Builder去构建BackFlow request
```java
BackFlow.builder(BackFlowType.back_to_fragments, FCSFSecondDFragment.this).setFragments(FcsfAFragment.class, FCSFSecondAFragment.class).create().request();
```


## 内部实现
1. 利用startActivityForResult、onActivityResult、setResult与finish(activity)4四个方法，进行实现的；
2. 需要有两个基础类：BaseActivity与BaseFragment，所有的activity与fragment都需要继承于他们；
3. 需要@Override App中BaseActivity与BaseFragment两个类的startActivity(intent)方法，并且在内部实现中调用startActivityForResult(intent, requestCode)方法，使得在BackFlow操作时，能串行链式的回退；
```java
@Override
public void startActivity(Intent intent) {
   startActivityForResult(intent, BackFlow.REQUEST_CODE);
}
```
4. 需要@Override App中BaseActivity的onActivityResult(requestCode, resultCode, data)方法，并在内部调用BackFlow.handle(this, resultCode, data)来进行回退操作的管理，并在目标位置结束继续调用onActivityResult方法；
    * 不需要@Override BaseFragment
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   if (BackFlow.handle(this, getSupportFragmentManager().getFragments(), requestCode, resultCode, data)) {
       return;
   }
   super.onActivityResult(requestCode, resultCode, data);
}
```
5. 调用BackFlow方法执行回退操作；
    * BackFlow操作内部会调用setResult与finish(activity)方法，用于链式回退；
    ```java
    static void request(@NonNull Activity activity, @NonNull Intent backFlowData) {
        activity.setResult(RESULT_CODE, backFlowData);
        activity.finish();
    }
    ```
    * 例如：退出当前的activity所属的task（finish该task中所有的activity）
        * **tip：有些情况会有影响: [BackFlow不能使用的情况或不能回退到目标位置](#backflow不能使用的情况或不能回退到目标位置)**
    ```java
    BackFlow.finishTask(activity)
    ```


## 代码简介
1. 主功能：BackFlowType.java
    * 共五个分类：error，finish_task，back_to_activity，back_to_fragments，back_to_activity_fragments
        * finish_task
            * 结束task：若该App是单task，则有结束App中所有的activity效果（finish该task中所有的activity）
            * 若在整个回退流程流程中，没有匹配到目标，也相当于finish_task的功能。
            * 若中间有onActivityResult方法被消耗，则会停留在最后一个被消耗的activity（因为setResult已无效）。
        * back_to_activity
            * 返回到指定的activity（回退到指定的activity），若有多个activity实例，则只会回退到第一个匹配；
        * back_to_fragments
            * 返回到指定的fragment列（回退到第一个匹配该fragment顺序列的activity）
        * back_to_activity_fragments
            * 返回到activity和fragment列都一致的activity（回退到包含了该fragment顺序列的activity）
        * error: 异常情况
            * onActivityResult方法参数data中data.getIntExtra(BACK_FLOW_TYPE, ERROR_BACK_FLOW_TYPE)，异常类型都返回该类型，且直接抛出异常；
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
        ```java
        startActivityForResult(intent, BackFlow.REQUEST_CODE);
        ```
        * @Override BaseActivity的onActivityResult(requestCode, resultCode, data)方法
            * 并在内部调用BackFlow.handle(this, getSupportFragmentManager().getFragments(), requestCode, resultCode, data)来进行回退操作的管理，
            * 并在目标位置结束继续调用BackFlow.request(activity, data)


## BackFlow不能使用的情况或不能回退到目标位置
1. 若在回退链中间有任何一个TempActivity消耗过onActivityResult方法，则会停留在该TempActivity，不能继续回退
    * 因为整个回退功能都是依赖于setResult方法将回退数据，链式的传递给前一个activity的onActivityResult方法，而在activity消耗了onActivityResult方法之后，是不会再调用该方法的。
2. 现在发现的消耗onActivityResult方法的情况有：
    * 切换task；
    * 切换process；
    * 在startActivity时，调用了intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


## TIP(限制)
1. onActivityResults方法不会被触发的情况：
    * 若是从activity调用的startActivity方法，则activity中fragment的onActivityResults方法不会被触发；
    * 若是从AFragment调用的startActivity方法，则在其activity中其他Fragment的onActivityResults方法不会被触发；
    * 具体事例可以从request activity and fragment-->ContainerAF1Activity.LetterAFFFragment的第二个按钮
2.
3.
4.


