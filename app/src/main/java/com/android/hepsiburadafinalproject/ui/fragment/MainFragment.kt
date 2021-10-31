package com.android.hepsiburadafinalproject.ui.fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.android.hepsiburadafinalproject.R
import com.android.hepsiburadafinalproject.databinding.FragmentMainBinding
import com.android.hepsiburadafinalproject.ui.adapter.MainFragmentAdapter
import com.android.hepsiburadafinalproject.util.Constants
import com.android.hepsiburadafinalproject.util.Constants.Companion.DEFAULT_SEARCH_TERM
import com.android.hepsiburadafinalproject.util.Constants.Companion.QUERY_APPS
import com.android.hepsiburadafinalproject.util.Constants.Companion.QUERY_BOOKS
import com.android.hepsiburadafinalproject.util.Constants.Companion.QUERY_MOVIES
import com.android.hepsiburadafinalproject.util.Constants.Companion.QUERY_MUSIC
import com.android.hepsiburadafinalproject.util.Constants.Companion.SHARED_PREF_CATEGORY_KEY
import com.android.hepsiburadafinalproject.util.Constants.Companion.SHARED_PREF_SEARCHTERM_KEY
import com.android.hepsiburadafinalproject.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainFragment : Fragment() {


    private lateinit var binding: FragmentMainBinding
    private var fragmentAdapter: MainFragmentAdapter? = null
    private val viewModel:MainViewModel by viewModels()
    var preferences:SharedPreferences?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        binding.mainFragment=this
        initRecylerView()
        getPreferences()
        return binding.root
    }

    fun getPreferences(){  //Shared preferences te tutulan (varsa) arama kelimesi ve seçili kategori butonu türünün alınması
        preferences=activity?.getSharedPreferences("com.android.hepsiburadafinalproject",Context.MODE_PRIVATE)
        binding.apply {
            textFieldInput.setText(preferences?.getString(SHARED_PREF_SEARCHTERM_KEY,""))
            toggleButton.check(getSelectedButtonId(preferences?.getString(SHARED_PREF_CATEGORY_KEY,"movie")!!))
        }

    }

    override fun onDestroyView() { //Detay sayfasına giderken arama kelimesinin ve seçili butonun kaydedilmesi
        super.onDestroyView()
        preferences?.edit()?.putString(SHARED_PREF_CATEGORY_KEY,getSelectedType())?.apply()
        preferences?.edit()?.putString(SHARED_PREF_SEARCHTERM_KEY,binding.textFieldInput.text.toString())?.apply()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchListener()
        showShimmer()
    }

    private fun search(term:String,entity:String) {//Internet bağlantısını kontrol ederek verilen arama türü ve kategorisine göre veriyi isteyen fonksiyon

        if(viewModel.hasInternetConnection()){
            //binding.shimmerRecyclerView.showShimmer()
            lifecycleScope.launchWhenCreated {
                viewModel.getData(term, entity).collectLatest {
                    fragmentAdapter?.submitData(it)
                }
            }
        }
        else{
          Toast.makeText(context,getString(R.string.connection_toast),Toast.LENGTH_LONG).show()
        }
    }

    private fun initRecylerView(){
        fragmentAdapter=MainFragmentAdapter()
        binding.shimmerRecyclerView.adapter=fragmentAdapter

    }


    fun searchListener(){ //Text field ı dinleyerek karakter sayısına göre default olarak "a" ile arama yapar karakter sayısı 2 den fazla ise girilen kelimeyi arar

        binding.textFieldInput.apply {
            if(text.isNullOrEmpty()){
                search(DEFAULT_SEARCH_TERM, getSelectedType())
            }else{
                search(text.toString(), getSelectedType())

            }
        }

        binding.textFieldInput.doOnTextChanged { text, start, before, count ->
            if(text?.length!!>2) {
                search(binding.textFieldInput.text.toString(), getSelectedType())
            }
            else{
                search(DEFAULT_SEARCH_TERM, getSelectedType())
            }
        }
    }

    fun getSelectedType():String{ //seçilen buton türüne göre query kelimesi döndürür
        when(binding.toggleButton.checkedButtonId){
            binding.MoviesButton.id->{
                return QUERY_MOVIES
            }
            binding.AppsButton.id->{
                return QUERY_APPS
            }
            binding.BooksButton.id->{
                return QUERY_BOOKS
            }
            binding.MusicsButton.id->{
                return QUERY_MUSIC
            }
            else-> return QUERY_MOVIES
        }
    }


    fun getSelectedButtonId(type:String):Int{//verilen tür stringine göre hangi butonun id si seçilmesi gerektiğini döndürür.
        when(type){
            QUERY_MOVIES->{
                return binding.MoviesButton.id
            }
            QUERY_APPS->{
                return binding.AppsButton.id
            }
            QUERY_BOOKS->{
                return binding.BooksButton.id
            }
            QUERY_MUSIC->{
                return binding.MusicsButton.id
            }
            else-> return binding.MoviesButton.id
        }
    }



     fun booksOnClick(){ //eğer text field boşsa seçilen kategoride default aramayı yapar, boş değilse text field daki kelimeyi arar, diğer butonlarda aynı şekilde
         binding.textFieldInput.apply {
             if(text.isNullOrEmpty()){
                 search(DEFAULT_SEARCH_TERM,QUERY_BOOKS)
             }
             else{
                 search(binding.textFieldInput.text.toString(),QUERY_BOOKS)
             }
         }
    }

     fun musicOnClick(){
         binding.textFieldInput.apply {
             if(text.isNullOrEmpty()){
                 search(DEFAULT_SEARCH_TERM,QUERY_MUSIC)
             }
             else{
                 search(binding.textFieldInput.text.toString(),QUERY_MUSIC)
             }
         }
    }

     fun appsOnClick() {
         binding.textFieldInput.apply {
             if (text.isNullOrEmpty()) {
                 search(DEFAULT_SEARCH_TERM, QUERY_APPS)
             } else {
                 search(binding.textFieldInput.text.toString(), QUERY_APPS)
             }
         }

     }
     fun moviesOnClick() {
             binding.textFieldInput.apply {
                 if (text.isNullOrEmpty()) {
                     search(DEFAULT_SEARCH_TERM, QUERY_MOVIES)
                 } else {
                     search(binding.textFieldInput.text.toString(), QUERY_MOVIES)
                 }
             }
     }

    fun showShimmer(){//loadstate e göre shimmer effect in açılıp kapanması
        lifecycleScope.launch {
            fragmentAdapter?.loadStateFlow?.collectLatest { loadStates ->
                if(loadStates.refresh is LoadState.Loading){
                    binding.shimmerRecyclerView.showShimmer()
                }
                else{
                    binding.shimmerRecyclerView.hideShimmer()
                }

            }
        }

    }

}