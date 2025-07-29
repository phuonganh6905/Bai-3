package com.example.bai3

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ThemGiaoDichActivity : AppCompatActivity() {

    private lateinit var oNhapSoTien: EditText
    private lateinit var oNhapMoTa: EditText
    private lateinit var nhomLuaChonLoai: RadioGroup
    private lateinit var radioThu: RadioButton
    private lateinit var radioChi: RadioButton
    private lateinit var oChonDanhMuc: TextView
    private lateinit var tvChonNgay: TextView
    private lateinit var nutLuu: Button

    private var danhMucDuocChon: String = ""
    private val cacDanhMucThu = arrayOf("Lương", "Thưởng", "Đầu tư", "Khác")
    private val cacDanhMucChi = arrayOf("Ăn uống", "Di chuyển", "Mua sắm", "Khác")
    private var cacDanhMuc = cacDanhMucChi
    private var soTienThuc: Double? = null
    private var ngayDuocChon: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        oNhapSoTien = findViewById(R.id.oNhapSoTien)
        oNhapMoTa = findViewById(R.id.oNhapMoTa)
        nhomLuaChonLoai = findViewById(R.id.nhomLuaChonLoai)
        radioThu = findViewById(R.id.radioThu)
        radioChi = findViewById(R.id.radioChi)
        oChonDanhMuc = findViewById(R.id.oChonDanhMuc)
        tvChonNgay = findViewById(R.id.tvChonNgay)
        nutLuu = findViewById(R.id.nutLuu)

        nutLuu.isEnabled = false

        oNhapSoTien.addTextChangedListener(object : TextWatcher {
            private var isEditing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isEditing) return
                isEditing = true

                val inputRaw = s.toString()
                val cleanString = inputRaw.replace(",", "").trim()

                if (cleanString.isEmpty()) {
                    soTienThuc = null
                    oNhapSoTien.setText("")
                    oNhapSoTien.error = "Vui lòng nhập số tiền"
                    isEditing = false
                    capNhatTrangThaiNutLuu()
                    return
                }

                try {
                    val parsed = cleanString.toDouble()
                    soTienThuc = parsed
                    val formatted = NumberFormat.getInstance(Locale.US).format(parsed)
                    oNhapSoTien.setText(formatted)
                    oNhapSoTien.setSelection(formatted.length)
                    oNhapSoTien.error = null
                } catch (e: Exception) {
                    soTienThuc = null
                    oNhapSoTien.error = "Vui lòng nhập số hợp lệ"
                }

                isEditing = false
                capNhatTrangThaiNutLuu()
            }
        })

        nhomLuaChonLoai.setOnCheckedChangeListener { _, checkedId ->
            capNhatTrangThaiNutLuu()
            when (checkedId) {
                R.id.radioThu -> {
                    capNhatMauGiaoDien(Color.parseColor("#388E3C")) // xanh lá
                    cacDanhMuc = cacDanhMucThu
                }
                R.id.radioChi -> {
                    capNhatMauGiaoDien(Color.RED)
                    cacDanhMuc = cacDanhMucChi
                }
            }
            oChonDanhMuc.text = "Chọn danh mục"
            danhMucDuocChon = ""
        }

        oChonDanhMuc.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Chọn danh mục")
                .setItems(cacDanhMuc) { _, which ->
                    danhMucDuocChon = cacDanhMuc[which]
                    oChonDanhMuc.text = danhMucDuocChon
                    oChonDanhMuc.error = null
                    capNhatTrangThaiNutLuu()
                }.show()
        }

        capNhatTextNgay()
        tvChonNgay.setOnClickListener {
            val nam = ngayDuocChon.get(Calendar.YEAR)
            val thang = ngayDuocChon.get(Calendar.MONTH)
            val ngay = ngayDuocChon.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { _, y, m, d ->
                    ngayDuocChon.set(y, m, d)
                    capNhatTextNgay()
                }, nam, thang, ngay
            )
            datePickerDialog.datePicker.calendarViewShown = false
            datePickerDialog.datePicker.spinnersShown = true
            datePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            datePickerDialog.show()
        }

        if (oNhapSoTien.text.toString().isEmpty()) {
            oNhapSoTien.error = "Vui lòng nhập số tiền"
        }
        if (nhomLuaChonLoai.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Vui lòng chọn loại giao dịch", Toast.LENGTH_SHORT).show()
        }
        if (danhMucDuocChon.isEmpty()) {
            oChonDanhMuc.error = "Vui lòng chọn danh mục"
        }

        capNhatTrangThaiNutLuu()

        nutLuu.setOnClickListener {
            if (!kiemTraHopLe()) return@setOnClickListener

            val moTaText = oNhapMoTa.text.toString()
            val loai = if (radioThu.isChecked) "Thu" else "Chi"
            val ngay = tvChonNgay.text.toString()

            val giaoDichMoi = GiaoDich(
                soTien = soTienThuc ?: 0.0,
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

    private fun capNhatTextNgay() {
        val dinhDang = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        tvChonNgay.text = dinhDang.format(ngayDuocChon.time)
    }

    private fun capNhatMauGiaoDien(mau: Int) {
        val borderViews = listOf(oNhapSoTien, oChonDanhMuc, tvChonNgay)
        borderViews.forEach { view ->
            val background = view.background?.mutate()
            if (background is GradientDrawable) {
                background.setStroke(2, mau)
            }
        }

        nutLuu.setTextColor(Color.WHITE)
        nutLuu.setBackgroundColor(mau)
    }

    private fun kiemTraHopLe(): Boolean {
        var hopLe = true

        if (soTienThuc == null) {
            oNhapSoTien.error = "Vui lòng nhập số tiền"
            hopLe = false
        }

        if (nhomLuaChonLoai.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Vui lòng chọn loại giao dịch", Toast.LENGTH_SHORT).show()
            hopLe = false
        }

        if (danhMucDuocChon.isEmpty()) {
            oChonDanhMuc.error = "Vui lòng chọn danh mục"
            hopLe = false
        }

        return hopLe
    }

    private fun capNhatTrangThaiNutLuu() {
        val isValid = soTienThuc != null &&
                nhomLuaChonLoai.checkedRadioButtonId != -1 &&
                danhMucDuocChon.isNotEmpty()
        nutLuu.isEnabled = isValid
    }
}
