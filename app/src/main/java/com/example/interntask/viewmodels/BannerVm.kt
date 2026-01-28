package com.example.interntask.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interntask.Data.Apiservice
import com.example.interntask.Uistate.Uistate
import com.example.interntask.model.Bannermodel.Photo
import com.example.interntask.model.Bannermodel.PhotoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class BannerVm @jakarta.inject.Inject constructor(val api: Apiservice) : ViewModel() {

    private val _bannerPhoto=MutableStateFlow<Uistate<List<Photo>>>(Uistate.Loading())

    val bannerPhoto: StateFlow<Uistate<List<Photo>>> = _bannerPhoto.asStateFlow()

    private val _toast = MutableSharedFlow<String>(  extraBufferCapacity = 1)
    val toast  =_toast.asSharedFlow()


    fun getPhoto(){

                viewModelScope.launch{

                    _bannerPhoto.value= Uistate.Loading()
                    try {
                        val response= withContext(Dispatchers.IO){
                            api.getPhotobanner()}

                        if(response.isSuccessful&&response.body()!=null){
                            _toast.emit("Response got successfully with list :"+response.body()!!.photos.size)
                            val photo=response.body()!!.photos
                            photo.chunked(2).forEach { chunk ->
                                _bannerPhoto.value= Uistate.Success(chunk)
                                delay(800)
                            }
                        }
                        else{
                            _toast.emit("Data not available")
                            _bannerPhoto.value= Uistate.Error("Data not available")
                        }
                    }catch (e: Exception){
                        _toast.emit(e.message.toString())
                        _bannerPhoto.value= Uistate.Error(e.message.toString())
                    }




        }
    }
}