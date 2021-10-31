package com.android.hepsiburadafinalproject.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.hepsiburadafinalproject.R
import com.android.hepsiburadafinalproject.model.Result
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

class BindingAdapters {

    companion object{

        @BindingAdapter("primaryText")
        @JvmStatic
        fun primaryText(view: TextView,result: Result){//Birinci text in gelen veride trackName null değilse trackName olarak set edilmesi yoksa collectionName olarak set edilmesi.
            if(!result.trackName.isNullOrEmpty()){
                view.text=result.trackName
            }else if(!result.collectionName.isNullOrEmpty()){
                view.text=result.collectionName
            }
        }


        @BindingAdapter("collectionPrice")
        @JvmStatic
        fun setCollectionPrice(view:TextView,result: Result){ //Gelen verilerde para birimine göre ui ın düzenlenmesi
            var label=""
            var text=""
            when(result.currency){
                "USD"->label=" $"
                "EUR"->label=" €"
                "GBP"->label=" £"
                "TRY"->label=" ₺"
                else->label=" ?"
            }

            if(result.collectionPrice.toString().isNullOrEmpty()){
                if(result.price<= 0){
                    text="Free"
                }
                else{
                    text=result.price.toString()+label
                }
            }
            else{
                if(result.collectionPrice<= 0){
                    text="Free"
                }
                else{
                    text=result.collectionPrice.toString()+label
                }
            }

            view.text="Price: "+text

        }

        @BindingAdapter("releaseDate")
        @JvmStatic
        fun setReleaseDate(view:TextView,date:String){
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            val formatter= SimpleDateFormat("dd/MM/yyyy")
            val text=formatter.format(parser.parse(date))
            view.text="Release Date: "+text
        }

        @BindingAdapter("imgUrl")
        @JvmStatic
        fun setImg(view:ImageView,url:String){

            Glide.with(view.context)
                .setDefaultRequestOptions(glideOptions(view))
                .load(url)
                .into(view)

        }

        private fun glideOptions(view: View):RequestOptions{
            val reqOptions= RequestOptions().apply {
                timeout(5000)
                error(R.drawable.ic_error)
                fitCenter()
                placeholder(circularProgressOptions(view))
            }
            return reqOptions
        }

        private fun circularProgressOptions(view: View):CircularProgressDrawable{
            val circularProgress=CircularProgressDrawable(view.context)
            circularProgress.apply {
                centerRadius=30f
                strokeWidth=5f
                start()
            }
            return circularProgress
        }




    }


}
