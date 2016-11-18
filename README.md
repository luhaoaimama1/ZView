# ZView
>本项目主要是为了搜集 `常用的` `小型的` `稳定的` `自定义View`
>Ps:虽然暂时还很小~ 但是架不住收集的时间长啊
>如果涉及什么的问题。请联系我。我会做出相应修改；

### JicPack
Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

Step 2. Add the dependency

> compile 'com.github.luhaoaimama1:ZView:[Latest release](https://github.com/luhaoaimama1/ZView/releases)'

#功能介绍

##com.zone.view.base包

| 名字 | 功能  | 优点|
| :------------: |:---------------:| :-----:|
| ViewGroup_Zone | 自定义布局的基类 | 封装过ZGridView与FlowLayout |
| SurfaceViewTemplate   | surfaceView模板 |  |

##com.zone.view.list包
| 名字 | 功能  | 优点|
| :------------: |:---------------:| :-----:|
| NoScrollGridView |  | [使用参考](https://github.com/luhaoaimama1/ZAdapter/blob/master/app/src/main/java/com/zone/zadapter/ScrollerGridActivity.java) |
| NoScrollListView   |  | [使用参考](https://github.com/luhaoaimama1/ZAdapter/blob/master/app/src/main/java/com/zone/zadapter/ScrollerListActivity.java) |

##com.zone.view.ninegridview包
| 名字 | 功能  | 优点|
| :------------: |:---------------:| :-----:|
| preview包:ImagePreviewActivity |  | [照着这个来一遍~练手](https://github.com/jeasonlzy/NineGridView) |##com.zone.view.ninegridview包
| ZGridView | 不过继承ViewGroup_Zone实现的 | 同上面的链接 |

##com.zone.view.label包
| 名字 | 功能  | 优点|
| :------------: |:---------------:| :-----:|
| LabelView | `多个图片` `旋转` `缩放` `位移` `生成合成后的位图` | 因为当时写的很乱,并且涉及 `逆矩阵` `矩阵映射`  暂时没勇气重构了~ |

##com.zone.view.switchbutton包
| 名字 | 功能  | 优点|
| :------------: |:---------------:| :-----:|
| SwitchButton | 不满足需求拿出来了,但是最近更新了 有时间看看此类还需要摘出来不了 | [来处](https://github.com/kyleduo/SwitchButton) |

##com.zone.view包
| 名字 | 功能  | 优点|
| :------------: |:---------------:| :-----:|
| FlowLayout | 流式布局支持padding margin并继承ViewGroup_Zone | |
| SideBar | 通讯录的边栏(项目摘过来~不知道是谁的) | |
| SquareImageView2 | 等宽或者等高ImageView | |
| ZSeekBarUnslide | 不能滑动的seekBar(能当进度条了~) | |

##com.zone
| 名字 | 功能  | 优点|
| :------------: |:---------------:| :-----:|
| LogUtil | 内部打印log用的 | |

[▲ 回到顶部](#top)