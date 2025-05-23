package com.example.floclone.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//각 기능을 구현해 클래스화
class AuthService(private val view: AuthView) {
    //NetworkManager에 정의한 인스턴스 + ApiService에 정의한 서비스 연결
    private val api = NetworkManager.retrofit.create(ApiService::class.java)

    //회원가입 기능
    fun singUp(name: String, email: String, pw: String){
        //결국에는 NetworkManger -> Retrofit 인스턴스 + ApiService 연결 후 호출
        //꼭 enqueue로 비동기로 넣자
        api.signUP(SignUpRequest(name, email, pw))
            .enqueue(object : Callback<SignUpResponse>{
                override fun onResponse(
                    call: Call<SignUpResponse?>,
                    response: Response<SignUpResponse?>
                ) {

                    /**  이를 response가 받아서 이를 해체해서 확인하기
                     * data class Response<T> (
                     *     val isSuccesss: Boolean,
                     *     val code: String,
                     *     val message: String,
                     *     val result: T?
                     * )
                     * **/

                    //response가 성공적으로 받아졌어
                    if(response.isSuccessful){
                        //받은 내용을 뜯어서
                        val body = response.body()
                        //유효하면
                        if (body != null && body.isSuccess && body.result != null) {
                            // 성공: SignUpResult 객체 전달 - 콜백함수로
                            view.onSignUpSuccess(body.result)
                        } else {
                            // API 레벨 실패(400 등) 또는 isSuccesss == false
                            val msg = body?.message ?: "회원가입 실패: HTTP ${response.code()}"
                            view.onSignUpFailure(msg)
                        }
                    } else {
                        // HTTP 에러
                        view.onSignUpFailure("회원가입 실패: HTTP ${response.code()}")
                    }

                }

                override fun onFailure(call: Call<SignUpResponse?>, t: Throwable) {
                    view.onSignUpFailure("네트워크 오류: ${t.message}")
                }
            })
    }

    fun login(email: String, pw: String){
        api.login(LoginRequest(email, pw))
            .enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    //제대로 받았어
                    if(response.isSuccessful){
                        val body = response.body()
                        if (body != null && body.isSuccess && body.result != null) {
                            // 성공: LoginResult 객체 전달
                            view.onLoginSuccess(body.result)
                        }
                        else{
                            // API 레벨 실패(401 등) 또는 isSuccesss == false
                            val msg = body?.message ?: "로그인 실패: HTTP ${response.code()}"
                            view.onLoginFailure(msg)
                        }
                    }
                    //문제가 생겼어
                    else{
                        val msg = when (response.code()) {
                            401 -> "인증 실패: 계정 정보를 확인해주세요."
                            else -> "로그인 실패: HTTP ${response.code()}"
                        }
                        view.onLoginFailure(msg)
                    }
                }


                override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                    view.onLoginFailure("네트워크 오류: ${t.message}")
                }
            })
    }

}