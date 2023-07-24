package com.docapp.frnt.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.docapp.frnt.MainActivity
import com.docapp.frnt.R
import com.docapp.frnt.data.companions.Constants.DISPLAY_NAME
import com.docapp.frnt.data.companions.Constants.PROFILE_PIC_LOCATION
import com.docapp.frnt.data.companions.Constants.USER_EMAIL
import com.docapp.frnt.data.companions.Constants.UUID

import com.docapp.frnt.databinding.ActivityLoginBinding
import com.docapp.frnt.doctors_section.DoctorSideMain

open class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer
            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })
        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {

                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString(),
                            "${getString(R.string.backend_base)}/${getString(R.string.login_endpoint)}"
                        )
                    }
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(
                    username.text.toString(),
                    password.text.toString(),
                    "${getString(R.string.backend_base)}/${getString(R.string.login_endpoint)}"
                )
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val displayName = model.displayName
        val welcome = getString(R.string.welcome, displayName)
        Toast.makeText(applicationContext, welcome, Toast.LENGTH_SHORT).show()

        val patientMain = Intent(this, MainActivity::class.java)
        val doctorMain = Intent(this, DoctorSideMain::class.java)
        intent = if ((binding.isDoc as SwitchCompat).isChecked)
            doctorMain
        else
            patientMain

        // TODO : initiate successful logged in experience
        intent.putExtras(
            bundleOf(
                UUID to model.uuid,
                DISPLAY_NAME to model.displayName,
                USER_EMAIL to model.userEmail,
                PROFILE_PIC_LOCATION to model.profilePicLocation
            )
        )
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    fun getLoginview()
            : LoginViewModel {
        return this.loginViewModel
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}