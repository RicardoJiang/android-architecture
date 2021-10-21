## 前言
`Android`开发发展到今天已经相当成熟了，各种架构大家也都耳熟能详，如`MVC`,`MVP`,`MVVM`等，其中`MVVM`更是被官方推荐，成为`Android`开发中的显学。     
不过软件开发中没有银弹，`MVVM`架构也不是尽善尽美的，在使用过程中也会有一些不太方便之处，而`MVI`可以很好的解决一部分`MVVM`的痛点。     
本文主要包括以下内容
1. `MVC`,`MVP`,`MVVM`等经典架构介绍
2. `MVI`架构到底是什么?
3. `MVI`架构实战

> 需要重点指出的是,标题中说`MVI`架构是`MVVM`的进阶版是指`MVI`在`MVVM`非常相似，并在其基础上做了一定的改良，并不是说`MVI`架构一定比`MVVM`适合你的项目       
> 各位同学可以在分析比较各个架构后，选择合适项目场景的架构

## 经典架构介绍
### `MVC`架构介绍
`MVC`是个古老的`Android`开发架构，随着`MVP`与`MVVM`的流行已经逐渐退出历史舞台，我们在这里做一个简单的介绍,其架构图如下所示：      
![](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2018/7/2/1645b73891134b78~tplv-t2oaga2asx-watermark.awebp)         
`MVC`架构主要分为以下几部分
1. 视图层（`View`）：对应于`xml`布局文件和`java`代码动态`view`部分
2. 控制层（`Controller`）：主要负责业务逻辑，在`android`中由`Activity`承担，同时因为`XML`视图功能太弱，所以`Activity`既要负责视图的显示又要加入控制逻辑，承担的功能过多。
3. 模型层（`Model`）：主要负责网络请求，数据库处理，`I/O`的操作，即页面的数据来源

由于`android`中`xml`布局的功能性太弱,`Activity`实际上负责了`View`层与`Controller`层两者的工作，所以在`android`中`mvc`更像是这种形式：        
![](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2018/7/2/1645b7389d014b6f~tplv-t2oaga2asx-watermark.awebp)        
因此`MVC`架构在`android`平台上的主要存在以下问题：
1. `Activity`同时负责`View`与`Controller`层的工作，违背了单一职责原则
2. `Model`层与`View`层存在耦合，存在互相依赖，违背了最小知识原则

### `MVP`架构介绍
由于`MVC`架构在`Android`平台上的一些缺陷，`MVP`也就应运而生了,其架构图如下所示     
![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/993bec0ee3ad477a93f3b03c3b35732f~tplv-k3u1fbpfcp-watermark.awebp)                 
`MVP`架构主要分为以下几个部分
1. `View`层：对应于`Activity`与`XML`,只负责显示`UI`,只与`Presenter`层交互，与`Model`层没有耦合
2. `Presenter`层： 主要负责处理业务逻辑，通过接口回调`View`层
3. `Model`层：主要负责网络请求，数据库处理等操作，这个没有什么变化

我们可以看到，`MVP`解决了`MVC`的两个问题，即`Activity`承担了两层职责与`View`层与`Model`层耦合的问题

但`MVP`架构同样有自己的问题
1. `Presenter`层通过接口与`View`通信，实际上持有了`View`的引用
2. 但是随着业务逻辑的增加，一个页面可能会非常复杂，这样就会造成`View`的接口会很庞大。

### `MVVM`架构介绍
`MVVM` 模式将 `Presenter` 改名为 `ViewModel`，基本上与 `MVP` 模式完全一致。        
唯一的区别是，它采用双向数据绑定（`data-binding`）：`View`的变动，自动反映在 `ViewModel`，反之亦然      
`MVVM`架构图如下所示：   
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/623a288804004cd5b89754d45592b431~tplv-k3u1fbpfcp-watermark.awebp)              
可以看出`MVVM`与`MVP`的主要区别在于,你不用去主动去刷新`UI`了，只要`Model`数据变了，会自动反映到`UI`上。换句话说，`MVVM`更像是自动化的`MVP`。

`MVVM`的双向数据绑定主要通过`DataBinding`实现，不过相信有很多人跟我一样，是不喜欢用`DataBinding`的，这样架构就变成了下面这样     
![](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8485b8fe71dc44a088832bc63e1abb50~tplv-k3u1fbpfcp-watermark.awebp)

1. `View`观察`ViewModle`的数据变化并自我更新,这其实是单一数据源而不是双向数据绑定，所以其实`MVVM`的这一大特性我其实并没有用到
2. `View`通过调用`ViewModel`提供的方法来与`ViewMdoel`交互

