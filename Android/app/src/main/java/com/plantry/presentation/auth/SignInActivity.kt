package com.plantry.presentation.auth


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.plantry.MainActivity
import com.plantry.R
import com.plantry.coreui.base.BindingActivity
import com.plantry.coreui.view.UiState
import com.plantry.databinding.ActivitySigninBinding


class SignInActivity : BindingActivity<ActivitySigninBinding>(R.layout.activity_signin) {


    private lateinit var googleSignResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel  = SignInViewModel(this)

    override fun initView() {
        getGoogleClient()
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

            if (!googleTokenAuth.isNullOrBlank()) {
                if (Build.VERSION.SDK_INT >= 34) {
                    viewModel.getGoogleLogin(googleTokenAuth, deviceId.toString())
                } else {
                    // 34 이하 일 때 사용할 코드 작성
                    val deviceId = Settings.Secure.getString(
                        contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    Log.d("Aaa23", googleTokenAuth)
                    viewModel.getGoogleLogin(googleTokenAuth+1, deviceId.toString())
                }
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

    private inline fun <reified T : Activity> navigateTo() {
        Intent(this@SignInActivity, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

}