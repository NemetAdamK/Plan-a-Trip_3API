package com.example.plan_a_trip.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.plan_a_trip.Globals.username
import com.example.plan_a_trip.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {

    lateinit var userAuth: FirebaseAuth
    var storedVerificationId:String = ""
    lateinit var resendToken:Any



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userAuth = FirebaseAuth.getInstance()

        val buttonSendCode = findViewById<Button>(R.id.buttonSendCode)
        buttonSendCode.setOnClickListener{
            if (editTextUsername.text.isNotEmpty()){

                username = editTextUsername.text.toString()
                    sendVerificationCode()

            } else {
                Toast.makeText(applicationContext,"Please enter username",Toast.LENGTH_LONG).show()
            }

        }

        val buttonVerify= findViewById<Button>(R.id.buttonVerify)
        buttonVerify.setOnClickListener{
            verifySignInCode()
        }

    }

    private fun verifySignInCode() {
        val code = editTextVerificationCode.text.toString()
        try {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
            val toast =
                Toast.makeText(this, "Verification Code is wrong", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }


    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("Verify", "onVerificationCompleted:$credential")

            editTextVerificationCode.setText(credential.getSmsCode())
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("Verify fail", "onVerificationFailed", e)

            Toast.makeText(applicationContext, "Verification failed", Toast.LENGTH_SHORT)
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("Code sent", "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token

            // ...
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        userAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("After sent", "signInWithCredential:success")

                    val user = task.result?.user
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("users")

                    myRef.child(username).child("Phone number").setValue(FirebaseAuth.getInstance().currentUser?.phoneNumber)

                    val intent = Intent(applicationContext, MainActivitySelect::class.java)
                    startActivity(intent)


                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("Failed sign in", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    private fun sendVerificationCode() {
        val phone: String = editTextPhoneNumber.getText().toString()
        if (phone.isEmpty()) {
            editTextPhoneNumber.error = "Phone number is required"
            editTextPhoneNumber.requestFocus()
            return
        }
        if (phone.length < 10) {
            editTextPhoneNumber.error = "Please enter a valid phone"
            editTextPhoneNumber.requestFocus()
            return
        }
        Toast.makeText(
            applicationContext,
            "Verification code sent", Toast.LENGTH_LONG
        ).show()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+4"+editTextPhoneNumber.text.toString(), // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks) // OnVerificationStateChangedCallbacks// OnVerificationStateChangedCallbacks
        Toast.makeText(
            applicationContext,
            "Verification code sent toooo", Toast.LENGTH_LONG
        ).show()
    }


}
