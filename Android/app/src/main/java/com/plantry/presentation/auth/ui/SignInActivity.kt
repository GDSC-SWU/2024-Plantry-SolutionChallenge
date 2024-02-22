package com.plantry.presentation.auth.ui


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.plantry.MainActivity
import com.plantry.R
import com.plantry.coreui.base.BindingActivity
import com.plantry.coreui.context.longToast
import com.plantry.coreui.view.UiState
import com.plantry.databinding.ActivitySigninBinding
import com.plantry.presentation.auth.viewmodel.SignInViewModel


class SignInActivity : BindingActivity<ActivitySigninBinding>(R.layout.activity_signin) {
    private lateinit var googleSignResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel by viewModels<SignInViewModel>()

    override fun initView() {
        getGoogleClient()
        checkAlarmPermmission()
        googleSignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            Log.d("aaa", task.toString())
            handleSignInResult(task)
        }
        observe()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            // navigateTo<MainActivity>()
        }

    }

    private fun getGoogleClient() {
        binding.tvSigninLogin.setOnClickListener {
            // 사용자 ID와 기본 프로필 정보 요청
            val googleSignInOption =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken("29775346424-c3ocpsd6pv76lu9bgdi046lft97ftkkn.apps.googleusercontent.com")
                    .requestServerAuthCode("29775346424-c3ocpsd6pv76lu9bgdi046lft97ftkkn.apps.googleusercontent.com")
                    .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)
            val signIntent: Intent = mGoogleSignInClient.signInIntent
            googleSignResultLauncher.launch(signIntent)
        }
    }

    @SuppressLint("HardwareIds")
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val googleTokenAuth = account.idToken

            // FCM 토큰 값을 가져오기
            val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
            val savedToken = pref.getString("token", null)

            Log.d("Aaa", pref.toString())
            Log.d("Aaa", savedToken.toString())

            if (!googleTokenAuth.isNullOrBlank()) {
                viewModel.getGoogleLogin(googleTokenAuth, savedToken.toString())
            }
        } catch (e: ApiException) {
            Log.d("aaa", "signInResult:failed Code = " + e.statusCode)

        }
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
        // 알림 권한 허용 팝업 띄우기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult.launch(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            )
        }
    }

    private val registerForActivityResult = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val deniedPermissionList = permissions.filter { !it.value }.map { it.key }
        when {
            deniedPermissionList.isNotEmpty() -> {
                val map = deniedPermissionList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                }
                map[DENIED]?.let {
                    // 단순히 권한이 거부 되었을 때
                    longToast("If permission is not allowed, product deadline notification cannot appear.")
                }
                map[EXPLAINED]?.let {
                }
            }

            else -> {
                // 모든 권한이 허가 되었을 때
                Log.d("aaa", "allset")
            }
        }
    }

    companion object {
        const val DENIED = "denied"
        const val EXPLAINED = "explained"
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(this@SignInActivity, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}