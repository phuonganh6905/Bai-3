package com.example.bai3

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bai3.R
import java.text.SimpleDateFormat
import java.util.*

class ThemGiaoDichActivity : AppCompatActivity() {

    private lateinit var oNhapSoTien: EditText
    private lateinit var oNhapMoTa: EditText
    private lateinit var nhomLuaChonLoai: RadioGroup
    private lateinit var radioThu: RadioButton
    private lateinit var radioChi: RadioButton
    private lateinit var oChonDanhMuc: TextView
    private lateinit var oChonNgay: DatePicker
    private lateinit var nutLuu: Button

    private var danhMucDuocChon: String = ""
    private val cacDanhMuc = arrayOf("Ăn uống", "Di chuyển", "Mua sắm", "Lương", "Khác")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        oNhapSoTien = findViewById(R.id.oNhapSoTien)
        oNhapMoTa = findViewById(R.id.oNhapMoTa)
        nhomLuaChonLoai = findViewById(R.id.nhomLuaChonLoai)
        radioThu = findViewById(R.id.radioThu)
        radioChi = findViewById(R.id.radioChi)
        oChonDanhMuc = findViewById(R.id.oChonDanhMuc)
        oChonNgay = findViewById(R.id.oChonNgay)
        nutLuu = findViewById(R.id.nutLuu)

        oChonDanhMuc.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Chọn danh mục")
                .setItems(cacDanhMuc) { _, which ->
                    danhMucDuocChon = cacDanhMuc[which]
                    oChonDanhMuc.text = danhMucDuocChon
                }.show()
        }

        nutLuu.setOnClickListener {
            val soTienText = oNhapSoTien.text.toString()
            val moTaText = oNhapMoTa.text.toString()

            if (soTienText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loai = when {
                radioThu.isChecked -> "Thu"
                radioChi.isChecked -> "Chi"
                else -> ""
            }

            if (loai.isEmpty() || danhMucDuocChon.isEmpty()) {
                Toast.makeText(this, "Chọn loại và danh mục", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ngay = "${oChonNgay.dayOfMonth.toString().padStart(2, '0')}/" +
                    "${(oChonNgay.month + 1).toString().padStart(2, '0')}/" +
                    "${oChonNgay.year}"

            val giaoDichMoi = GiaoDich(
                soTien = soTienText.toDouble(),
                moTa = moTaText,
                loai = loai,
                danhMuc = danhMucDuocChon,
                ngay = ngay
            )

            val resultIntent = intent
            resultIntent.putExtra("transaction", giaoDichMoi)
            setResult(Activity.RESULT_OK, resultIntent)

            Toast.makeText(this, "Lưu giao dịch thành công!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}