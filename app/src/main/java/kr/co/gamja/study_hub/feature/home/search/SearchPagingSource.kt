package kr.co.gamja.study_hub.feature.home.search

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.co.gamja.study_hub.data.model.ContentXXXX
import kr.co.gamja.study_hub.data.repository.RetrofitManager

class SearchPagingSource (
    private val isHot : Boolean,
    private val searchContent : String,
    private val isDepartment: Boolean
) : PagingSource<Int, ContentXXXX>() {

    override fun getRefreshKey(state: PagingState<Int, ContentXXXX>): Int? {
        return state.anchorPosition?.let{
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentXXXX> {
        val page = params.key ?: 1
        try {
            val response = RetrofitManager.api.getStudyPostAll(
                hot = isHot,
                page = page,
                size = 10,
                inquiryText = searchContent,
                titleAndMajor = !isDepartment
            )

            if (response.isSuccessful){
                val data = response.body()?.postDataByInquiries?.content ?: emptyList()

                val prevKey = if(page == 1) null else page - 1
                val nextKey = if(data.isEmpty()) null else page + 1

                return LoadResult.Page(
                    data = data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                val exception = IllegalArgumentException("Response is Failed")
                return LoadResult.Error(exception)
                //Error
            }
        } catch (e: Exception){
            return LoadResult.Error(e)
        }
    }
}