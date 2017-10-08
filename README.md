# SuperAdapter
Android RecyclerView多功能集合适配器

## SuperAdapter是什么

`SuperAdapter`是对`RecyclerView.Adapter`进行封装并将许多常用功能集成之中的一个Android库。你不需要为每个列表单独去写`ViewHolder`后再声明控件，当构建不需要重用的列表时你甚至不需要单独创建类去继承`RecyclerView.Adapter`定制适配器，只要在`Activity`或`Fragment`中简单写下几行代码即可实现，有效的减化了复写代码的数量。

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

**注意：上面依赖中的 `x.y.z` 为版本号，目前最新的版本号为 -> `0.1.2`**

## 关于开源
如果你只是想了解如何使用可以跳过本节。

如果你对这个项目感兴趣

## 定义与声明

如果你的列表只出现在了一个布局中并不会重复用使用，那么使用`SuperAdapter`时就不需要您单独创建类继承父类来定制化适配器，但是还是需要遵守一项规则：在`Activity`或`Fragment`中声明`SuperAdapter`属性的同时需要声明您的列表数据源类型：
 ```
List<DataEntity> mData;
SuperAdapter<DataEntity> mAdapter; 
```
**如果你的列表需要重复使用需要对`SuperAdapter`进行一层封装，下面有专门一节讲如何高效的封装`SuperAdapter`**

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
为方便管理，SuperAdapter 中的 ViewHolder 均为一个自定义的通用 ViewHolder ，其中它对外提供了一个 `<T extends View>T getView(int viewId)` 方法来代替 `View findViewById(int ViewId)`获取布局中的控件，目的是简化绑定控件方法以及对每个条目中的子 view 做了简单的缓存,具体使用方法上述代码段中有体现。

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
              
        }

 };
```
最后在方法`convert()`中绑定控件逻辑时需要针对不同的控件类型分别处理。

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

## 分页与上拉加载更多数据
现实使用场景中有上拉加载更多数据的功能就一定有分页功能，所以我们将这两个功能绑定在了一起，自动加载更多数据方法：
```
public void addLoadMoreListeneraddLoadMoreListener(int startPage, LoadMoreListener loadMoreListener);
```
- 参数`int startPage`为数据列表的起始页数，默认起始页数为0页。如设置参数`startPage = 1`，当触发`loadMoreListener`时回调的参数`page` 就是 `2`。
- 参数`loadMoreListener`为加载更多数据监听器，当列表滑动到底部时回调。

为适配器添加自动加载更多数据：
```
mAdapter.addLoadMoreListener(0, new SuperAdapter.LoadMoreListener() {

      @Override
      public void onLoadMore(int page, SuperAdapter.LoadDataStatus status) {

            loadData(page,new Callback(){

                  @Override
                  public void onSuccess(List<DataEntity > data){
                        // 判断是否成功获取到新数据
                        if(data.size() == 0){
                            status.onSuccess(data);
                        }else{
                            status.onNoMoreData();
                        }
                       
                  }

                  @Override
                  public void onFailure(){
                       status.onFailure();
                  }
            });
      }
});
```
在列表滑动到底部时监听器会回调`onLoadMore(int page, SuperAdapter.LoadDataStatus status) `方法来加载更多数据，方法中包含两个参数：
- `int page` 当前要获取新数据的页数。
- ` SuperAdapter.LoadDataStatus status` 设置加载数据的状态：
    1. `status.onSuccess(data)` 获取成功时调用并传入新数据
    2. `status.onFailure()` 获取数据失败时调用
    3. `status.onNoMoreData()`数据加载完毕没有新的数据时调用。

##使用加载更多数据监听器的注意事项：
- 在监听器回调的`onLoadMore()`方法中加载数据时一定要至少给`LoadDataStatus`设置一种状态来回复监听器。
- 在设置了`LoadMoreListener`监听器后不可在外部调用`mAdapter.notifyDataSetChanged();`方法。


## 封装能够重复使用的`SuperAdapter`