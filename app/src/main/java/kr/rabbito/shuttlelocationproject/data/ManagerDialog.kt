package kr.rabbito.shuttlelocationproject.data

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import kr.rabbito.shuttlelocationproject.R

class ManagerDialog(context:Context) {

    private val dialog  = Dialog(context)

    fun showDialog(){
        dialog.setContentView(R.layout.manager_dialog)
        // dialog 크기 조절
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        // 바깥쪽 터치 -> dialog cancel
        dialog.setCancelable(true)

        val editText = dialog.findViewById<EditText>(R.id.deletedialog_et_password)
        val okBtn = dialog.findViewById<Button>(R.id.deletedialog_btn_delete)
        val cancelBtn = dialog.findViewById<Button>(R.id.deletedialog_btn_cancel)

        okBtn.setOnClickListener {
            onClickListener.onClicked(editText.text.toString())
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    interface ButtonClickListener{
        fun onClicked(text:String)
    }

    private lateinit var onClickListener:ButtonClickListener

    fun setOnClickListner(listener: ButtonClickListener){
        onClickListener = listener
    }
}