package com.hj.store

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hj.store.data.User
import com.hj.store.remote.StoreApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreLoginFragment(val onLogin: OnLoginListener) : Fragment() {

    private lateinit var rootView: View

    companion object {
        fun newInstance(isLogin:OnLoginListener) = StoreLoginFragment(isLogin)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.store_login_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idEditText = rootView.findViewById<EditText>(R.id.store_login_id_editTextText)
        val passwordEditText =
            rootView.findViewById<EditText>(R.id.store_login_password_editTextText)

        val loginButton = rootView.findViewById<Button>(R.id.store_login_confirm_button)
        loginButton.setOnClickListener {
            val idStr = idEditText.text.toString()
            val passwordStr = passwordEditText.text.toString()
            val user = User(idStr, passwordStr)
            if (idStr.isNotEmpty() && passwordStr.isNotEmpty()) {
                StoreApi.storeLoginService.loginUser(user = user)
                    .enqueue(object : Callback<Boolean?> {
                        override fun onResponse(
                            call: Call<Boolean?>,
                            response: Response<Boolean?>
                        ) {
                            if (response.isSuccessful) {
                                val result: Boolean = response.body() ?: false
                                if (result) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.login_success),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val sharedPref =
                                        activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                                    with(sharedPref.edit()) {
                                        putInt(getString(R.string.saved_user_login_key), 1)
                                        apply()
                                    }

                                    finish()

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.login_fail),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.login_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Boolean?>, t: Throwable) {
                            Log.d("로그인", "onFailure: $t")
                        }
                    })
            }
        }

        val cancelButton = rootView.findViewById<Button>(R.id.store_login_cancel_button)
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun finish() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        onLogin.onLogin(true)
    }
}

class OnLoginListener(val loginListener: (isLogin: Boolean) -> Unit) {
    fun onLogin(isLogin: Boolean) = loginListener(isLogin)
}
