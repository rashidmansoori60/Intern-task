package com.example.interntask.Resources

sealed class ValidateState() {
    object Success : ValidateState()
    data class Error(val message: String) : ValidateState()

}

          data class Validationfaild(
            val email: ValidateState,
            val pass: ValidateState)



data class RagistationFaild(val email: ValidateState,
                            val pass: ValidateState,
                            val correctpass: ValidateState)

sealed class Resourcesstate<T>(
    val data:T?=null,
    val message: String?=null
){

    class Success<T>(data:T?):Resourcesstate<T>(data=data)
    class Error<T>(message:String?):Resourcesstate<T>(null,message)
    class Loading<T>: Resourcesstate<T>()
    class Unspecyfied<T>: Resourcesstate<T>()

}