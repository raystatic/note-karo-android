package com.raystatic.notekaro.ui.activities.initials

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.raystatic.notekaro.R
import com.raystatic.notekaro.data.requests.AuthRequest
import com.raystatic.notekaro.other.Constants
import com.raystatic.notekaro.other.PrefManager
import com.raystatic.notekaro.other.ProgressBarExtension.hide
import com.raystatic.notekaro.other.ProgressBarExtension.show
import com.raystatic.notekaro.other.Status
import com.raystatic.notekaro.other.Utility
import com.raystatic.notekaro.ui.activities.HomeActivity
import com.raystatic.notekaro.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var prefManager: PrefManager

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private val vm: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val email = prefManager.getString(Constants.USER_EMAIL)
        val name = prefManager.getString(Constants.USER_NAME)
        val avatar = prefManager.getString(Constants.USER_AVATAR)

        if (email.isNullOrEmpty() || name.isNullOrEmpty() || avatar.isNullOrEmpty()){
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }else{
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

            progress_splash.show()
            signIn()

            subscribeToObservers()
        }

    }

    private fun subscribeToObservers() {

        vm.authResponse.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {

                    progress_splash.hide()

                    it.data?.let {res->
                        if (!res.error.isNullOrEmpty()){
                            Utility.showToast(Constants.SOMETHING_WENT_WRONG,this)
                        }else{
                            res._user?.let { user ->
                                vm.insertUser(user)
                                prefManager.saveString(Constants.USER_NAME, user.name)
                                prefManager.saveString(Constants.USER_EMAIL, user.email)
                                prefManager.saveString(Constants.USER_AVATAR, user.avatar)
                                prefManager.saveString(Constants.JWT_TOKEN,res._token)

                                startActivity(Intent(this,
                                    HomeActivity::class.java))
                                finish()

                            }

                        }
                    }
                }
                Status.LOADING -> {
                    progress_splash.show()
                }
                Status.ERROR -> {
                    progress_splash.hide()
                    Utility.showToast(it.message.toString(),this)
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)

            val name:String = account?.displayName.toString()
            val email:String = account?.email.toString()
            val avatar:String = account?.photoUrl.toString()

            val authRequest = AuthRequest(name, email, avatar)

            vm.authenticateUser(authRequest)

        } catch (e: ApiException) {
            Timber.d("signInResult:failed code=" + e.statusCode)
            progress_splash.hide()
            Utility.showToast(Constants.SOMETHING_WENT_WRONG,this)
        }

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,
            RC_SIGN_IN
        )

    }
}