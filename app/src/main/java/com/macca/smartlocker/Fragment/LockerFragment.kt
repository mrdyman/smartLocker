package com.macca.smartlocker.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.macca.smartlocker.Adapter.LockerAdapter
import com.macca.smartlocker.Model.Locker
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_locker.*

class LockerFragment : Fragment() {

    private lateinit var lockerAdapter : LockerAdapter
    private lateinit var databaseReference : DatabaseReference
    private lateinit var lockerList : ArrayList<Locker>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lockerList = arrayListOf()
        lockerAdapter = LockerAdapter(lockerList)
        rv_list_locker_available.setHasFixedSize(true)
        rv_list_locker_available.layoutManager = LinearLayoutManager(activity)
        rv_list_locker_available.adapter = lockerAdapter
        getLocker()
    }

    fun getLocker(){
        databaseReference = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Locker")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){
                    for (data in dataSnapshot.children){
                        //ambil data status locker (ready/booked)
                        val lockerStatus = data.child("Status").value

                        //cek, kalo status locker = ready, tampilkan ke recyclerview
                        if (lockerStatus == "Ready") {
                            //status = Ready, masukkan data ke recyclerView
                            val lockerData = data.getValue(Locker::class.java)
                            lockerList.add(lockerData!!)
                            Log.d("lockerStatus", "Locker is Ready")
                        }
                    }
                    rv_list_locker_available.adapter = lockerAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Firebase", "Database Error with message ${databaseError.message}")
            }

        })
    }
}