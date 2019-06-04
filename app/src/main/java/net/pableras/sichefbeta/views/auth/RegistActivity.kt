package net.pableras.sichefbeta.views.auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_regist.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.model.User
import org.jetbrains.anko.toast

class RegistActivity : AppCompatActivity() {
    companion object {
        const val TAG = "pablerasChefRegist"
    }

    lateinit var auth: FirebaseAuth
    lateinit var recetasFS: FirebaseFirestore
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        auth = FirebaseAuth.getInstance()
        recetasFS = FirebaseFirestore.getInstance()

        user = User()

        btnregist.setOnClickListener { guardarUsuario() }
    }

    private fun guardarUsuario() {

        if (etnickname.text.toString().isEmpty()){
            etnickname.error = "El nick despistao"
            etnickname.requestFocus()
            return
        }else{
            user.nick = etnickname.text.toString()
        }

        if (etemailnew.text.toString().isEmpty()){
            etemailnew.error = "El email cipote"
            etemailnew.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etemailnew.text.toString()).matches()){
            etemailnew.error = "El email no es valido"
            etemailnew.requestFocus()
            return
        }else{
            user.email = etemailnew.text.toString()
        }

        if (etpasswdnew.text.toString().isEmpty()){
            etpasswdnew.error = "La Password melón"
            etpasswdnew.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(user.email, etpasswdnew.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    //val user = auth.currentUser
                    user.id = auth.currentUser!!.uid
                    saveFS(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail ${user.email}:failure", task.exception)
                    toast("Authentication failed try again.")
                }
            }
    }

    private fun saveFS(user: User) {
        recetasFS.collection("usuarios").document(user.email)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        startLogActv(user)
    }

    private fun startLogActv(user: User) {
        val intent = Intent(this, LogActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }
}
