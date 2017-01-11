# change log

## v0.2.0
* 添加了back_activity_count类型的BackFlow
    * **适用于固定顺序的业务流程中，每个activity界面都能有固定的position**
    * 回退数量为backActivityCount个的Activity
    * 两个activity position的差值，即为backActivityCount

## v0.1.3
fix null pointer exception for data`s extras

## v0.1.2
fix null pointer exception and add some sample gif

## v0.1.1
1. 添加了对android:launchMode的测试与注意事项
2. 添加了禁用BackFlow功能的方法：startActivity4NonBackFlow
3. 添加了工具的简介

## v0.1.0
实现了BackFlow功能：链式回退多层视图

