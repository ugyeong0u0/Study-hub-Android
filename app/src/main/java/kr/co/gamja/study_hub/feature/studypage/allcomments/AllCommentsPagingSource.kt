package kr.co.gamja.study_hub.feature.studypage.allcomments

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.co.gamja.study_hub.data.model.Content
import kr.co.gamja.study_hub.data.repository.StudyHubApi
import retrofit2.HttpException

class AllCommentsPagingSource(private val studyHubApi: StudyHubApi, private val postId:Int):PagingSource<Int, Content>() {
    override fun getRefreshKey(state: PagingState<Int, Content>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Content> {
        return try {
            val currentPageNumber = params.key ?: 0
            val response = studyHubApi.getComments(postId, currentPageNumber, params.loadSize)
            val responseData = response.body()?.content ?: emptyList()
            val nextPageNumber =
                if (response.body()?.last == true) {
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