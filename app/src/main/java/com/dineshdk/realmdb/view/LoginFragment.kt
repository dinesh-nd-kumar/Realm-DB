package com.dineshdk.realmdb.view

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
import com.dineshdk.realmdb.others.SessionData.loginUserEmail
import com.dineshdk.realmdb.databinding.FragmentLoginBinding
import com.dineshdk.realmdb.repo.CurrentUser
import io.realm.Realm


class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private lateinit var realm: Realm


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        realm = Realm.getDefaultInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navGraph = Navigation.findNavController(view)

        binding.createAccount.setOnClickListener {
            navGraph.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.signinBtn.setOnClickListener {
            signIn()
        }
        binding.emailAsUserName.setText(loginUserEmail)

  }
    private fun signIn(){
        val email = binding.emailAsUserName.text.toString()
        val pass = binding.password.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty() ){

//            val ids = realm.where(User::class.java).max("id")
            val availUsers = realm.where(User::class.java)
                .equalTo("email",email).findAll()
            val usize = availUsers.size
            if (usize >0){
                val availUser = availUsers[0]

                if (pass == availUser?.password){

                        CurrentUser(requireContext())
                            .setUser(availUser)
                        val intent = Intent(context, MapsActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    else{
                        Toast.makeText(context, " input INVALID", Toast.LENGTH_SHORT).show()
                    }

                }else{
                Toast.makeText(context, " input INVALID", Toast.LENGTH_SHORT).show()
            }
            }else{
            Toast.makeText(context, "Empty Not Allowed", Toast.LENGTH_SHORT).show()
        }
    }


}








