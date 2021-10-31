package com.android.hepsiburadafinalproject.viewmodel

import androidx.lifecycle.AndroidViewModel
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.hepsiburadafinalproject.data.network.ResultPagingSource
import com.android.hepsiburadafinalproject.data.iTunesAPI
import com.android.hepsiburadafinalproject.data.network.RetrofitInstance
import com.android.hepsiburadafinalproject.model.Result
import com.android.hepsiburadafinalproject.util.Constants.Companion.BASE_URL
import com.android.hepsiburadafinalproject.util.Constants.Companion.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class MainViewModel(application: Application):AndroidViewModel(application) {

    val retrofit:Retrofit=RetrofitInstance.createInstance(BASE_URL)
    val service:iTunesAPI=retrofit.create(iTunesAPI::class.java)


    fun getData(term:String,entity:String): Flow<PagingData<Result>> { //Paging
            return Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE,enablePlaceholders = false),
                pagingSourceFactory = { ResultPagingSource(service,term,entity) })
                .flow.cachedIn(viewModelScope)
    }


     fun hasInternetConnection():Boolean{  //Telefondaki internet bağlantısının kontrolü
        val connectivityManager=getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork=connectivityManager.activeNetwork?:return false
        val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork)?: return false

        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true

            else -> false
        }

    }


}