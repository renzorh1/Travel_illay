package ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelillay.models.UserBasicInfo

class PerfilViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserBasicInfo>()
    val userData: LiveData<UserBasicInfo> get() = _userData

    fun setUserData(user: UserBasicInfo) {
        _userData.value = user
    }
}
