package kr.co.gamja.study_hub.feature.toolbar.bookmark

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.co.gamja.study_hub.data.model.ContentXX
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import retrofit2.HttpException

class BookmarkPagingSource(private val studyHubApi: StudyHubApi) : PagingSource<Int, ContentXX>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentXX> {
        return try {
            val currentPageNumber = params.key ?: 0
            val response = studyHubApi.getBookmark(currentPageNumber, params.loadSize)
            val responseData = response.body()?.getBookmarkedPostsData?.content ?: emptyList()
            val nextPageNumber =
                if (response.body()?.getBookmarkedPostsData?.last == true) {
                    null
                } else {
                    currentPageNumber + 1
                }
            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPageNumber > 0) currentPageNumber - 1 else null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e) // 오류 paging에 전달
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ContentXX>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}