# SuperAdapter
Android RecyclerView多功能集合适配器

## SuperAdapter是什么

SuperAdapter 是对`RecyclerView.Adapter`进行封装并将许多常用功能集成之中的一个Android库。你不需要为每个列表单独去写`ViewHolder`后再声明控件，当构建不需要重用的列表时你甚至不需要单独创建类去继承`RecyclerView.Adapter`定制适配器，只要在`Activity`或`Fragment`中简单写下几行代码即可实现，有效的减化了复写代码的数量。


## SuperAdapter目前有哪些功能

  - 快速绑定单一布局列表
  - 简化多类型布局列表
  - 列表点击事件
  - 分页显示数据
  - 自定义列表唯一顶部Header
  - 自定义列表底部Footer
  - 上拉自动加载更多数据
  - 添加空数据提示视图


## 下载SuperAdapter
你需要在项目的根 `build.gradle` 加入如下JitPack仓库链接：

```
allprojects {
    repositories {
	    ...		
	    maven { url 'https://jitpack.io' }		
	}
}

```
着在你的需要依赖的Module的`build.gradle`加入依赖:

`compile 'com.github.JesseWuuu:SuperAdapter:x.y.z'`

**注意：上面依赖中的 `x.y.z` 为版本号，目前最新的版本号为 -> `0.1.3`**

## 关于开源
如果你只是想了解如何使用可以跳过本节。

如果你对这个项目感兴趣

## 定义与声明

如果你的列表只出现在了一个布局中并不会重复用使用，那么使用`SuperAdapter`时就不需要您单独创建类继承父类来定制化适配器，但是还是需要遵守一项规则：在`Activity`或`Fragment`中声明`SuperAdapter`属性的同时需要声明您的列表数据源类型：
 ```
List<DataEntity> mData;
SuperAdapter<DataEntity> mAdapter; 
```
**如果你的列表会在多处重复使用，这时你需要将 SuperAdapter 封装成自定义的 Adapter，下面会有专门一节讲封装 SuperAdapter 需要注意的事项，此处先不提。**

## 绑定单一布局的列表
绑定单一布局列表的方法非常简单，只需要在`SuperAdapter`的构造函数中添加布局文件的`layoutId`与数据源即可。
```
SuperAdapter<DataEntity> adapter = new SuperAdapter<DataEntity>(R.layout.view_list_item_1){

    @Override
    public void bindView(ViewHolder itemView, DataEntity data,int position) {
        // 此处写绑定itemview中的控件逻辑
        itemView.<TextView>getView(R.id.text_1).setText(data.getTitle());
        Button button = holder.<Button>getView(R.id.button);
        button.setEnabled(entity.IsEnabled());   
    }               
};
```
我们需要在方法 `void bindView(ViewHolder itemView, DataEntity data,int position)` 中绑定列表每个条目中的控件并进行逻辑处理。其中方法参数`ViewHolder itemView` 为自定义 `ViewHolder`。

### 通用ViewHolder
为方便管理，SuperAdapter 中持有的 ViewHolder 均为一个通用的自定义 ViewHolder ，其中它对外提供了一个 `<T extends View>T getView(int viewId)` 方法来代替 `View findViewById(int ViewId)`获取布局中的控件，目的是简化绑定控件方法以及对每个条目中的子 view 做了简单的缓存,具体使用方法上述代码段中有体现。

## 绑定多种布局的列表


绑定多种类型布局文件需要构造器`MultiItemViewBuilder<T>`来辅助适配器确定每个位置调用对应的布局文件。同理，声明属性同时需要声明数据源类型：

```
MultiItemViewBuilder<DataEntity> multiItemViewBuilder;
```

`MultiItemViewBuilder`需要您实现两个方法：

- `int getItemType(int position,T data)`，通过参数中的位置和数据源来判断返回ItemView类型，ItemView类型的值需自定义。
- `int getLayoutId(int type)`，通过判断`itemType`返回对应的布局文件id。
```
MultiItemViewBuilder<DataEntity> multiItemViewBuilder = MultiItemViewBuilder<TestEntity>() {
        @Override
        public int getLayoutId(int type) {
            if (type == 0){
                return R.layout.view_item_normal;
            }
            return R.layout.view_item_other;
        }

        @Override
        public int getItemType(int position, DataEntity data) {
                
            if(entity.isTest()){
                return 0;
            }else{
                return 1;
            }

        }
 };
```
最后，将`MultiItemViewBuilder`直接添加到`SuperAdapter`构造函数中即可：
```
mAdapter = new SuperAdapter<DataEntity>(multiItemViewBuilder,mData) {

        @Override
        public void convert(ViewHolder holder, DataEntity entity) {
              // 此处写绑定itemview中的控件逻辑
        }

 };
```
最后在方法`convert()`中绑定控件逻辑时需要针对不同的控件类型分别处理。

