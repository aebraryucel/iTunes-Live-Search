package com.android.hepsiburadafinalproject.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.hepsiburadafinalproject.data.iTunesAPI
import com.android.hepsiburadafinalproject.model.Result
import com.android.hepsiburadafinalproject.util.Constants.Companion.INITIAL_LOAD_SIZE
import com.android.hepsiburadafinalproject.util.Constants.Companion.NETWORK_PAGE_SIZE

class ResultPagingSource(val service:iTunesAPI,val term:String,val entity:String) :PagingSource<Int, Result>(){

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {


        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val position = params.key ?: INITIAL_LOAD_SIZE
        val offset = if (params.key != null) ((position - 1) * NETWORK_PAGE_SIZE) + 1 else INITIAL_LOAD_SIZE
        return try {
            val response = service.searchRequest(term = term,entity = entity,offset = offset-1, limit = params.loadSize).body()!!.results
            Log.d("LOAD SIZE",params.loadSize.toString())
            Log.d("KEY",params.key.toString())
            val nextKey = if (response.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}