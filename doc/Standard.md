# 包结构

| 包名            | 内容                         | 备注                 |
| ------------- | -------------------------- | ------------------ |
| entity        | 项目实体类                      |                    |
| net           | 网络有关的工具                    |                    |
| util          | 工具类，与业务没有任何关系              |                    |
| activity      | Activity                   | 暂时负担起Controller的作用 |
| view.fragment | Fragment界面                 |                    |
| view.adapter  | 放置RecyclerView之类需要的adapter |                    |
| view.widget   | 自定义控件                      |                    |



# 项目结构

暂时只考虑最原始的MVC结构开发

绿色为UI+Controller，尽可能不接入业务处理，仅控制UI方面的工作

蓝色为Model，主要处理业务内容

红色为底层能力支撑，不会接入业务的处理

 ![QQ20160625-0@2x](img/QQ20160625-0@2x.png)









# 实例设计

![](img/QQ20160625-1@2x.png)



# API

