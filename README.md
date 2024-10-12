[TOC]

> [Google教程](https://developer.android.com/jetpack/compose/setup?hl=zh-cn#bom-version-mapping)
>
> [Navigation页面导航的使用](https://stars-one.site/2023/02/25/jetpack-compose-study-11#%E4%B8%8E%E5%BA%95%E9%83%A8%E5%AF%BC%E8%88%AA%E6%A0%8F%E8%81%94%E7%94%A8) 
>
> [Jetpack Compose中的副作用](https://blog.csdn.net/lyabc123456/article/details/128518034)
> 
> [官方用例compose-samples](https://github.com/android/compose-samples)
>
> [BoM 物料清单](https://developer.android.com/jetpack/compose/bom/bom?hl=zh-cn)
>
> [Kotlin 预发布版本兼容的 Compose Compiler 版本](https://developer.android.com/jetpack/androidx/releases/compose-kotlin?hl=zh-cn)
>
> [Flutter 俄罗斯方块](https://github.com/boyan01/flutter-tetris)

# Compose UI 布局结构

![Compose UI 布局结构](ComposeUi结构.png)

# 小技巧

## 软键盘显示/隐藏的控制

```kotlin
 val keyboard = LocalSoftwareKeyboardController.current
 onClick = {
     keyboard?.hide()
     keyboard?.show()
 }
```

## 不借助State手动刷新（不推荐这么做）

```kotlin
//触发当前Composable的recomposition
currentRecomposeScope.invalidate()
```

## Compose函数中dp转px

```kotlin
import androidx.compose.ui.platform.LocalDensity

val pxValue = with(LocalDensity.current) { 16.dp.toPx() }
```

# Compose和Android View的区别

| Android View | compose |
| ------------ | :------ |
| Button | Button |
|TextView|Text|
|EditText|TextField|
|ImageView|Image|
|LinearLayout(horizontally)|Row|
|LinearLayout(vertically)|Column|
|FrameLayout|Box|
|RecyclerView|LazyColumn|
|RecyclerView(horizontally)|LazyRow|
|Snackbar|Snackbar|

## Scaffold

Scaffold主要用于快速搭建一个项目的结构，包含：

- topBar:通常是TopAppBar
- bottomBar 通常是一个 BottomNavigation,里面每个item是BottomNavigationItem
- floatingActionButton 悬浮按钮
- floatingActionButtonPosition 悬浮按钮位置
- isFloatingActionButtonDocked 悬浮按钮是否贴到 bottomBar 上
- drawerContent 侧滑菜单
- content:内容区域

# Effect的使用 [Google教程](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn)

## 生命周期

![组合中可组合项的生命周期。进入组合，执行 0 次或多次重组，然后退出组合](https://developer.android.com/static/images/jetpack/compose/lifecycle-composition.png?hl=zh-cn)

组合中可组合项的生命周期。进入组合，执行 0 次或多次重组，然后退出组合。

- onActive（or onEnter）：当Composable首次进入组件树时
- onCommit（or onUpdate）：UI随着recomposition发生更新时
- onDispose（or onLeave）：当Composable从组件树移除时

当 Compose 更新重组时，它会经历三个阶段（跟传统View比较类似）：

- **组合**：Compose 确定**要显示的内容**  - 运行可组合函数并构建界面树。
- **布局**：Compose 确定界面树中每个元素的**尺寸和位置**。
- **绘图**：Compose 实际**渲染**各个界面元素。

对于一个Compose页面来说，它会经历以下步骤：

- 第一步，Composition，这其实就代表了我们的Composable函数执行的过程。
- 第二步，Layout，这跟我们View体系的Layout类似，但总体的分发流程是存在一些差异的。
- 第三步，Draw，也就是绘制，Compose的UI元素最终会绘制在Android的Canvas上。由此可见，Jetpack Compose虽然是全新的UI框架，但它的底层并没有脱离Android的范畴。
- 最后，Recomposition，也就是重组，并且重复1、2、3步骤。

尽可能推迟状态读取的原因，其实还是**希望我们可以在某些场景下直接跳过Recomposition的阶段、甚至Layout的阶段，只影响到Draw。**

## 副作用
建议先看[官方文档](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn)然后再看一下这篇博客[Jetpack Compose中的副作用](https://blog.csdn.net/lyabc123456/article/details/128518034)就差不多理解副作用概念了  

- **非挂起的副作用**：例如当 Composable 进入组合时，运行一个副作用来初始化一个回调，当 Composable 离开组合，销毁这个回调。
  
  - DisposableEffect
  - SideEffect
  - currentRecomposeScope：currentRecomposeScope 的作用与View#invalidate方法类似，通过调用 currentRecomposeScope.invalidate()，它将使当前时刻的本地组合无效，并强制触发重组。一般用于手动触发重组。
  
- **挂起的副作用**：例如从网络加载数据以提供一些UI状态。
  - LaunchedEffect
  - rememberCoroutineScope
  - rememberUpdatedState
  - snapshotFlow
  - produceState
  - derivedStateOf

### [LaunchedEffect：在某个可组合项的作用域内运行挂起函数](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#launchedeffect)

如需从可组合项内安全调用挂起函数，请使用 [`LaunchedEffect`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?hl=zh-cn#LaunchedEffect(kotlin.Any,kotlin.coroutines.SuspendFunction1)) 可组合项。当 `LaunchedEffect` 进入组合时，它会启动一个协程，并将代码块作为参数传递。如果 `LaunchedEffect` 退出组合，协程将取消。如果使用不同的键重组 `LaunchedEffect`（请参阅下方的[重启效应](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#restarting-effects)部分），系统将取消现有协程，并在新的协程中启动新的挂起函数。
key1传Unit或者true，首次渲染执行一次，重组则不再执行。key1传State，如果State值变化，则再次执行。
如果是条件启动该副作用，条件应该写在外面，而不是LaunchedEffect里面，尽量减少资源的浪费。

### [rememberCoroutineScope：获取组合感知作用域，以便在可组合项外启动协程](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#remembercoroutinescope)

由于 `LaunchedEffect` 是可组合函数，因此只能在其他可组合函数中使用。为了在可组合项外启动协程，但存在作用域限制，以便协程在退出组合后自动取消，请使用 [`rememberCoroutineScope`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?hl=zh-cn#rememberCoroutineScope(kotlin.Function0))。 此外，如果您需要手动控制一个或多个协程的生命周期，请使用 `rememberCoroutineScope`，例如在用户事件发生时取消动画。

`rememberCoroutineScope` 是一个可组合函数，会返回一个 `CoroutineScope`，该 CoroutineScope 绑定到调用它的组合点。调用退出组合后，作用域将取消。

> 与 `LaunchedEffect` 不同的是，`LaunchedEffect` 用于限定由**组合**发起的作业的作用域，而`rememberCoroutineScope` 则用于限定由**用户交互**发起的作业的作用域。

### [rememberUpdatedState：在效应中引用某个值，该效应在值改变时不应重启](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#rememberupdatedstate)

当其中一个键参数发生变化时，`LaunchedEffect` 会重启。不过，在某些情况下，您可能希望在效应中捕获某个值，但如果该值发生变化，您不希望效应重启。为此，需要使用 `rememberUpdatedState` 来创建对可捕获和更新的该值的引用。这种方法对于包含长期操作的效应十分有用，因为重新创建和重启这些操作可能代价高昂或令人望而却步。

### [DisposableEffect：需要清理的效应](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#disposableeffect)

它表示组合生命周期的副作用。`DisposableEffect`可以感知`Composable`的`onActive`和`onDispose`，允许通过副作用完成一些预处理和收尾工作。

和生命周期相关的逻辑执行使用。比如广播注册和反注册、监听和注销监听等等。
key1传Unit或者true，首次渲染执行一次，重组则不再执行。key1传State，则State值变化，则再次执行。

### [SideEffect：将 Compose 状态发布为非 Compose 代码](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#sideeffect-publish)

每次重组都需要执行的副作用。比如日志打印等等。**它旨在将更新发布到某些不受组合状态系统管理的外部状态，以保持其始终同步**

### [produceState：将非 Compose 状态转换为 Compose 状态](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#producestate)

[`produceState`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?hl=zh-cn#produceState(kotlin.Any,kotlin.coroutines.SuspendFunction1)) 会启动一个协程，该协程将作用域限定为可将值推送到返回的 [`State`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/State?hl=zh-cn) 的组合。使用此协程将非 Compose 状态转换为 Compose 状态，例如将外部订阅驱动的状态（如 `Flow`、`LiveData` 或 `RxJava`）引入组合。

### [derivedStateOf：将一个或多个状态对象转换为其他状态](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#derivedstateof)

如果某个状态是从其他状态对象计算或派生得出的，请使用 [`derivedStateOf`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?hl=zh-cn#derivedStateOf(kotlin.Function0))。使用此函数可确保仅当计算中使用的状态之一发生变化时才会进行计算。

### [snapshotFlow：将 Compose 的 State 转换为 Flow](https://developer.android.com/jetpack/compose/side-effects?hl=zh-cn#snapshotFlow)

使用 [`snapshotFlow`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary?hl=zh-cn#snapshotFlow(kotlin.Function0)) 将 [`State`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/State?hl=zh-cn) 对象转换为冷 Flow。`snapshotFlow` 会在收集到块时运行该块，并发出从块中读取的 `State` 对象的结果。当在 `snapshotFlow` 块中读取的 `State` 对象之一发生变化时，如果新值与之前发出的值不[相等](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/equals.html)，Flow 会向其收集器发出新值（此行为类似于 [`Flow.distinctUntilChanged`](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/distinct-until-changed.html) 的行为）。

## 总结
下图来自于这篇博客[Jetpack Compose中的副作用](https://blog.csdn.net/lyabc123456/article/details/128518034)

![总结1](https://img-blog.csdnimg.cn/133b07ad41554d0b8a32622efbbcc6cb.png)
![总结2](https://img-blog.csdnimg.cn/8c623bb912f24f6e9492573abe5bbe3e.png)
![总结3](https://img-blog.csdnimg.cn/4d3009ab17f24be58b18abe5bc7ffc72.png) 

# 实战

## TAB

> 思路
> TabRow包含一行Tab，并在当前选定的Tab下方显示一个指示器。 TabRow将其Tab在整行中均匀分布，每个选项卡占用相等的空间。 
> ScrollableTabRow包含一行Tab，并在当前选定的选项卡下方显示一个指示器。 ScrollableTabRow将其Tab从起始位置开始放置，并允许滚动到屏幕外放置的Tab。 

## 验证码输入框

> 思路
> BasicTextField 的 decorationBox 属性

### 基类`DVerificationCodeTextField`
### demo样式 `VerificationCodeTextField`
### 预览UI `TestVCodeUi`

## [学习 WordsFairyNoote词仙笔记](https://github.com/JIULANG9/WordsFairyNote)
