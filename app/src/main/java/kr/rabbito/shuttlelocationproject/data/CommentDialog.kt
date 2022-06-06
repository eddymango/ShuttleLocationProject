package kr.rabbito.shuttlelocationproject.data

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kr.rabbito.shuttlelocationproject.R
//Setting
class CommentDialog(context:Context) {

    private val dialog  = Dialog(context)
    fun showDialog(comment: String?){
        dialog.setContentView(R.layout.comment_dialog)
        val editText = dialog.findViewById<EditText>(R.id.commentdialog_et_comment)
        val okBtn = dialog.findViewById<TextView>(R.id.commentdialog_btn_ok)
        val cancelBtn = dialog.findViewById<TextView>(R.id.commentdialog_btn_cancle)

        editText.addTextChangedListener(object : TextWatcher {
            var prev = ""

            override fun afterTextChanged(p0: Editable?) {
                if (editText.lineCount >= 8) {
                    editText.setText(prev)
                    editText.setSelection(editText.length())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                prev = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        if (comment != null) {
            editText.setText(comment)
            Log.d("comment", comment)
        }

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