## 绑定列表点击事件
绑定点击事件：
```
mAdapter.setOnItemClickListener(new SuperAdapter.OnItemClickListener<DataEntity>() {
        @Override
        public void onItemClick(int position, DataEntity entity) {
            // 此处写点击事件的逻辑
        }
});

```
绑定长按事件：
```
mAdapter.setOnItemLongClickListener(new SuperAdapter.OnItemLongClickListener<DataEntity>() {
        @Override
        public void onItemLongClick(int position, DataEntity entity) {
            // 此处写长按事件的逻辑
        }
});

```

## 设置空数据视图

空数据视图是指宕列表的数据源数据为空时显示的提示视图。设置的使用方法非常简单：

```
mAdapter.setEmptyDataView(R.layout.empty_view);

```

## 添加列表唯一顶部Header

为什么要说是列表“唯一顶部”的 Header 呢？因为除该 Header 外，还有一种用于显示分类信息的 Header ，类似与通讯录中通过首字母 A~Z 顺序显示联系人。

自定义头部控件的本质其实就是列表中的一个特殊`ItemView`，它永远存在列表最上方的位置且跟随列表滑动。

添加自定义头部需要实现一个头部构造器来管理自定义头部布局：

```
HeaderBuilder builder = new HeaderBuilder() {
        @Override
        public int getHeaderLayoutId() {
            return R.layout.view_header;
        }

        @Override
        public void bindHeaderView(ViewHolder holder) {
            ImageView background = holder.getView(R.id.header_img);
            holder.<TextView>getView(R.id.header_name).setText("Test UserName);
        }

};
```
构造器接口中有两个回调方法：

- `int getLayoutId()` 设置自定义头部的布局id；
- `void convert(ViewHolder view) `  绑定布局中的控件及添加逻辑；

最后，将实现好的头部构造器添加到适配器中即可：
```
mAdapter.addHeader(builder);

```

## 添加列表分类Header

## 添加列表底部Footer

Footer 是为满足特殊需求一直存在于列表底部的ItemView，通常情况下是配合列表分页加载数据使用。添加 Footer 的方法与之前添加 Header 的方法类似，需要一个构造器管理：

```
FooterBuilder builder = new FooterBuilder() {
        
       /**
        * 获取Footer的布局文件Id
        */        
        @Override
        public int getFooterLayoutId() {
            return R.layout.view_footer;
        }

       /**
        * 非用于分页加载更多数据状态下Footer的界面逻辑处理
        */
        @Override
        public void onNormal(ViewHolder holder) {
            holder.<ProgressBar>getView(R.id.footer_progress).setVisibility(View.GONE);
            holder.<TextView>getView(R.id.footer_msg).setText("这是个底部");
        }

       /**
        * 分页加载更多数据 - “正在加载数据中” 状态的界面逻辑处理
        */
        @Override
        public void onLoading(ViewHolder holder) {
            holder.<ProgressBar>getView(R.id.footer_progress).setVisibility(View.VISIBLE);
            holder.<TextView>getView(R.id.footer_msg).setText("正在加载数据中");
        }

       /**
        * 分页加载更多数据 - “加载数据失败” 状态的界面逻辑处理
        *
        * @param msg 数据加载失败的原因
        */
        @Override
        public void onLoadingFailure(ViewHolder holder, String msg) {
            holder.<ProgressBar>getView(R.id.footer_progress).setVisibility(View.GONE);
            holder.<TextView>getView(R.id.footer_msg).setText(msg);
        }

       /**
        * 分页加载更多数据 - “没有更多数据” 状态的界面逻辑处理
        */
        @Override
        public void onNoMoreData(ViewHolder holder) {
            holder.<ProgressBar>getView(R.id.footer_progress).setVisibility(View.GONE);
            holder.<TextView>getView(R.id.footer_msg).setText("已经到底啦");
        }
};

```

