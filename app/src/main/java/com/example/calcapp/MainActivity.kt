package com.example.calcapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calcapp.ui.theme.CalcAppTheme
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calcapp.menu.MenuAdapter
import com.example.calcapp.menu.MenuItem

class MainActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var btnReset: Button
    private lateinit var adapter: MenuAdapter

    private val menuList = mutableListOf(
        MenuItem("메뉴1", 12000),
        MenuItem("메뉴2", 12000),
        MenuItem("메뉴3", 12000),
        MenuItem("메뉴4", 10000),
        MenuItem("메뉴5", 10000),
        MenuItem("", 0, isSeparator = true), // ✅ 구분선
        MenuItem("메뉴11", 7000),
        MenuItem("메뉴12", 7000),
        MenuItem("메뉴13", 7000),
        MenuItem("", 0, isSeparator = true), // ✅ 구분선
        MenuItem("메뉴21", 4000),
        MenuItem("메뉴22", 3000),
        MenuItem("메뉴23", 2000)

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        txtTotal = findViewById(R.id.txtTotal)
        btnReset = findViewById(R.id.btnReset)

        adapter = MenuAdapter(menuList) { updateTotal() }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnReset.setOnClickListener {
            menuList.forEach { it.count = 0 }
            adapter.notifyDataSetChanged()
            updateTotal()
        }

        // ✅ 총액 클릭 시 영수증 다이얼로그 띄우기
        txtTotal.setOnClickListener {
            showReceiptDialog(menuList)
        }

        updateTotal()
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            CalcAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
    }

    private fun updateTotal() {
        val total = menuList.sumOf { it.count * it.price }
        // 콤마 표시(예: 12,345)
        val formatted = java.text.NumberFormat.getNumberInstance(java.util.Locale.KOREA).format(total)
        txtTotal.text = "총액: ${formatted} 원"


    }


    private fun showReceiptDialog(menuList: List<MenuItem>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_receipt, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val btnClose = dialogView.findViewById<ImageView>(R.id.btnClose)
        val layoutItems = dialogView.findViewById<LinearLayout>(R.id.layoutItems)
        val txtTotal = dialogView.findViewById<TextView>(R.id.txtTotal)

        var totalPrice = 0
        for (item in menuList.filter { it.count > 0 && !it.isSeparator }) {
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8) // 항목 간격
                }
            }

            val txtName = TextView(this).apply {
                text = "${item.name} * ${item.count}"
                textSize = 18f   // ✅ 메뉴명 크기 키움
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val txtPrice = TextView(this).apply {
                text = "%,d 원".format(item.price * item.count)   // ✅ 3자리 콤마 적용
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            row.addView(txtName)
            row.addView(txtPrice)
            layoutItems.addView(row)

            totalPrice += item.price * item.count
        }

        txtTotal.text = "TOTAL: %,d 원".format(totalPrice)  // ✅ 총액에도 콤마 적용

        btnClose.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(true)

        dialog.show()
    }







}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalcAppTheme {
        Greeting("Android")
    }
}