### 小结
1. `MVC`架构的主要问题在于`Activity`承担了`View`与`Controller`两层的职责，同时`View`层与`Model`层存在耦合
2. `MVP`引入`Presenter`层解决了`MVC`架构的两个问题，`View`只能与`Presenter`层交互，业务逻辑放在`Presenter`层
3. `MVP`的问题在于随着业务逻辑的增加，`View`的接口会很庞大，`MVVM`架构通过双向数据绑定可以解决这个问题
4. `MVVM`与`MVP`的主要区别在于,你不用去主动去刷新`UI`了，只要`Model`数据变了，会自动反映到`UI`上。换句话说，`MVVM`更像是自动化的`MVP`。
5. `MVVM`的双向数据绑定主要通过`DataBinding`实现，但有很多人(比如我)不喜欢用`DataBinding`，而是`View`通过`LiveData`等观察`ViewModle`的数据变化并自我更新,这其实是单一数据源而不是双向数据绑定

## `MVI`架构到底是什么?
### `MVVM`架构有什么不足?
要了解`MVI`架构，我们首先来了解下`MVVM`架构有什么不足    
相信使用`MVVM`架构的同学都有如下经验，为了保证数据流的单向流动，`LiveData`向外暴露时需要转化成`immutable`的，这需要添加不少模板代码并且容易遗忘，如下所示
```kotlin
class TestViewModel : ViewModel() {
    private val _pageState: MutableLiveData<PageState> = MutableLiveData()
    val pageState: LiveData<PageState> = _pageState
    private val _state1: MutableLiveData<String> = MutableLiveData()
    val state1: LiveData<String> = _state1
    //...
}
```
如上所示，如果页面逻辑比较复杂，`ViewModel`中将会有许多全局变量的`LiveData`,并且每个`LiveData`都必须定义两遍，一个可变的，一个不可变的。这其实就是我通过`MVVM`架构写比较复杂页面时最难受的点。     
其次就是`View`层通过调用`ViewModel`层的方法来交互的，`View`层与`ViewModel`的交互比较分散，不成体系

小结一下，在我的使用中，`MVVM`架构主要有以下不足
1. 为保证对外暴露的`LiveData`是不可变的，需要添加不少模板代码并且容易遗忘
2. `View`层与`ViewModel`层的交互比较分散零乱，不成体系

### `MVI`架构是什么?
`MVI` 与 `MVVM` 很相似，其借鉴了前端框架的思想，更加强调数据的单向流动和唯一数据源,架构图如下所示    
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/2ecd46797c084f08b9efc8fb5246a5db~tplv-k3u1fbpfcp-watermark.awebp)           
其主要分为以下几部分
1. `Model`: 与`MVVM`中的`Model`不同的是，`MVI`的`Model`主要指`UI`状态（`State`）。例如页面加载状态、控件位置等都是一种`UI`状态
2. `View`: 与其他`MVX`中的`View`一致，可能是一个`Activity`或者任意`UI`承载单元。`MVI`中的`View`通过订阅`Intent`的变化实现界面刷新（注意：这里不是`Activity`的`Intent`）
3. `Intent`: 此`Intent`不是`Activity`的`Intent`，用户的任何操作都被包装成`Intent`后发送给`Model`层进行数据请求

### 单向数据流
`MVI`强调数据的单向流动，主要分为以下几步：
1. 用户操作以`Intent`的形式通知`Model`
2. `Model`基于`Intent`更新`State`
3. `View`接收到`State`变化刷新UI。

