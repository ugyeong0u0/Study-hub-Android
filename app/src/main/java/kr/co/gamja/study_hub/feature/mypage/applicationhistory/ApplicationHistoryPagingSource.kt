package kr.co.gamja.study_hub.feature.mypage.applicationhistory

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.co.gamja.study_hub.data.model.ContentX
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import retrofit2.HttpException

class ApplicationHistoryPagingSource(private val studyHubApi: StudyHubApi) :
    PagingSource<Int, ContentX>() {
    val tag: String = this.javaClass.simpleName
    override fun getRefreshKey(state: PagingState<Int, ContentX>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentX> {
        return try {
            val currentPageNumber = params.key ?: 0
            val response = studyHubApi.getUserApplyHistory(currentPageNumber, params.loadSize)
            val responseData = response.body()?.requestStudyData?.content ?: emptyList()
            val nextPageNumber =
                if (response.body()?.requestStudyData?.last == true) {
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
}