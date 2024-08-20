package com.dineshdk.realmdb.view

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.dineshdk.realmdb.R
import com.dineshdk.realmdb.Model.User
import com.dineshdk.realmdb.others.Constant.USER_EMAIL
import com.dineshdk.realmdb.others.Constant.USER_ID
import com.dineshdk.realmdb.others.Constant.USER_PREF
import com.dineshdk.realmdb.others.Constant.USER_NAME
import com.dineshdk.realmdb.databinding.FragmentSignUpBinding
import com.dineshdk.realmdb.others.SessionData
import io.realm.Realm


class SignUpFragment : Fragment() {

    private lateinit var binding : FragmentSignUpBinding
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater,container,false)
        realm = Realm.getDefaultInstance()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navGraph = Navigation.findNavController(binding.root)

        binding.back.setOnClickListener {
            navGraph.navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.btnSignIn.setOnClickListener {
            navGraph.navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.registerBtn.setOnClickListener {
            register()
        }









    }
    private fun register(){
        val userName = binding.userName.text.toString()
        val email = binding.email.text.toString()
        val pass = binding.editTextPassword.text.toString()
        val confirmPass = binding.editTextTextPasswordConfirm.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
            if (isValidEmail(email)){

                if (pass == confirmPass ) {
                    if (pass.length >= 1){


                        val ids = realm.where(User::class.java).max("id")


                        val nextId = ids?.toInt()?.plus(1) ?: 1

                        val user = User(nextId, userName, email,pass)
//                        val userLocation = UserLocation(73.0,11.1)

                        realm.executeTransaction {
//                            it.insert(userLocation)
                            it.insert(user)
                        }
                        SessionData.userid = nextId

                        val userPref = requireContext().getSharedPreferences(USER_PREF,MODE_PRIVATE)
                        val editor = userPref.edit()

                        editor.putInt(USER_ID, nextId)
                        editor.putString(USER_NAME, userName)
                        editor.putString(USER_EMAIL, email)
                        editor.commit()






                        val intent = Intent(context, MapsActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    else{
                        Toast.makeText(context, "password weak", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "password mismatch", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(context, "email not valid", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(context, "Empty Not Allowed", Toast.LENGTH_SHORT).show()
        }
    }





    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )
        return emailRegex.matches(email)
    }
}