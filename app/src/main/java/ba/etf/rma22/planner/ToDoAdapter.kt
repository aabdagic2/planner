package ba.etf.rma22.planner

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class ToDoAdapter(
    private var data: List<Stavka>,
    dani: DanDAO,
    stavke: StavkaDAO,
    progres: ProgressBar,
    txtProgress: TextView,
    danId: Int
) : RecyclerView.Adapter<ToDoAdapter.MyViewHolder>() {
val stavke:StavkaDAO=stavke
    var progres:ProgressBar=progres
    var txtProgress=txtProgress
    var danId=danId
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val naziv: TextView = itemView.findViewById(R.id.textView)
        val editText: EditText= itemView.findViewById(R.id.editText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        CoroutineScope(Job() + Dispatchers.Main).launch {
            withContext(Dispatchers.IO){
        data= stavke.getByDate(danId)!!}}
        return MyViewHolder(view)
    }
@SuppressLint("SetTextI18n")
public fun updateProgress(){
    var uradjeno=0;
for(i in data){
    if(i.uradjeno)uradjeno++;
}
    var p=(uradjeno.toDouble() / data.size * 100).roundToInt()
    progres.progress=p
    txtProgress.text= "$p%"
}
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var text:String=holder.naziv.text.toString()
        if(data[position].naziv!="")
        holder.naziv.text = data[position].naziv
        if(!data[position].uradjeno)
       holder.checkBox.isChecked= data[position].uradjeno
        else{
            holder.checkBox.isChecked= data[position].uradjeno
            text=holder.naziv.text.toString()
            var string:SpannableString = SpannableString(holder.naziv.text);
            string.setSpan( StrikethroughSpan(), 0, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.editText.setText(string)
            holder.naziv.text=string
            holder.editText.setTextColor(Color.parseColor("#84c189"))
            holder.naziv.setTextColor(Color.parseColor("#84c189"))
            holder.checkBox.setTextColor(Color.parseColor("#84c189"))
        }
        holder.naziv.setOnClickListener {
            holder.naziv.visibility = View.GONE
            holder.editText.visibility = View.VISIBLE
            holder.editText.setText(holder.naziv.text)
        }

        holder.editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                holder.naziv.visibility = View.VISIBLE
                holder.naziv.text=holder.editText.text
                CoroutineScope(Job() + Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        stavke.updateNazivById(data[position].id.toLong(),holder.naziv.text.toString())}
                    println(holder.naziv.text.toString())
                }
                holder.editText.visibility = View.GONE
            }
        }
        holder.editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                holder.naziv.visibility = View.VISIBLE
                holder.naziv.text = holder.editText.text
                CoroutineScope(Job() + Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        stavke.updateNazivById(data[position].id.toLong(),holder.naziv.text.toString())}
                }
                holder.editText.visibility=View.GONE
                // add the TextView to your layout
            }
            false
        }
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            // Do something when checkbox is checked or unchecked
            if (isChecked) {
                 data[position].uradjeno=true
                CoroutineScope(Job() + Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                stavke.updateUradjenoById(data[position].id.toLong(),true)}
                }
                updateProgress()
                text=holder.naziv.text.toString()
                if(text==""){
                    text=holder.editText.text.toString()
                    holder.naziv.visibility = View.VISIBLE
                    holder.naziv.text = holder.editText.text
                    holder.editText.visibility=View.GONE
                }
                var string:SpannableString = SpannableString(holder.naziv.text);
                string.setSpan( StrikethroughSpan(), 0, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.editText.setText(string)
                holder.naziv.text=string
                holder.editText.setTextColor(Color.parseColor("#84c189"))
                holder.naziv.setTextColor(Color.parseColor("#84c189"))
                holder.checkBox.setTextColor(Color.parseColor("#84c189"))

            } else {
                data[position].uradjeno=false
                CoroutineScope(Job() + Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                    stavke.updateUradjenoById(data[position].id.toLong(),false)}
                }
                updateProgress()
               holder.naziv.text= text
                holder.editText.setText(text)
                holder.editText.setTextColor(Color.parseColor("#000000"))
                holder.naziv.setTextColor(Color.parseColor("#000000"))
                holder.checkBox.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

    override fun getItemCount() = data.size

}