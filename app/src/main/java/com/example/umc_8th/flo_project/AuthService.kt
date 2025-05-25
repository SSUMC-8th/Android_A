package com.example.umc_8th.flo_project

import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.umc_8th.flo_project.NetworkModule.getClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView
    private val api by lazy {
        NetworkModule.getClient().create(AuthRetrofitInterface::class.java)
    }

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView=signUpView
    }
    fun setLoginView(loginView: LoginView){
        this.loginView=loginView
    }
    fun signUp(user: User) {
        api.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                // 네트워크 실패 etc.
                Log.e("AuthService", "signUp ▶ onFailure: 통신 실패, 메시지=${t.message}", t)
                signUpView.onSignUpFailure()
            }
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("AuthService", "signUp ▶ URL=${call.request().url}")
                Log.d("AuthService", "signUp ▶ HTTP=${response.code()}, success=${response.isSuccessful}")

                response.errorBody()?.string()?.let {
                    Log.e("AuthService", "signUp ▶ errorBody: $it")
                }

                // 2) 서버 응답 코드 확인
                Log.d("AuthService", "signUp ▶ HTTP 상태코드=${response.code()}")

                if (response.isSuccessful) {
                    // 3) 바디가 null 인지 확인
                    val body = response.body()
                    if (body == null) {
                        Log.e("AuthService", "signUp ▶ response.body() 가 null 입니다.")
                        signUpView.onSignUpFailure()
                        return
                    }
                    // 4) 백엔드에서 isSuccess 가 false 인 경우
                    Log.d("AuthService", "signUp ▶ body: isSuccess=${body.isSuccess}, code='${body.code}', message='${body.message}'")
                    if (!body.isSuccess) {
                        Log.e("AuthService", "signUp ▶ 백엔드 오류: 코드='${body.code}', 메시지='${body.message}'")
                        signUpView.onSignUpFailure()
                    } else {
                        Log.i("AuthService", "signUp ▶ 회원가입 성공!")
                        signUpView.onSignUpSuccess()
                    }
                } else {
                    // 5) HTTP 에러(4xx,5xx)인 경우 errorBody 까지 찍어보기
                    val err = try { response.errorBody()?.string() } catch (e: Exception) { "errorBody 파싱 실패" }
                    Log.e("AuthService", "signUp ▶ HTTP 에러: 코드=${response.code()}, errorBody=$err")
                    signUpView.onSignUpFailure()
                }
            }
        })
    }

    fun login(user: User) {
        // ① LoginRequest 생성
        val req = LoginRequest(
            email    = user.email,
            password = user.password
        )
        // ② 요청 JSON 찍어보기
        Log.d("AuthService", "login ▶ 요청 JSON=${Gson().toJson(req)}")

        api.login(req).enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("AuthService", "login ▶ onFailure: ${t.message}", t)
                loginView.onLoginFailure("네트워크 에러: ${t.message}")
            }

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("AuthService", "login ▶ HTTP 코드=${response.code()}, 성공=${response.isSuccessful}")
                if (!response.isSuccessful) {
                    val err = response.errorBody()?.string()
                    Log.e("AuthService", "login ▶ HTTP 에러 바디=$err")
                    loginView.onLoginFailure("서버 에러: $err")
                    return
                }

                val body = response.body()
                if (body == null) {
                    Log.e("AuthService", "login ▶ body == null")
                    loginView.onLoginFailure("서버 응답 이상")
                    return
                }

                Log.d("AuthService", "login ▶ body.isSuccess=${body.isSuccess}, code=${body.code}, message=${body.message}")
                if (body.isSuccess && body.result != null) {
                    loginView.onLoginSuccess(body.code, body.result)
                } else {
                    // 서버에서 전달된 정확한 메시지를 토스트로 띄워줍니다
                    loginView.onLoginFailure(body.message)
                }
            }
        })
    }
//    fun signUp(user:User){
//
//        val authService = getClient().create(AuthRetrofitInterface::class.java)
//
//        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
//            override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
//                Log.d("SIGNUP/FAILURE", t.message.toString())
//            }
//
//            override fun onResponse(
//                call: retrofit2.Call<AuthResponse>,
//                response: Response<AuthResponse>
//            ) {
//                Log.d("SIGNUP/SUCCESS", response.toString())
//                val resp: AuthResponse = response.body()!!
//                when(resp.code){
//                    "200" ->signUpView.onSignUpSuccess()
//                    else -> signUpView.onSignUpFailure()
//                }
//            }
//
//        })
//        Log.d("SIGNUP", "HELLO")
//    }
//
//    fun login(user:User){
//
//        val authService = getClient().create(AuthRetrofitInterface::class.java)
//
//        authService.login(user).enqueue(object: Callback<AuthResponse> {
//            override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
//                Log.d("LOGIN/FAILURE", t.message.toString())
//            }
//
//            override fun onResponse(
//                call: retrofit2.Call<AuthResponse>,
//                response: Response<AuthResponse>
//            ) {
//                Log.d("LOGIN/SUCCESS", response.toString())
//                val resp: AuthResponse = response.body()!!
//                when(val code = resp.code){
//                    "1000" -> loginView.onLoginSuccess(code, resp.result!!)
//                    "AUTH_014" -> {
//                        // TODO: 회원가입 유도, 에러 메시지 표시
//                    }
//                    "AUTH_008" -> {
//                        // 비밀번호 오류
//                        // TODO: 비밀번호 재입력 유도
//                    }
//                    "AUTH_009" -> {
//                        // 인증 실패
//                        // TODO: 인증 실패 안내
//                    }
//                    else -> loginView.onLoginFailure()
//                }
//            }
//
//        })
//        Log.d("LOGIN", "HELLO")
//    }
}

