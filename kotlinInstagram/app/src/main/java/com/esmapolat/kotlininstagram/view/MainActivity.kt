package com.esmapolat.kotlininstagram.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.esmapolat.kotlininstagram.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth

        val currentUser=auth.currentUser
        if(currentUser!=null){
            intent= Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(intent)
            finish()

        }


    }
    fun signinClick(view: View){
        val email=binding.meilText.text.toString()
        val password=binding.passwordText.text.toString()
        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Enter email and pasword",Toast.LENGTH_LONG).show()

        }
        else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                //basarılı olursa kayıt şlemi
                val intent=Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                //hata verirse
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }
    fun signupClick(view:View){
        val email=binding.meilText.text.toString()
        val password=binding.passwordText.text.toString()
        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Enter email and pasword",Toast.LENGTH_LONG).show()

        }
        else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                //basarılı olursa kayıt şlemi
                val intent=Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                //hata verirse
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }
}