package com.example.interntask.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interntask.Data.MainhomeApi
import com.example.interntask.Data.Repository.MainhomeRepo
import com.example.interntask.Uistate.Uistate
import com.example.interntask.model.MainhomeModel.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BestdealsVm @Inject constructor(val repo: MainhomeRepo): ViewModel() {


    val allitemArraylist=mutableListOf<Product>()
    private var isLoading = false
    private var currentIndex = 0
    private val pageSize = 20

    private val _batchFlow = MutableSharedFlow<List<Product>>(replay = 0)
    val batchFlow = _batchFlow.asSharedFlow()


    private val _detailItem = MutableStateFlow<Uistate<Product>>(Uistate.Loading())
    val detailItem = _detailItem.asStateFlow()

     val itemstack=mutableListOf<Product>()

    private val _detailsuggetionA = MutableStateFlow<Uistate<List<Product>>>(Uistate.Loading())
    val detailsuggetionA = _detailsuggetionA.asStateFlow()

    private val _detailsuggetionB = MutableStateFlow<Uistate<List<Product>>>(Uistate.Loading())
    val detailsuggetionB = _detailsuggetionB.asStateFlow()

    private val _detailAllGridshared = MutableSharedFlow<List<Product>>()
    val detailAllGridshared = _detailAllGridshared.asSharedFlow()



    private val _bestDeals = MutableStateFlow<Uistate<List<Product>>>(Uistate.Loading())
    val bestDeals: StateFlow<Uistate<List<Product>>> = _bestDeals.asStateFlow()


    private val _specialProduct = MutableStateFlow<Uistate<List<Product>>>(Uistate.Loading())
    val specialProduct: StateFlow<Uistate<List<Product>>> = _specialProduct.asStateFlow()

    private val _allGriditem = MutableStateFlow<Uistate<List<Product>>>(Uistate.Loading())
    val allGriditem: StateFlow<Uistate<List<Product>>> = _allGriditem.asStateFlow()



    private val _toastbestdeal = MutableSharedFlow<String>(  extraBufferCapacity = 1)
    val toastbestdeal  =_toastbestdeal.asSharedFlow()

    init {
        getBestdeal()
    }

    fun getBestdeal(){

        viewModelScope.launch {
            _bestDeals.emit(Uistate.Loading())
            try {
                val data=repo.getProduct(30,0)
                if(data.isNotEmpty()){
                    _toastbestdeal.emit("got ${data.size}")
                _bestDeals.emit(Uistate.Success(data))
                }
                else{
                    _toastbestdeal.emit("empty data")
                    _bestDeals.emit(Uistate.Error("data empty"))
                }
            }
            catch (e: Exception){
                _toastbestdeal.emit(e.message.toString())
                _bestDeals.emit(Uistate.Error(e.message.toString()))
            }

        }

        viewModelScope.launch {
            _specialProduct.emit(Uistate.Loading())
            try {
                val special=repo.getSpecial()
                if(special.isNotEmpty()){
                    _specialProduct.emit(Uistate.Success(special))
                }
                else{
                    _toastbestdeal.emit("special data not gated")
                    _specialProduct.emit(Uistate.Error("empty data"))
                }


            }catch (e: Exception){
                _toastbestdeal.emit(e.message.toString())
                _specialProduct.emit(Uistate.Error(e.message.toString()))
            }

        }
        viewModelScope.launch {
            if(isLoading)return@launch
            isLoading=true
            _allGriditem.emit(Uistate.Loading())
            try {
                val data=repo.getAllgrid()
                if(data.isNotEmpty()){
                    // allitemArraylist.addAll(data)
                    _batchFlow.emit(data)
                   // currentIndex += pageSize
                    _toastbestdeal.emit("got grid item ${data.size}")
                    _allGriditem.emit(Uistate.Success(data))
                }
                else{
                    _toastbestdeal.emit("empty data")
                    _allGriditem.emit(Uistate.Error("data empty"))
                }
            }
            catch (e: Exception){
                _toastbestdeal.emit(e.message.toString())
                _allGriditem.emit(Uistate.Error(e.message.toString()))
            }
            finally {
                isLoading=false
            }

        }


    }

    fun emitnextItem(){
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            try {
                val data=repo.getAllgrid()
                if(data.isNotEmpty()){
                    _batchFlow.emit(data)
                    _toastbestdeal.emit("got batchflow item ${data.size}")

                }
            }
                catch (e: Exception) {
                    _toastbestdeal.emit(e.message ?: "error")
                } finally {
                    isLoading = false
                }
        }
    }

    fun getbyId(id: Int){
        viewModelScope.launch {
            _toastbestdeal.emit("item getting start")
            _detailItem.emit(Uistate.Loading())
            try {
                val all =repo.getAllgrid()
                if(all!=null){
                    _allGriditem.emit(Uistate.Success(all))
                }
                val item=repo.getproductbyId(id)
                if(item!=null){
                     itemstack.add(item)
                    _toastbestdeal.emit("item gated")
                    _detailItem.emit(Uistate.Success(item))
                     suggetionAemit(item.category)

                }
            }catch (e: Exception){
                _toastbestdeal.emit("item problem :"+e.message.toString())
                _detailItem.emit(Uistate.Error(e.message.toString()))
            }

        }
    }

    fun suggetionAemit(cetegory:String){
        viewModelScope.launch {
            _detailsuggetionA.emit(Uistate.Loading())
            _detailsuggetionB.emit(Uistate.Loading())

            try {
                val result=repo.getdetailsuggetion(cetegory)
                val result2=repo.getProduct(30,30)
                if(result2!=null){
                    _detailsuggetionB.emit(Uistate.Success(result2))
                }else{
                    _detailsuggetionB.emit(Uistate.Success(emptyList()))
                }


                if(result!=null){
                    _detailsuggetionA.emit(Uistate.Success(result))
                }
                else{
                    _detailsuggetionA.emit(Uistate.Success(repo.getProduct(30,60)))
                }

            }catch (e: Exception)
            {
                _detailsuggetionA.emit(Uistate.Error(e.message.toString()))
                _detailsuggetionB.emit(Uistate.Error(e.message.toString()))

            }
        }
    }


    fun detailAllitememitshared(){

        viewModelScope.launch {
            val result=repo.getAllgrid()
            if(result.isNotEmpty()){
                _detailAllGridshared.emit(result)
            }
            else{
                _detailAllGridshared.emit(emptyList())
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun popitemstack() {
        itemstack.removeLast()

        if (itemstack.size >= 1) {
            viewModelScope.launch {
                _detailItem.emit(Uistate.Success(itemstack.last()))
            }
        }
    }



//    fun loadNext20() {
//        if (isLoading) return
//        if (currentIndex >= allitemArraylist.size) return
//
//        isLoading = true
//
//        val nextBatch = allitemArraylist.subList(
//            currentIndex,
//            minOf(currentIndex + pageSize, allitemArraylist.size)
//        ).toList()
//
//        currentIndex += pageSize
//
//        viewModelScope.launch {
//            try {
//                _batchFlow.emit(nextBatch)
//                _toastbestdeal.emit("$currentIndex - $currentIndex+$pageSize")
//            } finally {
//                isLoading = false
//            }
//        }
//   }

}