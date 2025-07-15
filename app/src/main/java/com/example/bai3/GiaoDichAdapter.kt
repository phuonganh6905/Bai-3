package com.example.bai3

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.*

class GiaoDichAdapter(private val danhSachGiaoDich: List<GiaoDich>) :
    RecyclerView.Adapter<GiaoDichAdapter.ViewHolderGiaoDich>() {

    inner class ViewHolderGiaoDich(view: View) : RecyclerView.ViewHolder(view) {
        val tvSoTienDanhMuc: TextView = view.findViewById(R.id.tvSoTienDanhMuc)
        val tvMoTa: TextView = view.findViewById(R.id.tvMoTa)
        val tvNgay: TextView = view.findViewById(R.id.tvNgay)
        val imgIcon: ImageView = view.findViewById(R.id.iconDanhMuc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGiaoDich {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolderGiaoDich(view)
    }

    override fun onBindViewHolder(holder: ViewHolderGiaoDich, position: Int) {
        val giaoDich = danhSachGiaoDich[position]
        val dinhDangTien = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val hienThiSoTien = if (giaoDich.loai == "Thu") {
            "+${dinhDangTien.format(giaoDich.soTien)}"
        } else {
            "-${dinhDangTien.format(giaoDich.soTien)}"
        }

        holder.tvSoTienDanhMuc.text = "$hienThiSoTien  (${giaoDich.danhMuc})"
        holder.tvMoTa.text = giaoDich.moTa
        holder.tvNgay.text = giaoDich.ngay

        holder.tvSoTienDanhMuc.setTextColor(
            if (giaoDich.loai == "Thu") Color.parseColor("#388E3C")
            else Color.RED
        )

        // Gán hình ảnh danh mục phù hợp
        val iconId = when (giaoDich.danhMuc) {
            "Ăn uống" -> R.drawable.ic_an_uong
            "Di chuyển" -> R.drawable.ic_di_chuyen
            "Mua sắm" -> R.drawable.ic_mua_sam
            "Lương" -> R.drawable.ic_luong
            else -> R.drawable.ic_khac
        }
        holder.imgIcon.setImageResource(iconId)
    }

    override fun getItemCount(): Int = danhSachGiaoDich.size
}
