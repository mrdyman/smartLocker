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
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var lockerAdapter : LockerAdapter
    private lateinit var databaseReference : DatabaseReference
    private lateinit var lockerList : ArrayList<Locker>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lockerList = arrayListOf()
        lockerAdapter = LockerAdapter(lockerList)
        rv_locker.setHasFixedSize(true)
        rv_locker.layoutManager = LinearLayoutManager(activity)
        rv_locker.adapter = lockerAdapter
        getLocker()
    }

    fun getLocker(){
        databaseReference = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Locker")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){
                    for (data in dataSnapshot.children){
                        val locker = data.getValue(Locker::class.java)
                        lockerList.add(locker!!)
                    }
                    rv_locker.adapter = lockerAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Firebase", "Database Error with message ${databaseError.message}")
            }

        })
    }
}