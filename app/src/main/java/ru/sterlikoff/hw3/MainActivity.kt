package ru.sterlikoff.hw3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.dialog_login.view.*
import kotlinx.coroutines.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_login.view.editLogin
import kotlinx.android.synthetic.main.dialog_login.view.editPassword
import kotlinx.android.synthetic.main.dialog_registration.view.*
import ru.sterlikoff.hw3.adapters.PostAdapter
import ru.sterlikoff.hw3.components.Repository

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun registrationDialog() {

        val alert = AlertDialog.Builder(this).create()
        val view =
            layoutInflater.inflate(
                R.layout.dialog_registration,
                alert.findViewById(R.id.bodyRegistration)
            )

        with(view) {

            buttonRegistration.setOnClickListener {

                if (editLogin.text.toString().isEmpty() || editPassword.text.toString().isEmpty()) {

                    showMessage("Заполните все поля")

                } else {

                    launch {

                        val response = Repository.registration(
                            editLogin.text.toString(),
                            editPassword.text.toString()
                        )

                        if (response.isSuccessful) {
                            alert.dismiss()
                        } else {
                            showMessage(context.getString(R.string.error_label))
                        }

                    }

                }

            }

        }

        alert.setView(view)
        alert.show()

    }

    private fun loginDialog() {

        val alert = AlertDialog.Builder(this).create()
        val view =
            layoutInflater.inflate(R.layout.dialog_login, alert.findViewById(R.id.bodyLogin))

        with(view) {

            buttonLogin.setOnClickListener {

                if (editLogin.text.toString().isEmpty() || editPassword.text.toString().isEmpty()) {

                    showMessage("Заполните все поля")

                } else {

                    launch {

                        val response = Repository.authenticate(
                            editLogin.text.toString(),
                            editPassword.text.toString()
                        )

                        val token = response.body()?.token ?: ""

                        if (token.isNotEmpty() && response.isSuccessful) {
                            alert.dismiss()
                        } else {
                            showMessage(context.getString(R.string.illegal_login_or_password_label))
                        }

                    }

                }

            }

            buttonOpenRegistration.setOnClickListener {
                registrationDialog()
            }

        }

        alert.setCancelable(false)
        alert.setView(view)
        alert.show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemList.layoutManager = LinearLayoutManager(this)
        itemList.adapter = PostAdapter(mutableListOf(), mutableListOf(), this)

        loginDialog()

    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}