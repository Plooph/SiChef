package net.pableras.sichefbeta.views.auth

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_log.*
import net.pableras.sichefbeta.R
import net.pableras.sichefbeta.model.User
import net.pableras.sichefbeta.views.home.HomeActivity
import org.jetbrains.anko.toast

class LogActivity : AppCompatActivity() {
    companion object {
        const val TAG = "pablerasChef"
    }

    //autentificación con firebase
    private lateinit var auth: FirebaseAuth
    //usuario logeado
    private lateinit var user: User
    lateinit var recetasFS: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        auth = FirebaseAuth.getInstance()
        recetasFS = FirebaseFirestore.getInstance()

        user = User()
        if (Intent().data != null)
            user = intent.getSerializableExtra("user") as User

        new_user.setOnClickListener { startActivity(Intent(this, RegistActivity::class.java)) }

        sing_in.setOnClickListener {  autentificacion() }
    }

    /***************************** AUTH INICIAL ********************************/
    public override fun onStart() {
        super.onStart()
        // Checkea si el usuario esta logeado in (non-null) y actualiza UI accordingly.
        val currentUser = auth.currentUser

        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        //si hay un usuario logeado pasa directo a la home
        if(currentUser != null){
            val email = currentUser.email.toString()
            findUser(email)
        }else{
            toast("Hello World!")
        }
    }

    /***************************** AUTH INICIAL ********************************/

    /***************************** SINGIN ********************************/

    private fun autentificacion() {
        if (etemail.text.toString().isEmpty()){
            etemail.error = "El email cipote"
            etemail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etemail.text.toString()).matches()){
            etemail.error = "El email no es valido"
            etemail.requestFocus()
            return
        }

        if (etpassword.text.toString().isEmpty()){
            etpassword.error = "La Password melón"
            etpassword.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(etemail.text.toString(), etpassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                    val email = etemail.text.toString()
                    findUser(email)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
                    updateUI(null)
                }

            }
    }

    /***************************** SINGIN ********************************/

    /***************************** SHARE ********************************/

    private fun findUser(email: String) {
        user = User()
        val docRef = recetasFS.collection("usuarios").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "${document.data}")
                    user = document.toObject(User::class.java)!!

                    goHome(user)
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
   }


    /***************************** SHARE ********************************/

    /***************************** GOHOME ********************************/

    private fun goHome(user: User) {
        val goUser = user
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("loguser", goUser)
        //Log.d(TAG, "El usuario que no viaja ${goUser.email}")
        startActivity(intent)
        finish()
    }

    /***************************** GOHOME ********************************/
}
