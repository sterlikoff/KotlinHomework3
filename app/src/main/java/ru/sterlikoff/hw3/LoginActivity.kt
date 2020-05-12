package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.models.AuthRequestParams
import ru.sterlikoff.hw3.components.Repository
import splitties.toast.toast

class LoginActivity : AppCompatActivity(R.layout.activity_login), ActivityUI {

    override var dialog: ProgressDialog? = null

    private fun startApp() {

        Repository.createRetrofitWithAuth(getToken(this)!!)

        val intent = Intent(this@LoginActivity, FeedActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (isAuthenticated(this)) startApp()

        super.onCreate(savedInstanceState)

        buttonLogin.setOnClickListener {

            val auth = AuthRequestParams(
                editLogin.text.toString(),
                editPassword.text.toString()
            )

            if (auth.validate()) {

                lifecycleScope.launch {

                    showProgress(this@LoginActivity)

                    try {

                        val response = Repository.authenticate(auth)
                        val token = response.body()?.token ?: ""

                        if (token.isNotEmpty() && response.isSuccessful) {

                            setUserAuth(token, this@LoginActivity)
                            startApp()

                        } else {
                            toast(R.string.illegal_login_or_password_label)
                        }

                    } catch (e: Exception) {
                        toast(R.string.connection_error_label)
                    } finally {
                        hideProgress()
                    }

                }

            } else {

                auth.errors.forEach {

                    val field = when (it.key) {
                        "username" -> editLogin
                        "password" -> editPassword
                        else -> throw Exception("Неизвестная ошибка")
                    }

                    field.error = it.value

                }

            }

        }

        buttonOpenRegistration.setOnClickListener {

            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)

        }

    }

}