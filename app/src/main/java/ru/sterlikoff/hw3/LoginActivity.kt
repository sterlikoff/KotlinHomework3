package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.models.AuthRequestParams
import ru.sterlikoff.hw3.components.Repository

class LoginActivity : MyActivity() {

    private fun isAuthenticated() =

        getSharedPreferences(API_SHARED_FILE, MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

    private fun setUserAuth(token: String) =

        getSharedPreferences(API_SHARED_FILE, MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .commit()

    private fun startApp() {

        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (isAuthenticated()) {
            startApp()
            return
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {

            val auth = AuthRequestParams(
                editLogin.text.toString(),
                editPassword.text.toString()
            )

            if (auth.validate()) {

                launch {

                    val dialog = ProgressDialog(this@LoginActivity).apply {
                        setMessage("Пожалуйста подождите...")
                        setTitle("Загрузка данных")
                        setCancelable(false)
                        show()
                    }

                    val response = Repository.authenticate(auth)

                    dialog.dismiss()

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

    companion object {
        const val AUTHENTICATED_SHARED_KEY = "authenticated_shared_key"
        const val API_SHARED_FILE = "API_shared_file"
    }

}