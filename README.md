# SuperAdapter
Android RecyclerView多功能集合适配器

## SuperAdapter是什么

`SuperAdapter`是对`RecyclerView.Adapter`进行封装并将许多常用功能集成之中的一个Android库。你不需要为每个列表单独去写`ViewHolder`后再声明控件，当构建不需要重用的列表时你甚至不需要单独创建类去继承`RecyclerView.Adapter`定制适配器，只要在`Activity`或`Fragment`中简单写下几行代码即可实现，有效的减化了复写代码的数量。

## SuperAdapter有哪些功能

  - 快速绑定单一布局
  - 简单绑定多种布局
  - 自定义列表Header
  - 列表点击事件
  - 分页显示数据
  - 上拉加载更多数据

还有:
当然您也可以自定义列表Footer。但是当列表装载分页处理模块的时候库中会自动加载一个Footer，如果再添加自定义Footer无论是在用户体验上还是在`com.jessewu.superadapter.SuperAdapter`内部的逻辑上都不太乐观，所以`com.jessewu.superadapter.SuperAdapter.addFooterView`方法目前是`private`状态。

## 如何使用SuperAdapter

#### 定义与声明
使用`com.jessewu.superadapter.SuperAdapter`您不需要单独创建类来定制化适配器，但是还是需要遵守一项规则：在`Activity`或`Fragment`中声明`com.jessewu.superadapter.SuperAdapter`的同时需要声明您的数据源类型：
 ```
List<DataEntity> mData;
com.jessewu.superadapter.SuperAdapter<DataEntity> mSuperAdapter; 
```
##### 绑定单一布局的列表
绑定单一布局列表的方法非常简单，只需要在`com.jessewu.superadapter.SuperAdapter`的构造函数中添加布局文件的`layoutId`与数据源即可。
```
mSuperAdapter = new com.jessewu.superadapter.SuperAdapter<DataEntity>(R.layout.view_list_item,mData) {
     @Override
     public void convert(com.jessewu.superadapter.ViewHolder holder, DataEntity entity) {
                
     }
};
```
方法`convert(com.jessewu.superadapter.ViewHolder holder, DataEntity entity)`中的两个参数是列表中同一`position`对应的`viewHolder`和数据源，您可以通过`holder.getView(int id)`方法来绑定控件并填充数据。
```
holder.<TextView>.getView(R.id.tv_name).setText(entity.getName());
Button button = holder.getView(R.id.button);
button.setEnabled(entity.getEnabled());
```

##### 绑定多种布局的列表
绑定多种类型布局文件需要`com.jessewu.superadapter.SuperAdapter.MultiItemViewTypeHelper`接口来辅助适配器确定每个位置调用对应的布局文件。同理，声明属性同时需要声明数据源类型：
```
com.jessewu.superadapter.SuperAdapter.MultiItemViewTypeHelper<DataEntity> multiViewType;
```
`com.jessewu.superadapter.SuperAdapter.MultiItemViewTypeHelper`需要您实现两个方法：
- 在方法`getItemViewType(int position, DataEntity entity)`中通过参数`position`和数据源`entity`来判断返回ItemView类型，ItemView的类型为`int`且具体的值需要您自己定义。
- 在方法`getItemViewLayoutId(int itemType)`中判断`itemType`来返回对应的布局文件id。
```
multiViewType = new com.jessewu.superadapter.SuperAdapter.MultiItemViewTypeHelper<DataEntity>() {
            @Override
            public int getItemViewLayoutId(int itemType) {
                if (itemType == TestImpl.TYPE_NORMAL){
                    return R.layout.view_item_normal;
                }
                return R.layout.view_item_other;
            }

            @Override
            public int getItemViewType(int position, DataEntity entity) {
                return entity.getType();
            }
 };
```
最后，将`MultiItemViewTypeHelper`直接添加到`com.jessewu.superadapter.SuperAdapter`构造函数中即可：
```
mAdapter = new com.jessewu.superadapter.SuperAdapter<DataEntity>(multiViewType,mData) {
            @Override
            public void convert(com.jessewu.superadapter.ViewHolder holder, DataEntity entity) {
              
            }
 };
```

##### 添加自定义Header
自定义头部控件本质是一个特殊的`ItemView`，它永远存在列表最上方的位置且跟随列表滑动。

添加自定义头部控件需要实现一个辅助接口来管理自定义头部布局：
```
com.jessewu.superadapter.SuperAdapter.SpecialViewHelper mHeaderHelper = new com.jessewu.superadapter.SuperAdapter.SpecialViewHelper() {
            @Override
            public int getLayoutId() {
                return R.layout.view_header;
            }

            @Override
            public void convert(com.jessewu.superadapter.ViewHolder view) {
                ImageView background = view.getView(R.id.header_img);
                TextView name = view.getView(R.id.header_name);
            }

            @Override
            public void onClick(View view) {

            }
};
```
辅助接口中有三个回调方法：

- `int getLayoutId()` 设置自定义头部的布局id；
- `void convert(com.jessewu.superadapter.ViewHolder view) `  绑定布局中的控件及添加逻辑；
- ` void onClick(View view)` 头部布局的点击事件。

最后，将实现好的辅助接口设置到适配器中即可：
```
mAdapter.addHeaderView(mHeaderHelper);

```

##### 绑定列表点击事件
绑定点击事件：
```
mAdapter.setOnItemClickListener(new com.jessewu.superadapter.SuperAdapter.OnItemClickListener<DataEntity>() {
            @Override
            public void onItemClick(int position, DataEntity entity) {

            }
});

```
绑定长按事件：
```
mAdapter.setOnItemLongClickListener(new com.jessewu.superadapter.SuperAdapter.OnItemLongClickListener<DataEntity>() {
            @Override
            public void onItemLongClick(int position, DataEntity entity) {

            }
});

```
##### 分页与上拉加载更多数据
现实使用场景中有上拉加载更多数据的功能就一定有分页功能，所以我们将这两个功能绑定在了一起，自动加载更多数据方法：
```
public void addLoadMoreListeneraddLoadMoreListener(int startPage, LoadMoreListener loadMoreListener);
```
- 参数`int startPage`为数据列表的起始页数，默认起始页数为0页。如设置参数`startPage = 1`，当触发`loadMoreListener`时回调的参数`page` 就是 `2`。
- 参数`loadMoreListener`为加载更多数据监听器，当列表滑动到底部时回调。

为适配器添加自动加载更多数据：
```
mAdapter.addLoadMoreListener(0, new com.jessewu.superadapter.SuperAdapter.LoadMoreListener() {

      @Override
      public void onLoadMore(int page, com.jessewu.superadapter.SuperAdapter.LoadDataStatus status) {

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
在列表滑动到底部时监听器会回调`onLoadMore(int page, com.jessewu.superadapter.SuperAdapter.LoadDataStatus status) `方法来加载更多数据，方法中包含两个参数：
- `int page` 当前要获取新数据的页数。
- ` com.jessewu.superadapter.SuperAdapter.LoadDataStatus status` 设置加载数据的状态：
    1. `status.onSuccess(data)` 获取成功时调用并传入新数据
    2. `status.onFailure()` 获取数据失败时调用
    3. `status.onNoMoreData()`数据加载完毕没有新的数据时调用。

######使用加载更多数据监听器的注意事项：
- 在监听器回调的`onLoadMore()`方法中加载数据时一定要至少给`LoadDataStatus`设置一种状态来回复监听器。
- 在设置了`LoadMoreListener`监听器后不可在外部调用`mAdapter.notifyDataSetChanged();`方法。
