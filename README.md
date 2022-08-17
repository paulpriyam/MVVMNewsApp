# MVVMNewsApp

Pagination added, using paging 3 library

Depenndecy added in App Gradle

implementation 'androidx.paging:paging-runtime:3.1.1'

Paging Library has 3 parts


1.PagingSource -->   This is the data source from where we get the paginated data. Here we create a class which inherits pagingSource class<KeyType,DataSourceType>

2.Pager -->  Here we specify the configuration of our pager like number of items per page to be loaded(pageSize) and items that we can cached in (maxSize)

3.PagingDataAdapter --->  And we would be using PagingDataAdapter instead of normal RecyclerView.Adapter. And we would be passing diffUtil in the constructor.


Inorder to showing loading/progress bar when we fetch the next page we would be using LoadingState

1.  Need to create a LoadingAdapter which inherits from LoadingStateAdapter

2.	Then to our PagingDataApapter we need to call the following method withLoadStateHeaderAndFooter to add the LoaderAdapter.

