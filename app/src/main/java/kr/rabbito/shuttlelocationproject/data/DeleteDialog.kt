package kr.rabbito.shuttlelocationproject.data

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kr.rabbito.shuttlelocationproject.R
//Setting
class DeleteDialog(context:Context) {

    private val dialog  = Dialog(context)
    fun showDialog(){
        dialog.setContentView(R.layout.delete_dialog)
        val editText = dialog.findViewById<EditText>(R.id.deletedialog_et_password)
        val okBtn = dialog.findViewById<TextView>(R.id.deletedialog_btn_delete)
        val cancelBtn = dialog.findViewById<TextView>(R.id.deletedialog_btn_cancel)


        // dialog 크기 조절
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        // 바깥쪽 터치 -> dialog cancel
        dialog.setCancelable(true)


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