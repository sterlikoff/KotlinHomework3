package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.components.Repository

class RegistrationActivity: MyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        buttonRegistration.setOnClickListener {

            if (editLogin.text.toString().isEmpty() || editPassword.text.toString().isEmpty()) {

                showMessage("Заполните все поля")

            } else {

                launch {

                    val dialog = ProgressDialog(this@RegistrationActivity).apply{
                        setMessage("Пожалуйста подождите...")
                        setTitle("Загрузка данных")
                        setCancelable(false)
                        show()
                    }

                    val response = Repository.registration(
                        editLogin.text.toString(),
                        editPassword.text.toString()
                    )

                    dialog.dismiss()

                    if (response.isSuccessful) {
                        finish()
                    } else {
                        showMessage(getString(R.string.error_label))
                    }

                }

            }

        }

    }

}