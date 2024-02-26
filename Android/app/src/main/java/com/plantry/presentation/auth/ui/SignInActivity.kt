package com.plantry.presentation.auth.ui


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.plantry.presentation.MainActivity
import com.plantry.R
import com.plantry.coreui.base.BindingActivity
import com.plantry.coreui.context.longToast
import com.plantry.coreui.context.toast
import com.plantry.coreui.view.UiState
import com.plantry.databinding.ActivitySigninBinding
import com.plantry.presentation.auth.viewmodel.SignInViewModel
import com.plantry.presentation.profile.popup.DeleteIdPopUp.Companion.SIGNOUT
import com.plantry.presentation.profile.popup.LogOutPopUp.Companion.LOGOUT


class SignInActivity : BindingActivity<ActivitySigninBinding>(R.layout.activity_signin) {
    private lateinit var googleSignResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel by viewModels<SignInViewModel>()

    val googleSignInOption =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(resources.getString(R.string.client_id))
            .requestServerAuthCode(resources.getString(R.string.client_id))
            .build()

    lateinit var mGoogleSignInClient : GoogleSignInClient


    override fun initView() {
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)
        FirebaseApp.initializeApp(this)
        checkSignState()
        getGoogleClient()
        checkAlarmPermmission()
        googleSignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
        observe()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            viewModel.getGoogleLogin(account.idToken.toString(), getDeviceToken())
        }
    }

    private fun getGoogleClient() {
        binding.tvSigninLogin.setOnClickListener {
            // 사용자 ID와 기본 프로필 정보 요청
            val signIntent: Intent = mGoogleSignInClient.signInIntent
            googleSignResultLauncher.launch(signIntent)
        }
    }

    @SuppressLint("HardwareIds")
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val googleTokenAuth = account.idToken

            if (!googleTokenAuth.isNullOrBlank()) {
                viewModel.getGoogleLogin(googleTokenAuth, getDeviceToken())
            }
        } catch (e: ApiException) {
            Log.d("aaa", "signInResult:failed Code = " + e.statusCode)

        }
    }

    private fun getDeviceToken():String{
        // FCM 토큰 값을 가져오기
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val savedToken = pref.getString("token", null)
        Log.d("Aaa", pref.toString())
        Log.d("Aaa 알림", savedToken.toString())

        return savedToken.toString()
    }

    private fun observe() {
        viewModel.accessToken.observe(this) {
            when (it) {
                is UiState.Success -> {
                    navigateTo<MainActivity>()
                }

                else -> Unit
            }
        }
    }

    private fun checkAlarmPermmission() {
        val prefs = getSharedPreferences(ALARM_PERMITTED_USED, Context.MODE_PRIVATE)
        val alarmPermissionRequested = prefs.getBoolean(ALARM_PERMITTED_USED_REQUESTED, false)

        if (!alarmPermissionRequested) {
            // 알림 권한을 요청한 적이 없다면 요청
            requestAlarmPermission()
        }

    }
    private fun requestAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult.launch(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            )
        }
    }

    private val registerForActivityResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->

        val prefsAlarm = getSharedPreferences(ALARM_STATE, Context.MODE_PRIVATE)
        val deniedPermissionList = permissions.filter { !it.value }.map { it.key }
        when {
            deniedPermissionList.isNotEmpty() -> {
                val map = deniedPermissionList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                }
                map[DENIED]?.let {
                    // 단순히 권한이 거부 되었을 때
                    longToast("If permission is not allowed, product deadline notification cannot appear.")
                    with(prefsAlarm.edit()) {
                        putBoolean(ALARM_STATE_REQUESTED, false)
                        apply()
                    }
                }
                map[EXPLAINED]?.let {
                    with(prefsAlarm.edit()) {
                        putBoolean(ALARM_STATE_REQUESTED, false)
                        apply()
                    }
                }
            }
        }

        // 알림 권한을 요청한 상태를 저장
        val prefs = getSharedPreferences(ALARM_PERMITTED_USED, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(ALARM_PERMITTED_USED_REQUESTED, true)
            apply()
        }
    }

    private fun checkSignState(){
        val signState: Int = intent.getIntExtra("sign_state", -1)
        when(signState){
            LOGOUT ->{
                logOut()
            }
            SIGNOUT -> {
                signOut()
            }
        }

    }
    private fun logOut(){
         mGoogleSignInClient.signOut()
    }

    private fun signOut(){
        mGoogleSignInClient.revokeAccess()
        clearSharedPreferences(this)
    }

    // SharedPreferences 초기화
    fun clearSharedPreferences(context: Context) {
        val sharedPreferencesAlarmState = context.getSharedPreferences(ALARM_STATE, Context.MODE_PRIVATE)

        val editorAlarmState = sharedPreferencesAlarmState.edit()

        // 모든 값을 제거
        editorAlarmState.clear()

        // 변경 사항을 적용
        editorAlarmState.apply()
    }

    companion object {
        const val DENIED = "denied"
        const val EXPLAINED = "explained"
        const val ALARM_PERMITTED_USED = "AlarmPermittedUsed"
        const val ALARM_PERMITTED_USED_REQUESTED = "alarm_permission_requested"
        const val ALARM_STATE = "AlarmState"
        const val ALARM_STATE_REQUESTED = "alarm_state"
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(this@SignInActivity, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}