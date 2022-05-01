# aw06

下载了两个类别的 metadata，分别是 Arts_Crafts_and_Sewing 和 Grocery_and_Gourmet_Food

将其按照每 100000 行一个文件分割为 7 个文件，使用 partition step 进行处理

每个处理的 step 读取文件中的一行并转换为 json node，再转换至 POJO，最后使用 spring data jpa 存入数据库

观测到 slave step 的 chunk size 会严重影响运行速度，在 chunk size 大小为 1 时完成一个 step 要接近半小时，而将其提升到 100 后每个 step 只需要约 1 分钟，猜想是多次写入数据库大幅提升了运行时间。

最终写入约 58 万条数据，用时约5分钟

集成至 aw04 的方案为直接使用 spring data jpa，在原本实体类上加上对应注解即可