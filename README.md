# Weico
 
 这是一个简单的Android微博客户端，采用ButterKnife，OKHttp框架
 
 服务器采用 PHP+MySQL+Redis 构建


## 笔记
- 个人觉得最重要的是设计好Json数据协议，利用PHP输出Json数据，采用Okhttp和Gson得到Java数据模型，Gson泛型的问题，将实体消息封装到inner字段内，通过类继承绕开泛型，code字段和msg字段交给BaseActivity，BaseFragment检查处理。

-  Token字段登录后存入MySQL，并缓存到Redis,登录后自动刷新，旧的自动废弃。

- 其余主要是一些常规的优化，图片的标签防止错位，缩略图，LRU的缓存等

- 微博比较有意思的就是@功能，#话题#功能，通过正则表达式匹配来提取信息，生成提醒等批量插入MySQL,提高性能，#话题#功能有起始和终止#号，比较好匹配，而@功能由于没有中止符号较为难办，本项目支持两种:@Java@Scala@Kotlin 或者@Java @Scala @Kotlin 即通过后面的@符号或者空格来界定，不知有无更好的设计。UI通过SpannableString显示和响应点击事件。

- 本项目没有实现转发微博，特别是多人连续转发，本人考虑了两种方案，一种是每条微博保存被转发的微博id(如果多次转发，只保留最近的一条),显示时只显示对当前和最近的一条微博。另外一种是冗余设计，保存最原始的微博ID，并且保存所有中间转发的内容，这样可以显示全部转发。



## 运行效果

![](https://github.com/dingdangmao123/weico/blob/master/demo/11.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/12.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/13.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/1.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/2.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/3.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/4.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/5.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/6.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/7.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/8.png) ![](https://github.com/dingdangmao123/weico/blob/master/demo/9.png)

