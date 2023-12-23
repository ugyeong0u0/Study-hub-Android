package kr.co.gamja.study_hub.feature.mypage.myStudy

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.co.gamja.study_hub.data.model.ContentXXX
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import retrofit2.HttpException

class MyStudyPagingSource(private val studyHybApi: StudyHubApi) : PagingSource<Int, ContentXXX>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentXXX> {
        return try {
            // 페이지부터 시작
            val currentPageNumber = params.key ?: 0
            val response = studyHybApi.getMyStudy(currentPageNumber, params.loadSize)
            val responseData = response.body()?.posts?.content ?: emptyList()
            val nextPageNumber =
                if (response.body()?.posts?.last == true) {
                    null
                } else {
                    currentPageNumber + 1
                }
            // 성공시
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

    // 새로 고침할 시 페이징시스템이 어떤 "페이지"에서 시작해야 할지 결정
    override fun getRefreshKey(state: PagingState<Int, ContentXXX>): Int? {
        // prevKey == null -> anchorPage 첫 페이지
        // nextKey == null -> anchorPage 마지막 페이지.
        // prevKey && nextKey == null -> anchorPage 한 페이지 return null.
        return state.anchorPosition?.let { anchorPosition ->
            // pagingState(로딩된거)에서 anchorPostion(현재 사용자가 보고있는 항목)에 가장 가까운 페이지 반환
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}