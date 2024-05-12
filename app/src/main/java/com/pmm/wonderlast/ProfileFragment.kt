package com.pmm.wonderlast

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private lateinit var tvUserEmail: TextView
    private lateinit var btnLogout: RelativeLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var tvnama: TextView
    private lateinit var tvPhone: TextView
    private lateinit var personalData: RelativeLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUserEmail = view.findViewById(R.id.user_email)
        tvnama = view.findViewById(R.id.user_name)
        btnLogout = view.findViewById(R.id.log_out)
        auth = FirebaseAuth.getInstance()
        personalData = view.findViewById(R.id.personal_data)
        tvPhone = view.findViewById(R.id.user_phonenumber)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.displayName.isNullOrEmpty()) {
                "${currentUser.email}".also { tvUserEmail.text = it }
                "Nama belum di set".also { tvnama.text = it }
                "${currentUser.phoneNumber}".also { tvPhone.text = it }
            } else {
                "${currentUser.email}".also { tvUserEmail.text = it }
                "${currentUser.displayName}".also { tvnama.text = it }
                "${currentUser.phoneNumber}".also { tvPhone.text = it }
            }
        }

        personalData.setOnClickListener {
            startActivity(Intent(requireContext(), UserdataActivity::class.java))
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        auth.signOut()
        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }
}
