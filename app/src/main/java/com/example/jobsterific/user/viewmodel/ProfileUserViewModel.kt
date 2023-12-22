
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.jobsterific.data.response.ProfileUserResponse
import com.example.jobsterific.pref.UserModel
import com.example.jobsterific.user.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileUserViewModel(private val repository: UserRepository) : ViewModel() {


    private val _userProfile = MutableLiveData<ProfileUserResponse>()
    val userProfile: LiveData<ProfileUserResponse>
        get() = _userProfile

    fun getSession(): LiveData<UserModel> {
        val sessionFlow = repository.getSession()
        return sessionFlow?.asLiveData() ?: MutableLiveData<UserModel>().apply {
            value = null
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }


    fun getProfile(token: String) {
        try {
            val call: Call<ProfileUserResponse> = ApiConfig.getApiService2(token).getUserProfile()

            call.enqueue(object : Callback<ProfileUserResponse> {
                override fun onResponse(
                    call: Call<ProfileUserResponse>,
                    response: Response<ProfileUserResponse>
                ) {
                    if (response.isSuccessful) {
                        val profileResponse = response.body()
                        _userProfile.value = profileResponse!!
                    } else {
                    }
                }
                override fun onFailure(call: Call<ProfileUserResponse>, t: Throwable) {
                }
            })
        } catch (e: Exception) {
            // Handle exception if needed
        }
    }

}
