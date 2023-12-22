
import com.example.jobsterific.data.response.DeleteResponse
import com.example.jobsterific.data.response.DetailUserResponse
import com.example.jobsterific.data.response.EditCompanyProfileResponse
import com.example.jobsterific.data.response.EditUserResponse
import com.example.jobsterific.data.response.GetAllBatchResponse
import com.example.jobsterific.data.response.GetAllCandidateResponse
import com.example.jobsterific.data.response.GetAllUserResponseItem
import com.example.jobsterific.data.response.GetMyCandidateResponse
import com.example.jobsterific.data.response.LoginCompanyResponse
import com.example.jobsterific.data.response.LoginUserResponse
import com.example.jobsterific.data.response.LogoutResponse
import com.example.jobsterific.data.response.MakeApplymentResponse
import com.example.jobsterific.data.response.MyApplymentResponse
import com.example.jobsterific.data.response.MyBatchResponse
import com.example.jobsterific.data.response.NewCampaignResponse
import com.example.jobsterific.data.response.ProfileCompanyResponse
import com.example.jobsterific.data.response.ProfileUserResponse
import com.example.jobsterific.data.response.RegisterCompanyResponse
import com.example.jobsterific.data.response.RegisterUserResponse
import com.example.jobsterific.data.response.UploadResumeResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("users")
    fun register(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("job") job: String,
        @Field("sex") sex: String,
        @Field("phone") phone: String,

        @Field("address") address: String,
        @Field("isCustomer") isCustomer: Boolean,
        @Field("isAdmin") isAdmin: Boolean
    ): retrofit2.Call<RegisterUserResponse>


    @FormUrlEncoded
    @POST("customers/register")
    fun registerCompany(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("description") description: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("sex") sex: String,
        @Field("adress") adress: String,
        @Field("website") website: String,
    ): retrofit2.Call<RegisterCompanyResponse>



    @FormUrlEncoded
    @POST("users/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String,
    ): retrofit2.Call<LoginUserResponse>

    @FormUrlEncoded
    @POST("customers/login")
    fun loginCompany(
        @Field("email") email: String,
        @Field("password") password: String,
    ): retrofit2.Call<LoginCompanyResponse>


    @FormUrlEncoded
    @PUT("users/current")
    fun editUser(
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("job") job: String,
        @Field("email") email: String,
    ): retrofit2.Call<EditUserResponse>

    @DELETE("users/resume")
    fun deleteResume(): Call<DeleteResponse>
    @DELETE("users/{batchId}")
    fun deleteBatch(@Query("batchId") batchId: String): Call<DeleteResponse>

    @DELETE("users/logout")
    fun logout():retrofit2.Call<LogoutResponse>
    @POST("customers/logout")
    fun logoutCompany():Call<LogoutResponse>
    @GET("users")
    fun getUserProfile(): Call<ProfileUserResponse>
    @GET("/")
    fun getAllUser(): Call<List<GetAllUserResponseItem>>

    @GET("users/batch")
    fun getAllBatch(): Call<GetAllBatchResponse>

    @GET("customers/{userId}")
    fun getCompanyProfile(@Path("userId") userId: String): Call<ProfileCompanyResponse>

    @GET("customers/campaigns/candidate/{userId}")
    fun getDetailUser(@Path("userId") userId: String): Call<DetailUserResponse>

    @FormUrlEncoded
    @PUT("customers/{userId}")
    fun editCompanyProfile(
        @Path("userId") userId: String,
        @Field("firstName") firstName: String,
        @Field("description") description: String,
        @Field("website") website: String,
        @Field("email") email: String,
        @Field("address") address: String
    ): Call<EditCompanyProfileResponse>


    @FormUrlEncoded
    @POST("customers/campaigns")
    fun createCampaign(
        @Field("campaignName") campaignName: String,
        @Field("campaignDesc") campaignDesc: String,
        @Field("campaignPeriod") campaignPeriod: String?,
        @Field("campaignKeyword") campaignKeyword: String,
        @Field("status") status: Boolean,
        @Field("startDate") startDate: String,
        @Field("endDate") endDate: String,
    ): retrofit2.Call<NewCampaignResponse>

    @FormUrlEncoded
    @PUT("customers/campaigns/{userId}")
    fun editCampaign(
        @Path("userId") userId: String,
        @Field("campaignName") campaignName: String,
        @Field("campaignDesc") campaignDesc: String,
        @Field("campaignPeriod") campaignPeriod: String?,
        @Field("campaignKeyword") campaignKeyword: String,
        @Field("status") status: Boolean,
        @Field("startDate") startDate: String,
        @Field("endDate") endDate: String,
    ): retrofit2.Call<NewCampaignResponse>
    @FormUrlEncoded
    @PUT("customers/campaigns/{userId}")
    fun editStatus(
        @Path("userId") userId: String,
        @Field("status") status: Boolean,
    ): retrofit2.Call<NewCampaignResponse>

    @FormUrlEncoded
    @POST("applyment")
    fun makeApplyment(
        @Field("batchId") batchId: String,
    ): retrofit2.Call<MakeApplymentResponse>
    @FormUrlEncoded
    @POST("applyment/batch")
    fun getMycandidate( @Field("batchId") batchId: String,): Call<GetMyCandidateResponse>



    @GET("applyment/user")
    fun getMyApplyment(): Call<MyApplymentResponse>
    @GET("customers/allCampaign")
    fun getMyBatch(): Call<MyBatchResponse>


    @GET("customers/candidates")
    fun getAllCandidateCompany(): Call<GetAllCandidateResponse>

    @Headers("Content-Type: application/json")
    @Multipart
    @POST("users/resume")
    suspend fun uploadResume(
        @Part resume: MultipartBody.Part
    ): UploadResumeResponse


    @GET("users/batch/search")
    fun getFindBatch(
        @Query("search") search: String
    ): Call<GetAllBatchResponse>
    @FormUrlEncoded
    @POST("users/batch/search")
    fun uploadImage(
        @Field("resume") resume: String,
    ): retrofit2.Call<UploadResumeResponse>



}