因为 Footer 需要经常配合列表分页加载数据使用，所以在构造器中除了正常使用情况下的方法`onLoading(ViewHolder holder)`外还提供了三个用于管理分页加载数据状态的方法：

- `onLoading(ViewHolder holder)` 正在加载数据中
- `onLoadingFailure(ViewHolder holder, String msg)` 数据加载失败，`msg`为失败的原因
- `onNoMoreData(ViewHolder holder)` 已经加载完所有数据

最后，将构造器添加到 SuperAdapter 中：

```
mAdapter.addFooter(builder);

```

### 简易Footer构造器SimpleFooterBuilder

考虑到构造 Footer 的过程有些繁琐, SuperAdapter 库中提供了一个简易的内部定义好的 Footer 构造器 `SimpleFooterBuilder`,使用方法很简单：

```
mAdapter.addFooter(new SimpleFooterBuilder("这是个底部","正在加载数据中","加载数据失败","已经到底啦"));

```

`SimpleFooterBuilder`构造方法中的4个参数依次对应着 Footer 的正常模式、正在加载、加载失败、加载完毕这几种状态的提示信息。


## 分页自动加载更多数据

**注意！在设置分页加载数据前一定要先添加Footer**

在使用该功能时，用户可以自己设置分页请求数据的起始页，每当列表滑动到底部的时候就会按之前设置的页数依次累加请求新的数据。


设置分页加载数据调用 SuperAdapter 的`setPaginationData()`方法就可以，但是在这之前需要一个加载数据监听器`LoadDataListener`来管理数据的加载状态，当列表需要加载新的数据时就会调用监听器的`onLoadingData()`方法，该方法有两个参数：

- `int loadPage` 需要加载新数据的页数
- `LoadDataStatus loadDataStatus`  分页加载数据的状态控制器

分页加载数据的状态控制器`LoadDataStatus` 提供了三种状态：

- `onSuccess(List<T> datas)` 数据请求成功调用该方法传入新数据
- `onFailure(String msg)` 数据请求失败调用该方法传入失败信息
- `onNoMoreData()` 数据全部加载完毕调用该方法


**注意！加载数据一定要调用状态控制器`LoadDataStatus`中的方法给列表加载状态做反馈**


具体的实现代码：

```
// 分页加载数据的起始页
private static final int START_PAGE = 0;

...
...

// 实现加载数据监听器
LoadDataListener listener = new LoadDataListener() {
    
        @Override
        public void onLoadingData(final int loadPage, final LoadDataStatus loadDataStatus) {
            
                DataManager.getListPaginationData(loadPage,new Callback(){
                        
                        @Override
                        public void onSuccess(List<String> datas){
                            if(datas.size() == 0){
                                loadDataStatus.onNoMoreData();
                            }else{
                                loadDataStatus.onSuccess(datas);
                            }
                        }

                        @Override
                        public void onFailure(String msg){
                            loadDataStatus.onFailure(msg);
                        }

                        @Override
                        public void onError(){
                            loadDataStatus.onFailure("网络请求出错");
                        }                                
                });
        }
}

// 设置分页加载数据
mAdapter.setPaginationData(START_PAGE, listener);

```

## 封装 SuperAdapter 的注意事项

`SuperAdaper`类中提供了两个有参构造方法： 
- `SuperAdapter(int layoutId )`  
- `SuperAdapter(MultiItemViewBuilder multiItemViewBuilder)` 

他们实现了不同类型的子布局，所以继承`SuperAdapter`时要`Super()`其中一个构造方法，但是你肯定不希望每次实例化适配器都要定义它们，所以建议在你的自定义类中将布局类型以`static`方法定义好：

```
private static int layoutId = R.layout.view_list_item;

```
或者

```
private static MultiItemViewBuilder<TestEntity> multiItemViewBuilder = new MultiItemViewBuilder<TestEntity>() {

        @Override
        public int getLayoutId(int type) {
            if (type == 0){
                return R.layout.view_list_item_1;
            }else {
                return R.layout.view_list_item_2;
            }
        }

        @Override
        public int getItemType(int position, TestEntity data) {
            return data.getType();
        }
    };

```

这样自定义类的构造方法中就无需填写参数：

```
public MineAdapter() {
    super(layoutId);
    // do something
}
```

或者

```
public MineAdapter() {
    super(multiItemViewBuilder);
    // do something
}
```