数据永远在一个环形结构中单向流动，不能反向流动：       
![](https://raw.githubusercontents.com/shenzhen2017/resource/main/2021/october/p17.png)

上面简单的介绍了下`MVI`架构，下面我们一起来看下具体是怎么使用`MVI`架构的
## `MVI`架构实战
### 总体架构图
![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/765d1d45817744728c78ceb1e2566b9a~tplv-k3u1fbpfcp-watermark.awebp)               
我们使用`ViewModel`来承载`MVI`的`Model`层，总体结构也与`MVVM`类似,主要区别在于`Model`与`View`层交互的部分
1. `Model`层承载`UI`状态，并暴露出`ViewState`供`View`订阅，`ViewState`是个`data class`,包含所有页面状态
2. `View`层通过`Action`更新`ViewState`，替代`MVVM`通过调用`ViewModel`方法交互的方式

### `MVI`实例介绍
#### 添加`ViewState`与`ViewEvent`
`ViewState`承载页面的所有状态，`ViewEvent`则是一次性事件，如`Toast`等,如下所示
```kotlin
data class MainViewState(val fetchStatus: FetchStatus, val newsList: List<NewsItem>)  

sealed class MainViewEvent {
    data class ShowSnackbar(val message: String) : MainViewEvent()
    data class ShowToast(val message: String) : MainViewEvent()
}
```
1. 我们这里`ViewState`只定义了两个，一个是请求状态，一个是页面数据
2. `ViewEvent`也很简单，一个简单的密封类，显示`Toast`与`Snackbar`

#### `ViewState`更新
```kotlin
class MainViewModel : ViewModel() {
    private val _viewStates: MutableLiveData<MainViewState> = MutableLiveData()
    val viewStates = _viewStates.asLiveData()
    private val _viewEvents: SingleLiveEvent<MainViewEvent> = SingleLiveEvent()
    val viewEvents = _viewEvents.asLiveData()

    init {
        emit(MainViewState(fetchStatus = FetchStatus.NotFetched, newsList = emptyList()))
    }

    private fun fabClicked() {
        count++
        emit(MainViewEvent.ShowToast(message = "Fab clicked count $count"))
    }

    private fun emit(state: MainViewState?) {
        _viewStates.value = state
    }

    private fun emit(event: MainViewEvent?) {
        _viewEvents.value = event
    }
}
```
如上所示
1. 我们只需定义`ViewState`与`ViewEvent`两个`State`,后续增加状态时在`data class`中添加即可，不需要再写模板代码
2. `ViewEvents`是一次性的，通过`SingleLiveEvent`实现，当然你也可以用`Channel`当来实现
3. 当状态更新时，通过`emit`来更新状态

#### `View`监听`ViewState`
```kotlin
    private fun initViewModel() {
        viewModel.viewStates.observe(this) {
            renderViewState(it)
        }
        viewModel.viewEvents.observe(this) {
            renderViewEvent(it)
        }
    }
```
如上所示，`MVI` 使用 `ViewState` 对 `State` 集中管理，只需要订阅一个 `ViewState` 便可获取页面的所有状态，相对 `MVVM` 减少了不少模板代码。

#### `View`通过`Action`更新`State`
```kotlin
class MainActivity : AppCompatActivity() {
	private fun initView() {
        fabStar.setOnClickListener {
            viewModel.dispatch(MainViewAction.FabClicked)
        }
    }
}
class MainViewModel : ViewModel() {
    fun dispatch(action: MainViewAction) =
        reduce(viewStates.value, action)

    private fun reduce(state: MainViewState?, viewAction: MainViewAction) {
        when (viewAction) {
            is MainViewAction.NewsItemClicked -> newsItemClicked(viewAction.newsItem)
            MainViewAction.FabClicked -> fabClicked()
            MainViewAction.OnSwipeRefresh -> fetchNews(state)
            MainViewAction.FetchNews -> fetchNews(state)
        }
    }
}
```
如上所示，`View`通过`Action`与`ViewModel`交互，通过 `Action` 通信，有利于 `View` 与 `ViewModel` 之间的进一步解耦，同时所有调用以 `Action` 的形式汇总到一处，也有利于对行为的集中分析和监控

## 总结
本文主要介绍了`MVC`,`MVP`,`MVVM`与`MVI`架构，目前`MVVM`是官方推荐的架构，但仍然有以下几个痛点
1. `MVVM`与`MVP`的主要区别在于双向数据绑定，但由于很多人(比如我)并不喜欢使用`DataBindg`，其实并没有使用`MVVM`双向绑定的特性，而是单一数据源
2. 当页面复杂时，需要定义很多`State`,并且需要定义可变与不可变两种,状态会以双倍的速度膨胀，模板代码较多且容易遗忘
3. `View`与`ViewModel`通过`ViewModel`暴露的方法交互，比较零乱难以维护

而`MVI`可以比较好的解决以上痛点，它主要有以下优势
1. 强调数据单向流动，很容易对状态变化进行跟踪和回溯
2. 使用`ViewState`对`State`集中管理，只需要订阅一个 `ViewState` 便可获取页面的所有状态，相对 `MVVM` 减少了不少模板代码
3. `ViewModel`通过`ViewState`与`Action`通信，通过浏览`ViewState` 和 `Aciton` 定义就可以理清 `ViewModel` 的职责，可以直接拿来作为接口文档使用。

当然`MVI`也有一些缺点，比如
1. 所有的操作最终都会转换成`State`，所以当复杂页面的`State`容易膨胀
2. `state`是不变的，因此每当`state`需要更新时都要创建新对象替代老对象，这会带来一定内存开销

软件开发中没有银弹，所有架构都不是完美的，有自己的适用场景,读者可根据自己的需求选择使用。        
但通过以上的分析与介绍，我相信使用`MVI`架构代替没有使用`DataBinding`的`MVVM`是一个比较好的选择~

### `Sample`代码
[https://github.com/shenzhen2017/android-architecture](https://github.com/shenzhen2017/android-architecture)

### 参考资料
[基于Android的MVI架构：从双向绑定到单向数据流](https://juejin.cn/post/6920427168749060110)      
[Jetpack Compose 架构如何选？ MVP, MVVM, MVI](https://juejin.cn/post/6969382803112722446)      
[站在思想层面看MVX架构](https://juejin.cn/post/6998093259893407757)             
[Best Architecture For Android : MVI + LiveData + ViewModel = ❤️](https://proandroiddev.com/best-architecture-for-android-mvi-livedata-viewmodel-71a3a5ac7ee3)