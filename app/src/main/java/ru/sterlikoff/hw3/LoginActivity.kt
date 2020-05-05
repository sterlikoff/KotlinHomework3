package ru.sterlikoff.hw3

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.models.AuthRequestParams
import ru.sterlikoff.hw3.components.Repository

class LoginActivity : MyActivity() {

    private fun startApp() {

        Repository.createRetrofitWithAuth(getToken()!!)

        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (isAuthenticated()) startApp()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {

            val auth = AuthRequestParams(
                editLogin.text.toString(),
                editPassword.text.toString()
            )

            if (auth.validate()) {

                launch {

                    showProgress()
                    val response = Repository.authenticate(auth)
                    hideProgress()

                    val token = response.body()?.token ?: ""

                    if (token.isNotEmpty() && response.isSuccessful) {

                        setUserAuth(token)
                        startApp()

                    } else {
                        showMessage(getString(R.string.illegal_login_or_password_label))
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