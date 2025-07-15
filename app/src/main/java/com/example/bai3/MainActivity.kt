package com.example.bai3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var nutThem: Button
    private lateinit var hienThiDanhSach: RecyclerView
    private val danhSachGiaoDich = mutableListOf<GiaoDich>()
    private lateinit var giaoDichAdapter: GiaoDichAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nutThem = findViewById(R.id.nutThem)
        hienThiDanhSach = findViewById(R.id.hienThiDanhSach)

        giaoDichAdapter = GiaoDichAdapter(danhSachGiaoDich)
        hienThiDanhSach.layoutManager = LinearLayoutManager(this)
        hienThiDanhSach.adapter = giaoDichAdapter

        nutThem.setOnClickListener {
            val intent = Intent(this, ThemGiaoDichActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val giaoDichMoi = data?.getSerializableExtra("transaction") as? GiaoDich
            giaoDichMoi?.let {
                danhSachGiaoDich.add(it)
                giaoDichAdapter.notifyItemInserted(danhSachGiaoDich.size - 1)
            }
        }
    }
}