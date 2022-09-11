> Layui（谐音：类 UI) 是一套开源的 Web UI 组件库，采用自身轻量级模块化规范，遵循原生态的 HTML/CSS/JavaScript 开发模式，极易上手，拿来即用。使用 Layui，无需涉足各类构建工具，只需面向浏览器本身，即可轻松掌握页面所需的元素与交互。

# 引入资源

可以到官网下载 Layui 的 CSS 和 JS 文件到本地，然后从本地引入 HTML，也可以直接从网络引入。为方便测试，这里直接从网络引入：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 layui.css -->
    <link href="//unpkg.com/layui@2.7.6/dist/css/layui.css" rel="stylesheet">
    <!-- 引入 layui.js -->
    <script src="//unpkg.com/layui@2.7.6/dist/layui.js"></script>
    <!-- 引入 jquery.js -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <title>Title</title>
</head>
<body>
    
</body>
</html>
```

为简化代码，这里同时引入了 jQuery。

# 如何使用

引入了资源之后，直接到[在线示例](https://layui.gitee.io/v2/demo/)中复制相关组件的代码，然后删减、修改一下就行了。